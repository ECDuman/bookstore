package com.iwallet.caseProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iwallet.caseProject.model.Book;
import jakarta.transaction.Transactional;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	@Transactional
	@Modifying
	@Query("delete from Book b where b.id = :bookId")
	void deleteByBookId(Long bookId);

	@Transactional
	List<Book> findByName(String bookName);

}