package com.shoto.elasticjob.service;

import com.alibaba.fastjson.JSONObject;
import com.shoto.elasticjob.domain.OrderEntity;
import com.shoto.elasticjob.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public OrderEntity selectById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id).get();
        log.info(JSONObject.toJSONString(orderEntity));
        return orderEntity;
    }
}
