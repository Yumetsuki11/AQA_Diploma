package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class CreditPage {
    private SelenideElement cardNumberField = $("[placeholder='0000 0000 0000 0000']");
    private SelenideElement monthField = $("[placeholder='08']");
    private SelenideElement yearField = $("[placeholder='22']");
    private SelenideElement nameField = $x("//*[text()='Владелец']/following-sibling::span/input");
    private SelenideElement cvvField = $("[placeholder='999']");

    private SelenideElement proceedButton = $x("//*[contains(@class, 'form')]//*[contains(@class, 'button')]");

    private SelenideElement cardNumberErrorText = $x("//*[text()='Номер карты']/following-sibling::span[2]");
    private SelenideElement monthErrorText = $x("//*[text()='Месяц']/following-sibling::span[2]");
    private SelenideElement yearErrorText = $x("//*[text()='Год']/following-sibling::span[2]");
    private SelenideElement nameErrorText = $x("//*[text()='Владелец']/following-sibling::span[2]");
    private SelenideElement cvvErrorText = $x("//*[text()='CVC/CVV']/following-sibling::span[2]");

    public void pay(DataHelper.CardInfo info) {
        cardNumberField.setValue(info.getCardNumber());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        nameField.setValue(info.getName());
        cvvField.setValue(info.getCvv());
        proceedButton.click();
    }

    public void successNotificationAssertion() {
        // на мой взгляд, в этих объектах проще ориентироваться, не вводя переменные элементов
        $(".notification_status_ok").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(".notification_status_ok .notification__title").shouldHave(Condition.text("Успешно"));
        $(".notification_status_ok .notification__content").shouldHave(Condition.text("Операция одобрена Банком."));
    }

    public void errorNotificationAssertion() {
        $(".notification_status_error").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(".notification_status_error .notification__title").shouldHave(Condition.text("Ошибка"));
        $(".notification_status_error .notification__content").shouldHave(Condition.text("Ошибка! Банк отказал в проведении операции."));
    }

    public void invalidCardNumberAssertion(String errorText) {
        cardNumberErrorText.shouldHave(Condition.text(errorText)).shouldBe(Condition.visible);
    }

    public void invalidMonthAssertion(String errorText) {
        monthErrorText.shouldHave(Condition.text(errorText)).shouldBe(Condition.visible);
    }

    public void invalidYearAssertion(String errorText) {
        yearErrorText.shouldHave(Condition.text(errorText)).shouldBe(Condition.visible);
    }

    public void invalidNameAssertion(String errorText) {
        nameErrorText.shouldHave(Condition.text(errorText)).shouldBe(Condition.visible);
    }

    public void invalidCvvAssertion(String errorText) {
        cvvErrorText.shouldHave(Condition.text(errorText)).shouldBe(Condition.visible);
    }
}
