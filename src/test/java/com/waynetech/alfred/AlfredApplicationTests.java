package com.waynetech.alfred;

import com.waynetech.alfred.order.Order;
import com.waynetech.alfred.order.OrderRepository;
import com.waynetech.alfred.user.User;
import com.waynetech.alfred.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AlfredApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void saveUser() {

        User testUser = new User();
        testUser.setId(1);
        testUser.setFirstName("Alfred");
        testUser.setLastName("Pennyworth");
        testUser.setEmail("alfred_pennyworth@wwaynetech.com");

        User user = userRepository.save(testUser);
        Optional<User> userInDB = userRepository.findById(user.getId());
        assertTrue(userInDB.isPresent());
    }

    @Test
    void saveOrder() {
        Order testOrder = new Order();
        testOrder.setId(1);
        testOrder.setProductName("Earl Grey Black Tea");
        testOrder.setOrderAmount(365);
        testOrder.setUserId(1);

        Order order = orderRepository.save(testOrder);
        Optional<Order> orderInDB = orderRepository.findById(order.getId());
        assertTrue(orderInDB.isPresent());
    }

}
