package io.aeron.monitor.plugin.elastic;

import io.aeron.monitor.plugin.elastic.indexrotation.IndexRotationStrategyFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;

@Configuration
public class SpringApp {
    @Autowired
    private PluginConfig config;

    @Autowired
    private IndexRotationStrategyFactory indexRotationStrategyFactory;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        try {
            SSLContext sslContext = getSslContext();
            CredentialsProvider credentialsProvider = getCredentialsProvider();

            HttpHost httpHost = new HttpHost(this.config.getElasticHost(),
                    this.config.getElasticPort(),
                    this.config.getElasticScheme());

            return new RestHighLevelClient(
                    RestClient.builder(httpHost)
                            .setHttpClientConfigCallback(
                                    httpClientBuilder -> httpClientBuilder.setSSLContext(sslContext)
                                            .setDefaultCredentialsProvider(credentialsProvider)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SSLContext getSslContext() throws Exception {
        SSLContext sslContext = null;
        if (config.getElasticKeyStorePath() != null && !config.getElasticKeyStorePath().isEmpty()) {
            KeyStore keyStore = KeyStore.getInstance("jks");
            Path path = Paths.get(config.getElasticKeyStorePath());
            try (InputStream is = Files.newInputStream(path)) {
                keyStore.load(is, config.getElasticPassword().toCharArray());
            }
            SSLContextBuilder sslContextBuilder = SSLContexts.custom().loadTrustMaterial(keyStore,
                    null);
            sslContext = sslContextBuilder.build();
        }
        return sslContext;
    }

    private CredentialsProvider getCredentialsProvider() {
        CredentialsProvider credentialsProvider = null;
        if (config.getElasticUser() != null && !config.getElasticUser().isEmpty()) {
            credentialsProvider = new BasicCredentialsProvider();
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                    config.getElasticUser(),
                    config.getElasticPassword()
            );
            credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        }
        return credentialsProvider;
    }

    @Bean
    public ElasticSender sender() {
        return new ElasticSender(restHighLevelClient(),
                indexRotationStrategyFactory.indexNameStrategy(),
                config);
    }
}