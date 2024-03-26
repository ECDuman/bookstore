java 17 - maven - Spring boot 3.2.4 - postgres13

download the zip (iwallet-main)

extract

open eclipse 

  -open project from file system
  
  -choose caseProject folder which is located under iwallet-main
  
  -right click to the project than properties -> Java build path -> Libraries -> choose Classpath and Add Library... -> select Junit -> select Junit 4 -> apply and close
  
right click to the project -> run as -> maven build... -> Goals "clean install" and check skip test -> run

I assume that docker, docker desktop and postgres13 is already installed.

right click to the project -> show in -> terminal

  -run the commands below
  
  -docker build -t caseproject .
  
  -docker run -d -p 8080:8080 caseproject
  
  -docker run -d -p 5432:5432 -e POSTGRES_PASSWORD=yourpassword -e POSTGRES_USER=postgres -e POSTGRES_DB=iwallet_caseproject --name=iwallet_caseproject postgres:13

go to docker desktop and see if containers are running.

copy the id that is under iwallet_caseproject container.

open terminal and run commands below

  -docker exec -it (iwallet_caseprojectID that is coppied from docker desktop) bash
  
  -psql -h localhost -U postgres
  
  -\c iwallet_caseproject
  
  -INSERT INTO roles (admin) VALUES ('admin');
  
  -INSERT INTO roles (name) VALUES ('USER');

Now its ready to test apis by postman.



  

