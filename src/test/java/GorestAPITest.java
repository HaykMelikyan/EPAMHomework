import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GorestAPITest {

    @BeforeClass
    public void setBase() {
        RestAssured.baseURI = "https://gorest.co.in/";
        RestAssured.basePath = "public-api/";
    }

    @Test
    public void APITestGorest() {
        Header header = new Header("Authorization", "Bearer 53b517c08020390a239e02b19714d9e6665715878869e69f811d3e81e8a0029a");

        String name = "HaykPost Test";
        String email = "HaykPostasd@test.io";
        String gender = "Male";
        String status = "Active";

        String postBody = String.format("{\n" +
                        "     \"name\": \"%s\",\n" +
                        "     \"email\": \"%s\",\n" +
                        "     \"gender\": \"%s\",\n" +
                        "     \"status\": \"%s\"\n" +
                        "}\n",
                name, email, gender, status);

        // Post a user and obtain id
        int postId = post(postBody, header);

        // Get user by id
        Response getResponse = get(postId);

        // Assertions
        SoftAssert assertion = new SoftAssert();

        String actualName = getResponse.jsonPath().getString("data.name");
        assertion.assertEquals(actualName, name, "");

        String actualEmail = getResponse.jsonPath().getString("data.email");
        assertion.assertEquals(actualEmail, email);

        String actualGender = getResponse.jsonPath().getString("data.gender");
        assertion.assertEquals(actualGender, gender);

        String actualStatus = getResponse.jsonPath().getString("data.status");
        assertion.assertEquals(actualStatus, status);

        assertion.assertAll();

        // Delete user by id
        delete(postId, header);
    }

    public int post(String postBody, Header header) {
        Response postResponse = RestAssured
                .given()
                .header(header)
                .body(postBody)
                .contentType(ContentType.JSON)
                .when()
                .post("users")
                .then()
                .extract()
                .response();

        return postResponse.jsonPath().getInt("data.id");
    }

    public Response get(int postId) {
        return RestAssured
                .when()
                .get("users/" + postId)
                .then()
                .extract()
                .response();
    }

    public void delete(int postId, Header header) {
        RestAssured
                .given()
                .header(header)
                .when()
                .delete("users/" + postId);
    }
}
