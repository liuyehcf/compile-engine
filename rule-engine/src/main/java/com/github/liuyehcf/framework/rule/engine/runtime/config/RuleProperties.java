package com.github.liuyehcf.framework.rule.engine.runtime.config;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterConfig;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterNodeConfig;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.io.protocol.SerializeType;
import com.github.liuyehcf.framework.rule.engine.util.AddressUtils;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author hechenfeng
 * @date 2019/9/6
 */
public class RuleProperties extends HashMap<String, Object> {

    public ClusterConfig getClusterConfig() {
        return getDefault(Property.CLUSTER_CONFIG);
    }

    public void setClusterConfig(ClusterConfig clusterConfig) {
        Assert.assertNotNull(clusterConfig, "clusterConfig");

        // self
        ClusterNodeConfig self = clusterConfig.getSelf();
        if (self == null) {
            clusterConfig.setSelf(self = new ClusterNodeConfig());
        }

        if (StringUtils.isBlank(self.getHost())) {
            self.setHost(Property.HOST.getDefaultValue());
        }
        Assert.assertTrue(AddressUtils.isLocalIp(self.getHost()), String.format("host '%s' doesn't match any local network interfaces", self.getHost()));
        if (self.getPort() == null) {
            self.setPort(Property.PORT.getDefaultValue());
        } else {
            Assert.assertTrue(1024 <= self.getPort() && self.getPort() <= 65535, "self.port must between 1024 and 65535");
        }

        // seed
        List<ClusterNodeConfig> seeds = clusterConfig.getSeeds();
        Assert.assertNotEmpty(seeds, "cluster's seeds is empty");
        Set<String> seedNodeIdentifiers = Sets.newHashSet();
        boolean isSeed = false;
        for (ClusterNodeConfig seed : seeds) {
            Assert.assertNotNull(seed, "seed");
            Assert.assertNotNull(seed.getHost(), "seed.host");
            if (seed.getPort() == null) {
                seed.setPort(Property.PORT.getDefaultValue());
            } else {
                Assert.assertTrue(1024 <= seed.getPort() && seed.getPort() <= 65535, "seed.port must between 1024 and 65535");
            }
            Assert.assertTrue(seedNodeIdentifiers.add(String.format("%s:%d", seed.getHost(), seed.getPort())), "duplicate seeds");

            if (Objects.equals(self.getHost(), seed.getHost())
                    && Objects.equals(self.getPort(), seed.getPort())) {
                isSeed = true;
            }
        }

        // idle time
        Integer idleTime = clusterConfig.getIdleTime();
        if (idleTime == null) {
            clusterConfig.setIdleTime(idleTime = Property.IDLE_TIME.getDefaultValue());
        }

        // heartbeat interval
        Integer heartbeatInterval = clusterConfig.getHeartbeatInterval();
        if (heartbeatInterval == null) {
            clusterConfig.setHeartbeatInterval(heartbeatInterval = Property.HEARTBEAT_INTERVAL.getDefaultValue());
        }

        // heartbeat retry interval
        Integer heartbeatRetryInterval = clusterConfig.getHeartbeatRetryInterval();
        if (heartbeatInterval == null) {
            clusterConfig.setHeartbeatRetryInterval(heartbeatRetryInterval = Property.HEARTBEAT_RETRY_INTERVAL.getDefaultValue());
        }

        // topology keep alive interval
        Integer topologyKeepAliveInterval = clusterConfig.getTopologyKeepAliveInterval();
        if (topologyKeepAliveInterval == null) {
            clusterConfig.setTopologyKeepAliveInterval(topologyKeepAliveInterval = Property.TOPOLOGY_KEEP_LIVE_INTERVAL.getDefaultValue());
        }

        // serializeType
        String serializeType = clusterConfig.getSerializeType();
        SerializeType serializeEnum;
        if (StringUtils.isBlank(serializeType)) {
            serializeEnum = Property.SERIALIZE_TYPE.getDefaultValue();
            clusterConfig.setSerializeType(serializeEnum.name());
        } else {
            serializeEnum = SerializeType.valueOf(serializeType);
        }

        put(Property.RUNTIME_MODE, RuntimeMode.cluster);
        put(Property.HOST, self.getHost());
        put(Property.PORT, self.getPort());
        put(Property.IS_SEED, isSeed);
        put(Property.IDLE_TIME, idleTime);
        put(Property.HEARTBEAT_INTERVAL, heartbeatInterval);
        put(Property.HEARTBEAT_RETRY_INTERVAL, heartbeatRetryInterval);
        put(Property.TOPOLOGY_KEEP_LIVE_INTERVAL, topologyKeepAliveInterval);
        put(Property.SERIALIZE_TYPE, serializeEnum);
        put(Property.CLUSTER_CONFIG, clusterConfig);
    }

    public ClusterNodeConfig getSelfConfig() {
        return getClusterConfig().getSelf();
    }

    public boolean isClusterMode() {
        return RuntimeMode.cluster.equals(getRuntimeMode());
    }

    public RuntimeMode getRuntimeMode() {
        return getDefault(Property.RUNTIME_MODE);
    }

    public String getHost() {
        return getDefault(Property.HOST);
    }

    public int getPort() {
        return getDefault(Property.PORT);
    }

    public boolean isSeed() {
        return getDefault(Property.IS_SEED);
    }

    public int getIdleTime() {
        return getDefault(Property.IDLE_TIME);
    }

    public int getHeartbeatInterval() {
        return getDefault(Property.HEARTBEAT_INTERVAL);
    }

    public int getHeartbeatRetryInterval() {
        return getDefault(Property.HEARTBEAT_RETRY_INTERVAL);
    }

    public int getTopologyKeepAliveInterval() {
        return getDefault(Property.TOPOLOGY_KEEP_LIVE_INTERVAL);
    }

    public SerializeType getSerializeType() {
        return getDefault(Property.SERIALIZE_TYPE);
    }

    @SuppressWarnings("unchecked")
    private <T> T getDefault(Property property) {
        return (T) getOrDefault(property.getKey(), property.getDefaultValue());
    }

    private void put(Property property, Object value) {
        put(property.getKey(), value);
    }

    private enum Property {
        RUNTIME_MODE("runtimeMode", RuntimeMode.singleton),
        HOST("host", "127.0.0.1"),
        PORT("port", 16689),
        IS_SEED("isSeed", false),
        IDLE_TIME("idleTime", 60),
        HEARTBEAT_INTERVAL("heartbeatInterval", 5),
        HEARTBEAT_RETRY_INTERVAL("heartbeatRetryInterval", 5),
        TOPOLOGY_KEEP_LIVE_INTERVAL("topologyKeepAliveInterval", 5),
        SERIALIZE_TYPE("serializeType", SerializeType.hessian),
        CLUSTER_CONFIG("clusterConfig", null);

        private final String key;
        private final Object defaultValue;

        Property(String key, Object defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public String getKey() {
            return key;
        }

        @SuppressWarnings("unchecked")
        public <T> T getDefaultValue() {
            return (T) defaultValue;
        }
    }
}
