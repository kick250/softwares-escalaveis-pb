package infra.global.relational.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    PORTAL_USER,
    ERP_USER,
    ADMIN_PERMISSION;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
