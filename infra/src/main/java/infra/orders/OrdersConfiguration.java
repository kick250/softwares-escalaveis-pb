package infra.orders;

import application.orders.useCases.CreateOrder;
import application.orders.repositories.AllOrders;
import application.orders.repositories.AllItems;
import application.orders.repositories.AllUsers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrdersConfiguration {

    @Bean
    public CreateOrder createOrder(AllOrders allOrders, AllUsers allUsers, AllItems allProducts) {
        return new CreateOrder(allOrders, allUsers, allProducts);
    }
}
