package za.co.shilton.vasorderj23.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import za.co.shilton.vasorderj23.service.OrderService;

import java.math.BigInteger;
import java.util.Random;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public String createOrder(@RequestParam Integer numBits) throws InterruptedException {
        orderService.createOrder();
        return "hello";
    }
}
