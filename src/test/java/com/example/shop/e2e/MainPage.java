package com.example.shop.e2e;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class MainPage {
    SelenideElement anchorCreateShop = $("#links :nth-child(1)");

    SelenideElement anchorAllShops = $("#links :nth-child(2)");

    SelenideElement anchorDeleteShop = $("#links :nth-child(3)");

    ElementsCollection allShopsTable = $$("#response tr");

    SelenideElement refreshButton = $("#shops_div button");

    SelenideElement createShopFiled = $("#name");

    SelenideElement nameValidation = $("#name_validation");

    SelenideElement checkboxPublic = $("[type='checkbox']");

    SelenideElement createShopButton = $("[onclick='addShop()']");

    SelenideElement deleteShopFiled = $("#id");

    SelenideElement idValidation = $("#id_validation");

    SelenideElement deleteShopButton = $("[onclick='deleteShop();']");

    SelenideElement telegramButton = $("footer :nth-child(2) :nth-child(1)");

    SelenideElement vkButton = $("footer :nth-child(2) :nth-child(2)");

}
