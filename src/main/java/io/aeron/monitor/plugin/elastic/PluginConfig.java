package io.aeron.monitor.plugin.elastic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:${filename}")
public class PluginConfig {
    @Value("${elastic.host}") private String elasticHost;
    @Value("${elastic.port}") private int elasticPort;
    @Value("${elastic.scheme}") private String elasticScheme;
    @Value("${elastic.user}") private String elasticUser;
    @Value("${elastic.password}") private String elasticPassword;
    @Value("${elastic.keystore.path}") private String elasticKeyStorePath;
    @Value("${elastic.keystore.password}") private String elasticKeyStorePassword;

    @Value("${data.index_name}") private String dataIndexName;
    @Value("${data.index_rotation}") private String indexRotation;
    @Value("${data.create_index_if_not_exists}") private String createIndexIfNotExists;

    @Value("${data.send_interval_value}") private long sendIntervalValue;
    @Value("${data.send_interval_unit}") private String sendIntervalUnit;
    @Value("${data.index_name}") private String indexName;

    @Value("${data.driver_metrics.enabled}") private boolean dataDriverMetrics;

    public String getElasticHost() {
        return elasticHost;
    }

    public PluginConfig setElasticHost(String elasticHost) {
        this.elasticHost = elasticHost;
        return this;
    }

    public int getElasticPort() {
        return elasticPort;
    }

    public PluginConfig setElasticPort(int elasticPort) {
        this.elasticPort = elasticPort;
        return this;
    }

    public String getElasticScheme() {
        return elasticScheme;
    }

    public PluginConfig setElasticScheme(String elasticScheme) {
        this.elasticScheme = elasticScheme;
        return this;
    }

    public String getElasticUser() {
        return elasticUser;
    }

    public PluginConfig setElasticUser(String elasticUser) {
        this.elasticUser = elasticUser;
        return this;
    }

    public String getElasticPassword() {
        return elasticPassword;
    }

    public PluginConfig setElasticPassword(String elasticPassword) {
        this.elasticPassword = elasticPassword;
        return this;
    }

    public String getElasticKeyStorePath() {
        return elasticKeyStorePath;
    }

    public PluginConfig setElasticKeyStorePath(String elasticKeyStorePath) {
        this.elasticKeyStorePath = elasticKeyStorePath;
        return this;
    }

    public String getElasticKeyStorePassword() {
        return elasticKeyStorePassword;
    }

    public PluginConfig setElasticKeyStorePassword(String elasticKeyStorePassword) {
        this.elasticKeyStorePassword = elasticKeyStorePassword;
        return this;
    }

    public String getDataIndexName() {
        return dataIndexName;
    }

    public PluginConfig setDataIndexName(String dataIndexName) {
        this.dataIndexName = dataIndexName;
        return this;
    }

    public String getIndexRotation() {
        return indexRotation;
    }

    public PluginConfig setIndexRotation(String indexRotation) {
        this.indexRotation = indexRotation;
        return this;
    }

    public String getCreateIndexIfNotExists() {
        return createIndexIfNotExists;
    }

    public PluginConfig setCreateIndexIfNotExists(String createIndexIfNotExists) {
        this.createIndexIfNotExists = createIndexIfNotExists;
        return this;
    }

    public long getSendIntervalValue() {
        return sendIntervalValue;
    }

    public PluginConfig setSendIntervalValue(long sendIntervalValue) {
        this.sendIntervalValue = sendIntervalValue;
        return this;
    }

    public String getSendIntervalUnit() {
        return sendIntervalUnit;
    }

    public PluginConfig setSendIntervalUnit(String sendIntervalUnit) {
        this.sendIntervalUnit = sendIntervalUnit;
        return this;
    }

    public boolean isDataDriverMetrics() {
        return dataDriverMetrics;
    }

    public PluginConfig setDataDriverMetrics(boolean dataDriverMetrics) {
        this.dataDriverMetrics = dataDriverMetrics;
        return this;
    }

    public String getIndexName() {
        return indexName;
    }

    public PluginConfig setIndexName(String indexName) {
        this.indexName = indexName;
        return this;
    }
}
