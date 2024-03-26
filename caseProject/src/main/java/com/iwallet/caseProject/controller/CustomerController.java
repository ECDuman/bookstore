package com.iwallet.caseProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iwallet.caseProject.exception.DefinedException;
import com.iwallet.caseProject.model.Book;
import com.iwallet.caseProject.model.CurrentSession;
import com.iwallet.caseProject.service.CustomerService;
import com.iwallet.caseProject.service.ShoppingCartService;

@RestController
@RequestMapping("/iwalletapi")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CurrentSession currentSession;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
    @PostMapping("/saveCustomer")
    public String saveCustomer(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName){
    	customerService.saveCustomer(firstName, lastName);
        return firstName + "customer added";
    }
	//sepete kitap ekleme/çıkarma, sepeti görüntüleme
	
	@PostMapping("/cart/addBookToCart")
	public String addBookToCart(@RequestParam("bookName") String bookName) throws DefinedException {
		
		shoppingCartService.addBookToCart(currentSession.getCustomer(),bookName);
						
		return "book added to cart";
	}

	@PutMapping("/cart/updateBookInCart")
	public String updateBookInCart(@RequestParam("oldBookName") String oldBookName , @RequestParam("newBookName") String newBookName) throws DefinedException {
		
		shoppingCartService.updateBookInCart(oldBookName, newBookName);
		
		return "book updated";
	}
	
	@DeleteMapping("/cart/removeBookFromCart")
	public String removeBookFromCart(@RequestParam("bookName") String bookName) {

		shoppingCartService.removeBookFromCart(currentSession.getCustomer(), bookName);

		return "book removed from cart";
	}
	
	
	@GetMapping("/cart")
	public List<Book> showCart() {
		return shoppingCartService.getCart();
	}
	
	//Ödeme
    @GetMapping("/payment")
    public ResponseEntity<String> payment() {
		
        return ResponseEntity.ok("Payment price: " + shoppingCartService.payment());
        }
}
