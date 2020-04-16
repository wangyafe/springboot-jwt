package com.pjb.springbootjjwt.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @program: thymeleaf
 * @author: Bell
 * @create: 2020-04-15 18:52
 **/
@Data
public class OrderInfo {

//    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

//    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
