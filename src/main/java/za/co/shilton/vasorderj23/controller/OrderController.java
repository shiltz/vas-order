package za.co.shilton.vasorderj23.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.Random;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @PostMapping
    public String createOrder(@RequestParam Integer numBits) throws InterruptedException {
        Thread.sleep(1000);
        BigInteger veryBig = new BigInteger(numBits, new Random());
        return veryBig.nextProbablePrime().toString();
//        return "hello";
    }
}
