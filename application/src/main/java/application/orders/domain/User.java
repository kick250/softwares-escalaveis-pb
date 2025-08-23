package application.orders.domain;

public class User {
    private Long id;

    public User(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public boolean exceedQuota(double amount) {
        double userQuota = 10000.0;
        return amount > userQuota;
    }
}
