package com.example.shop.API;

import com.example.shop.models.ShopDto;
import com.example.shop.models.ShopPojo;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.shop.Configuration.buildFactory;
import static com.example.shop.Configuration.createNewSession;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ShopAPITests extends Basis {
    @Test
    @DisplayName("Получение списка магазинов")
    public void shouldGetShopsList() {
        RequestSpecification getAllShops = given();

        getAllShops.when()
                .get("/all")
                .then()
                .statusCode(200);

        //ToDo: тело ответа не должно быть пустым. Ограничиться этой проверкой.
    }

    @Test
    @DisplayName("Добавление магазина")
    public void shouldAddNewShop() {
       ShopDto newShop = new ShopDto(0L, "JKLMNOP", true);

       SessionFactory factory = buildFactory();
       Session session = createNewSession(factory);
       var res = session.createNativeQuery("select * from shops", ShopPojo.class).list();
       int shopsQuantity = res.size();

       RequestSpecification getAllShops = given()
                .contentType(ContentType.JSON)
                .body(newShop);

       getAllShops.when()
                .post("/add")
                .then()
                .statusCode(200);

       res = session.createNativeQuery("select * from shops", ShopPojo.class).list();

       assertThat(res.size()).isEqualTo(shopsQuantity + 1);
    }

    @Test
    @DisplayName("Удаление магазина")
    public void shouldDeleteShop() {
        ShopDto newShop = new ShopDto(0L, "TestShop", true);

        SessionFactory factory = buildFactory();
        Session session = createNewSession(factory);

        step("Создание тестового магазина", () -> {
            RequestSpecification getAllShops = given()
                    .contentType(ContentType.JSON)
                    .body(newShop);

            getAllShops.when()
                    .post("/add")
                    .then()
                    .statusCode(200);
        });

        var shopsList = session.createNativeQuery("SELECT * FROM shops ORDER BY shop_id DESC LIMIT 1", ShopPojo.class).list();
        ShopPojo testShop = shopsList.get(0);
        Long testShopId = testShop.getShopId();

        step("Получение Id тестового магазина", () -> {

        });

        shopsList = session.createNativeQuery("select * from shops", ShopPojo.class).list();
        final int shopsQuantityBeforeDelete = shopsList.size();

        step("Удаление тестового магазина", () -> {
            RequestSpecification getAllShops = given();

            getAllShops.when()
                    .delete("/delete/" + testShopId)
                    .then()
                    .statusCode(204);
        });

        shopsList = session.createNativeQuery("select * from shops", ShopPojo.class).list();
        final int shopsQuantityAfterDelete = shopsList.size();

        step("Был удалён только тестовый магазин", () -> {
            assertThat(shopsQuantityAfterDelete).isEqualTo(shopsQuantityBeforeDelete - 1);
        });
    }

    @Test
    @DisplayName("Поиск магазина по ID")
    public void shouldGetShopWithId() {
        RequestSpecification getAllShops = given();

        Response response = getAllShops
                .when()
                .get("/3052");

        response.then()
                .log().body()
                .statusCode(200)
                .body("shopId", equalTo(3052),
                        "shopName", equalTo("Abcdefg"),
                        "shopPublic", equalTo(false));
    }

    @Test
    @DisplayName("Попытка извлечь Id магазина")
    public void shouldGetShopId() {
        ShopDto newShop = new ShopDto(0L, "AQASHOP", true);

        RequestSpecification getAllShops = given()
                .contentType(ContentType.JSON)
                .body(newShop);

        getAllShops.when()
                .post("/add")
                .then()
                .statusCode(200);

        SessionFactory factory = buildFactory();
        Session session = createNewSession(factory);

        var res = session.createNativeQuery("SELECT * FROM shops ORDER BY shop_id DESC LIMIT 1", ShopPojo.class).list();
        ShopPojo addedShop = res.get(0);
        Long newShopId = addedShop.getShopId();

        assertThat(newShopId).isEqualTo(3103);

    }
}
