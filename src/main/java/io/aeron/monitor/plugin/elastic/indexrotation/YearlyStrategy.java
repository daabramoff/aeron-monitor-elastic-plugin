package io.aeron.monitor.plugin.elastic.indexrotation;

import java.util.Calendar;

public class YearlyStrategy extends IndexNameStrategy {
    @Override
    protected String indexNameSuffix() {
        Calendar c = Calendar.getInstance();
        return String.valueOf(c.get(Calendar.YEAR));
    }
}
