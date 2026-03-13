package com.ignishers.repository;

import org.springframework.data.repository.CrudRepository;

import com.ignishers.pojo.Wallet;


import java.util.Optional;

public interface WallerRepository extends CrudRepository<Wallet, Long>{

	Optional<Wallet> findByCustomerId(Long customerId);
}
