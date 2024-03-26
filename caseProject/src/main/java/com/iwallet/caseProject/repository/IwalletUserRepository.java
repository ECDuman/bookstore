package com.iwallet.caseProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iwallet.caseProject.model.IwalletUser;


@Repository
public interface IwalletUserRepository extends JpaRepository<IwalletUser, Long> {
    Optional<IwalletUser> findByUsername(String username);
    Boolean existsByUsername(String username);
	public IwalletUser getUserByUsername(String username);
}
