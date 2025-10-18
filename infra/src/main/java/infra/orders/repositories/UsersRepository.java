package infra.orders.repositories;

import application.orders.domain.User;
import application.orders.exceptions.UserNotFoundException;
import application.orders.repositories.AllUsers;
import infra.global.relational.entities.UserEntity;
import infra.global.relational.repositories.UsersJpaRepository;
import infra.orders.mappers.UsersMapper;
import org.springframework.stereotype.Component;

@Component
public class UsersRepository implements AllUsers {
    private final UsersJpaRepository usersJpaRepository;
    private final UsersMapper usersMapper;

    public UsersRepository(UsersJpaRepository usersJpaRepository, UsersMapper usersMapper) {
        this.usersJpaRepository = usersJpaRepository;
        this.usersMapper = usersMapper;
    }

    @Override
    public User getById(Long id) throws UserNotFoundException {
        UserEntity user = usersJpaRepository.findById(id).orElseThrow(UserNotFoundException::new);

        return usersMapper.toDomain(user);
    }
}
