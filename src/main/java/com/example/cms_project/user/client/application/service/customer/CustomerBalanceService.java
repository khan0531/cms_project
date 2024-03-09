package com.example.cms_project.user.client.application.service.customer;

import static com.example.cms_project.user.client.exception.ErrorCode.NOT_ENOUGH_BALANCE;
import static com.example.cms_project.user.client.exception.ErrorCode.NOT_FOUND_USER;

import com.example.cms_project.user.client.domain.ChangeBalanceForm;
import com.example.cms_project.user.client.exception.CustomException;
import com.example.cms_project.user.client.exception.ErrorCode;
import com.example.cms_project.user.client.model.CustomerBalanceHistory;
import com.example.cms_project.user.client.repository.CustomerBalanceHistoryRepository;
import com.example.cms_project.user.client.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerBalanceService {

  private final CustomerBalanceHistoryRepository customerBalanceHistoryRepository;
  private final CustomerRepository customerRepository;

  @Transactional(noRollbackFor = {CustomException.class})
  public CustomerBalanceHistory changeBalance(Long customerId, ChangeBalanceForm form) throws CustomException {
    CustomerBalanceHistory customerBalanceHistory =
        customerBalanceHistoryRepository.findFirstByCustomer_IdOrderByIdDesc(customerId)
            .orElse(CustomerBalanceHistory.builder()
                .changeMoney(0)
                .currentMoney(0)
                .customer(customerRepository.findById(customerId)
                    .orElseThrow(() -> new CustomException(NOT_FOUND_USER)))
                .build());
    if (customerBalanceHistory.getCurrentMoney() + form.getMoney() < 0) {
      throw new CustomException(NOT_ENOUGH_BALANCE);
    }

    customerBalanceHistory = CustomerBalanceHistory.builder()
        .changeMoney(form.getMoney())
        .currentMoney(customerBalanceHistory.getCurrentMoney())
        .description(form.getMessage())
        .fromMessage(form.getFrom())
        .customer(customerBalanceHistory.getCustomer())
        .build();
    customerBalanceHistory.getCustomer().setBalance(customerBalanceHistory.getCurrentMoney());

    return customerBalanceHistoryRepository.save(customerBalanceHistory);
  }

}
