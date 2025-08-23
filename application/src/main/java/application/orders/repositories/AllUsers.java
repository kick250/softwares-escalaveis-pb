package application.orders.repositories;

import application.orders.domain.User;
import application.orders.exceptions.UserNotFoundException;

public interface AllUsers {

    public User getById(Long id) throws UserNotFoundException;
}
