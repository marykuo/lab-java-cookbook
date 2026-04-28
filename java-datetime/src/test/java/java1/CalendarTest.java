package java1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static java.util.TimeZone.getTimeZone;
import static org.assertj.core.api.Assertions.assertThat;

public class CalendarTest {

    private static final long ONE_HOUR_IN_MILLIS = 60 * 60 * 1000;
    private static final long ONE_DAY_IN_MILLIS = 24 * ONE_HOUR_IN_MILLIS;

    @Test
    @DisplayName("initial calendar from constructor")
    void initial_calendar_from_constructor_test() {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance(TimeZone.getDefault());
        Calendar calendar3 = Calendar.getInstance(Locale.getDefault());
        Calendar calendar4 = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
    }

    /**
     * Calendar 的 week definition 會影響 WEEK_OF_YEAR 的計算結果。
     */
    @Nested
    class calendar_week_definition_test {

        private static final long FIRST_DAY_IS_MONDAY = LocalDate.of(1973, 1, 1).toEpochDay() * ONE_DAY_IN_MILLIS;
        private static final long FIRST_DAY_IS_TUESDAY = LocalDate.of(1974, 1, 1).toEpochDay() * ONE_DAY_IN_MILLIS;
        private static final long FIRST_DAY_IS_WEDNESDAY = LocalDate.of(1975, 1, 1).toEpochDay() * ONE_DAY_IN_MILLIS;
        private static final long FIRST_DAY_IS_THURSDAY = LocalDate.of(1976, 1, 1).toEpochDay() * ONE_DAY_IN_MILLIS;
        private static final long FIRST_DAY_IS_FRIDAY = LocalDate.of(1982, 1, 1).toEpochDay() * ONE_DAY_IN_MILLIS;
        private static final long FIRST_DAY_IS_SATURDAY = LocalDate.of(1977, 1, 1).toEpochDay() * ONE_DAY_IN_MILLIS;
        private static final long FIRST_DAY_IS_SUNDAY = LocalDate.of(1978, 1, 1).toEpochDay() * ONE_DAY_IN_MILLIS;

