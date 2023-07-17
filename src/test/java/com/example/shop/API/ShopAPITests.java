package com.example.shop.API;

import com.example.shop.models.ShopDto;
import com.example.shop.models.ShopPojo;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static com.example.shop.Configuration.buildFactory;
import static com.example.shop.Configuration.createNewSession;
import static com.jayway.jsonpath.internal.function.ParamType.JSON;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

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
        createTestShop();
        Long testShopId = getTestShopId();
        searchTestShop(testShopId);
        deleteTestShop(testShopId);
    }

    @Test
    @DisplayName("Добавление магазина")
    public void shouldAddNewShop() {
        Session session = createNewSession(factory);

        var shopsList = session.createNativeQuery("SELECT * FROM shops", ShopPojo.class).list();
        int shopsQuantityBeforeAdding = shopsList.size();

        createTestShop();
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

        createTestShop();
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
}
