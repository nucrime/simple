package by.ak.simple.db;

import by.ak.simple.customer.Customer;
import by.ak.simple.order.Order;
import by.ak.simple.customer.CustomerRepository;
import by.ak.simple.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Profile("dev")
@Component
@RequiredArgsConstructor
@Slf4j
public class JpaAppDbRunner implements ApplicationRunner {

    public static final int START_INCLUSIVE = 1;
    public static final int END_INCLUSIVE = 10000;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("[SIMPLE] Beginning of db population");
        LongStream customerStream = IntStream.rangeClosed(START_INCLUSIVE, END_INCLUSIVE)
                .asLongStream();
        log.info("[SIMPLE] Populating Customer table");
        customerStream
                .forEach(i -> {
                    customerRepository.save(Customer.builder()
                            .id(i)
                            .firstName(RandomString.make(10))
                            .lastName(RandomString.make(10))
                            .address(RandomString.make(10))
                            .email(RandomString.make(10))
                            .build());
                });
        log.info("[SIMPLE] Populating Order table");
        LongStream orderStream = IntStream.rangeClosed(START_INCLUSIVE, END_INCLUSIVE)
                .asLongStream();
        orderStream
                .forEach(i -> {
                    orderRepository.save(Order.builder()
                            .id(i)
                            .sku(RandomString.make(10))
                            .build());
                });
        log.info("[SIMPLE] Population completed");
        var orderTableCount = orderRepository.count();
        log.info("[SIMPLE] Order table contains {} records", orderTableCount);
        var customerTableCount = customerRepository.count();
        log.info("[SIMPLE] Customer table contains {} records", customerTableCount);
    }
}
