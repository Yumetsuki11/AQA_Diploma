package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class LoginPage {
    private SelenideElement paymentButton = $x("//*[@id='root']/div/button[1]");
    private SelenideElement creditButton = $x("//*[@id='root']/div/button[2]");

    private SelenideElement cardNumberField = $x("//*[contains(@class, 'form')]/fieldset/div/span/span/span[2]/input");
    private SelenideElement monthField = $x("//*[contains(@class, 'form')]/fieldset/div[2]/span/span/span/span/span[2]/input");
    private SelenideElement yearField = $x("//*[contains(@class, 'form')]/fieldset/div[2]/span/span[2]/span/span/span[2]/input");
    private SelenideElement nameField = $x("//*[contains(@class, 'form')]/fieldset/div[3]/span/span/span/span/span[2]/input");
    private SelenideElement cvvField = $x("//*[contains(@class, 'form')]/fieldset/div[3]/span/span[2]/span/span/span[2]/input");

    private SelenideElement proceedButton = $x("//*[contains(@class, 'form')]//*[contains(@class, 'button')]");

    private SelenideElement cardNumberErrorText = $x("//*[contains(@class, 'form')]/fieldset/div/span/span/span[3]");
    private SelenideElement monthErrorText = $x("//*[contains(@class, 'form')]/fieldset/div[2]/span/span/span/span/span[3]");
    private SelenideElement yearErrorText = $x("//*[contains(@class, 'form')]/fieldset/div[2]/span/span[2]/span/span/span[3]");
    private SelenideElement nameErrorText = $x("//span[text()='Поле обязательно для заполнения']");
    private SelenideElement cvvErrorText = $x("//*[contains(@class, 'form')]/fieldset/div[3]/span/span[2]/span/span/span[3]");
    public void standardPayment(DataHelper.CardInfo info) {
        paymentButton.click();
        cardNumberField.setValue(info.getCardNumber());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        nameField.setValue(info.getName());
        cvvField.setValue(info.getCvv());
        proceedButton.click();
    }

    public void creditPayment(DataHelper.CardInfo info) {
        creditButton.click();
        cardNumberField.setValue(info.getCardNumber());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        nameField.setValue(info.getName());
        cvvField.setValue(info.getCvv());
        proceedButton.click();
    }

    public void successNotificationAssert() {
        // на мой взгляд, в этих объектах проще ориентироваться, не вводя переменные элементов
        $(".notification_status_ok").shouldBe(Condition.visible, Duration.ofSeconds(20));
        $(".notification_status_ok .notification__title").shouldHave(Condition.text("Успешно"));
        $(".notification_status_ok .notification__content").shouldHave(Condition.text("Операция одобрена Банком."));
    }

    public void errorNotificationAssert() {
        $(".notification_status_error").shouldBe(Condition.visible, Duration.ofSeconds(20));
        $(".notification_status_error .notification__title").shouldHave(Condition.text("Ошибка"));
        $(".notification_status_error .notification__content").shouldHave(Condition.text("Ошибка! Банк отказал в проведении операции."));
    }

    public void emptyCardNumberAssert() {
        cardNumberErrorText.shouldBe(Condition.visible).shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    public void incorrectCardNumberAssert() {
        cardNumberErrorText.shouldBe(Condition.visible).shouldHave(Condition.text("Неверный формат"));
    }

    public void emptyMonthAssert() {
        monthErrorText.shouldBe(Condition.visible).shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    public void incorrectMonthAssert() {
        monthErrorText.shouldBe(Condition.visible).shouldHave(Condition.text("Неверно указан срок действия карты"));
    }

    public void incorrectMonthFormatAssert() {
        monthErrorText.shouldBe(Condition.visible).shouldHave(Condition.text("Неверный формат"));
    }

    public void emptyYearAssert() {
        yearErrorText.shouldBe(Condition.visible).shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    public void incorrectYearAssert() {
        yearErrorText.shouldBe(Condition.visible).shouldHave(Condition.text("Неверно указан срок действия карты"));
    }

    public void incorrectYearFormatAssert() {
        yearErrorText.shouldBe(Condition.visible).shouldHave(Condition.text("Неверный формат"));
    }

    public void tooEarlyYearAssert() {
        yearErrorText.shouldBe(Condition.visible).shouldHave(Condition.text("Истёк срок действия карты"));
    }

    public void emptyNameAssert() {
        nameErrorText.shouldBe(Condition.visible).shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    public void incorrectNameFormatAssert() {
        nameErrorText.shouldBe(Condition.visible).shouldHave(Condition.text("Неверный формат"));
    }

    public void emptyCvvAssert() {
        cvvErrorText.shouldBe(Condition.visible).shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    public void incorrectCvvFormatAssert() {
        cvvErrorText.shouldBe(Condition.visible).shouldHave(Condition.text("Неверный формат"));
    }
}
