package com.pjb.springbootjjwt.api;

import com.pjb.springbootjjwt.entity.OrderInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @program: thymeleaf
 * @author: Bell
 * @create: 2020-04-15 19:03
 **/
@RestController
public class TeController {
    @RequestMapping("/timeTest")
    public OrderInfo timeTest() {
        OrderInfo order = new OrderInfo();
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(new Date());
        return order;
    }
}
