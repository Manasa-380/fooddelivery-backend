package com.fooddelivery.service.impl;

import com.fooddelivery.entity.Customer;
import com.fooddelivery.exception.InvalidRequestException;
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.repository.CustomerRepository;
import com.fooddelivery.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link CustomerService} providing business logic
 * for customer profile management.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Creates a new customer profile after validating the input.
     *
     * @param customer the customer data to persist
     * @throws InvalidRequestException if the customer data is null or incomplete
     */
    @Override
    public void createCustomer(Customer customer) {
        if (customer == null ||
                customer.getCustomerName() == null ||
                customer.getUserId() <= 0) {
            log.warn("Invalid customer data provided for creation");
            throw new InvalidRequestException("Invalid customer data");
        }
        log.info("Creating customer profile for userId: {}", customer.getUserId());
        customerRepository.save(customer);
        log.info("Customer profile created successfully for userId: {}", customer.getUserId());
    }

    /**
     * Retrieves a customer by their customer ID.
     *
     * @param customerId the ID of the customer to retrieve
     * @return the found {@link Customer}
     * @throws InvalidRequestException   if the ID is invalid
     * @throws ResourceNotFoundException if no customer exists with the given ID
     */
    @Override
    public Customer getCustomerById(Long customerId) {
        if (customerId <= 0) {
            log.warn("Invalid customer ID received: {}", customerId);
            throw new InvalidRequestException("Invalid customer ID");
        }
        try {
            log.debug("Fetching customer by ID: {}", customerId);
            return customerRepository.findById(customerId);
        } catch (Exception e) {
            log.error("Customer not found for ID: {}", customerId, e);
            throw new ResourceNotFoundException("Customer not found");
        }
    }

    /**
     * Retrieves the customer profile linked to a given user ID.
     *
     * @param userId the user ID associated with the customer
     * @return the found {@link Customer}
     * @throws InvalidRequestException   if the user ID is invalid
     * @throws ResourceNotFoundException if no customer is linked to the user
     */
    @Override
    public Customer getCustomerByUserId(Long userId) {
        if (userId <= 0) {
            log.warn("Invalid user ID received: {}", userId);
            throw new InvalidRequestException("Invalid user ID");
        }
        try {
            log.debug("Fetching customer by userId: {}", userId);
            return customerRepository.findByUserId(userId);
        } catch (Exception e) {
            log.error("No customer found for userId: {}", userId, e);
            throw new ResourceNotFoundException("Customer not found for user");
        }
    }

    /**
     * Updates an existing customer's profile.
     *
     * @param customer the customer object with updated information
     * @throws InvalidRequestException if the customer or their ID is invalid
     */
    @Override
    public void updateCustomer(Customer customer) {
        if (customer == null || customer.getCustomerId() <= 0) {
            log.warn("Invalid customer details for update");
            throw new InvalidRequestException("Invalid customer details");
        }
        log.info("Updating customer ID: {}", customer.getCustomerId());
        customerRepository.update(customer);
        log.info("Customer updated successfully, ID: {}", customer.getCustomerId());
    }
}