package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class PaymentPage {
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
        $(".notification_status_ok").shouldBe(Condition.visible, Duration.ofSeconds(20));
        $(".notification_status_ok .notification__title").shouldHave(Condition.text("Успешно"));
        $(".notification_status_ok .notification__content").shouldHave(Condition.text("Операция одобрена Банком."));
    }

    public void errorNotificationAssertion() {
        $(".notification_status_error").shouldBe(Condition.visible, Duration.ofSeconds(20));
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
