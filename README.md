java 17 - maven - Spring boot 3.2.4 - postgres13

download the zip (iwallet-main)

extract

open eclipse 

  -open project from file system
  
  -choose caseProject folder which is located under iwallet-main
  
  -right click to the project than properties -> Java build path -> Libraries -> choose Classpath and Add Library... -> select Junit -> select Junit 4 -> apply and close
  
right click on the project -> run as -> maven build... -> Goals "clean install" and check skip test -> run

I assume that docker, docker desktop and postgres13 are already installed.

right click on the project -> show in -> terminal

  run the commands below
  
  -docker build -t caseproject .
  
  -docker run -d -p 8080:8080 caseproject
  
  -docker run -d -p 5432:5432 -e POSTGRES_PASSWORD=yourpassword -e POSTGRES_USER=postgres -e POSTGRES_DB=iwallet_caseproject --name=iwallet_caseproject postgres:13

go to docker desktop and see if containers are running.

copy the id that is under iwallet_caseproject container.

open the terminal and run the commands below

  -docker exec -it (iwallet_caseprojectID that is copied from docker desktop) bash
  
  -psql -h localhost -U postgres
  
  -\c iwallet_caseproject
  
  -INSERT INTO roles (name) VALUES ('admin');
  
  -INSERT INTO roles (name) VALUES ('USER');

Now it's ready to test APIs by postman.

AUTH:

POST http://localhost:8080/iwalletapi/auth/register  [requestBody should include username and password to create a user]

POST http://localhost:8080/iwalletapi/auth/login [requestBody should include the registered username and password to get JWT token]

POST http://localhost:8080/iwalletapi/auth/logout [Authorization type: Bearer Token]

### DON'T FORGET TO GET JWT TOKEN FROM login ###

POST http://localhost:8080/iwalletapi/saveCustomer [Params are firstName and lastName to save a customer] [Authorization type: Bearer Token]

GET http://localhost:8080/iwalletapi/allBooks [Get all books] [Authorization type: Bearer Token]

POST http://localhost:8080/iwalletapi/addBook [Params are name, quantity and price to save a book] [Authorization type: Bearer Token]

POST http://localhost:8080/iwalletapi/cart/addBookToCart  [Param is bookName to add the book to shopping cart] [Authorization type: Bearer Token]

DELETE http://localhost:8080/iwalletapi/cart/removeBookFromCart  [Param is bookName to remove the book from shopping cart] [Authorization type: Bearer Token]

GET http://localhost:8080/iwalletapi/cart [Get all books in the shopping cart] [Authorization type: Bearer Token]

PUT http://localhost:8080/iwalletapi/cart/updateBookInCart [Params are oldBookName and newBookName to update the book in the shopping cart] [Authorization type: Bearer Token]

GET http://localhost:8080/iwalletapi/payment [Get payment price of shopping cart] [Authorization type: Bearer Token]




  

