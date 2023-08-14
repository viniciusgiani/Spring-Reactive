package com.spring.reactive.handler;

import com.spring.reactive.dao.CustomerDao;
import com.spring.reactive.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerHandler {

    @Autowired
    private CustomerDao dao;

    public Mono<ServerResponse> loadCustomers(ServerRequest request) {
        Flux<Customer> customerList = dao.getCustomerList();
        return ServerResponse.ok().body(customerList, Customer.class);
    }

    public Mono<ServerResponse> findCustomers(ServerRequest request) {
        int customerId = Integer.valueOf(request.pathVariable("input"));
        Mono<Customer> customerMono = dao.getCustomerList().filter(c->c.getId() == customerId).next();
        return ServerResponse.ok()
                .body(customerMono, Customer.class);
    }

    public Mono<ServerResponse> saveCustomers(ServerRequest request) {
        Mono<Customer> customerMono = request.bodyToMono(Customer.class);
        customerMono.map(dto->dto.getId() + ": "+dto.getName());
        return ServerResponse.ok().body(customerMono, Customer.class);
    }

}
