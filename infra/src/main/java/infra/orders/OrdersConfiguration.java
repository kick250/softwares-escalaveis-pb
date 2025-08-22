package infra.orders;

import application.orders.actions.CreateOrder;
import application.orders.repositories.AllOrders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrdersConfiguration {

    @Bean
    public CreateOrder createOrder(AllOrders allOrders) {
        return new CreateOrder(allOrders);
    }
}
