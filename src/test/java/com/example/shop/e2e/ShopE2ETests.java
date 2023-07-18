package com.example.shop.e2e;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.Selenide.open;

public class ShopE2ETests extends Basis{
    @Test
    @DisplayName("Первый тест")
    public void openOZONPage() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);

        driver.get("http://localhost:63342/shop/src/main/java/com/example/shop/ui/main.html?_ijt=ihc56knqo9khc7n8fv1fkkk1b3");
    }

    @Test
    @DisplayName("Второй тест")
    public void openShopsPage() {
        Configuration.browserCapabilities = new ChromeOptions().addArguments("--remote-allow-origins=*");
        Configuration.browser = "Chrome";
        open("http://localhost:63342/shop/src/main/java/com/example/shop/ui/main.html?_ijt=okqjhdjstsue1ak76q16733958");
        String URLAuthorization = "http://localhost:63342/shop/src/main/java/com/example/shop/ui/main.html?_ijt=2bcdv2cf8hc0f3rla61mk86721";
    }
}