        /**
         * ISO 8601：一年中第一個包含「至少 4 天」的週。
         * 只要 1 月 4 日那天出現在該週，那一週就算新年的第一週。
         */
        @Test
        @DisplayName("calendar week definition test")
        void iso_calendar_week_definition_test() {
            Calendar isoCalendar = new Calendar.Builder()
                    .setWeekDefinition(Calendar.MONDAY, 4)
                    .build();
            assertThat(isoCalendar.getFirstDayOfWeek()).isEqualTo(Calendar.MONDAY);
            assertThat(isoCalendar.getMinimalDaysInFirstWeek()).isEqualTo(4);

            isoCalendar.setTimeInMillis(FIRST_DAY_IS_MONDAY);
            assertThat(isoCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(1);

            isoCalendar.setTimeInMillis(FIRST_DAY_IS_TUESDAY);
            assertThat(isoCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(1);

            isoCalendar.setTimeInMillis(FIRST_DAY_IS_WEDNESDAY);
            assertThat(isoCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(1);

            isoCalendar.setTimeInMillis(FIRST_DAY_IS_THURSDAY);
            assertThat(isoCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(1);

            isoCalendar.setTimeInMillis(FIRST_DAY_IS_FRIDAY);
            assertThat(isoCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(53);

            isoCalendar.setTimeInMillis(FIRST_DAY_IS_SATURDAY);
            assertThat(isoCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(53);

            isoCalendar.setTimeInMillis(FIRST_DAY_IS_SUNDAY);
            assertThat(isoCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(52);
        }

        /**
         * 寬鬆模式：一年中第一個包含「至少 1 天」的週。
         * 只要 1 月 1 日那天出現在該週，那一週就算新年的第一週。
         */
        @Test
        @DisplayName("loose calendar week definition test")
        void loose_calendar_week_definition_test() {
            Calendar looseCalendar = new Calendar.Builder()
                    .setWeekDefinition(Calendar.MONDAY, 1)
                    .build();
            assertThat(looseCalendar.getFirstDayOfWeek()).isEqualTo(Calendar.MONDAY);
            assertThat(looseCalendar.getMinimalDaysInFirstWeek()).isEqualTo(1);

            looseCalendar.setTimeInMillis(FIRST_DAY_IS_MONDAY);
            assertThat(looseCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(1);

            looseCalendar.setTimeInMillis(FIRST_DAY_IS_TUESDAY);
            assertThat(looseCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(1);

            looseCalendar.setTimeInMillis(FIRST_DAY_IS_WEDNESDAY);
            assertThat(looseCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(1);

            looseCalendar.setTimeInMillis(FIRST_DAY_IS_THURSDAY);
            assertThat(looseCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(1);

            looseCalendar.setTimeInMillis(FIRST_DAY_IS_FRIDAY);
            assertThat(looseCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(1);

            looseCalendar.setTimeInMillis(FIRST_DAY_IS_SATURDAY);
            assertThat(looseCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(1);

            looseCalendar.setTimeInMillis(FIRST_DAY_IS_SUNDAY);
            assertThat(looseCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(1);
        }

        /**
         * 嚴格模式：一年中第一個包含「完整 7 天」的週。
         * 那一週必須「整整 7 天」都在 1970 年裡，才算 1970 的第一週。
         */
        @Test
        @DisplayName("strict calendar week definition test")
        void strict_calendar_week_definition_test() {
            Calendar strictCalendar = new Calendar.Builder()
                    .setWeekDefinition(Calendar.MONDAY, 7)
                    .build();
            assertThat(strictCalendar.getFirstDayOfWeek()).isEqualTo(Calendar.MONDAY);
            assertThat(strictCalendar.getMinimalDaysInFirstWeek()).isEqualTo(7);

            strictCalendar.setTimeInMillis(FIRST_DAY_IS_MONDAY);
            assertThat(strictCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(1);

            strictCalendar.setTimeInMillis(FIRST_DAY_IS_TUESDAY);
            assertThat(strictCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(53);

            strictCalendar.setTimeInMillis(FIRST_DAY_IS_WEDNESDAY);
            assertThat(strictCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(52);

            strictCalendar.setTimeInMillis(FIRST_DAY_IS_THURSDAY);
            assertThat(strictCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(52);

            strictCalendar.setTimeInMillis(FIRST_DAY_IS_FRIDAY);
            assertThat(strictCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(52);

            strictCalendar.setTimeInMillis(FIRST_DAY_IS_SATURDAY);
            assertThat(strictCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(52);

            strictCalendar.setTimeInMillis(FIRST_DAY_IS_SUNDAY);
            assertThat(strictCalendar.get(Calendar.WEEK_OF_YEAR)).isEqualTo(52);
        }

    }

    @Test
    @DisplayName("initial calendar by weekOfYear and dayOfWeek")
    void initial_calendar_by_week_of_year_and_day_of_week_test() {
        Calendar calendar = new Calendar.Builder()
                .setWeekDate(1970, 1, Calendar.MONDAY)
                .build();
        assertThat(calendar.getTime().toString()).isEqualTo("Mon Dec 29 00:00:00 CST 1969");
    }

    @Test
    @DisplayName("initial calendar by dayOfMonth")
    void initial_calendar_by_day_of_month_test() {
        Calendar calendar = new Calendar.Builder()
                .setDate(1970, Calendar.JANUARY, 1)
                .setTimeOfDay(8, 0, 0)
                .build();
        assertThat(calendar.getTimeInMillis()).isEqualTo(0L);
    }

    @Test
    @DisplayName("getter test")
    void getter_test() {
        // arrange
        TimeZone timeZone = getTimeZone("Asia/Taipei");
        Locale locale = Locale.getDefault();
        Date date = new Date(0L);

        Calendar calendar = Calendar.getInstance(timeZone, locale);
        calendar.setTime(date);

        // act & assert
        assertThat(calendar.get(Calendar.YEAR)).isEqualTo(1970);
        assertThat(calendar.get(Calendar.MONTH)).isEqualTo(0); // 0-based month
        assertThat(calendar.get(Calendar.DAY_OF_MONTH)).isEqualTo(1);
        assertThat(calendar.get(Calendar.HOUR_OF_DAY)).isEqualTo(8); // 8 hours ahead of UTC
        assertThat(calendar.get(Calendar.MINUTE)).isEqualTo(0);
        assertThat(calendar.get(Calendar.SECOND)).isEqualTo(0);

        assertThat(calendar.getTime()).isEqualTo(date);
        assertThat(calendar.getTimeInMillis()).isEqualTo(0L);
        assertThat(calendar.getTimeZone()).isEqualTo(timeZone);
        assertThat(calendar.getCalendarType()).isEqualTo("gregory"); // default calendar type is "Gregorian"
        assertThat(calendar.toInstant()).isEqualTo(date.toInstant());
    }

    @Test
    @DisplayName("set date using milliseconds since epoch")
    void setter_date_test() {
        // arrange
        long fixedTime = 0L; // 1970-01-01T00:00:00Z

        // act
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fixedTime);

        // assert
        assertThat(calendar.getTime().toString()).isEqualTo("Thu Jan 01 08:00:00 CST 1970");
        assertThat(calendar.getTimeInMillis()).isEqualTo(fixedTime);
    }

    @Test
    @DisplayName("compare date using before, after, equals and compareTo")
    void compare_date_test() {
        // arrange
        Calendar first = Calendar.getInstance();
        first.setTimeInMillis(0L);
        Calendar second = Calendar.getInstance();
        second.setTimeInMillis(1L);

        // act & assert
        assertThat(first.before(second)).isTrue();
        assertThat(second.before(first)).isFalse();
        assertThat(first.before(first)).isFalse();

        assertThat(first.after(second)).isFalse();
        assertThat(second.after(first)).isTrue();
        assertThat(first.after(first)).isFalse();

        assertThat(first.equals(second)).isFalse();
        assertThat(second.equals(first)).isFalse();
        assertThat(first.equals(first)).isTrue();

        assertThat(first.compareTo(second)).isLessThan(0);
        assertThat(second.compareTo(first)).isGreaterThan(0);
        assertThat(first.compareTo(first)).isEqualTo(0);
    }

}
