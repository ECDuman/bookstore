package com.iwallet.caseProject.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;

	@Column(name="name")
	private String name;

	@Column(name="quantity")
	private int quantity;

	@Column(name="price")
	private double price;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="book_shopping_cart", joinColumns = @JoinColumn(name="book_id"),
	inverseJoinColumns = @JoinColumn(name="shopping_cart_id"))
	@JsonIgnore
	private Set<ShoppingCart> shoppingCart;

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

//	public List<Customer> getCustomers() {
//		return customers;
//	}
//
//	public void setCustomers(List<Customer> customers) {
//		this.customers = customers;
//	}

	public Set<ShoppingCart> getShoppingCart() {
	    if (shoppingCart == null) {
	    	shoppingCart = new HashSet<ShoppingCart>();
	    }
	    return shoppingCart;
	}

	public void setShoppingCart(Set<ShoppingCart> shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", quantity=" + quantity + ", price=" + price + "]";
	}
}
