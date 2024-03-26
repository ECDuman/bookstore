package com.iwallet.caseProject.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private long id;

	@Column(name="first_name")
	private String firstName;

	@Column(name="last_name")
	private String lastName;

	@OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.PERSIST,
												CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="user_id", referencedColumnName = "id")
	private IwalletUser user;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval= true)
	@JoinColumn(name="shopping_cart_id", referencedColumnName = "id")
	private ShoppingCart shoppingCart;
	
	public long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public IwalletUser getUser() {
		return user;
	}

	public void setUser(IwalletUser user) {
		this.user = user;
	}

	public ShoppingCart getShoppingCart() {
	    if (shoppingCart == null) {
	    	shoppingCart = new ShoppingCart();
	    }
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	@Override
	public String toString() {
		return "Customer [firstName=" + firstName + ", lastName=" + lastName + ", user=" + user + ", shoppingCart="
				+ shoppingCart + "]";
	}

}
