package com.iwallet.caseProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iwallet.caseProject.model.Customer;
import com.iwallet.caseProject.model.IwalletUser;

import jakarta.transaction.Transactional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	
	@Transactional
	Optional<Customer> findByFirstNameAndLastName(String firstName, String lastName);

	@Transactional
	Optional<Customer> findByUser(IwalletUser user);
}
