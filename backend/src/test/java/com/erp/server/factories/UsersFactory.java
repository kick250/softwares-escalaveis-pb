package com.erp.server.factories;

import com.erp.server.entities.User;
import com.erp.server.enums.Role;
import org.instancio.Instancio;
import org.instancio.Select;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

public class UsersFactory {
    public static final String DEFAULT_PASSWORD = "123456";

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    public User createUser() {
        return Instancio.of(User.class)
                .set(Select.field("id"), null)
                .set(Select.field("name"), "testuser")
                .set(Select.field("username"), "test@test.com.br")
                .set(Select.field("password"), this.encoder.encode(DEFAULT_PASSWORD))
                .set(Select.field("roles"), Set.of(Role.ERP_USER))
                .create();
    }
}
