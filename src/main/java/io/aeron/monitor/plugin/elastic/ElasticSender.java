package io.aeron.monitor.plugin.elastic;

import io.aeron.monitor.DriverAccess;
import io.aeron.monitor.DriverAccessSupport;
import io.aeron.monitor.plugin.elastic.indexrotation.IndexNameStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ElasticSender {

    public static final String MEDIA_DRIVER_STAT = "mediaDriverStat";

    private RestHighLevelClient highLevelClient;
    private RestClient restClient;
    private IndexNameStrategy indexNameStrategy;
    private PluginConfig pluginConfig;

    private String currentIndex;

    public ElasticSender(RestHighLevelClient client,
                         IndexNameStrategy indexNameStrategy,
                         PluginConfig config) {
        this.highLevelClient = client;
        this.restClient = client.getLowLevelClient();
        this.indexNameStrategy = indexNameStrategy;
        this.pluginConfig = config;
        this.currentIndex = null;
    }

    public void send(Map<String, DriverAccess> driverAccessMap) {
        try {
            String indexName = indexNameStrategy.indexName();
            if (currentIndex == null || !indexName.equals(currentIndex)) {
                ensureIndexAndMappingCreated(indexName);
            }

            driverAccessMap.forEach((s, access) -> {
                Map<String, Object> jsonMap = new HashMap<>();
                jsonMap.put("timeStamp", System.currentTimeMillis());
                jsonMap.put("name", access.getName());
                jsonMap.put("cncVersion", access.getCncVersion());
                jsonMap.put("consumerHeartbeatTime", access.getConsumerHeartbeatTime());
                jsonMap.put("startTimeStamp", access.getStartTimeStamp());

                DriverAccessSupport.getSystemCounters(access).forEach(counter -> {
                    jsonMap.put(toCamelCase(counter.getDescriptor().name()), counter.getValue());
                });

                IndexRequest indexRequest = new IndexRequest(indexName, MEDIA_DRIVER_STAT)
                        .source(jsonMap);
                try {
                    IndexResponse indexResponse = highLevelClient.index(indexRequest);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ensureIndexAndMappingCreated(String indexName) throws IOException {
        Response resp = restClient.performRequest("HEAD", indexName);
        if (!"OK".equals(resp.getStatusLine().getReasonPhrase())) {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            highLevelClient.indices().create(createIndexRequest);
        }

        String data = getResourceFileAsString(MEDIA_DRIVER_STAT + ".json");
        String endpoint = indexName + "/_mapping/" + MEDIA_DRIVER_STAT;
        HttpEntity entity = new NStringEntity(data, ContentType.APPLICATION_JSON);
        resp = restClient.performRequest("PUT", endpoint, Collections.emptyMap(), entity);
        currentIndex = indexName;
    }

    private String getResourceFileAsString(String fileName) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        if (is == null) {
            throw new RuntimeException("File not found: " + fileName);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }

    private String toCamelCase(String descriptor) {
        String [] arr = descriptor.split("_");
        StringBuilder builder = new StringBuilder(arr[0].toLowerCase());
        for (int i = 1; i < arr.length; i++) {
            builder.append(arr[i].substring(0, 1))
                .append(arr[i].substring(1).toLowerCase());
        }
        return builder.toString();

    }
}
