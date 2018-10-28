package io.aeron.monitor.plugin.elastic.indexrotation;

import io.aeron.monitor.plugin.elastic.PluginConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndexRotationStrategyFactory {

    @Autowired
    private PluginConfig config;

    public IndexNameStrategy indexNameStrategy() {
        final String indexRotation = config.getIndexRotation();

        IndexNameStrategy indexNameStrategy;
        if ("DAILY".equalsIgnoreCase(indexRotation)) {
            indexNameStrategy = new DailyStrategy();
        } else if ("WEEKLY".equalsIgnoreCase(indexRotation)) {
            indexNameStrategy = new WeeklyStrategy();
        } else if ("MONTHLY".equalsIgnoreCase(indexRotation)) {
            indexNameStrategy = new MonthlyStrategy();
        } else if ("YEARLY".equalsIgnoreCase(indexRotation)) {
            indexNameStrategy = new YearlyStrategy();
        } else if ("NEVER".equalsIgnoreCase(indexRotation)) {
            indexNameStrategy = new NoRotationStrategy();
        } else {
            throw new RuntimeException();
        }

        indexNameStrategy.setIndex(config.getElasticIndexName());
        return indexNameStrategy;
    }
}
