package gorestAPI;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;

public class APIRequest {
    private Header header;
    private int postId;
    private String endPoint;

    public APIRequest(Header header, String endPoint) {
        this.header = header;
        this.endPoint = endPoint;
    }

    public int post(User user) {
        Response postResponse = RestAssured
                .given()
                .header(header)
                .contentType(ContentType.JSON)
                .body(user)
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
