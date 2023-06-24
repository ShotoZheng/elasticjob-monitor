package com.shoto.elasticjob.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 自定义 elastic-job 任务注解
 * @author admin
 */
@Component
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskJob {

    /**
     * corn 表达式
     */
    String cron() default "";

    /**
     * 任务名称
     */
    String jobName() default "";

    /**
     * 分片数量
     */
    int shardingTotalCount() default 1;

    /**
     * 分片参数
     */
    String shardingItemParameters() default "";

    /**
     * 任务参数
     */
    String jobParameter() default "";

    String dataSource() default "";

    /**
     * 任务描述
     */
    String description() default "";

    boolean disabled() default false;

    boolean overwrite() default true;

    /**
     * 是否快速失败
     */
    boolean failover() default true;

    boolean monitorExecution() default true;
}
