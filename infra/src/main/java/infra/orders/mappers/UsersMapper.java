package infra.orders.mappers;

import application.orders.domain.User;
import infra.global.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {

    public User toDomain(UserEntity userEntity) {
        return new User(userEntity.getId());
    }
}
