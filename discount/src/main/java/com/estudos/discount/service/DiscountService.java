package com.estudos.discount.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.estudos.discount.dto.DiscountDTO;
import com.estudos.discount.entities.Discount;
import com.estudos.discount.infra.kafka.dto.DiscountKDTO;
import com.estudos.discount.infra.kafka.sender.KafkaDiscountSender;
import com.estudos.discount.infra.quartz.service.QuartzService;
import com.estudos.discount.repositories.DiscountRepository;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private KafkaDiscountSender kafkaDiscountSender;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private QuartzService quartzService;

    public Optional<String> applyDiscount(DiscountDTO dto) throws SchedulerException {
        UUID discountUUID = UUID.randomUUID();
        String discountStringID = discountUUID.toString();

        Discount discount = Discount.builder()
                .discountId(discountUUID)
                .productId(UUID.fromString(dto.productId()))
                .discountType(dto.discountType())
                .discountValue(dto.discountValue())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMinutes(2))
                .notified(false)
                .build();

        discountRepository.save(discount);

        DiscountKDTO discountKDTO = DiscountKDTO.builder()
                .discountId(discountStringID)
                .discountType(dto.discountType())
                .discountValue(dto.discountValue())
                .productId(dto.productId())
                .endDate(LocalDateTime.now().plusSeconds(30))
                .build();

        kafkaDiscountSender.applyDiscount(discountKDTO);
        quartzService.scheduleExpiration(discountKDTO);
        redisTemplate.opsForValue().set("discount:" + discountStringID, discountKDTO);

        return Optional.of("foi");
    }
}
