package com.pjb.springbootjjwt.api;

import com.pjb.springbootjjwt.annotation.Permission;
import com.pjb.springbootjjwt.annotation.UserLoginToken;
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
@RequestMapping("/api")
public class TeController {

    @UserLoginToken
    @Permission
    @RequestMapping("/timeTest")
    public OrderInfo timeTest() {
        OrderInfo order = new OrderInfo();
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(new Date());
        return order;
    }
}
