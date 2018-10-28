package io.aeron.monitor.plugin.elastic.indexrotation;

public class NoRotationStrategy extends IndexNameStrategy {

    @Override
    protected String indexNameSuffix() {
        return "";
    }
}
