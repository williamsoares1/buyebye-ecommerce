package com.estudos.discount.infra.quartz.service;

import java.time.ZoneId;
import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estudos.discount.infra.kafka.dto.DiscountKDTO;
import com.estudos.discount.infra.quartz.jobs.DiscountExpirationJob;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class QuartzService {

    @Autowired
    private Scheduler scheduler;

    public void scheduleExpiration(DiscountKDTO discount) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(DiscountExpirationJob.class)
                .withIdentity("discountExpirationJob" + discount.discountId())
                .usingJobData("discountId", discount.discountId())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger" + discount.discountId())
                .startAt(Date.from(discount.endDate().atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

}
