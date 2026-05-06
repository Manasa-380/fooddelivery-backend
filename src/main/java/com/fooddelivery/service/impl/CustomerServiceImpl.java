package com.fooddelivery.service.impl;
import com.fooddelivery.entity.Customer;
import com.fooddelivery.exception.InvalidRequestException;
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.repository.CustomerRepository;
import com.fooddelivery.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // ✅ Create customer profile
    @Override
    public void createCustomer(Customer customer) {

        if (customer == null ||
                customer.getCustomerName() == null ||
                customer.getUserId() <= 0) {

            throw new InvalidRequestException("Invalid customer data");
        }

        customerRepository.save(customer);
    }

    // ✅ Get customer by ID
    @Override
    public Customer getCustomerById(Long customerId) {

        if (customerId <= 0) {
            throw new InvalidRequestException("Invalid customer ID");
        }

        try {
            return customerRepository.findById(customerId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Customer not found");
        }
    }

    // ✅ Get customer using user ID
    @Override
    public Customer getCustomerByUserId(Long userId) {

        if (userId <= 0) {
            throw new InvalidRequestException("Invalid user ID");
        }

        try {
            return customerRepository.findByUserId(userId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Customer not found for user");
        }
    }

    // ✅ Update customer profile
    @Override
    public void updateCustomer(Customer customer) {

        if (customer == null || customer.getCustomerId() <= 0) {
            throw new InvalidRequestException("Invalid customer details");
        }

        customerRepository.update(customer);
    }
}