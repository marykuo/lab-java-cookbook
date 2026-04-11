package java1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class DateTest {

    @Test
    @DisplayName("initial date using milliseconds since epoch")
    void initial_date_test() {
        // arrange
        long fixedTime = 0L; // 1970-01-01T00:00:00Z

        // act
        Date actual = new Date(fixedTime);

        // assert
        assertThat(actual.toString()).isEqualTo("Thu Jan 01 08:00:00 CST 1970");
        assertThat(actual.getTime()).isEqualTo(fixedTime);
    }

    @Test
    @DisplayName("set date using milliseconds since epoch")
    void set_date_test() {
        // arrange
        long fixedTime = 0L; // 1970-01-01T00:00:00Z

        // act
        Date actual = new Date();
        actual.setTime(fixedTime);

        // assert
        assertThat(actual.toString()).isEqualTo("Thu Jan 01 08:00:00 CST 1970");
        assertThat(actual.getTime()).isEqualTo(fixedTime);
    }

    @Test
    @DisplayName("compare date using before, after, equals and compareTo")
    void compare_date_test() {
        // arrange
        Date first = new Date(0L);
        Date second = new Date(1L);

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
