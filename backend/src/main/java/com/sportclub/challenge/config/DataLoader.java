package com.sportclub.challenge.config;

import com.sportclub.challenge.entity.Customer;
import com.sportclub.challenge.entity.Order;
import com.sportclub.challenge.entity.OrderItem;
import com.sportclub.challenge.enums.OrderStatus;
import com.sportclub.challenge.repository.CustomerRepository;
import com.sportclub.challenge.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private static final List<String> HARDWARE_PRODUCTS = List.of(
            "Mechanical Keyboard",
            "Gaming Mouse",
            "27-inch Monitor",
            "Graphics Card RTX 4070",
            "AMD Ryzen 7 Processor",
            "Intel Core i7 Processor",
            "16GB DDR5 RAM Kit",
            "32GB DDR5 RAM Kit",
            "1TB NVMe SSD",
            "2TB SATA SSD",
            "750W Power Supply",
            "ATX Mid Tower Case",
            "Liquid CPU Cooler",
            "Air CPU Cooler",
            "B650 Motherboard",
            "Z790 Motherboard",
            "WiFi PCIe Adapter",
            "USB-C Docking Station",
            "Webcam Full HD",
            "Noise Cancelling Headset");

    public DataLoader(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) {
        if (customerRepository.count() > 0 || orderRepository.count() > 0) {
            return;
        }

        List<Customer> customers = customerRepository.saveAll(List.of(
                new Customer(null, "Juan Perez", "juan.perez@example.com"),
                new Customer(null, "Maria Gomez", "maria.gomez@example.com"),
                new Customer(null, "Carlos Lopez", "carlos.lopez@example.com"),
                new Customer(null, "Ana Martinez", "ana.martinez@example.com"),
                new Customer(null, "Sofia Rodriguez", "sofia.rodriguez@example.com")));

        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Customer customer = customers.get(i % customers.size());

            Order order = new Order();
            order.setDate(LocalDate.now().minusDays(i));
            order.setStatus(switch (i % 3) {
                case 0 -> OrderStatus.PENDING;
                case 1 -> OrderStatus.PAID;
                default -> OrderStatus.CANCELLED;
            });

            List<OrderItem> items = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            for (int j = 1; j <= 3; j++) {
                OrderItem item = new OrderItem();
                item.setProductName(HARDWARE_PRODUCTS.get((i * 3 + j - 1) % HARDWARE_PRODUCTS.size()));
                item.setQuantity(j);

                BigDecimal price = BigDecimal.valueOf(10 + i + j);
                item.setPrice(price);
                item.setOrder(order);

                items.add(item);
                total = total.add(price.multiply(BigDecimal.valueOf(j)));
            }

            order.setCustomer(customer);
            order.setOrderItems(items);
            order.setTotal(total);

            orders.add(order);
        }

        orderRepository.saveAll(orders);
    }
}
