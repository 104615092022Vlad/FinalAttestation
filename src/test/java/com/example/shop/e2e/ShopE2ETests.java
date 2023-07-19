package com.example.shop.e2e;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Date;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ShopE2ETests extends MainPage {
    @BeforeAll
    public static void setUp() {
        Configuration.browserCapabilities = new ChromeOptions().addArguments("--remote-allow-origins=*");
        Configuration.browser = "Chrome";
        Configuration.baseUrl = "http://localhost:63342/shop/shop.main/com/example/shop/ui/main.html?_ijt=d2uo4iijkc544camhctfilgkt0&_ij_reload=RELOAD_ON_SAVE";
    }

    @BeforeEach
    public void openBrowser() {
        open("");
    }

    @Test
    @DisplayName("Второй тест")
    public void openShopsPage() throws InterruptedException {
        $(refreshButton).click();
        Thread.sleep(5000);
    }

    @Test
    @DisplayName("Обновление списка текущих магазинов")
    public void refreshButton() {
        allShopsTable.get(0).shouldBe(Condition.visible);
        int shopsQuantityBeforeAdding = allShopsTable.size();
        Date currentTime = new Date();
        String testShopName = "TestShop" + currentTime.getTime();

        step("Создать тестовый магазин", () -> {
            $(createShopFiled).sendKeys(testShopName);
            $(createShopButton).click();
            Selenide.switchTo().alert().accept();
        });

        step("Нажать на кнопку обновления", () -> {
            assertThat($(refreshButton).exists()).isTrue();
            $(refreshButton).click();
        });

        allShopsTable.get(0).shouldBe(Condition.visible);
        int shopsQuantityAfterAdding = allShopsTable.size();

        step("Проверить изменения в таблице", () -> {
            assertThat(shopsQuantityAfterAdding).isEqualTo(shopsQuantityBeforeAdding + 1);
        });

        SelenideElement row;
        int i = 1;
        do {
            row = allShopsTable.get(shopsQuantityAfterAdding - i);
            i++;
        } while (!Objects.equals(row.$("td:nth-child(2)").getAttribute("innerText"), testShopName));
        String createdShopId = row.$("td:nth-child(1)").getAttribute("innerText");

        step("Удалить тестовый магазин", () -> {
            $(deleteShopFiled).sendKeys(createdShopId);
            $(deleteShopButton).click();
            Selenide.switchTo().alert().accept();
        });

        step("Нажать на кнопку обновления", () -> {
            $(refreshButton).click();
        });

        step("Проверить изменения в таблице", () -> {
            allShopsTable.get(0).shouldBe(Condition.visible);
            assertThat(allShopsTable.size()).isEqualTo(shopsQuantityBeforeAdding);
        });
    }

    @Test
    @DisplayName("Второй тест")
    public void telegramButtonClick() throws InterruptedException {
        open("");
        $(vkButton).click();
        Thread.sleep(5000);
    }
}
