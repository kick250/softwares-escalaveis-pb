package com.erp.server.services;

import com.erp.server.exceptions.OrderNotFoundException;
import infra.global.entities.OrderEntity;
import infra.global.repositories.OrdersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;

    public List<OrderEntity> getAll() {
        return ordersRepository.findAll();
    }

    public OrderEntity getById(Long id) throws OrderNotFoundException {
        return ordersRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }
}
