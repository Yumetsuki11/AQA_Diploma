package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class StartingPage {
    private SelenideElement paymentButton = $x("//*[@id='root']/div/button[1]");
    private SelenideElement creditButton = $x("//*[@id='root']/div/button[2]");

    public CreditPage chooseCredit() {
        creditButton.click();
        return new CreditPage();
    }

    public PaymentPage choosePayment() {
        paymentButton.click();
        return new PaymentPage();
    }
}
