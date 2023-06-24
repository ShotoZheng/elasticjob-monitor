package com.shoto.elasticjob.service;

import com.shoto.elasticjob.domain.DnReturnGaugeMetric;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author admin
 */
@Slf4j
@Service
public class GaugeMetricService {

    @Resource
    private CronTimeAfterService cronTimeAfterService;

    public void gaugeMonitor(String taskJobName, String shardingParameter, String cron) {
        log.info("GaugeMetric taskJobName=" + taskJobName);
        if (StringUtils.isEmpty(taskJobName) || StringUtils.isEmpty(cron) || !CronExpression.isValidExpression(cron)) {
            return;
        }
        try {
            Map<String, String> tags = new LinkedHashMap<>();
            tags.put("sharding", StringUtils.isEmpty(shardingParameter) ? "" : shardingParameter);
            tags.put("taskJob", taskJobName);
            DnReturnGaugeMetric dnReturnGaugeMetric = new DnReturnGaugeMetric(cron);
            Iterable<Tag> tag = getTagList(tags, dnReturnGaugeMetric);
            log.info("tag=" + tag.toString() + ";value=" + dnReturnGaugeMetric.applyAsDouble(this.cronTimeAfterService));
            // 上报 job 下次执行时间的 double 值
            Metrics.gauge("simple_job_monitor", tag, this.cronTimeAfterService, dnReturnGaugeMetric);
        } catch (Exception var7) {
            log.warn("GaugeMetric gaugeMonitor failed:{}", var7.getMessage());
        }
    }

    private Iterable<Tag> getTagList(Map<String, String> tagsMap, DnReturnGaugeMetric dnReturnGaugeMetric) {
        Iterable<Tag> tagsWithId = Collections.emptyList();
        Map.Entry<String, String> entry;
        if (tagsMap != null && tagsMap.size() > 0) {
            for(Iterator<Map.Entry<String, String>> var4 = tagsMap.entrySet().iterator(); var4.hasNext();
                tagsWithId = Tags.concat(tagsWithId, entry.getKey(), entry.getValue())) {
                entry = var4.next();
            }
        }
        return tagsWithId;
    }
}