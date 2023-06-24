package com.shoto.elasticjob.domain;

import com.shoto.elasticjob.service.CronTimeAfterService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.ToDoubleFunction;

/**
 * @author admin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DnReturnGaugeMetric implements ToDoubleFunction<CronTimeAfterService> {
    private String cron;

    /**
     * Applies this function to the given argument.
     *
     * @param value the function argument
     * @return the function result
     */
    @Override
    public double applyAsDouble(CronTimeAfterService value) {
        return value.dnReturnTimeAfter(this.cron);
    }
}
