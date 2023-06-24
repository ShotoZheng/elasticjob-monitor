package com.shoto.elasticjob.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.shoto.elasticjob.annotations.TaskJob;
import com.shoto.elasticjob.service.GaugeMetricService;
import com.shoto.elasticjob.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * @author admin
 */
public interface MonitoredJob extends SimpleJob {

    Logger log = LoggerFactory.getLogger(MonitoredJob.class);

    /**
     * 增强 execute 方法，自定义上报 actuator 端点
     * @param shardingContext 分片信息
     */
    @Override
    default void execute(ShardingContext shardingContext) {
        try {
            Class<?> classTarget = this.getClass();
            TaskJob taskJob = classTarget.getAnnotation(TaskJob.class);
            GaugeMetricService gaugeMetricService = SpringContextUtil.getBean(GaugeMetricService.class);
            Assert.notNull(gaugeMetricService, "MonitoredJob error, gaugeMetricService is null");
            gaugeMetricService.gaugeMonitor(taskJob.jobName(), shardingContext.getShardingParameter(), taskJob.cron());
        } catch (Throwable var5) {
            log.error("doExecute failed", var5);
        }
        doExecute(shardingContext);
    }

    /**
     * 业务 job execute 方法
     * @param shardingContext 分片信息
     */
    void doExecute(ShardingContext shardingContext);
}
