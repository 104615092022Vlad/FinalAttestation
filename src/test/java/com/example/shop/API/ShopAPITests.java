package com.example.shop.API;

import com.example.shop.models.ShopDto;
import com.example.shop.models.ShopPojo;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hibernate.Session;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.example.shop.Configuration.createNewSession;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ShopAPITests extends Basis {
    @Test
    @DisplayName("Получение списка магазинов")
    public void shouldGetShopsList() {
        RequestSpecification getAllShops = given();
        Response response = getAllShops.get("/all");
        String responseBody = response.body().asString();

        step("Отправка запроса на получение списка всех существующих магазинов", () -> {
            getAllShops.when()
                    .get("/all")
                    .then()
                    .statusCode(200);

            assertThat(response.body()).isNotNull();
        });

        step("Вывод Id, названия и приватности магазинов", () -> {
            assertThat(responseBody.contains("Name")).isTrue();
            assertThat(responseBody.contains("Id")).isTrue();
            assertThat(responseBody.contains("Public")).isTrue();
        });
    }

    @Test
    @DisplayName("Поиск магазина по ID")
    public void shouldGetShopWithId() {
        ShopDto testShop = new ShopDto(0L, "TestShop", true);
        createTestShop(testShop);
        Long testShopId = getTestShopId();
        searchTestShop(testShopId, testShop.getShopName(), testShop.isShopPublic());
        deleteTestShop(testShopId);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "TestShop",
            "ТестовыйМагазин",
            "A ?!#@$%&№~*^\"`'\\/|\\(_)[]{}-+=.,:;<>"
    })
    @DisplayName("Добавление магазина")
    public void shouldAddShop(String testShopName) {
        Session session = createNewSession(factory);

        var shopsList = session.createNativeQuery("SELECT * FROM shops", ShopPojo.class).list();
        int shopsQuantityBeforeAdding = shopsList.size();

        ShopDto testShop = new ShopDto(0L, testShopName, true);

        createTestShop(testShop);
        Long testShopId = getTestShopId();

        shopsList = session.createNativeQuery("SELECT * FROM shops", ShopPojo.class).list();
        int shopsQuantityAfterAdding = shopsList.size();

        session.close();

        deleteTestShop(testShopId);

        step("Был добавлен только тестовый магазин", () -> {
            assertThat(shopsQuantityAfterAdding).isEqualTo(shopsQuantityBeforeAdding + 1);
        });
    }

    @Test
    @DisplayName("Удаление магазина")
    public void shouldDeleteShop() {
        Session session = createNewSession(factory);

        ShopDto testShop = new ShopDto(0L, "TestShop", true);

        createTestShop(testShop);
        Long testShopId = getTestShopId();

        var shopsList = session.createNativeQuery("SELECT * FROM shops", ShopPojo.class).list();
        final int shopsQuantityBeforeRemoving = shopsList.size();

        deleteTestShop(testShopId);

        shopsList = session.createNativeQuery("SELECT * FROM shops", ShopPojo.class).list();
        final int shopsQuantityAfterRemoving = shopsList.size();

        session.close();

        step("Был удалён только тестовый магазин", () -> {
            assertThat(shopsQuantityAfterRemoving).isEqualTo(shopsQuantityBeforeRemoving - 1);
        });
    }

    @Disabled
    @Test
    @DisplayName("Удаление магазина без указания ID")
    public void shouldDeleteShopWithoutID() {
        RequestSpecification getAllShops = given();

        getAllShops.when()
                .delete("/delete/")
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("Обработка ошибки при попытке добавить магазин с названием меньше 6 символов")
    public void shouldNotAddShopWithShortName() {
        ShopDto testShop = new ShopDto(0L, "TShop", true);
        String message = "Name should be more than 6 letters";

        doNotCreateTestShop1(testShop, message);
    }

    @Test
    @DisplayName("Обработка ошибки при попытке добавить магазин, название которого начинается со строчной буквы")
    public void shouldNotAddShopWithIncorrectName() {
        ShopDto testShop = new ShopDto(0L, "testShop", true);
        String message = "Name should begin with a capital letter";

        doNotCreateTestShop1(testShop, message);
    }

    @Disabled
    @Test
    @DisplayName("Обработка ошибки при попытке добавить магазин с названием более 256 символов")
    public void shouldNotAddShopWithTooLongName() {
        String testShopName = "";
        do {
            testShopName += "BestShopEver";
        } while (testShopName.length() <= 256);

        ShopDto testShop = new ShopDto(0L, testShopName, true);
        String message = "Name should be less or equal than 256 letters";

        doNotCreateTestShop2(testShop, message);
    }
}
