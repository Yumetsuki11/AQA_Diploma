package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.StartingPage;

import static com.codeborne.selenide.Selenide.open;

public class AppTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        DataHelper.deleteOldData();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
        Selenide.closeWindow();
    }

    @BeforeEach
    public void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:8080");
    }

    @AfterEach
    public void cleanup() {
        DataHelper.deleteOldData();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Integration")
    @DisplayName("01-01 Позитивные сценарии оплаты тура. Прямая оплата с карты статуса APPROVED")
    public void test0101() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        page.choosePayment().pay(info);
        page.choosePayment().successNotificationAssertion();
        Assertions.assertEquals("APPROVED", DataHelper.getPaymentStatus());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Integration")
    @DisplayName("01-02 Позитивные сценарии оплаты тура. Оплата в кредит с карты статуса APPROVED")
    public void test0102() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        page.chooseCredit().pay(info);
        page.chooseCredit().successNotificationAssertion();
        Assertions.assertEquals("APPROVED", DataHelper.getCreditStatus());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Integration")
    @DisplayName("02-01 Негативные сценарии оплаты тура. Прямая оплата с карты статуса DECLINED")
    public void test0201() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        page.choosePayment().pay(info);
        page.choosePayment().errorNotificationAssertion();
        Assertions.assertEquals("DECLINED", DataHelper.getPaymentStatus());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Integration")
    @DisplayName("02-02 Негативные сценарии оплаты тура. Прямая оплата с несуществующей карты")
    public void test0202() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getRandomCardNumber());
        page.choosePayment().pay(info);
        page.choosePayment().errorNotificationAssertion();
        Assertions.assertNull(DataHelper.getPaymentStatus());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Integration")
    @DisplayName("02-03 Негативные сценарии оплаты тура. Оплата в кредит с карты статуса DECLINED")
    public void test0203() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        page.chooseCredit().pay(info);
        page.chooseCredit().errorNotificationAssertion();
        Assertions.assertEquals("DECLINED", DataHelper.getCreditStatus());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Integration")
    @DisplayName("02-04 Негативные сценарии оплаты тура. Оплата в кредит с несуществующей карты")
    public void test0204() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getRandomCardNumber());
        page.chooseCredit().pay(info);
        page.chooseCredit().errorNotificationAssertion();
        Assertions.assertNull(DataHelper.getCreditStatus());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Card number")
    @DisplayName("03-01 Поле \"Номер карты\", оплата по карте. Валидное значение. Номер карты (отсутствующей в базе) из 16 цифр, не полностью состоящий из нулей")
    public void test0301() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getRandomCardNumber());
        page.choosePayment().pay(info);
        page.choosePayment().errorNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Card number")
    @Feature("All-zero values handling")
    @DisplayName("03-02 Поле \"Номер карты\", оплата по карте. Валидное значение. Номер карты из 16 цифр, полностью состоящий из нулей")
    public void test0302() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber("0000000000000000");
        page.choosePayment().pay(info);
        page.choosePayment().errorNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Card number")
    @DisplayName("03-03 Поле \"Номер карты\", оплата по карте. Граничное невалидное значение. 15 цифр")
    public void test0303() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber("444444444444444");
        page.choosePayment().pay(info);
        page.choosePayment().invalidCardNumberAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Card number")
    @DisplayName("03-04 Поле \"Номер карты\", оплата по карте. Граничное невалидное значение. 1 цифра")
    public void test0304() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber("6");
        page.choosePayment().pay(info);
        page.choosePayment().invalidCardNumberAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Card number")
    @Feature("Empty fields handling")
    @DisplayName("03-05 Поле \"Номер карты\", оплата по карте. Граничное невалидное значение. Пустое поле")
    public void test0305() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber("");
        page.choosePayment().pay(info);
        page.choosePayment().invalidCardNumberAssertion("Поле обязательно для заполнения");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Card number")
    @DisplayName("04-01 Поле \"Номер карты\", оплата в кредит. Валидное значение. Номер карты (отсутствующей в базе) из 16 цифр, не полностью состоящий из нулей")
    public void test0401() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getRandomCardNumber());
        page.chooseCredit().pay(info);
        page.chooseCredit().errorNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Card number")
    @Feature("All-zero values handling")
    @DisplayName("04-02 Поле \"Номер карты\", оплата в кредит. Валидное значение. Номер карты из 16 цифр, полностью состоящий из нулей")
    public void test0402() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber("0000000000000000");
        page.chooseCredit().pay(info);
        page.chooseCredit().errorNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Card number")
    @DisplayName("04-03 Поле \"Номер карты\", оплата в кредит. Граничное невалидное значение. 15 цифр")
    public void test0403() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber("444444444444444");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidCardNumberAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Card number")
    @DisplayName("04-04 Поле \"Номер карты\", оплата в кредит. Граничное невалидное значение. 1 цифра")
    public void test0404() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber("6");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidCardNumberAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Card number")
    @Feature("Empty fields handling")
    @DisplayName("04-05 Поле \"Номер карты\", оплата в кредит. Граничное невалидное значение. Пустое поле")
    public void test0405() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber("");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidCardNumberAssertion("Поле обязательно для заполнения");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("05-01 Поле \"Месяц\", оплата по карте. Граничное валидное значение. Число 01 при годе, равном текущему+1")
    public void test0501() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("01");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.choosePayment().pay(info);
        page.choosePayment().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("05-02 Поле \"Месяц\", оплата по карте. Граничное валидное значение. Число 02 при годе, равном текущему+1")
    public void test0502() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("02");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.choosePayment().pay(info);
        page.choosePayment().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("05-03 Поле \"Месяц\", оплата по карте. Граничное валидное значение. Число 11 при годе, равном текущему+1")
    public void test0503() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("11");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.choosePayment().pay(info);
        page.choosePayment().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("05-04 Поле \"Месяц\", оплата по карте. Граничное валидное значение. Число 12 при годе, равном текущему+1")
    public void test0504() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("12");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.choosePayment().pay(info);
        page.choosePayment().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("05-05 Поле \"Месяц\", оплата по карте. Граничное валидное значение. Текущий месяц при текущем годе")
    public void test0505() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.generateMonthByCurr(0));
        info.setYear(DataHelper.generateYearByCurr(0, 0));
        page.choosePayment().pay(info);
        page.choosePayment().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Month")
    @Feature("All-zero values handling")
    @DisplayName("05-06 Поле \"Месяц\", оплата по карте. Граничное невалидное значение. Число 00 при годе, равном текущему+1")
    public void test0506() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("00");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.choosePayment().pay(info);
        page.choosePayment().invalidMonthAssertion("Неверно указан срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("05-07 Поле \"Месяц\", оплата по карте. Граничное невалидное значение. Число 13 при годе, равном текущему+1")
    public void test0507() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("13");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.choosePayment().pay(info);
        page.choosePayment().invalidMonthAssertion("Неверно указан срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("05-08 Поле \"Месяц\", оплата по карте. Текущий месяц-1 при текущем годе")
    public void test0508() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.generateMonthByCurr(-1));
        info.setYear(DataHelper.generateYearByCurr(0, 0));
        page.choosePayment().pay(info);
        page.choosePayment().invalidMonthAssertion("Неверно указан срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Month")
    @Feature("Empty fields handling")
    @DisplayName("05-09 Поле \"Месяц\", оплата по карте. Пустое поле")
    public void test0509() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.choosePayment().pay(info);
        page.choosePayment().invalidMonthAssertion("Поле обязательно для заполнения");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("05-10 Поле \"Месяц\", оплата по карте. Одна цифра")
    public void test0510() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("5");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.choosePayment().pay(info);
        page.choosePayment().invalidMonthAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("06-01 Поле \"Месяц\", оплата в кредит. Граничное валидное значение. Число 01 при годе, равном текущему+1")
    public void test0601() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("01");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.chooseCredit().pay(info);
        page.chooseCredit().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("06-02 Поле \"Месяц\", оплата в кредит. Граничное валидное значение. Число 02 при годе, равном текущему+1")
    public void test0602() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("02");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.chooseCredit().pay(info);
        page.chooseCredit().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("06-03 Поле \"Месяц\", оплата в кредит. Граничное валидное значение. Число 11 при годе, равном текущему+1")
    public void test0603() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("11");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.chooseCredit().pay(info);
        page.chooseCredit().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("06-04 Поле \"Месяц\", оплата в кредит. Граничное валидное значение. Число 12 при годе, равном текущему+1")
    public void test0604() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("12");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.chooseCredit().pay(info);
        page.chooseCredit().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("06-05 Поле \"Месяц\", оплата в кредит. Граничное валидное значение. Текущий месяц при текущем годе")
    public void test0605() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.generateMonthByCurr(0));
        info.setYear(DataHelper.generateYearByCurr(0, 0));
        page.chooseCredit().pay(info);
        page.chooseCredit().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Month")
    @Feature("All-zero values handling")
    @DisplayName("06-06 Поле \"Месяц\", оплата в кредит. Граничное невалидное значение. Число 00 при годе, равном текущему+1")
    public void test0606() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("00");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidMonthAssertion("Неверно указан срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("06-07 Поле \"Месяц\", оплата в кредит. Граничное невалидное значение. Число 13 при годе, равном текущему+1")
    public void test0607() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("13");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidMonthAssertion("Неверно указан срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("06-08 Поле \"Месяц\", оплата в кредит. Текущий месяц-1 при текущем годе")
    public void test0608() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.generateMonthByCurr(-1));
        info.setYear(DataHelper.generateYearByCurr(0, 0));
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidMonthAssertion("Неверно указан срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Month")
    @Feature("Empty fields handling")
    @DisplayName("06-09 Поле \"Месяц\", оплата в кредит. Пустое поле")
    public void test0609() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidMonthAssertion("Поле обязательно для заполнения");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("06-10 Поле \"Месяц\", оплата в кредит. Одна цифра")
    public void test0610() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("5");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidMonthAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Year")
    @DisplayName("07-01 Поле \"Год\", оплата по карте. Граничное валидное значение. Текущий год+5")
    public void test0701() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(5, 0));
        page.choosePayment().pay(info);
        page.choosePayment().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Year")
    @DisplayName("07-02 Поле \"Год\", оплата по карте. Граничное невалидное значение. Текущий год-1")
    public void test0702() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(-1, 0));
        page.choosePayment().pay(info);
        page.choosePayment().invalidYearAssertion("Истёк срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Year")
    @DisplayName("07-03 Поле \"Год\", оплата по карте. Граничное невалидное значение. Текущий год+6")
    public void test0703() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(6, 0));
        page.choosePayment().pay(info);
        page.choosePayment().invalidYearAssertion("Неверно указан срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Year")
    @Feature("Empty fields handling")
    @DisplayName("07-04 Поле \"Год\", оплата по карте. Невалидное значение. Пустое поле")
    public void test0704() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear("");
        page.choosePayment().pay(info);
        page.choosePayment().invalidYearAssertion("Поле обязательно для заполнения");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Year")
    @DisplayName("07-05 Поле \"Год\", оплата по карте. Невалидное значение. Одна цифра")
    public void test0705() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear("2");
        page.choosePayment().pay(info);
        page.choosePayment().invalidYearAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Year")
    @Feature("All-zero values handling")
    @DisplayName("07-06 Поле \"Год\", оплата по карте. Невалидное значение. 00")
    public void test0706() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear("00");
        page.choosePayment().pay(info);
        page.choosePayment().invalidYearAssertion("Истёк срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Year")
    @DisplayName("08-01 Поле \"Год\", оплата в кредит. Граничное валидное значение. Текущий год+5")
    public void test0801() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(5, 0));
        page.chooseCredit().pay(info);
        page.chooseCredit().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Year")
    @DisplayName("08-02 Поле \"Год\", оплата в кредит. Граничное невалидное значение. Текущий год-1")
    public void test0802() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(-1, 0));
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidYearAssertion("Истёк срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Year")
    @DisplayName("08-03 Поле \"Год\", оплата в кредит. Граничное невалидное значение. Текущий год+6")
    public void test0803() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(6, 0));
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidYearAssertion("Неверно указан срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Year")
    @Feature("Empty fields handling")
    @DisplayName("08-04 Поле \"Год\", оплата в кредит. Невалидное значение. Пустое поле")
    public void test0804() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear("");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidYearAssertion("Поле обязательно для заполнения");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Year")
    @DisplayName("08-05 Поле \"Год\", оплата в кредит. Невалидное значение. Одна цифра")
    public void test0805() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear("2");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidYearAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Year")
    @Feature("All-zero values handling")
    @DisplayName("08-06 Поле \"Год\", оплата в кредит. Невалидное значение. 00")
    public void test0806() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear("00");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidYearAssertion("Истёк срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-01 Поле \"Владелец\", оплата по карте. Граничное по длине валидное значение. Латиница с пробелом посередине, 3 символа")
    public void test0901() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("X Y");
        page.choosePayment().pay(info);
        page.choosePayment().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-02 Поле \"Владелец\", оплата по карте. Граничное по длине валидное значение. Латиница с пробелом посередине, 27 символов")
    public void test0902() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Amogus Sussussybakaimpostor");
        page.choosePayment().pay(info);
        page.choosePayment().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-03 Поле \"Владелец\", оплата по карте. Валидное значение. Латиница с пробелом посередине и с дефисом")
    public void test0903() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Anni-Frid Lyngstad");
        page.choosePayment().pay(info);
        page.choosePayment().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-04 Поле \"Владелец\", оплата по карте. Валидное значение. Латиница с пробелом посередине и с апострофом")
    public void test0904() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Gilbert O'Sullivan");
        page.choosePayment().pay(info);
        page.choosePayment().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-05 Поле \"Владелец\", оплата по карте. Граничное по длине невалидное значение. Латиница, 28 символов")
    public void test0905() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Amogus Susssussybakaimpostor");
        page.choosePayment().pay(info);
        page.choosePayment().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @Feature("Empty fields handling")
    @DisplayName("09-06 Поле \"Владелец\", оплата по карте. Невалидное значение. Пустое поле")
    public void test0906() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("");
        page.choosePayment().pay(info);
        page.choosePayment().invalidNameAssertion("Поле обязательно для заполнения");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-07 Поле \"Владелец\", оплата по карте. Граничное по длине невалидное значение. Одна латинская буква")
    public void test0907() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("a");
        page.choosePayment().pay(info);
        page.choosePayment().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-08 Поле \"Владелец\", оплата по карте. Граничное по числу пробелов невалидное значение. Латиница без пробелов")
    public void test0908() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Yumetsuki");
        page.choosePayment().pay(info);
        page.choosePayment().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-09 Поле \"Владелец\", оплата по карте. Граничное по числу пробелов невалидное значение. Латиница с двумя пробелами посередине")
    public void test0909() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("I love Finger");
        page.choosePayment().pay(info);
        page.choosePayment().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-10 Поле \"Владелец\", оплата по карте. Невалидное значение. Латиница с пробелом в начале")
    public void test0910() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(" Yumetsuki");
        page.choosePayment().pay(info);
        page.choosePayment().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-11 Поле \"Владелец\", оплата по карте. Невалидное значение. Латиница с пробелом в конце")
    public void test0911() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Yumetsuki ");
        page.choosePayment().pay(info);
        page.choosePayment().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-12 Поле \"Владелец\", оплата по карте. Невалидное значение. Кириллица с пробелом посередине")
    public void test0912() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Евгений Поливанов");
        page.choosePayment().pay(info);
        page.choosePayment().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-13 Поле \"Владелец\", оплата по карте. Невалидное значение. Цифры с пробелом посередине")
    public void test0913() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("505 8425662");
        page.choosePayment().pay(info);
        page.choosePayment().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-14 Поле \"Владелец\", оплата по карте. Невалидное значение. Спецсимволы с пробелом посередине")
    public void test0914() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("!@#$%^&*() {}|/:'<>?");
        page.choosePayment().pay(info);
        page.choosePayment().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-01 Поле \"Владелец\", оплата в кредит. Граничное по длине валидное значение. Латиница с пробелом посередине, 3 символа")
    public void test1001() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("X Y");
        page.chooseCredit().pay(info);
        page.chooseCredit().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-02 Поле \"Владелец\", оплата в кредит. Граничное по длине валидное значение. Латиница с пробелом посередине, 27 символов")
    public void test1002() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Amogus Sussussybakaimpostor");
        page.chooseCredit().pay(info);
        page.chooseCredit().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-03 Поле \"Владелец\", оплата в кредит. Валидное значение. Латиница с пробелом посередине и с дефисом")
    public void test1003() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Anni-Frid Lyngstad");
        page.chooseCredit().pay(info);
        page.chooseCredit().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-04 Поле \"Владелец\", оплата в кредит. Валидное значение. Латиница с пробелом посередине и с апострофом")
    public void test1004() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Gilbert O'Sullivan");
        page.chooseCredit().pay(info);
        page.chooseCredit().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-05 Поле \"Владелец\", оплата в кредит. Граничное по длине невалидное значение. Латиница, 28 символов")
    public void test1005() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Amogus Susssussybakaimpostor");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @Feature("Empty fields handling")
    @DisplayName("10-06 Поле \"Владелец\", оплата в кредит. Невалидное значение. Пустое поле")
    public void test1006() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidNameAssertion("Поле обязательно для заполнения");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-07 Поле \"Владелец\", оплата в кредит. Граничное по длине невалидное значение. Одна латинская буква")
    public void test1007() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("a");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-08 Поле \"Владелец\", оплата в кредит. Граничное по числу пробелов невалидное значение. Латиница без пробелов")
    public void test1008() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Yumetsuki");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-09 Поле \"Владелец\", оплата в кредит. Граничное по числу пробелов невалидное значение. Латиница с двумя пробелами посередине")
    public void test1009() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("I love Finger");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-10 Поле \"Владелец\", оплата в кредит. Невалидное значение. Латиница с пробелом в начале")
    public void test1010() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(" Yumetsuki");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-11 Поле \"Владелец\", оплата в кредит. Невалидное значение. Латиница с пробелом в конце")
    public void test1011() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Yumetsuki ");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-12 Поле \"Владелец\", оплата в кредит. Невалидное значение. Кириллица с пробелом посередине")
    public void test1012() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Евгений Поливанов");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-13 Поле \"Владелец\", оплата в кредит. Невалидное значение. Цифры с пробелом посередине")
    public void test1013() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("505 8425662");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-14 Поле \"Владелец\", оплата в кредит. Невалидное значение. Спецсимволы с пробелом посередине")
    public void test1014() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("!@#$%^&*() {}|/:'<>?");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("CVV/CVC")
    @DisplayName("11-01 Поле \"CVC/CVV\", оплата по карте. Валидное значение. 3 цифры, исключая \"000\"")
    public void test1101() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        page.choosePayment().pay(info);
        page.choosePayment().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("CVV/CVC")
    @Feature("All-zero values handling")
    @DisplayName("11-02 Поле \"CVC/CVV\", оплата по карте. Валидное значение. \"000\"")
    public void test1102() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv("000");
        page.choosePayment().pay(info);
        page.choosePayment().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("CVV/CVC")
    @DisplayName("11-03 Поле \"CVC/CVV\", оплата по карте. Граничное невалидное значение. 1 цифра")
    public void test1103() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv("8");
        page.choosePayment().pay(info);
        page.choosePayment().invalidCvvAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("CVV/CVC")
    @Feature("Empty fields handling")
    @DisplayName("11-04 Поле \"CVC/CVV\", оплата по карте. Невалидное значение. Пустое поле")
    public void test1104() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv("");
        page.choosePayment().pay(info);
        page.choosePayment().invalidCvvAssertion("Поле обязательно для заполнения");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("CVV/CVC")
    @DisplayName("12-01 Поле \"CVC/CVV\", оплата в кредит. Валидное значение. 3 цифры, исключая \"000\"")
    public void test1201() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        page.chooseCredit().pay(info);
        page.chooseCredit().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("CVV/CVC")
    @Feature("All-zero values handling")
    @DisplayName("12-02 Поле \"CVC/CVV\", оплата в кредит. Валидное значение. \"000\"")
    public void test1202() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv("000");
        page.chooseCredit().pay(info);
        page.chooseCredit().successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("CVV/CVC")
    @DisplayName("12-03 Поле \"CVC/CVV\", оплата в кредит. Граничное невалидное значение. 1 цифра")
    public void test1203() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv("8");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidCvvAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("CVV/CVC")
    @Feature("Empty fields handling")
    @DisplayName("12-04 Поле \"CVC/CVV\", оплата в кредит. Невалидное значение. Пустое поле")
    public void test1204() {
        StartingPage page = new StartingPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv("");
        page.chooseCredit().pay(info);
        page.chooseCredit().invalidCvvAssertion("Поле обязательно для заполнения");
    }
}
