package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

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
    @Severity(SeverityLevel.NORMAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("User interaction")
    @DisplayName("01-01 Позитивные сценарии оплаты тура. Прямая оплата с карты статуса APPROVED. Сообщение пользователю")
    public void test0101() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        page.standardPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("DB interaction")
    @DisplayName("01-01а Позитивные сценарии оплаты тура. Прямая оплата с карты статуса APPROVED. Взаимодействие с БД")
    public void test0101a() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        page.standardPayment(info);
        Selenide.sleep(10000);
        DataHelper.approvedStatusAssert(DataHelper.getPaymentStatus());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("User interaction")
    @DisplayName("01-02 Позитивные сценарии оплаты тура. Оплата в кредит с карты статуса APPROVED. Сообщение пользователю")
    public void test0102() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        page.creditPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("DB interaction")
    @DisplayName("01-02a Позитивные сценарии оплаты тура. Оплата в кредит с карты статуса APPROVED. Взаимодействие с БД")
    public void test0102a() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        page.creditPayment(info);
        Selenide.sleep(10000);
        DataHelper.approvedStatusAssert(DataHelper.getCreditStatus());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @DisplayName("02-01 Негативные сценарии оплаты тура. Прямая оплата с карты статуса DECLINED. Сообщение пользователю")
    public void test0201() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        page.standardPayment(info);
        page.errorNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("DB interaction")
    @DisplayName("02-01a Негативные сценарии оплаты тура. Прямая оплата с карты статуса DECLINED. Взаимодействие с БД")
    public void test0201a() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        page.standardPayment(info);
        Selenide.sleep(10000);
        DataHelper.declinedStatusAssert(DataHelper.getPaymentStatus());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @DisplayName("02-02 Негативные сценарии оплаты тура. Прямая оплата с несуществующей карты. Сообщение пользователю")
    public void test0202() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getRandomCardNumber());
        page.standardPayment(info);
        page.errorNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("DB interaction")
    @DisplayName("02-02a Негативные сценарии оплаты тура. Прямая оплата с несуществующей карты. Взаимодействие с БД")
    public void test0202a() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getRandomCardNumber());
        page.standardPayment(info);
        Selenide.sleep(10000);
        DataHelper.nullStatusAssert(DataHelper.getPaymentStatus());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @DisplayName("02-03 Негативные сценарии оплаты тура. Оплата в кредит с карты статуса DECLINED. Сообщение пользователю")
    public void test0203() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        page.creditPayment(info);
        page.errorNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("DB interaction")
    @DisplayName("02-03a Негативные сценарии оплаты тура. Оплата в кредит с карты статуса DECLINED. Взаимодействие с БД")
    public void test0203a() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        page.creditPayment(info);
        Selenide.sleep(10000);
        DataHelper.declinedStatusAssert(DataHelper.getCreditStatus());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @DisplayName("02-04 Негативные сценарии оплаты тура. Оплата в кредит с несуществующей карты. Сообщение пользователю")
    public void test0204() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getRandomCardNumber());
        page.creditPayment(info);
        page.errorNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("DB interaction")
    @DisplayName("02-04a Негативные сценарии оплаты тура. Оплата в кредит с несуществующей карты. Взаимодействие с БД")
    public void test0204a() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getRandomCardNumber());
        page.creditPayment(info);
        Selenide.sleep(10000);
        DataHelper.nullStatusAssert(DataHelper.getCreditStatus());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Card number")
    @DisplayName("03-01 Поле \"Номер карты\", оплата по карте. Валидное значение. Номер карты (отсутствующей в базе) из 16 цифр, не полностью состоящий из нулей")
    public void test0301() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getRandomCardNumber());
        page.standardPayment(info);
        page.errorNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Card number")
    @Feature("All-zero values handling")
    @DisplayName("03-02 Поле \"Номер карты\", оплата по карте. Валидное значение. Номер карты из 16 цифр, полностью состоящий из нулей")
    public void test0302() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber("0000000000000000");
        page.standardPayment(info);
        page.errorNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Card number")
    @DisplayName("03-03 Поле \"Номер карты\", оплата по карте. Граничное невалидное значение. 15 цифр")
    public void test0303() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber("444444444444444");
        page.standardPayment(info);
        page.incorrectCardNumberAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Card number")
    @DisplayName("03-04 Поле \"Номер карты\", оплата по карте. Граничное невалидное значение. 1 цифра")
    public void test0304() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber("6");
        page.standardPayment(info);
        page.incorrectCardNumberAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Card number")
    @Feature("Empty fields handling")
    @DisplayName("03-05 Поле \"Номер карты\", оплата по карте. Граничное невалидное значение. Пустое поле")
    public void test0305() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber("");
        page.standardPayment(info);
        page.emptyCardNumberAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Card number")
    @DisplayName("04-01 Поле \"Номер карты\", оплата в кредит. Валидное значение. Номер карты (отсутствующей в базе) из 16 цифр, не полностью состоящий из нулей")
    public void test0401() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getRandomCardNumber());
        page.creditPayment(info);
        page.errorNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Card number")
    @Feature("All-zero values handling")
    @DisplayName("04-02 Поле \"Номер карты\", оплата в кредит. Валидное значение. Номер карты из 16 цифр, полностью состоящий из нулей")
    public void test0402() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber("0000000000000000");
        page.creditPayment(info);
        page.errorNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Card number")
    @DisplayName("04-03 Поле \"Номер карты\", оплата в кредит. Граничное невалидное значение. 15 цифр")
    public void test0403() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber("444444444444444");
        page.creditPayment(info);
        page.incorrectCardNumberAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Card number")
    @DisplayName("04-04 Поле \"Номер карты\", оплата в кредит. Граничное невалидное значение. 1 цифра")
    public void test0404() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber("6");
        page.creditPayment(info);
        page.incorrectCardNumberAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Card number")
    @Feature("Empty fields handling")
    @DisplayName("04-05 Поле \"Номер карты\", оплата в кредит. Граничное невалидное значение. Пустое поле")
    public void test0405() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber("");
        page.creditPayment(info);
        page.emptyCardNumberAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Month")
    @DisplayName("05-01 Поле \"Месяц\", оплата по карте. Граничное валидное значение. Число 01 при годе, равном текущему+1")
    public void test0501() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("01");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.standardPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Month")
    @DisplayName("05-02 Поле \"Месяц\", оплата по карте. Граничное валидное значение. Число 02 при годе, равном текущему+1")
    public void test0502() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("02");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.standardPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Month")
    @DisplayName("05-03 Поле \"Месяц\", оплата по карте. Граничное валидное значение. Число 11 при годе, равном текущему+1")
    public void test0503() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("11");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.standardPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Month")
    @DisplayName("05-04 Поле \"Месяц\", оплата по карте. Граничное валидное значение. Число 12 при годе, равном текущему+1")
    public void test0504() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("12");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.standardPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Month")
    @DisplayName("05-05 Поле \"Месяц\", оплата по карте. Граничное валидное значение. Текущий месяц при текущем годе")
    public void test0505() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.generateMonthByCurr(0));
        info.setYear(DataHelper.generateYearByCurr(0, 0));
        page.standardPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Month")
    @Feature("All-zero values handling")
    @DisplayName("05-06 Поле \"Месяц\", оплата по карте. Граничное невалидное значение. Число 00 при годе, равном текущему+1")
    public void test0506() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("00");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.standardPayment(info);
        page.incorrectMonthAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Month")
    @DisplayName("05-07 Поле \"Месяц\", оплата по карте. Граничное невалидное значение. Число 13 при годе, равном текущему+1")
    public void test0507() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("13");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.standardPayment(info);
        page.incorrectMonthAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Month")
    @DisplayName("05-08 Поле \"Месяц\", оплата по карте. Текущий месяц-1 при текущем годе")
    public void test0508() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.generateMonthByCurr(-1));
        info.setYear(DataHelper.generateYearByCurr(0, 0));
        page.standardPayment(info);
        page.incorrectMonthAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Month")
    @Feature("Empty fields handling")
    @DisplayName("05-09 Поле \"Месяц\", оплата по карте. Пустое поле")
    public void test0509() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.standardPayment(info);
        page.emptyMonthAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Month")
    @DisplayName("05-10 Поле \"Месяц\", оплата по карте. Одна цифра")
    public void test0510() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("5");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.standardPayment(info);
        page.incorrectMonthFormatAssert();
    }
    
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Month")
    @DisplayName("06-01 Поле \"Месяц\", оплата в кредит. Граничное валидное значение. Число 01 при годе, равном текущему+1")
    public void test0601() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("01");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.creditPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Month")
    @DisplayName("06-02 Поле \"Месяц\", оплата в кредит. Граничное валидное значение. Число 02 при годе, равном текущему+1")
    public void test0602() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("02");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.creditPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Month")
    @DisplayName("06-03 Поле \"Месяц\", оплата в кредит. Граничное валидное значение. Число 11 при годе, равном текущему+1")
    public void test0603() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("11");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.creditPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Month")
    @DisplayName("06-04 Поле \"Месяц\", оплата в кредит. Граничное валидное значение. Число 12 при годе, равном текущему+1")
    public void test0604() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("12");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.creditPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Month")
    @DisplayName("06-05 Поле \"Месяц\", оплата в кредит. Граничное валидное значение. Текущий месяц при текущем годе")
    public void test0605() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.generateMonthByCurr(0));
        info.setYear(DataHelper.generateYearByCurr(0, 0));
        page.creditPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Month")
    @Feature("All-zero values handling")
    @DisplayName("06-06 Поле \"Месяц\", оплата в кредит. Граничное невалидное значение. Число 00 при годе, равном текущему+1")
    public void test0606() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("00");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.creditPayment(info);
        page.incorrectMonthAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Month")
    @DisplayName("06-07 Поле \"Месяц\", оплата в кредит. Граничное невалидное значение. Число 13 при годе, равном текущему+1")
    public void test0607() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("13");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.creditPayment(info);
        page.incorrectMonthAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Month")
    @DisplayName("06-08 Поле \"Месяц\", оплата в кредит. Текущий месяц-1 при текущем годе")
    public void test0608() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.generateMonthByCurr(-1));
        info.setYear(DataHelper.generateYearByCurr(0, 0));
        page.creditPayment(info);
        page.incorrectMonthAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Month")
    @Feature("Empty fields handling")
    @DisplayName("06-09 Поле \"Месяц\", оплата в кредит. Пустое поле")
    public void test0609() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.creditPayment(info);
        page.emptyMonthAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Month")
    @DisplayName("06-10 Поле \"Месяц\", оплата в кредит. Одна цифра")
    public void test0610() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth("5");
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        page.creditPayment(info);
        page.incorrectMonthFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Year")
    @DisplayName("07-01 Поле \"Год\", оплата по карте. Граничное валидное значение. Текущий год+5")
    public void test0701() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(5, 0));
        page.standardPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Year")
    @DisplayName("07-02 Поле \"Год\", оплата по карте. Граничное невалидное значение. Текущий год-1")
    public void test0702() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(-1, 0));
        page.standardPayment(info);
        page.tooEarlyYearAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Year")
    @DisplayName("07-03 Поле \"Год\", оплата по карте. Граничное невалидное значение. Текущий год+6")
    public void test0703() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(6, 0));
        page.standardPayment(info);
        page.incorrectYearAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Year")
    @Feature("Empty fields handling")
    @DisplayName("07-04 Поле \"Год\", оплата по карте. Невалидное значение. Пустое поле")
    public void test0704() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear("");
        page.standardPayment(info);
        page.emptyYearAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Year")
    @DisplayName("07-05 Поле \"Год\", оплата по карте. Невалидное значение. Одна цифра")
    public void test0705() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear("2");
        page.standardPayment(info);
        page.incorrectYearFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Year")
    @Feature("All-zero values handling")
    @DisplayName("07-06 Поле \"Год\", оплата по карте. Невалидное значение. 00")
    public void test0706() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear("00");
        page.standardPayment(info);
        page.tooEarlyYearAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Year")
    @DisplayName("08-01 Поле \"Год\", оплата в кредит. Граничное валидное значение. Текущий год+5")
    public void test0801() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(5, 0));
        page.creditPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Year")
    @DisplayName("08-02 Поле \"Год\", оплата в кредит. Граничное невалидное значение. Текущий год-1")
    public void test0802() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(-1, 0));
        page.creditPayment(info);
        page.tooEarlyYearAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Year")
    @DisplayName("08-03 Поле \"Год\", оплата в кредит. Граничное невалидное значение. Текущий год+6")
    public void test0803() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(6, 0));
        page.creditPayment(info);
        page.incorrectYearAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Year")
    @Feature("Empty fields handling")
    @DisplayName("08-04 Поле \"Год\", оплата в кредит. Невалидное значение. Пустое поле")
    public void test0804() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear("");
        page.creditPayment(info);
        page.emptyYearAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Year")
    @DisplayName("08-05 Поле \"Год\", оплата в кредит. Невалидное значение. Одна цифра")
    public void test0805() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear("2");
        page.creditPayment(info);
        page.incorrectYearFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Year")
    @Feature("All-zero values handling")
    @DisplayName("08-06 Поле \"Год\", оплата в кредит. Невалидное значение. 00")
    public void test0806() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear("00");
        page.creditPayment(info);
        page.tooEarlyYearAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("09-01 Поле \"Владелец\", оплата по карте. Граничное по длине валидное значение. Латиница с пробелом посередине, 3 символа")
    public void test0901() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("X Y");
        page.standardPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("09-02 Поле \"Владелец\", оплата по карте. Граничное по длине валидное значение. Латиница с пробелом посередине, 27 символов")
    public void test0902() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Amogus Sussussybakaimpostor");
        page.standardPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("09-03 Поле \"Владелец\", оплата по карте. Валидное значение. Латиница с пробелом посередине и с дефисом")
    public void test0903() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Anni-Frid Lyngstad");
        page.standardPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("09-04 Поле \"Владелец\", оплата по карте. Валидное значение. Латиница с пробелом посередине и с апострофом")
    public void test0904() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Gilbert O'Sullivan");
        page.standardPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("09-05 Поле \"Владелец\", оплата по карте. Граничное по длине невалидное значение. Латиница, 28 символов")
    public void test0905() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Amogus Susssussybakaimpostor");
        page.standardPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @Feature("Empty fields handling")
    @DisplayName("09-06 Поле \"Владелец\", оплата по карте. Невалидное значение. Пустое поле")
    public void test0906() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("");
        page.standardPayment(info);
        page.emptyNameAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("09-07 Поле \"Владелец\", оплата по карте. Граничное по длине невалидное значение. Одна латинская буква")
    public void test0907() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("a");
        page.standardPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("09-08 Поле \"Владелец\", оплата по карте. Граничное по числу пробелов невалидное значение. Латиница без пробелов")
    public void test0908() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Yumetsuki");
        page.standardPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("09-09 Поле \"Владелец\", оплата по карте. Граничное по числу пробелов невалидное значение. Латиница с двумя пробелами посередине")
    public void test0909() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("I love Finger");
        page.standardPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("09-10 Поле \"Владелец\", оплата по карте. Невалидное значение. Латиница с пробелом в начале")
    public void test0910() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(" Yumetsuki");
        page.standardPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("09-11 Поле \"Владелец\", оплата по карте. Невалидное значение. Латиница с пробелом в конце")
    public void test0911() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Yumetsuki ");
        page.standardPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("09-12 Поле \"Владелец\", оплата по карте. Невалидное значение. Кириллица с пробелом посередине")
    public void test0912() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Евгений Поливанов");
        page.standardPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("09-13 Поле \"Владелец\", оплата по карте. Невалидное значение. Цифры с пробелом посередине")
    public void test0913() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("505 8425662");
        page.standardPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("09-14 Поле \"Владелец\", оплата по карте. Невалидное значение. Спецсимволы с пробелом посередине")
    public void test0914() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("!@#$%^&*() {}|/:'<>?");
        page.standardPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("10-01 Поле \"Владелец\", оплата в кредит. Граничное по длине валидное значение. Латиница с пробелом посередине, 3 символа")
    public void test1001() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("X Y");
        page.creditPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("10-02 Поле \"Владелец\", оплата в кредит. Граничное по длине валидное значение. Латиница с пробелом посередине, 27 символов")
    public void test1002() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Amogus Sussussybakaimpostor");
        page.creditPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("10-03 Поле \"Владелец\", оплата в кредит. Валидное значение. Латиница с пробелом посередине и с дефисом")
    public void test1003() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Anni-Frid Lyngstad");
        page.creditPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("10-04 Поле \"Владелец\", оплата в кредит. Валидное значение. Латиница с пробелом посередине и с апострофом")
    public void test1004() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Gilbert O'Sullivan");
        page.creditPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("10-05 Поле \"Владелец\", оплата в кредит. Граничное по длине невалидное значение. Латиница, 28 символов")
    public void test1005() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Amogus Susssussybakaimpostor");
        page.creditPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @Feature("Empty fields handling")
    @DisplayName("10-06 Поле \"Владелец\", оплата в кредит. Невалидное значение. Пустое поле")
    public void test1006() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("");
        page.creditPayment(info);
        page.emptyNameAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("10-07 Поле \"Владелец\", оплата в кредит. Граничное по длине невалидное значение. Одна латинская буква")
    public void test1007() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("a");
        page.creditPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("10-08 Поле \"Владелец\", оплата в кредит. Граничное по числу пробелов невалидное значение. Латиница без пробелов")
    public void test1008() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Yumetsuki");
        page.creditPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("10-09 Поле \"Владелец\", оплата в кредит. Граничное по числу пробелов невалидное значение. Латиница с двумя пробелами посередине")
    public void test1009() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("I love Finger");
        page.creditPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("10-10 Поле \"Владелец\", оплата в кредит. Невалидное значение. Латиница с пробелом в начале")
    public void test1010() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(" Yumetsuki");
        page.creditPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("10-11 Поле \"Владелец\", оплата в кредит. Невалидное значение. Латиница с пробелом в конце")
    public void test1011() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Yumetsuki ");
        page.creditPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("10-12 Поле \"Владелец\", оплата в кредит. Невалидное значение. Кириллица с пробелом посередине")
    public void test1012() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("Евгений Поливанов");
        page.creditPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("10-13 Поле \"Владелец\", оплата в кредит. Невалидное значение. Цифры с пробелом посередине")
    public void test1013() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("505 8425662");
        page.creditPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("Cardholder")
    @DisplayName("10-14 Поле \"Владелец\", оплата в кредит. Невалидное значение. Спецсимволы с пробелом посередине")
    public void test1014() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName("!@#$%^&*() {}|/:'<>?");
        page.creditPayment(info);
        page.incorrectNameFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("CVV/CVC")
    @DisplayName("11-01 Поле \"CVC/CVV\", оплата по карте. Валидное значение. 3 цифры, исключая \"000\"")
    public void test1101() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        page.standardPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("CVV/CVC")
    @Feature("All-zero values handling")
    @DisplayName("11-02 Поле \"CVC/CVV\", оплата по карте. Валидное значение. \"000\"")
    public void test1102() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv("000");
        page.standardPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("CVV/CVC")
    @DisplayName("11-03 Поле \"CVC/CVV\", оплата по карте. Граничное невалидное значение. 1 цифра")
    public void test1103() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv("8");
        page.standardPayment(info);
        page.incorrectCvvFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("CVV/CVC")
    @Feature("Empty fields handling")
    @DisplayName("11-04 Поле \"CVC/CVV\", оплата по карте. Невалидное значение. Пустое поле")
    public void test1104() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv("");
        page.standardPayment(info);
        page.emptyCvvAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("CVV/CVC")
    @DisplayName("12-01 Поле \"CVC/CVV\", оплата в кредит. Валидное значение. 3 цифры, исключая \"000\"")
    public void test1201() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        page.creditPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("User interaction")
    @Feature("CVV/CVC")
    @Feature("All-zero values handling")
    @DisplayName("12-02 Поле \"CVC/CVV\", оплата в кредит. Валидное значение. \"000\"")
    public void test1202() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv("000");
        page.creditPayment(info);
        page.successNotificationAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("CVV/CVC")
    @DisplayName("12-03 Поле \"CVC/CVV\", оплата в кредит. Граничное невалидное значение. 1 цифра")
    public void test1203() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv("8");
        page.creditPayment(info);
        page.incorrectCvvFormatAssert();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("User interaction")
    @Feature("CVV/CVC")
    @Feature("Empty fields handling")
    @DisplayName("12-04 Поле \"CVC/CVV\", оплата в кредит. Невалидное значение. Пустое поле")
    public void test1204() {
        LoginPage page = new LoginPage();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv("");
        page.creditPayment(info);
        page.emptyCvvAssert();
    }
}
