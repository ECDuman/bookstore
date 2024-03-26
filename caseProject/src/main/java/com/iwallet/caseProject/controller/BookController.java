package com.iwallet.caseProject.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iwallet.caseProject.model.Book;
import com.iwallet.caseProject.service.BookService;

@RestController
@RequestMapping("/iwalletapi")
public class BookController {
    @Autowired
    private BookService service;

    //Kitapları listeleme
    @GetMapping("/allBooks")
    public Set<Book> getAllBooks(){
        return service.getAllBooks();
    }

    @PostMapping("/addBook")
    public Book addBook(@RequestParam("name") String name, @RequestParam("quantity") int quantity, @RequestParam("price") double price){
//    	service.addBook(name, quantity, price);
        return service.addBook(name, quantity, price);
    }
}
