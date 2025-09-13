package application.orders.domain;

import lombok.Getter;

public class User {
    @Getter
    private Long id;
    private boolean isAdmin;

    private static final double DEFAULT_USER_QUOTA = 10000.0;

    public User(Long id, boolean isAdmin) {
        this.id = id;
        this.isAdmin = isAdmin;
    }

    public boolean exceedQuota(OrderItems items) {
        if (isAdmin) return false;

        return items.totalAmount() > DEFAULT_USER_QUOTA;
    }
}
