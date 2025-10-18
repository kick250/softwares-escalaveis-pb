package infra.orders;

import application.orders.repositories.AllCarts;
import application.orders.useCases.ApproveOrder;
import application.orders.useCases.CancelOrder;
import application.orders.useCases.CreateOrder;
import application.orders.repositories.AllOrders;
import application.orders.repositories.AllItems;
import application.orders.repositories.AllUsers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrdersConfiguration {

    @Bean
    public CreateOrder createOrder(AllOrders allOrders, AllUsers allUsers, AllItems allProducts, AllCarts allCarts) {
        return new CreateOrder(allOrders, allUsers, allProducts, allCarts);
    }

    @Bean
    public ApproveOrder approveOrder(AllOrders allOrders, AllUsers allUsers) {
        return new ApproveOrder(allOrders, allUsers);
    }

    @Bean
    public CancelOrder cancelOrder(AllOrders allOrders, AllUsers allUsers, AllItems allItems) {
        return new CancelOrder(allOrders, allUsers, allItems);
    }
}
