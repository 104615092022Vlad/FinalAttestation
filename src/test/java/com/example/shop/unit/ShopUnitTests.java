package com.example.shop.unit;

import com.example.shop.controllers.ShopController;
import com.example.shop.models.ShopDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ShopUnitTests {
    @Autowired
    ShopController operationsHandler;

    static ShopDto shopNameLess6Letters = new ShopDto(0L, "TShop", true);
    static ShopDto shopName6Letters = new ShopDto(0L, "TShopT", true);
    static ShopDto shopNameMore6Less256Letters = new ShopDto(0L, "TestShop", true);
    static ShopDto shopName256Letters = new ShopDto(0L, "TestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShop", true);
    static ShopDto shopNameMore256Letters = new ShopDto(0L, "TestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShopTestShop", true);
    static ShopDto shopNameAnySymbols = new ShopDto(0L, "A ?!#@$%&№~*^\"`'\\/|\\(_)[]{}-+=.,:;<>", true);
    static ShopDto shopNameCyrillic = new ShopDto(0L, "ТестовыйМагазин", true);
    static ShopDto shopNameStartsWithLowercaseLetter = new ShopDto(0L, "testShop", true);

    @Test
    @DisplayName("Название магазина содержит 8 символов")
    void shouldAddShopFrom6To256LettersName() {
        final ResponseEntity<String> res = operationsHandler.addShop(shopNameMore6Less256Letters);
        assertThat(res.getStatusCode().toString()).isEqualTo("200 OK");
        assertThat(res.getBody()).isNull();
    }

    @Test
    @DisplayName("Название магазина содержит 256 символов")
    void shouldAddShop256LettersName() {
        final ResponseEntity<String> res = operationsHandler.addShop(shopName256Letters);
        assertThat(res.getStatusCode().toString()).isEqualTo("200 OK");
        assertThat(res.getBody()).isNull();
    }

    @Test
    @DisplayName("Название магазина содержит любые символы")
    void shouldAddShopNameWithAnySymbols() {
        final ResponseEntity<String> res = operationsHandler.addShop(shopNameAnySymbols);
        assertThat(res.getStatusCode().toString()).isEqualTo("200 OK");
        assertThat(res.getBody()).isNull();
    }

    @Test
    @DisplayName("Название магазина на кириллице")
    void shouldAddShopCyrillicName() {
        final ResponseEntity<String> res = operationsHandler.addShop(shopNameCyrillic);
        assertThat(res.getStatusCode().toString()).isEqualTo("200 OK");
        assertThat(res.getBody()).isNull();
    }

    @Test
    @DisplayName("Название магазина не должно содержать меньше 6 символов")
    void shouldNotAddShopLess6LettersName() {
        final ResponseEntity<String> res = operationsHandler.addShop(shopNameLess6Letters);
        assertThat(res.getStatusCode().toString()).isEqualTo("400 BAD_REQUEST");
        assertThat(res.getBody()).isEqualTo("Name should be more than 6 letters");
    }

    @Test
    @DisplayName("Название магазина не должно содержать 6 символов")
    void shouldNotAddShop6LettersName() {
        final ResponseEntity<String> res = operationsHandler.addShop(shopName6Letters);
        assertThat(res.getStatusCode().toString()).isEqualTo("400 BAD_REQUEST");
        assertThat(res.getBody()).isEqualTo("Name should be more than 6 letters");
    }

    //@Disabled
    @Test
    @DisplayName("Название магазина не должно содержать более 256 символов")
    void shouldNotAddShopMore256LettersName() {
        final ResponseEntity<String> res = operationsHandler.addShop(shopNameMore256Letters);
        assertThat(res.getStatusCode().toString()).isEqualTo("400 BAD_REQUEST");
        assertThat(res.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Название магазина не должно начинаться со строчной буквы")
    void shouldNotAddShopNameStartsWithLowercaseLetter() {
        final ResponseEntity<String> res = operationsHandler.addShop(shopNameStartsWithLowercaseLetter);
        assertThat(res.getStatusCode().toString()).isEqualTo("400 BAD_REQUEST");
        assertThat(res.getBody()).isEqualTo("Name should begin with a capital letter");
    }
}
