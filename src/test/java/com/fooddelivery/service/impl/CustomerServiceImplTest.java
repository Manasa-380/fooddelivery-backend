package com.fooddelivery.service.impl;

import com.fooddelivery.entity.Customer;
import com.fooddelivery.exception.InvalidRequestException;
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CustomerServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer validCustomer;

    @BeforeEach
    void setUp() {
        validCustomer = new Customer(1L, 1L, "John", "9999999999", "Chennai");
    }

    // =========================================================================
    //  createCustomer
    // =========================================================================

    @Test
    void createCustomer_validData_savesSuccessfully() {
        customerService.createCustomer(validCustomer);
        verify(customerRepository, times(1)).save(validCustomer);
    }

    @Test
    void createCustomer_nullCustomer_throwsInvalidRequestException() {
        assertThrows(InvalidRequestException.class,
                () -> customerService.createCustomer(null));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void createCustomer_nullCustomerName_throwsInvalidRequestException() {
        validCustomer.setCustomerName(null);
        assertThrows(InvalidRequestException.class,
                () -> customerService.createCustomer(validCustomer));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void createCustomer_invalidUserId_throwsInvalidRequestException() {
        validCustomer.setUserId(0L);
        assertThrows(InvalidRequestException.class,
                () -> customerService.createCustomer(validCustomer));
        verify(customerRepository, never()).save(any());
    }

    // =========================================================================
    //  getCustomerById
    // =========================================================================

    @Test
    void getCustomerById_validId_returnsCustomer() {
        when(customerRepository.findById(1L)).thenReturn(validCustomer);
        Customer result = customerService.getCustomerById(1L);
        assertEquals(validCustomer, result);
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void getCustomerById_invalidId_throwsInvalidRequestException() {
        assertThrows(InvalidRequestException.class,
                () -> customerService.getCustomerById(0L));
        verify(customerRepository, never()).findById(any());
    }

    @Test
    void getCustomerById_notFound_throwsResourceNotFoundException() {
        when(customerRepository.findById(99L))
                .thenThrow(new RuntimeException("No data"));
        assertThrows(ResourceNotFoundException.class,
                () -> customerService.getCustomerById(99L));
    }

    // =========================================================================
    //  getCustomerByUserId
    // =========================================================================

    @Test
    void getCustomerByUserId_validUserId_returnsCustomer() {
        when(customerRepository.findByUserId(1L)).thenReturn(validCustomer);
        Customer result = customerService.getCustomerByUserId(1L);
        assertEquals(validCustomer, result);
        verify(customerRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getCustomerByUserId_invalidUserId_throwsInvalidRequestException() {
        assertThrows(InvalidRequestException.class,
                () -> customerService.getCustomerByUserId(0L));
        verify(customerRepository, never()).findByUserId(any());
    }

    @Test
    void getCustomerByUserId_notFound_throwsResourceNotFoundException() {
        when(customerRepository.findByUserId(99L))
                .thenThrow(new RuntimeException("No data"));
        assertThrows(ResourceNotFoundException.class,
                () -> customerService.getCustomerByUserId(99L));
    }

    // =========================================================================
    //  updateCustomer
    // =========================================================================

    @Test
    void updateCustomer_validData_updatesSuccessfully() {
        customerService.updateCustomer(validCustomer);
        verify(customerRepository, times(1)).update(validCustomer);
    }

    @Test
    void updateCustomer_nullCustomer_throwsInvalidRequestException() {
        assertThrows(InvalidRequestException.class,
                () -> customerService.updateCustomer(null));
        verify(customerRepository, never()).update(any());
    }

    @Test
    void updateCustomer_invalidCustomerId_throwsInvalidRequestException() {
        validCustomer.setCustomerId(0L);
        assertThrows(InvalidRequestException.class,
                () -> customerService.updateCustomer(validCustomer));
        verify(customerRepository, never()).update(any());
    }
}