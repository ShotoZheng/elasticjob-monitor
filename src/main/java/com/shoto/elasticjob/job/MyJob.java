package com.shoto.elasticjob.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.shoto.elasticjob.annotations.TaskJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author admin
 */
@Component
@Slf4j
@TaskJob(jobName = "my-monitored-job",
        cron = "/20 * * * * ? *",
        description = "自定义 actuator job 监控",
        shardingItemParameters = "0=0-1-2-3,1=4-5-6-7,2=8-9-10-11,3=12-13-14-15",
        shardingTotalCount = 4
)
public class MyJob implements MonitoredJob {
    @Override
    public void doExecute(ShardingContext shardingContext) {
        log.info("分片数信息：" + shardingContext.getShardingItem());
        log.info("分片参数：" + shardingContext.getShardingParameter());
    }
}
