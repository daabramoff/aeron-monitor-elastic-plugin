package io.aeron.monitor.plugin.elastic.indexrotation;

public abstract class IndexNameStrategy {
    private String index;

    protected abstract String indexNameSuffix();

    public String indexName() {
        String suffix = indexNameSuffix();
        StringBuilder builder = new StringBuilder();
        builder.append(index);
        if (!"".equals(index)) {
            builder.append("-").append(suffix);
        }
        return builder.toString();
     }

    IndexNameStrategy setIndex(String index) {
        this.index = index;
        return this;
    }
}
