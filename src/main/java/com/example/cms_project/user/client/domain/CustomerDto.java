package com.example.cms_project.user.client.domain;

import com.example.cms_project.user.client.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerDto {

    private Long id;
    private String email;
    private Integer balance;

    public static  CustomerDto from(Customer customer){
        return new CustomerDto(customer.getId(), customer.getEmail(), customer.getBalance());
    }
}
