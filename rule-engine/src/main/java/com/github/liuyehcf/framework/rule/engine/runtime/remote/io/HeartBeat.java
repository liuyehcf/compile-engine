package com.github.liuyehcf.framework.rule.engine.runtime.remote.io;

import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterNodeConfig;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterTopology;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.message.HeartbeatMessage;
import io.netty.channel.ChannelFuture;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
class HeartBeat extends BaseScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartBeat.class);

    private static final String DEFAULT_GROUP = "heartBeat";
    private static final String KEY_CHANNEL = "channel";
    private static final String KEY_EVENT_LOOP = "eventLoop";

    static void register(ClusterEventLoop clusterEventLoop, ClusterChannel clusterChannel, int heartbeatInterval) {
        String name = UUID.randomUUID().toString();

        JobDetail jobDetail = JobBuilder.newJob(HeartBeatJob.class)
                .withIdentity(name, DEFAULT_GROUP)
                .build();

        jobDetail.getJobDataMap().put(KEY_CHANNEL, clusterChannel);
        jobDetail.getJobDataMap().put(KEY_EVENT_LOOP, clusterEventLoop);

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, DEFAULT_GROUP)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(heartbeatInterval).repeatForever())
                .startAt(new Date(System.currentTimeMillis() + heartbeatInterval * 1000))
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
            clusterChannel.getChannel().closeFuture().addListener((ChannelFuture future) -> {
                if (scheduler.unscheduleJob(trigger.getKey())) {
                    LOGGER.info("Unregister heartbeat due to channel closed, channel={}", future.channel());
                }
            });
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public static final class HeartBeatJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            JobDetail jobDetail = context.getJobDetail();
            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            ClusterChannel clusterChannel = (ClusterChannel) jobDataMap.get(KEY_CHANNEL);
            ClusterEventLoop clusterEventLoop = (ClusterEventLoop) jobDataMap.get(KEY_EVENT_LOOP);

            ClusterNodeConfig selfConfig = clusterEventLoop.getEngine()
                    .getProperties()
                    .getSelfConfig();

            ClusterTopology topology = clusterEventLoop.getTopology();

            clusterChannel.write(new HeartbeatMessage(selfConfig.getIdentifier(), topology.getIdentifier()))
                    .addListener((ChannelFuture future) -> {
                        if (!future.isSuccess()) {
                            Trigger trigger = context.getTrigger();
                            Scheduler scheduler = context.getScheduler();
                            if (scheduler.unscheduleJob(trigger.getKey())) {
                                LOGGER.info("Unregister heartbeat due to writing failure, channel={}", future.channel());
                            }
                        }
                    });
        }
    }
}
