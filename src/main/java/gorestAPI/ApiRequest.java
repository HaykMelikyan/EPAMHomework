package gorestAPI;

import core.PropertyReader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.is;

public class ApiRequest {
    private final PropertyReader propReader = new PropertyReader("src/main/resources/ApiRequestProp.properties");

    public ApiRequest() {
        RestAssured.baseURI = propReader.getProperty("BASE_URI");
        RestAssured.basePath = propReader.getProperty("BASE_PATH");
    }

    public Response post(String endPoint, Object requestBody) {
        return RestAssured
                .given()
                .spec(requestSpec())
                .body(requestBody)
                .post(endPoint)
                .then()
                .spec(responseSpec())
                .extract()
                .response();
    }

    public Response get(String endPoint) {
        return RestAssured
                .get(endPoint)
                .then()
                .spec(responseSpec())
                .extract()
                .response();
    }

    public Response delete(String endPoint) {
        return RestAssured
                .given()
                .spec(requestSpec())
                .delete(endPoint)
                .then()
                .spec(responseSpec())
                .extract()
                .response();
    }

    private RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .addHeader("Authorization", propReader.getProperty("AUTHTORIZATION_TOKEN"))
                .setContentType(ContentType.JSON)
                .build();
    }

    private ResponseSpecification responseSpec() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(is(200))
                .build();
    }
}