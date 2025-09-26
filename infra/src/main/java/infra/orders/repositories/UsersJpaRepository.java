package infra.orders.repositories;

import application.orders.domain.User;
import application.orders.exceptions.UserNotFoundException;
import application.orders.repositories.AllUsers;
import infra.global.entities.UserEntity;
import infra.global.repositories.UsersRepository;
import infra.orders.mappers.UsersMapper;
import org.springframework.stereotype.Component;

@Component
public class UsersJpaRepository implements AllUsers {
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    public UsersJpaRepository(UsersRepository usersRepository, UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
    }

    @Override
    public User getById(Long id) throws UserNotFoundException {
        UserEntity user = usersRepository.findById(id).orElseThrow(UserNotFoundException::new);

        return usersMapper.toDomain(user);
    }
}
