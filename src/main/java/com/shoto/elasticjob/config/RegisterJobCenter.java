package com.shoto.elasticjob.config;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.shoto.elasticjob.annotations.TaskJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 获取 Task 注解的 job 信息并注入到 Elastic-Job 中
 * @author admin
 */
@Component
@Slf4j
public class RegisterJobCenter implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    @Resource
    private ZookeeperRegistryCenter regCenter;

    @Override
    public void afterPropertiesSet() {
        Map<String, Object> registerJobs = applicationContext.getBeansWithAnnotation(TaskJob.class);
        for (Map.Entry<String, Object> entry : registerJobs.entrySet()) {
            try {
                Object object = entry.getValue();
                if (!(object instanceof ElasticJob)) {
                    throw new ClassCastException("[" + object.getClass().getName()
                            + "] The class type is not com.dangdang.ddframe.job.api.ElasticJob");
                }
                TaskJob task = AnnotationUtils.findAnnotation(object.getClass(), TaskJob.class);
                assert task != null;
                SpringJobScheduler springJobScheduler = new SpringJobScheduler((ElasticJob) object, regCenter,
                        getJobConfiguration(task, object));
                springJobScheduler.init();
                log.info("JobName: {} Register Successfully .", task.jobName());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * description: 构建 Job 配置
     */
    private LiteJobConfiguration getJobConfiguration(TaskJob taskJob, Object object) {
        // 参数校验
        Assert.notNull(taskJob.jobName(), "The jobName cannot be null !");
        Assert.notNull(taskJob.cron(), "The cron cannot be null !");
        Assert.notNull(taskJob.description(), "The description cannot be null !");
        // 构建配置
        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(JobCoreConfiguration
                .newBuilder(taskJob.jobName(), taskJob.cron(), taskJob.shardingTotalCount())
                .shardingItemParameters(
                        StringUtils.isEmpty(taskJob.shardingItemParameters()) ? null : taskJob.shardingItemParameters())
                .description(taskJob.description()).failover(taskJob.failover())
                .jobParameter(StringUtils.isEmpty(taskJob.jobParameter()) ? null : taskJob.jobParameter()).build(),
                object.getClass().getName());
        return LiteJobConfiguration.newBuilder(simpleJobConfiguration).overwrite(taskJob.overwrite())
                .monitorExecution(true).build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}

