package io.oddworks.device;

import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import java.util.Map;

public class Util {
    /**
     * Returns {@code nullString} for null or {@code o.toString()}.
     */
    @Nullable public static String getString(Map<String, Object> map, String key, String nullString) {
        Object o = map.get(key);
        return (o == null) ? nullString : o.toString();
    }

    /**
     * Returns {@code nullInteger} for null or {@code o.tokrotkokt}
     */
    @Nullable public static Integer getInteger(Map<String, Object> map, String key, Integer nullInteger) {
        Object o = map.get(key);

        try {
            return ((Number) o).intValue();
        } catch (Exception e) {
            return nullInteger;
        }
    }

    /**
     * Returns {@code nullDateTime} for null or {@code DateTime.parse(o.toString())}
     **/
    @Nullable public static DateTime getDateTime(Map<String, Object> map, String key, DateTime nullDateTime) {
        Object o = map.get(key);

        try {
            return DateTime.parse(o.toString());
        } catch (Exception e) {
            return nullDateTime;
        }
    }

    private Util() {
        // nope
    }
}
