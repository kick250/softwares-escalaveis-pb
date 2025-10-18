package com.erp.server.services;

import com.erp.server.exceptions.OrderNotFoundException;
import infra.global.relational.entities.OrderEntity;
import infra.global.relational.repositories.OrdersJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrdersService {
    private final OrdersJpaRepository ordersJpaRepository;

    public List<OrderEntity> getAll() {
        return ordersJpaRepository.findAll();
    }

    public OrderEntity getById(Long id) throws OrderNotFoundException {
        return ordersJpaRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }
}
