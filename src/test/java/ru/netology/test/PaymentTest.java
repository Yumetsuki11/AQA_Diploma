package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.StartingPage;

import static com.codeborne.selenide.Selenide.open;

public class PaymentTest {

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

    StartingPage page;

    @BeforeEach
    public void setup() {
        Configuration.holdBrowserOpen = true;
        page = open("http://localhost:8080", StartingPage.class);
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
    @Feature("DB interaction")
    @DisplayName("01-01 Позитивные сценарии оплаты тура. Прямая оплата с карты статуса APPROVED")
    public void test0101() {
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        paymentPage.pay(info);
        paymentPage.successNotificationAssertion();
        Assertions.assertEquals("APPROVED", DataHelper.getPaymentStatus());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Integration")
    @Feature("DB interaction")
    @DisplayName("02-01 Негативные сценарии оплаты тура. Прямая оплата с карты статуса DECLINED")
    public void test0201() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        paymentPage.pay(info);
        paymentPage.errorNotificationAssertion();
        Assertions.assertEquals("DECLINED", DataHelper.getPaymentStatus());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Integration")
    @Feature("DB interaction")
    @DisplayName("02-02 Негативные сценарии оплаты тура. Прямая оплата с несуществующей карты")
    public void test0202() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getRandomCardNumber());
        paymentPage.pay(info);
        paymentPage.errorNotificationAssertion();
        Assertions.assertNull(DataHelper.getPaymentStatus());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Card number")
    @DisplayName("03-01 Поле \"Номер карты\", оплата по карте. Валидное значение. Номер карты (отсутствующей в базе) из 16 цифр, не полностью состоящий из нулей")
    public void test0301() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getRandomCardNumber());
        paymentPage.pay(info);
        paymentPage.errorNotificationAssertion();
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
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getAllZerosCardNumber());
        paymentPage.pay(info);
        paymentPage.errorNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Card number")
    @DisplayName("03-03 Поле \"Номер карты\", оплата по карте. Граничное невалидное значение. 15 цифр")
    public void test0303() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.get15DigitNumber());
        paymentPage.pay(info);
        paymentPage.invalidCardNumberAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Card number")
    @DisplayName("03-04 Поле \"Номер карты\", оплата по карте. Граничное невалидное значение. 1 цифра")
    public void test0304() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.get1DigitNumber());
        paymentPage.pay(info);
        paymentPage.invalidCardNumberAssertion("Неверный формат");
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
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getEmptyValue());
        paymentPage.pay(info);
        paymentPage.invalidCardNumberAssertion("Поле обязательно для заполнения");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("05-01 Поле \"Месяц\", оплата по карте. Граничное валидное значение. Число 01 при годе, равном текущему+1")
    public void test0501() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.getMonth01());
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        paymentPage.pay(info);
        paymentPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("05-02 Поле \"Месяц\", оплата по карте. Граничное валидное значение. Число 02 при годе, равном текущему+1")
    public void test0502() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.getMonth02());
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        paymentPage.pay(info);
        paymentPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("05-03 Поле \"Месяц\", оплата по карте. Граничное валидное значение. Число 11 при годе, равном текущему+1")
    public void test0503() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.getMonth11());
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        paymentPage.pay(info);
        paymentPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("05-04 Поле \"Месяц\", оплата по карте. Граничное валидное значение. Число 12 при годе, равном текущему+1")
    public void test0504() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.getMonth12());
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        paymentPage.pay(info);
        paymentPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("05-05 Поле \"Месяц\", оплата по карте. Граничное валидное значение. Текущий месяц при текущем годе")
    public void test0505() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.generateMonthByCurr(0));
        info.setYear(DataHelper.generateYearByCurr(0, 0));
        paymentPage.pay(info);
        paymentPage.successNotificationAssertion();
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
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.getMonth00orYear00());
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        paymentPage.pay(info);
        paymentPage.invalidMonthAssertion("Неверно указан срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("05-07 Поле \"Месяц\", оплата по карте. Граничное невалидное значение. Число 13 при годе, равном текущему+1")
    public void test0507() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.getMonth13());
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        paymentPage.pay(info);
        paymentPage.invalidMonthAssertion("Неверно указан срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("05-08 Поле \"Месяц\", оплата по карте. Текущий месяц-1 при текущем годе")
    public void test0508() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.generateMonthByCurr(-1));
        info.setYear(DataHelper.generateYearByCurr(0, 0));
        paymentPage.pay(info);
        paymentPage.invalidMonthAssertion("Неверно указан срок действия карты");
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
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.getEmptyValue());
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        paymentPage.pay(info);
        paymentPage.invalidMonthAssertion("Поле обязательно для заполнения");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("05-10 Поле \"Месяц\", оплата по карте. Одна цифра")
    public void test0510() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.get1DigitNumber());
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        paymentPage.pay(info);
        paymentPage.invalidMonthAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Year")
    @DisplayName("07-01 Поле \"Год\", оплата по карте. Граничное валидное значение. Текущий год+5")
    public void test0701() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(5, 0));
        paymentPage.pay(info);
        paymentPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Year")
    @DisplayName("07-02 Поле \"Год\", оплата по карте. Граничное невалидное значение. Текущий год-1")
    public void test0702() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(-1, 0));
        paymentPage.pay(info);
        paymentPage.invalidYearAssertion("Истёк срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Year")
    @DisplayName("07-03 Поле \"Год\", оплата по карте. Граничное невалидное значение. Текущий год+6")
    public void test0703() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(6, 0));
        paymentPage.pay(info);
        paymentPage.invalidYearAssertion("Неверно указан срок действия карты");
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
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.getEmptyValue());
        paymentPage.pay(info);
        paymentPage.invalidYearAssertion("Поле обязательно для заполнения");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Year")
    @DisplayName("07-05 Поле \"Год\", оплата по карте. Невалидное значение. Одна цифра")
    public void test0705() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.get1DigitNumber());
        paymentPage.pay(info);
        paymentPage.invalidYearAssertion("Неверный формат");
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
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.getMonth00orYear00());
        paymentPage.pay(info);
        paymentPage.invalidYearAssertion("Истёк срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-01 Поле \"Владелец\", оплата по карте. Граничное по длине валидное значение. Латиница с пробелом посередине, 3 символа")
    public void test0901() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getInitials());
        paymentPage.pay(info);
        paymentPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-02 Поле \"Владелец\", оплата по карте. Граничное по длине валидное значение. Латиница с пробелом посередине, 27 символов")
    public void test0902() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getNameOf27Symbols());
        paymentPage.pay(info);
        paymentPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-03 Поле \"Владелец\", оплата по карте. Валидное значение. Латиница с пробелом посередине и с дефисом")
    public void test0903() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getNameWithHyphen());
        paymentPage.pay(info);
        paymentPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-04 Поле \"Владелец\", оплата по карте. Валидное значение. Латиница с пробелом посередине и с апострофом")
    public void test0904() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getNameWithApostrophe());
        paymentPage.pay(info);
        paymentPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-05 Поле \"Владелец\", оплата по карте. Граничное по длине невалидное значение. Латиница, 28 символов")
    public void test0905() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getNameOf28Symbols());
        paymentPage.pay(info);
        paymentPage.invalidNameAssertion("Неверный формат");
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
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getEmptyValue());
        paymentPage.pay(info);
        paymentPage.invalidNameAssertion("Поле обязательно для заполнения");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-07 Поле \"Владелец\", оплата по карте. Граничное по длине невалидное значение. Одна латинская буква")
    public void test0907() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.get1Letter());
        paymentPage.pay(info);
        paymentPage.invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-08 Поле \"Владелец\", оплата по карте. Граничное по числу пробелов невалидное значение. Латиница без пробелов")
    public void test0908() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.get1Word());
        paymentPage.pay(info);
        paymentPage.invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-09 Поле \"Владелец\", оплата по карте. Граничное по числу пробелов невалидное значение. Латиница с двумя пробелами посередине")
    public void test0909() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.get3Words());
        paymentPage.pay(info);
        paymentPage.invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-10 Поле \"Владелец\", оплата по карте. Невалидное значение. Латиница с пробелом в начале")
    public void test0910() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.get1WordEndingWithSpace());
        paymentPage.pay(info);
        paymentPage.invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-11 Поле \"Владелец\", оплата по карте. Невалидное значение. Латиница с пробелом в конце")
    public void test0911() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.get1WordEndingWithSpace());
        paymentPage.pay(info);
        paymentPage.invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-12 Поле \"Владелец\", оплата по карте. Невалидное значение. Кириллица с пробелом посередине")
    public void test0912() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getCyrillicName());
        paymentPage.pay(info);
        paymentPage.invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-13 Поле \"Владелец\", оплата по карте. Невалидное значение. Цифры с пробелом посередине")
    public void test0913() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getNumberWithSpace());
        paymentPage.pay(info);
        paymentPage.invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("09-14 Поле \"Владелец\", оплата по карте. Невалидное значение. Спецсимволы с пробелом посередине")
    public void test0914() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getSymbolsWithSpace());
        paymentPage.pay(info);
        paymentPage.invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Standard payment")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("CVV/CVC")
    @DisplayName("11-01 Поле \"CVC/CVV\", оплата по карте. Валидное значение. 3 цифры, исключая \"000\"")
    public void test1101() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        paymentPage.pay(info);
        paymentPage.successNotificationAssertion();
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
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv(DataHelper.getZeroCvv());
        paymentPage.pay(info);
        paymentPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Standard payment")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("CVV/CVC")
    @DisplayName("11-03 Поле \"CVC/CVV\", оплата по карте. Граничное невалидное значение. 1 цифра")
    public void test1103() {
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv(DataHelper.get1DigitNumber());
        paymentPage.pay(info);
        paymentPage.invalidCvvAssertion("Неверный формат");
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
        
        val paymentPage = page.choosePayment();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv(DataHelper.getEmptyValue());
        paymentPage.pay(info);
        paymentPage.invalidCvvAssertion("Поле обязательно для заполнения");
    }
}
