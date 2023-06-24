package com.shoto.elasticjob.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author admin
 */
@Slf4j
@Service
public class CronTimeAfterService {

    /**
     * 以当前时间为基准按照 cron 计算下次 job 的执行时间，并转成 double 值返回
     * @param cron cron 表达式
     * @return 下次 job 执行时间的 double 值
     */
    public double dnReturnTimeAfter(String cron) {
        double timeAfter = 0.0D;
        if (StringUtils.isEmpty(cron) || !CronExpression.isValidExpression(cron)) {
            return  timeAfter;
        }
        try {
            CronExpression cronExpression = new CronExpression(cron);
            Date dateV = cronExpression.getTimeAfter(new Date());
            timeAfter = (double) (dateV.getTime() / 1000L);
        } catch (Exception var7) {
            log.error("dnReturnTimeAfter exec failed", var7);
        }
        return timeAfter;
    }
}