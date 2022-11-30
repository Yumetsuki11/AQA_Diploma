package ru.netology.data;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.jupiter.api.Assertions;

import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {

    private static final Faker faker = new Faker();
    private static final Faker ruFaker = new Faker(Locale.forLanguageTag("ru"));

    private DataHelper() {
    }

    @Data
    @AllArgsConstructor
    public static class CardInfo {
        private String cardNumber;
        private String month;
        private String year;
        private String name;
        private String cvv;
    }

    public static String getApprovedCardNumber() {
        return "4444444444444441";
    }

    public static String getDeclinedCardNumber() {
        return "4444444444444442";
    }

    public static CardInfo getApprovedCardInfo() {
        return new CardInfo(getApprovedCardNumber(),
                generateMonthByCurr(3),
                generateYearByCurr(0, 3),
                getRandomName(),
                getRandomNonZeroCvv());
    }

    public static CardInfo getDeclinedCardInfo() {
        return new CardInfo(getDeclinedCardNumber(),
                generateMonthByCurr(3),
                generateYearByCurr(0, 3),
                getRandomName(),
                getRandomNonZeroCvv());
    }

    public static String getRandomCardNumber() {
        long number = faker.number().randomNumber(16, true);
        int i = 0;
        while (i < 3 && (number == Long.parseLong(getApprovedCardNumber()) || number == Long.parseLong(getDeclinedCardNumber()))) {
            number = faker.number().randomNumber(16, true);
            i++;
        }
        return String.valueOf(number);
    }

    public static String generateYearByCurr(int years, int months) {
        return LocalDate.now().plusYears(years).plusMonths(months).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String generateMonthByCurr(int months) {
        return LocalDate.now().plusMonths(months).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getRandomName() {
        String name = faker.name().name();
        name.replace(".", "");
        name.replace("-", "");
        name.replace("\'", "");
        return name;
    }

    public static String getRandomNonZeroCvv() {
        return faker.numerify("1##");
    }

    public static String getEmptyValue() {
        return "";
    }

    public static String getAllZerosCardNumber() {
        return "0000000000000000";
    }

    public static String get15DigitNumber() {
        return faker.numerify("###############");
    }

    public static String get1DigitNumber() {
        return faker.numerify("#");
    }

    public static String getMonth00orYear00() {
        return "00";
    }

    public static String getMonth01() {
        return "01";
    }

    public static String getMonth02() {
        return "02";
    }

    public static String getMonth11() {
        return "11";
    }

    public static String getMonth12() {
        return "12";
    }

    public static String getMonth13() {
        return "13";
    }

    public static String getInitials() {
        return faker.letterify("? ?");
    }

    public static String getNameOf27Symbols() {
        return faker.letterify("???????????? ??????????????");
    }

    public static String getNameOf28Symbols() {
        return faker.letterify("????????????? ??????????????");
    }

    public static String getNameWithHyphen() {
        return faker.letterify("????-????? ?????");
    }

    public static String getNameWithApostrophe() {
        return faker.letterify("?????? ?'???????");
    }

    public static String get1Letter() {
        return faker.letterify("?");
    }

    public static String get1Word() {
        return faker.letterify("?????");
    }

    public static String get3Words() {
        return faker.letterify("???? ?????? ???????");
    }

    public static String get1WordStartingWithSpace() {
        return faker.letterify(" ?????");
    }

    public static String get1WordEndingWithSpace() {
        return faker.letterify("????? ");
    }

    public static String getCyrillicName() {
        return ruFaker.name().firstName() + " " + ruFaker.name().lastName();
    }

    public static String getNumberWithSpace() {
        return faker.numerify("### #######");
    }

    public static String getSymbolsWithSpace() {
        return "!@#$%^&*() {}|/:'<>?";
    }

    public static String getZeroCvv() {
        return "000";
    }

    @SneakyThrows
    public static String getPaymentStatus() {
        var runner = new QueryRunner();
        var select = "select status FROM payment_entity;";
        Selenide.sleep(500);
        try (
                var conn = DriverManager
                        .getConnection(System.getProperty("db.url"), "app", "pass")
        ) {
            return runner.query(conn, select, new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static String getCreditStatus() {
        var runner = new QueryRunner();
        var select = "SELECT status FROM credit_request_entity;";
        Selenide.sleep(500);
        try (
                var conn = DriverManager
                        .getConnection(System.getProperty("db.url"), "app", "pass")
        ) {
            return runner.query(conn, select, new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static void deleteOldData() {
        var runner = new QueryRunner();
        String deleteOrders = "DELETE FROM order_entity;";
        String deletePayments = "DELETE FROM payment_entity;";
        String deleteCredits = "DELETE FROM credit_request_entity;";

        try (
                var conn = DriverManager
                        .getConnection(System.getProperty("db.url"), "app", "pass")
        ) {
            runner.update(conn, deleteOrders);
            runner.update(conn, deletePayments);
            runner.update(conn, deleteCredits);
        }
    }
}
