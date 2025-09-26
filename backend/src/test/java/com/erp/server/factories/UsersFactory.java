package com.erp.server.factories;

import infra.global.entities.UserEntity;
import com.erp.server.enums.Role;
import org.instancio.Instancio;
import org.instancio.Select;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

public class UsersFactory {
    public static final String DEFAULT_PASSWORD = "123456";

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    public UserEntity createUser() {
        return Instancio.of(UserEntity.class)
                .set(Select.field("id"), null)
                .set(Select.field("name"), "testuser")
                .set(Select.field("username"), "test@test.com.br")
                .set(Select.field("password"), this.encoder.encode(DEFAULT_PASSWORD))
                .set(Select.field("roles"), Set.of(Role.ERP_USER))
                .create();
    }

    public UserEntity createPortalUser() {
        return Instancio.of(UserEntity.class)
                .set(Select.field("id"), null)
                .set(Select.field("name"), "portaluser")
                .set(Select.field("username"), "portal@test.com.br")
                .set(Select.field("password"), this.encoder.encode(DEFAULT_PASSWORD))
                .set(Select.field("roles"), Set.of(Role.PORTAL_USER))
                .create();
    }

    public UserEntity createAdminUser() {
        return Instancio.of(UserEntity.class)
                .set(Select.field("id"), null)
                .set(Select.field("name"), "portaladminuser")
                .set(Select.field("username"), "portaladmin@test.com.br")
                .set(Select.field("password"), this.encoder.encode(DEFAULT_PASSWORD))
                .set(Select.field("roles"), Set.of(Role.PORTAL_USER, Role.ADMIN_PERMISSION))
                .create();
    }
}
