package com.ignishers.repository;

import org.springframework.data.repository.CrudRepository;

import com.ignishers.pojo.Order;



import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long>{

	List<Order> findByCustomerId(Long customerId);
}
