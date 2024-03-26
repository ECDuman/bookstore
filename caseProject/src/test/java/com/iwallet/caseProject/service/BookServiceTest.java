package com.iwallet.caseProject.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iwallet.caseProject.model.Book;
import com.iwallet.caseProject.repository.BookRepository;

@AutoConfigureMockMvc
@SpringBootTest
public class BookServiceTest {
	
	@Mock
	private BookRepository bookRepository;
	
	@InjectMocks
	private BookService bookService;
	
	private Book testBook;
	
	@BeforeEach
	void init() {
		testBook = new Book();
		testBook.setName("Test Book");
		testBook.setPrice(123);
		testBook.setQuantity(123);
	}
	

	@Test
	public void addBook() throws JsonProcessingException, Exception {
			
			when(bookRepository.save(any(Book.class))).thenReturn(testBook);
			
			Book newBook = bookService.addBook(testBook.getName(), testBook.getQuantity(), testBook.getPrice());
			
			assertNotNull(newBook);
			assertThat(newBook.getName()).isEqualTo("Test Book");
		

	}

	@Test
	public void getAllBooks() {
		
		Set<Book> bookSet = new HashSet<>();
		bookSet.add(testBook);
		
		ArrayList<Book> list = new ArrayList<Book>(bookSet);

		
		when(bookRepository.findAll()).thenReturn((List<Book>) list);
		
		Set<Book> books = bookService.getAllBooks();
		
		assertEquals(1, books.size());
		assertNotNull(books);
		
		
	}

}
