package com.fooddelivery.service;

import com.fooddelivery.entity.Customer;

public interface CustomerService {

    // Create customer profile
    void createCustomer(Customer customer);

    // View customer by ID
    Customer getCustomerById(Long customerId);

    // View customer linked to logged-in user
    Customer getCustomerByUserId(Long userId);

    // Update customer profile
    void updateCustomer(Customer customer);
}