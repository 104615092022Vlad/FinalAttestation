package com.example.shop.API;

import com.example.shop.models.ShopDto;
import com.example.shop.models.ShopPojo;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

import static com.example.shop.Configuration.buildFactory;
import static com.example.shop.Configuration.createNewSession;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class Basis {
    @BeforeAll
    public static void setBaseURI() {
        RestAssured.baseURI = "http://localhost:4000/shops";
    }

    static ShopDto testShop = new ShopDto(0L, "TestShop", true);

    protected static SessionFactory factory = buildFactory();

    @Step("Создание тестового магазина")
    public static void createTestShop() {
        RequestSpecification getAllShops = given()
                .contentType(ContentType.JSON)
                .body(testShop);

        getAllShops.when()
                .post("/add")
                .then()
                .statusCode(200);
    }

    @Step("Получение ID тестового магазина")
    public static Long getTestShopId() {
        Session session = createNewSession(factory);
        var shopsList = session.createNativeQuery("SELECT * FROM shops ORDER BY shop_id DESC LIMIT 1", ShopPojo.class).list();
        ShopPojo testShop = shopsList.get(0);
        session.close();
        return testShop.getShopId();
    }

    @Step("Поиск тестового магазина")
    public static void searchTestShop(Long testShopId) {
        RequestSpecification getTestShop = given();

        Response response = getTestShop
                .when()
                .get("/" + testShopId);

        response.then()
                .statusCode(200)
                .body("shopId", equalTo(testShopId.intValue()),
                        "shopName", equalTo(testShop.getShopName()),
                        "shopPublic", equalTo(testShop.isShopPublic()));
    }

    @Step("Удаление тестового магазина")
    public static void deleteTestShop(Long testShopId) {
        RequestSpecification getAllShops = given();

        getAllShops.when()
                .delete("/delete/" + testShopId)
                .then()
                .statusCode(204);
    }
}
