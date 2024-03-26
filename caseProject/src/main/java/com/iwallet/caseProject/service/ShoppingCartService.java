package com.iwallet.caseProject.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iwallet.caseProject.exception.DefinedException;
import com.iwallet.caseProject.model.Book;
import com.iwallet.caseProject.model.CurrentSession;
import com.iwallet.caseProject.model.Customer;
import com.iwallet.caseProject.model.ShoppingCart;
import com.iwallet.caseProject.repository.BookRepository;
import com.iwallet.caseProject.repository.CustomerRepository;
import com.iwallet.caseProject.repository.ShoppingCartRepository;

import jakarta.transaction.Transactional;

@Service
public class ShoppingCartService {

	@Autowired
	private ShoppingCartRepository cartRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private CurrentSession currentSession;

	@Transactional
	public Set<Book> getAllItems(Customer customer) {
		Set<Book> items = new HashSet<Book>();
		items.addAll(customer.getShoppingCart().getBooks());
		return items;
	
	}

	@Transactional
	public void addBookToCart(Customer customer, String bookName) throws DefinedException {
		Book book = null ;
		List<Book> bookList = bookRepository.findByName(bookName);
		for (Book bookItr : bookList) {
			if(bookItr.getQuantity() >= 1) {
				book = bookItr;
			}
			else {
				throw new DefinedException("Not avaible."); 
			}
		}
		int flag = 1;
		if(book.getShoppingCart() == null || book.getShoppingCart().size() == 0) {
			
			flag = 0;
		}
		Customer customerTemp = customerRepository.findByUser(customer.getUser()).get();
		ShoppingCart shoppingCart = customerTemp.getShoppingCart();
		int qty = book.getQuantity();
				if(flag == 0  && qty > 0){
					List<Book> books = customerTemp.getShoppingCart().getBooks();
					if(books.stream().map(Book::getName).filter(book.getName()::equals).findFirst().isPresent()) {
						for (Book bookTemp : books) {
							if(book.getName().equals(bookTemp.getName())) {
								bookTemp.setQuantity(bookTemp.getQuantity() + 1);
							}
						}
						book.setQuantity(book.getQuantity() - 1);
						bookRepository.save(book);
						customerRepository.save(customerTemp);
					}
					else {
						Book newBook = new Book();
						newBook.setName(book.getName());
						newBook.setPrice(book.getPrice());
						Set<ShoppingCart> shoppingCartList = new HashSet<ShoppingCart>();
						shoppingCartList.add(shoppingCart);
						newBook.setShoppingCart(shoppingCartList);
						newBook.setQuantity(1);
						books.add(newBook);
						shoppingCart.setBooks(books);
						book.setQuantity(book.getQuantity() - 1);
						bookRepository.save(book);
						customerTemp.setShoppingCart(shoppingCart);;
						customerRepository.save(customerTemp);
					}
//				}
//			}
		}
	}

	@Transactional
	public void removeBookFromCart(Customer customer, String bookName) {
		List<Book> allBooks = bookRepository.findByName(bookName);
		Book originalBook = null;
		for (Book bookItr : allBooks) {
			if(bookItr.getShoppingCart().size() == 0) {
				originalBook = bookItr;
			}
		}
		if(customer.getUser().equals(currentSession.getCustomer().getUser())) {
			Customer customerTemp = customerRepository.findByUser(customer.getUser()).get();
			List<Book> books = customerTemp.getShoppingCart().getBooks();
			for (int i = 0; i < books.size(); i++) {

				if(books.get(i).getName().equals(bookName)){
					Book book = books.get(i);
					int qty = book.getQuantity();
					books.remove(i);
					bookRepository.deleteByBookId(book.getId());
					if(originalBook != null) {
						originalBook.setQuantity(originalBook.getQuantity() + qty);
						bookRepository.save(originalBook);
					}
					customerTemp.getShoppingCart().setBooks(books);
					cartRepository.save(customerTemp.getShoppingCart());
					customerRepository.save(customerTemp);
		
					}
			}
		}
						
						
	}
				
	@Transactional
	public void updateBookInCart(String oldBookName , String newBookName) throws DefinedException {
		Customer customer = customerRepository.findByUser(currentSession.getUser()).get();
		removeBookFromCart(customer, oldBookName);
		addBookToCart(customer, newBookName);
	}

	public List<Book> getCart() {
		Customer customer = customerRepository.findByUser(currentSession.getUser()).get();
		return customer.getShoppingCart().getBooks();
		
	}

	@Transactional
	public double payment() {
		double price = calculatePrice();
		Customer customer = customerRepository.findByUser(currentSession.getUser()).get();
		List<Book> bookList = customer.getShoppingCart().getBooks();
		if(bookList.size() > 0) {
			for (Book book : bookList) {
					bookRepository.deleteByBookId(book.getId());
			}
		}
		
		return price;
		
	}

	public double calculatePrice() {
		Customer customer = customerRepository.findByUser(currentSession.getUser()).get();
		List<Book> bookList = customer.getShoppingCart().getBooks();
		double totalPrice = 0;
		for (Book book : bookList) {
			totalPrice = totalPrice + (book.getPrice() * book.getQuantity());
		}
		return totalPrice;
	}

}
