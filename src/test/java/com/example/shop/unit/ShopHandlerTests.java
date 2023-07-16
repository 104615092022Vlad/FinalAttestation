package com.example.shop.unit;

import com.example.shop.ShopHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ShopHandlerTests {
    @Test
    @DisplayName("Имя магазина больше шести символов")
    public void checkLengthPositive() {
        String name = "CarPartsShop";
        int count = 6;
        assertThat(ShopHandler.checkLength(name, count)).isTrue();
    }

    @Test
    @DisplayName("Имя магазина начинается с заглавной буквы")
    public void checkFirstLetterPositive() {
        String name = "Shop";
        assertThat(ShopHandler.checkFirstLetter(name)).isTrue();
    }

    @Test
    @DisplayName("Имя магазина меньше шести символов")
    public void checkLengthNegative() {
        String name = "Shop";
        int count = 6;
        assertThat(ShopHandler.checkLength(name, count)).isFalse();
    }

    @Test
    @DisplayName("Имя магазина начинается со строчной буквы")
    public void checkFirstLetterNegative() {
        String name = "shop";
        assertThat(ShopHandler.checkFirstLetter(name)).isFalse();
    }
}
