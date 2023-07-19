package com.example.shop.e2e;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.assertj.core.internal.Conditions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ShopE2ETests extends MainPage {
    @BeforeAll
    public static void setUp() {
        Configuration.browserCapabilities = new ChromeOptions().addArguments("--remote-allow-origins=*");
        Configuration.browser = "Chrome";
        Configuration.baseUrl = "http://localhost:63342/shop/shop.main/com/example/shop/ui/main.html?_ijt=d2uo4iijkc544camhctfilgkt0&_ij_reload=RELOAD_ON_SAVE";
    }

    @Test
    @DisplayName("Второй тест")
    public void openShopsPage() throws InterruptedException{
        open("");
        $(refreshButton).click();
        Thread.sleep(5000);
    }

    @Test
    @DisplayName("Кнопка обновления")
    @RepeatedTest(10)
    public void refreshButton() throws InterruptedException{
        /*
        1. Посчитать размер таблицы.
        2. Добавить магазин.
        3. Нажать кнопку "обновить".
        4. Посчитать размер таблицы. Проверить, что он увеличился на 1.
        5. Удалить магазин.
        6. Посчитать размер таблицы. Проверить, что он уменьшился на 1.
         */

        open("");
        //Thread.sleep(3000);
        allShopsTable.get(0).shouldBe(Condition.visible);
        int shopsQuantityBeforeAdding = allShopsTable.size();
        $(createShopFiled).sendKeys("TestShop777");
        $(createShopButton).click();
        Selenide.switchTo().alert().accept();
        $(refreshButton);
        //Thread.sleep(3000);
        allShopsTable.get(0).shouldBe(Condition.visible);
        int shopsQuantityAfterAdding = allShopsTable.size();
        assertThat(shopsQuantityAfterAdding).isEqualTo(shopsQuantityBeforeAdding + 1);

        SelenideElement row;
        int i = 1;
        do {
            row = allShopsTable.get(shopsQuantityAfterAdding - i);
            i++;
        } while(!Objects.equals(row.$("td:nth-child(2)").getAttribute("innerText"), "TestShop777"));

        String createdShopId = row.$("td:nth-child(1)").getAttribute("innerText");

        $(deleteShopFiled).sendKeys(createdShopId);
        $(deleteShopButton).click();
        Selenide.switchTo().alert().accept();

        $(refreshButton);
        allShopsTable.get(0).shouldBe(Condition.visible);
        assertThat(allShopsTable.size()).isEqualTo(shopsQuantityBeforeAdding);
    }

    @Test
    @DisplayName("Второй тест")
    public void telegramButtonClick() throws InterruptedException{
        open("");
        $(vkButton).click();
        Thread.sleep(5000);
    }
}
