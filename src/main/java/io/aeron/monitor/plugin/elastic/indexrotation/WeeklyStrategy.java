package io.aeron.monitor.plugin.elastic.indexrotation;

import java.util.Calendar;

public class WeeklyStrategy extends IndexNameStrategy {

    @Override
    protected String indexNameSuffix() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) + "-" + c.getWeekYear();
    }
}
