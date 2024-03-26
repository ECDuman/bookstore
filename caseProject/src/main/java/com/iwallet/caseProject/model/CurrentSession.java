package com.iwallet.caseProject.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.iwallet.caseProject.repository.IwalletUserRepository;
import com.iwallet.caseProject.service.CustomerService;

@Component("currentSession")
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CurrentSession {

	@Autowired
	CustomerService customerService;

	@Autowired
	private IwalletUserRepository userRepository;

	private Customer customer;

	public Customer getCustomer() {
		if (customer == null) {
			Customer customerTemp = new Customer();
			IwalletUser userTemp = getUser();
			if(userTemp != null) {
				customerTemp.setUser(userTemp);
//				customer.setUser(user);
//				customer = customerTemp;
			}
			return customerTemp;
//			customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
		if (customer.getFirstName() == null) {
			IwalletUser userTemp = getUser();
			if(userTemp != null) {
				customer = customerService.getCustomerByUser(userTemp);
//				customer.setUser(user);
//				customer = customerTemp;
			}
			return customer;
//			customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
        return customer;
	}

	public IwalletUser getUser() {
		return userRepository.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
}
