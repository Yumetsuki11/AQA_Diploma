package ru.netology.data;

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

public class DataHelper {

    private static final Faker faker = new Faker();

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
        while (number == Long.parseLong(getApprovedCardNumber()) || number == Long.parseLong(getDeclinedCardNumber()))  {
            number = faker.number().randomNumber(16, true);
        }
        return String.valueOf(number);
    }

    public static String generateYearByCurr(int years, int months) {
        if (LocalDate.now().getMonthValue() + months <= 12) {
            return String.valueOf((LocalDate.now().getYear() + years) % 100);
        } else {
            int yearsToAdd = (LocalDate.now().getMonthValue() + months - 1) / 12;
            return String.valueOf((LocalDate.now().getYear() + years + yearsToAdd) % 100);
        }
    }

    public static String generateMonthByCurr(int months) {
        int finalMonth = (LocalDate.now().getMonthValue() + months) % 12;
        if (finalMonth == 0) {
            return String.valueOf(12);
        } else if (finalMonth > 9) {
            return String.valueOf(finalMonth);
        } else {
            return "0" + finalMonth;
        }
    }

    public static String getRandomName() {
        String name = faker.name().name();
        while (name.contains(".") || name.contains("-") || name.contains("'")) {
            name = faker.name().name();
        }
        return name;
    }

    public static String getRandomNonZeroCvv() {
        long cvv = faker.number().randomNumber(3, false);
        if (cvv > 99) {
            return String.valueOf(cvv);
        } else if (cvv > 9) {
            return "0" + cvv;
        } else if (cvv > 0) {
            return "00" + cvv;
        } else {
            return "001";
        }
    }

    @SneakyThrows
    public static String getPaymentStatus() {
        var runner = new QueryRunner();
        var select = "select status FROM payment_entity;";

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

        try (
                var conn = DriverManager
                        .getConnection(System.getProperty("db.url"), "app", "pass")
        ) {
            return runner.query(conn, select, new ScalarHandler<>());
        }
    }

    public static void approvedStatusAssert(String status) {
        Assertions.assertEquals("APPROVED", status);
    }

    public static void declinedStatusAssert(String status) {
        Assertions.assertEquals("DECLINED", status);
    }

    public static void nullStatusAssert(String status) {
        Assertions.assertEquals(null, status);
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
