package com.example.shop.API;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class Basis {
    @BeforeAll
    public static void setBaseURI() {
        RestAssured.baseURI = "http://localhost:4000/shops";
    }
}
