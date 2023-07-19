package com.example.shop.e2e;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Date;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ShopE2ETests extends MainPage {
    static String baseURL = "http://localhost:63342/shop/shop.main/com/example/shop/ui/main.html?_ijt=d2uo4iijkc544camhctfilgkt0&_ij_reload=RELOAD_ON_SAVE";

    @BeforeAll
    public static void setUp() {
        Configuration.browserCapabilities = new ChromeOptions().addArguments("--remote-allow-origins=*");
        Configuration.browser = "Chrome";
        Configuration.baseUrl = baseURL;
    }

    @BeforeEach
    public void openBrowser() {
        open("");
    }

    @Test
    @DisplayName("Навигация по странице")
    public void shouldBeNavigation() {
        step("Кнопка создания магазина", () -> {
            assertThat($(anchorCreateShop).exists()).isTrue();
            assertThat($(anchorCreateShop).getAttribute("innerText")).isEqualTo("Create shop");
            assertThat($(anchorCreateShop).getAttribute("href")).isEqualTo(baseURL + "#create_shop");
        });

        step("Кнопка списка магазинов", () -> {
            assertThat($(anchorAllShops).exists()).isTrue();
            assertThat($(anchorAllShops).getAttribute("innerText")).isEqualTo("All shops");
            assertThat($(anchorAllShops).getAttribute("href")).isEqualTo(baseURL + "#all_shops");
        });

        step("Кнопка удаления магазина", () -> {
            assertThat($(anchorDeleteShop).exists()).isTrue();
            assertThat($(anchorDeleteShop).getAttribute("innerText")).isEqualTo("Delete shop");
            assertThat($(anchorDeleteShop).getAttribute("href")).isEqualTo(baseURL + "#delete_shop");
        });
    }

    @Test
    @DisplayName("Обновление списка текущих магазинов")
    public void shouldBeRefreshButton() {
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
            assertThat($(refreshButton).getAttribute("innerText")).isEqualTo("Refresh");
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
    @DisplayName("Переключатель статуса приватности нового магазина")
    public void shouldBePublicCheckbox() {
        allShopsTable.get(0).shouldBe(Condition.visible);
        Date currentTime = new Date();
        String testShopName = "TestShop" + currentTime.getTime();

        step("Создание публичного тестового магазина", () -> {
            $(createShopFiled).sendKeys(testShopName);
            $(checkboxPublic).click();
            $(createShopButton).click();
            Selenide.switchTo().alert().accept();
        });

        step("Обновление списка магазинов", () -> {
            $(refreshButton).click();
            allShopsTable.get(0).shouldBe(Condition.visible);
        });

        SelenideElement row;
        int i = 1;
        do {
            row = allShopsTable.get(allShopsTable.size() - i);
            i++;
        } while (!Objects.equals(row.$("td:nth-child(2)").getAttribute("innerText"), testShopName));
        String createdShopId = row.$("td:nth-child(1)").getAttribute("innerText");
        String createdShopStatus = row.$("td:nth-child(3)").getAttribute("innerText");

        step("Тестовый магазин имеет публичный статус", () -> {
            assertThat(createdShopStatus).isEqualTo("true");
        });

        step("Удаление тестового магазина", () -> {
            $(deleteShopFiled).sendKeys(createdShopId);
            $(deleteShopButton).click();
            Selenide.switchTo().alert().accept();
        });
    }

    @Test
    @DisplayName("Кнопка Telegram")
    public void shouldBeTelegramButton() {
        step("Кнопка существует", () -> {
            assertThat($(telegramButton).exists()).isTrue();
        });

        step("Название кнопки верно", () -> {
            assertThat($(telegramButton).getAttribute("innerText")).isEqualTo("Telegram");
        });

        step("Кнопка перенаправляет на сайт", () -> {
            $(telegramButton).click();
            webdriver().shouldHave(url("https://web.telegram.org/k/"));
        });
    }

    @Test
    @DisplayName("Кнопка VK")
    public void shouldBeVkButton() {
        step("Кнопка существует", () -> {
            assertThat($(vkButton).exists()).isTrue();
        });

        step("Название кнопки верно", () -> {
            assertThat($(vkButton).getAttribute("innerText")).isEqualTo("VK");
        });

        step("Кнопка перенаправляет на сайт", () -> {
            $(vkButton).click();
            webdriver().shouldHave(url("https://m.vk.com/"));
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"testShop", "TShop"})
    @DisplayName("Нельзя создать магазин с некорректным названием")
    public void shouldNotCreateShopWithShortName(String testShopName) {
        allShopsTable.get(0).shouldBe(Condition.visible);
        int shopsQuantityBefore = allShopsTable.size();

        step("Попытка создать магазин с некорректным названием", () -> {
            $(createShopFiled).sendKeys(testShopName);
            $(createShopButton).click();
        });

        step("Обработка ошибки", () -> {
            $(nameValidation).shouldBe(Condition.visible);
            $(nameValidation).shouldHave(Condition.partialText("Store naming convention: "));
        });

        $(refreshButton).click();
        allShopsTable.get(0).shouldBe(Condition.visible);
        int shopsQuantityAfter = allShopsTable.size();

        step("Количество магазинов не изменилось", () -> {
            assertThat(shopsQuantityAfter).isEqualTo(shopsQuantityBefore);
        });
    }

    @Test
    @DisplayName("Нельзя удалить магазин без ID")
    public void shouldNotDeleteShopWithoutID() {
        step("Попытка удалить магазин без указания ID", () -> {
            $(deleteShopButton).click();
        });

        step("Обработка ошибки", () -> {
            $(idValidation).shouldBe(Condition.visible);
            $(idValidation).shouldHave(Condition.text("Must be not empty"));
        });
    }
}
