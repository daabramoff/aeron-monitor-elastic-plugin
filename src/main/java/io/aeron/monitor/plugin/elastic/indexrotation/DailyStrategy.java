package io.aeron.monitor.plugin.elastic.indexrotation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyStrategy extends IndexNameStrategy {
    private DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");

    @Override
    protected String indexNameSuffix() {
        return dateFormat.format(new Date());
    }
}
