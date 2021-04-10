package gorestAPI;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Optional;

public class APIRequest {
    private Header header;
    private int postId;

    private String endPoint = "";

    public APIRequest() {};

    public APIRequest(Header header){
        setHeader(header);
    }

    public APIRequest(String endPoint){
        setEndPoint(endPoint);
    }

    public APIRequest(Header header, String endPoint){
        setHeader(header);
        setEndPoint(endPoint);
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public void post(User user) {
        Response postResponse = RestAssured
                .given()
                .header(header)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(endPoint)
                .then()
                .extract()
                .response();
        postId = postResponse.jsonPath().getInt("data.id");
    }

    public Response get() {
        return RestAssured
                .when()
                .get(endPoint + postId)
                .then()
                .extract()
                .response();
    }

    public void delete() {
        RestAssured
                .given()
                .header(header)
                .when()
                .delete(endPoint + postId);
    }
}
