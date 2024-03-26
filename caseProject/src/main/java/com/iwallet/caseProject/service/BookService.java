package com.iwallet.caseProject.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iwallet.caseProject.model.Book;
import com.iwallet.caseProject.repository.BookRepository;
import com.iwallet.caseProject.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    CustomerRepository customerRepository;

    public Set<Book> getAllBooks(){
		Set<Book> books = new HashSet<Book>();
		Set<Book> resultBooks = new HashSet<Book>();
		
		books.addAll(bookRepository.findAll());
		for (Book book : books) {
			if(book.getShoppingCart().size() == 0 ) {
				resultBooks.add(book);
			}
		}
		return resultBooks;
    }

	@Transactional
	public Book addBook(String name, int quantity, double price) {
		Book book = new Book();
		book.setName(name);
		book.setQuantity(quantity);;
		book.setPrice(price);;
		bookRepository.save(book);
		return book;

	}
	
}
