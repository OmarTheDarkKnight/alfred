package com.waynetech.alfred;

import com.waynetech.alfred.order.Order;
import com.waynetech.alfred.order.OrderService;
import com.waynetech.alfred.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AlfredApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlfredApplication.class, args);
    }

    @Bean
    CommandLineRunner cmdRunner(UserService userService, OrderService orderService) {
        return args -> {
            System.out.println(userService.findall().getFirst());

            for(Order order: orderService.findAll())
                System.out.println(order);
        };
    }

}
