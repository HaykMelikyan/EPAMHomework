package gorestAPI;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;

public class ApiRequest {
    private Header header = new Header("Authorization", "Bearer 53b517c08020390a239e02b19714d9e6665715878869e69f811d3e81e8a0029a");
    private int postId;
    private String endPoint;

    public ApiRequest(String endPoint) {
        RestAssured.baseURI = "https://gorest.co.in/";
        RestAssured.basePath = "public-api/";
        this.endPoint = endPoint;
    }

    public int post(Object requestBody) {
        Response postResponse = RestAssured
                .given()
                .header(header)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(endPoint)
                .thenReturn();
        postId = postResponse.jsonPath().getInt("data.id");
        return postId;
    }

    public Response get() {
        return RestAssured
                .get(endPoint + postId)
                .thenReturn();
    }

    public void delete() {
        RestAssured
                .given()
                .header(header)
                .delete(endPoint + postId);
    }
}
