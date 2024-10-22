package com.estudos.discount.infra.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.estudos.discount.infra.dto.ExpiredDiscountKQDTO;
import com.estudos.discount.infra.kafka.sender.KafkaDiscountSender;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class DiscountExpirationJob implements Job {

    @Autowired
    private KafkaDiscountSender kafkaDiscountSender;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(JobExecutionContext context) {
        String discountId = context.getMergedJobDataMap().getString("discountId");
        ExpiredDiscountKQDTO taskDiscountDTO = objectMapper.convertValue(redisTemplate.opsForValue().get("discount:" + discountId), ExpiredDiscountKQDTO.class);

        kafkaDiscountSender.notifyExpiratedDiscount(taskDiscountDTO);
        redisTemplate.delete("discount:" + discountId);
    }
}
