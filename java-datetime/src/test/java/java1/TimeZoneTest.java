package java1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

import static java.util.TimeZone.getDefault;
import static java.util.TimeZone.getTimeZone;
import static org.assertj.core.api.Assertions.assertThat;

public class TimeZoneTest {

    private static final int ONE_HOUR_IN_MILLIS = 60 * 60 * 1000;

    @Test
    @DisplayName("default time zone")
    void default_time_zone_test() {
        TimeZone defaultTimeZone = getDefault();

        assertThat(defaultTimeZone.getID()).isEqualTo("Asia/Taipei");
    }

    @Test
    @DisplayName("initial time zone")
    void initial_time_zone_test() {
        TimeZone timeZone1 = getTimeZone("Asia/Taipei");
        TimeZone timeZone2 = getTimeZone("GMT+8");
        TimeZone timeZone3 = getTimeZone("GMT+08:00");

        assertThat(timeZone1.getID()).isEqualTo("Asia/Taipei");
        assertThat(timeZone2.getID()).isEqualTo("GMT+08:00");
        assertThat(timeZone3.getID()).isEqualTo("GMT+08:00");
    }

    @Test
    @DisplayName("TimeZone of Asia/Taipei")
    void taipei_getter_test() {
        // arrange
        Locale.setDefault(Locale.forLanguageTag("zh-Hant-TW"));

        // act
        TimeZone timeZone = getTimeZone("Asia/Taipei");

        // assert
        assertThat(timeZone.getID()).isEqualTo("Asia/Taipei");

        assertThat(timeZone.getDisplayName()).isEqualTo("台北標準時間");
        assertThat(timeZone.getDisplayName(false, TimeZone.SHORT)).isEqualTo("TST");
        assertThat(timeZone.getDisplayName(true, TimeZone.SHORT)).isEqualTo("TDT");
        assertThat(timeZone.getDisplayName(false, TimeZone.LONG)).isEqualTo("台北標準時間");
        assertThat(timeZone.getDisplayName(true, TimeZone.LONG)).isEqualTo("台北夏令時間");

        assertThat(timeZone.getRawOffset()).isEqualTo(8 * ONE_HOUR_IN_MILLIS);
        assertThat(timeZone.getOffset(0L)).isEqualTo(8 * ONE_HOUR_IN_MILLIS);
        assertThat(timeZone.getDSTSavings()).isEqualTo(0);
    }

    @Test
    @DisplayName("TimeZone of America/New_York")
    void new_york_getter_test() {
        // arrange
        Locale.setDefault(Locale.forLanguageTag("zh-Hant-TW"));

        // act
        TimeZone timeZone = getTimeZone("America/New_York");

        // assert
        assertThat(timeZone.getID()).isEqualTo("America/New_York");

        assertThat(timeZone.getDisplayName()).isEqualTo("東部標準時間");
        assertThat(timeZone.getDisplayName(false, TimeZone.SHORT)).isEqualTo("EST");
        assertThat(timeZone.getDisplayName(true, TimeZone.SHORT)).isEqualTo("EDT");
        assertThat(timeZone.getDisplayName(false, TimeZone.LONG)).isEqualTo("東部標準時間");
        assertThat(timeZone.getDisplayName(true, TimeZone.LONG)).isEqualTo("東部夏令時間");

        // the raw offset is -5 hours
        assertThat(timeZone.getRawOffset()).isEqualTo(-5 * ONE_HOUR_IN_MILLIS);

        // 2026-03-08 01:59:59 is not daylight saving time, so the offset is -5 hours
        long notDaylight = LocalDateTime.of(2026, 3, 8, 1, 59, 59).atZone(ZoneId.of("America/New_York")).toInstant().toEpochMilli();
        assertThat(timeZone.getOffset(notDaylight)).isEqualTo(-5 * ONE_HOUR_IN_MILLIS);

        // 2026-03-08 02:00:00 is daylight saving time, so the offset is -4 hours
        long daylight = LocalDateTime.of(2026, 3, 8, 2, 0, 0).atZone(ZoneId.of("America/New_York")).toInstant().toEpochMilli();
        assertThat(timeZone.getOffset(daylight)).isEqualTo(-4 * ONE_HOUR_IN_MILLIS);

        // the DST savings is 1 hour
        assertThat(timeZone.getDSTSavings()).isEqualTo(ONE_HOUR_IN_MILLIS);
    }

}
