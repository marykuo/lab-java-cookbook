package java1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LocaleTest {

    /**
     * JVM read OS environment variable to set default locale, and you can also set it by JVM argument (e.g. -Duser.language=zh -Duser.country=TW).
     * <p>
     * defaultLocale：代表這個 JVM 程序（Process） 的「基本身分」。當你呼叫 locale.toString() 或進行一些與顯示/格式無關的邏輯運算時，會參考它。
     * defaultDisplayLocale：專門負責 UI 語系，對應到環境變數 'LC_MESSAGES'。
     * defaultFormatLocale：專門負責 Formatter 的語系，對應到環境變數 'LC_TIME', 'LC_NUMERIC', 'LC_MONETARY'。
     */
    @Test
    void default_locale_test() {
        // since java 1.1
        // only accept one locale setting, mapping to environment variable of 'LANG'
        Locale locale1 = Locale.getDefault();

        // since java 1.7
        Locale localeDisplay = Locale.getDefault(Locale.Category.DISPLAY);
        Locale localeFormat = Locale.getDefault(Locale.Category.FORMAT);
    }

    /**
     * 混合語系：
     * <p>
     * 假設有一位工程師，他在台灣公司工作，但使用的是德文版的作業系統（UI），而他正在為美國客戶處理報表資料。
     */
    @Test
    void default_category_test() {
        // set default locale to US (basic identity)
        Locale.setDefault(Locale.US);

        // set default display locale to Germany
        Locale.setDefault(Locale.Category.DISPLAY, Locale.GERMANY);

        // set default format locale to TW (讓日期、數字符合台灣習慣)
        Locale.setDefault(Locale.Category.FORMAT, Locale.TRADITIONAL_CHINESE);

        // basic identity
        assertThatThrownBy(() -> ResourceBundle.getBundle("messages"))
                .hasMessage("Can't find bundle for base name messages, locale en_US");

        // format get by formatter, e.g., NumberFormat, DateFormat
        Date now = new Date(0); // 1970-01-01T00:00:00Z
        assertThat(new SimpleDateFormat().format(now)).isEqualTo("1970/1/1 上午8:00");

        // display
        assertThat(Locale.FRANCE.getDisplayLanguage()).isEqualTo("Französisch");
        assertThat(Locale.FRANCE.getDisplayLanguage(Locale.getDefault())).isEqualTo("French");
        assertThat(Locale.FRANCE.getDisplayLanguage(Locale.getDefault(Locale.Category.DISPLAY))).isEqualTo("Französisch");
        assertThat(Locale.FRANCE.getDisplayLanguage(Locale.getDefault(Locale.Category.FORMAT))).isEqualTo("法文");
    }

    /**
     * These methods do not make any syntactic checks on the input.
     */
    @Test
    @DisplayName("initial locale")
    void initial_locale_test() {
        // since java 1.7
        // Returns a locale for the specified IETF BCP 47 language tag string.
        Locale locale = Locale.forLanguageTag("zh-Hant-TW");
        assertThat(locale.toLanguageTag()).isEqualTo("zh-Hant-TW");

        // since java 19
        // This method normalizes the language value to lowercase.
        Locale locale1 = Locale.of("zh");
        Locale locale2 = Locale.of("zh", "Hant");
        Locale locale3 = Locale.of("zh", "Hant", "TW");
        assertThat(locale1.toLanguageTag()).isEqualTo("zh");
        assertThat(locale2.toLanguageTag()).isEqualTo("zh");
        assertThat(locale3.toLanguageTag()).isEqualTo("zh-x-lvariant-TW");

//        Locale locale = Locale.lookup();
    }

    /**
     * Use Locale.Builder for full syntactic checks with BCP47.
     *
     * @since 1.7
     */
    @Test
    @DisplayName("initial locale by builder")
    void initial_locale_builder_test() {
        // 當你的參數是從不同變數過來時（例如：從資料庫分別抓出語言和地區碼）
        Locale explicitSetting = new Locale.Builder()
                .setLanguage("zh") // 語言（中文）
                .setScript("Hant") // 繁體
                .setRegion("TW") // country
                .setVariant("")  // 變體
                .build();

        // 使用 IETF BCP 47 標準的字串
        // 同時是 HTTP Header 的 Accept-Language 的標準格式
        String languageTagString = "zh-Hant-TW";
        Locale languageTag = new Locale.Builder()
                .setLanguageTag(languageTagString)
                .build();

        assertThat(explicitSetting.toLanguageTag()).isEqualTo(languageTagString);
        assertThat(languageTag.toLanguageTag()).isEqualTo(languageTagString);
    }

    /**
     * make a copy of exists locale
     */
    @Test
    @DisplayName("copy locale")
    void copy_test() {
        Locale locale = new Locale.Builder()
                .setLanguageTag("zh-Hant-TW")
                .setExtension('u', "ca-chinese")
                .build();

        // Resets the Builder to match the provided locale. Existing state is discarded.
        Locale copy = new Locale.Builder().setLocale(locale).build();
        assertThat(copy.hasExtensions()).isTrue();

        // Returns a copy of this Locale with no extensions. If this Locale has no extensions, this Locale is returned.
        Locale copy2 = locale.stripExtensions();
        assertThat(copy2.hasExtensions()).isFalse();
    }

    /**
     * language: zh (Chinese).
     * script: Hant (Traditional Han). 指的是書寫系統（繁體中文）。
     * country/variant: TW (Taiwan), HK (Hong Kong). 指的是地理區域（影響貨幣、日期格式）。
     * variant: 通常用於非常特殊的場景（例如 NY 代表挪威語的特定方言，或 POSIX 代表電腦環境標準）。
     */
    @Test
    @DisplayName("getter locale test")
    void getter_test() {
        Locale locale = new Locale.Builder()
                .setLanguage("zh")
                .setScript("Hant")
                .setRegion("TW") // country
                .setVariant("")
                .build();

        assertThat(locale.toString()).isEqualTo("zh_TW_#Hant");

        assertThat(locale.toLanguageTag()).isEqualTo("zh-Hant-TW");
        assertThat(locale.getLanguage()).isEqualTo("zh");
        assertThat(locale.getDisplayLanguage()).isEqualTo("中文");
        assertThat(locale.getISO3Language()).isEqualTo("zho");

        assertThat(locale.getScript()).isEqualTo("Hant");
        assertThat(locale.getDisplayScript()).isEqualTo("繁體");

        assertThat(locale.getCountry()).isEqualTo("TW");
        assertThat(locale.getDisplayCountry()).isEqualTo("台灣");

        assertThat(locale.getVariant()).isEqualTo("");
        assertThat(locale.getDisplayVariant()).isEqualTo("");
    }

}
