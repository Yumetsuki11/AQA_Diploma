package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.CreditPage;
import ru.netology.page.PaymentPage;
import ru.netology.page.StartingPage;

import static com.codeborne.selenide.Selenide.open;

public class CreditTest {
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


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Integration")
    @Feature("DB interaction")
    @DisplayName("01-02 Позитивные сценарии оплаты тура. Оплата в кредит с карты статуса APPROVED")
    public void test0102() {
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        creditPage.pay(info);
        creditPage.successNotificationAssertion();
        Assertions.assertEquals("APPROVED", DataHelper.getCreditStatus());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Integration")
    @Feature("DB interaction")
    @DisplayName("02-03 Негативные сценарии оплаты тура. Оплата в кредит с карты статуса DECLINED")
    public void test0203() {

        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        creditPage.pay(info);
        creditPage.errorNotificationAssertion();
        Assertions.assertEquals("DECLINED", DataHelper.getCreditStatus());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Integration")
    @Feature("DB interaction")
    @DisplayName("02-04 Негативные сценарии оплаты тура. Оплата в кредит с несуществующей карты")
    public void test0204() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getRandomCardNumber());
        creditPage.pay(info);
        creditPage.errorNotificationAssertion();
        Assertions.assertNull(DataHelper.getCreditStatus());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Card number")
    @DisplayName("04-01 Поле \"Номер карты\", оплата в кредит. Валидное значение. Номер карты (отсутствующей в базе) из 16 цифр, не полностью состоящий из нулей")
    public void test0401() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getRandomCardNumber());
        creditPage.pay(info);
        creditPage.errorNotificationAssertion();
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
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getAllZerosCardNumber());
        creditPage.pay(info);
        creditPage.errorNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Card number")
    @DisplayName("04-03 Поле \"Номер карты\", оплата в кредит. Граничное невалидное значение. 15 цифр")
    public void test0403() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.get15DigitNumber());
        creditPage.pay(info);
        creditPage.invalidCardNumberAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Card number")
    @DisplayName("04-04 Поле \"Номер карты\", оплата в кредит. Граничное невалидное значение. 1 цифра")
    public void test0404() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.get1DigitNumber());
        creditPage.pay(info);
        creditPage.invalidCardNumberAssertion("Неверный формат");
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
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        info.setCardNumber(DataHelper.getEmptyValue());
        creditPage.pay(info);
        creditPage.invalidCardNumberAssertion("Поле обязательно для заполнения");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("06-01 Поле \"Месяц\", оплата в кредит. Граничное валидное значение. Число 01 при годе, равном текущему+1")
    public void test0601() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.getMonth01());
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        creditPage.pay(info);
        creditPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("06-02 Поле \"Месяц\", оплата в кредит. Граничное валидное значение. Число 02 при годе, равном текущему+1")
    public void test0602() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.getMonth02());
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        creditPage.pay(info);
        creditPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("06-03 Поле \"Месяц\", оплата в кредит. Граничное валидное значение. Число 11 при годе, равном текущему+1")
    public void test0603() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.getMonth11());
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        creditPage.pay(info);
        creditPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("06-04 Поле \"Месяц\", оплата в кредит. Граничное валидное значение. Число 12 при годе, равном текущему+1")
    public void test0604() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.getMonth12());
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        creditPage.pay(info);
        creditPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("06-05 Поле \"Месяц\", оплата в кредит. Граничное валидное значение. Текущий месяц при текущем годе")
    public void test0605() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.generateMonthByCurr(0));
        info.setYear(DataHelper.generateYearByCurr(0, 0));
        creditPage.pay(info);
        creditPage.successNotificationAssertion();
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
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.getMonth00orYear00());
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        creditPage.pay(info);
        creditPage.invalidMonthAssertion("Неверно указан срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("06-07 Поле \"Месяц\", оплата в кредит. Граничное невалидное значение. Число 13 при годе, равном текущему+1")
    public void test0607() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.getMonth13());
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        creditPage.pay(info);
        creditPage.invalidMonthAssertion("Неверно указан срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("06-08 Поле \"Месяц\", оплата в кредит. Текущий месяц-1 при текущем годе")
    public void test0608() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.generateMonthByCurr(-1));
        info.setYear(DataHelper.generateYearByCurr(0, 0));
        creditPage.pay(info);
        creditPage.invalidMonthAssertion("Неверно указан срок действия карты");
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
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.getEmptyValue());
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        creditPage.pay(info);
        creditPage.invalidMonthAssertion("Поле обязательно для заполнения");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Month")
    @DisplayName("06-10 Поле \"Месяц\", оплата в кредит. Одна цифра")
    public void test0610() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setMonth(DataHelper.get1DigitNumber());
        info.setYear(DataHelper.generateYearByCurr(1, 0));
        creditPage.pay(info);
        creditPage.invalidMonthAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Year")
    @DisplayName("08-01 Поле \"Год\", оплата в кредит. Граничное валидное значение. Текущий год+5")
    public void test0801() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(5, 0));
        creditPage.pay(info);
        creditPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Year")
    @DisplayName("08-02 Поле \"Год\", оплата в кредит. Граничное невалидное значение. Текущий год-1")
    public void test0802() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(-1, 0));
        creditPage.pay(info);
        creditPage.invalidYearAssertion("Истёк срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Year")
    @DisplayName("08-03 Поле \"Год\", оплата в кредит. Граничное невалидное значение. Текущий год+6")
    public void test0803() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.generateYearByCurr(6, 0));
        creditPage.pay(info);
        creditPage.invalidYearAssertion("Неверно указан срок действия карты");
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
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.getEmptyValue());
        creditPage.pay(info);
        creditPage.invalidYearAssertion("Поле обязательно для заполнения");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Year")
    @DisplayName("08-05 Поле \"Год\", оплата в кредит. Невалидное значение. Одна цифра")
    public void test0805() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.get1DigitNumber());
        creditPage.pay(info);
        creditPage.invalidYearAssertion("Неверный формат");
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
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setYear(DataHelper.getMonth00orYear00());
        creditPage.pay(info);
        creditPage.invalidYearAssertion("Истёк срок действия карты");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-01 Поле \"Владелец\", оплата в кредит. Граничное по длине валидное значение. Латиница с пробелом посередине, 3 символа")
    public void test1001() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getInitials());
        creditPage.pay(info);
        creditPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-02 Поле \"Владелец\", оплата в кредит. Граничное по длине валидное значение. Латиница с пробелом посередине, 27 символов")
    public void test1002() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getNameOf27Symbols());
        creditPage.pay(info);
        creditPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-03 Поле \"Владелец\", оплата в кредит. Валидное значение. Латиница с пробелом посередине и с дефисом")
    public void test1003() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getNameWithHyphen());
        creditPage.pay(info);
        creditPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-04 Поле \"Владелец\", оплата в кредит. Валидное значение. Латиница с пробелом посередине и с апострофом")
    public void test1004() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getNameWithApostrophe());
        creditPage.pay(info);
        creditPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-05 Поле \"Владелец\", оплата в кредит. Граничное по длине невалидное значение. Латиница, 28 символов")
    public void test1005() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getNameOf28Symbols());
        creditPage.pay(info);
        creditPage.invalidNameAssertion("Неверный формат");
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
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getEmptyValue());
        creditPage.pay(info);
        creditPage.invalidNameAssertion("Поле обязательно для заполнения");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-07 Поле \"Владелец\", оплата в кредит. Граничное по длине невалидное значение. Одна латинская буква")
    public void test1007() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.get1Letter());
        creditPage.pay(info);
        creditPage.invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-08 Поле \"Владелец\", оплата в кредит. Граничное по числу пробелов невалидное значение. Латиница без пробелов")
    public void test1008() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.get1Word());
        creditPage.pay(info);
        creditPage.invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-09 Поле \"Владелец\", оплата в кредит. Граничное по числу пробелов невалидное значение. Латиница с двумя пробелами посередине")
    public void test1009() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.get3Words());
        creditPage.pay(info);
        creditPage.invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-10 Поле \"Владелец\", оплата в кредит. Невалидное значение. Латиница с пробелом в начале")
    public void test1010() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.get1WordStartingWithSpace());
        creditPage.pay(info);
        creditPage.invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-11 Поле \"Владелец\", оплата в кредит. Невалидное значение. Латиница с пробелом в конце")
    public void test1011() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.get1WordEndingWithSpace());
        creditPage.pay(info);
        creditPage.invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-12 Поле \"Владелец\", оплата в кредит. Невалидное значение. Кириллица с пробелом посередине")
    public void test1012() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getCyrillicName());
        creditPage.pay(info);
        creditPage.invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-13 Поле \"Владелец\", оплата в кредит. Невалидное значение. Цифры с пробелом посередине")
    public void test1013() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getNumberWithSpace());
        creditPage.pay(info);
        creditPage.invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("Cardholder")
    @DisplayName("10-14 Поле \"Владелец\", оплата в кредит. Невалидное значение. Спецсимволы с пробелом посередине")
    public void test1014() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setName(DataHelper.getSymbolsWithSpace());
        creditPage.pay(info);
        creditPage.invalidNameAssertion("Неверный формат");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Credit request")
    @Epic("#Positive")
    @Epic("Unit")
    @Feature("CVV/CVC")
    @DisplayName("12-01 Поле \"CVC/CVV\", оплата в кредит. Валидное значение. 3 цифры, исключая \"000\"")
    public void test1201() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        creditPage.pay(info);
        creditPage.successNotificationAssertion();
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
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv(DataHelper.getZeroCvv());
        creditPage.pay(info);
        creditPage.successNotificationAssertion();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Story("Credit request")
    @Epic("#Negative")
    @Epic("Unit")
    @Feature("CVV/CVC")
    @DisplayName("12-03 Поле \"CVC/CVV\", оплата в кредит. Граничное невалидное значение. 1 цифра")
    public void test1203() {
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv(DataHelper.get1DigitNumber());
        creditPage.pay(info);
        creditPage.invalidCvvAssertion("Неверный формат");
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
        
        val creditPage = page.chooseCredit();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        info.setCvv(DataHelper.getEmptyValue());
        creditPage.pay(info);
        creditPage.invalidCvvAssertion("Поле обязательно для заполнения");
    }
}
