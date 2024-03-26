package com.iwallet.caseProject.service;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iwallet.caseProject.model.CurrentSession;
import com.iwallet.caseProject.model.Customer;
import com.iwallet.caseProject.model.IwalletUser;
import com.iwallet.caseProject.model.ShoppingCart;
import com.iwallet.caseProject.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerService {


	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	private CurrentSession currentSession;
	
	@Transactional
	public Customer getCustomerByUser(IwalletUser user) {
		
		Optional<Customer> custOpt = customerRepository.findByUser(user);
		
		Customer customer = null;
		
		if(custOpt.isPresent())
			customer = custOpt.get();
		
		return customer;
	}

	@Transactional
	public String saveCustomer(String firstName, String lastName) {
		Customer customer = new Customer();
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustomer(customer);
		customer.setFirstName(firstName);
		customer.setLastName(lastName);
		customer.setShoppingCart(shoppingCart);
		customer.setUser(currentSession.getUser());
		customerRepository.save(customer);
		
		return "Data Saved Successfully!";
	}

}
