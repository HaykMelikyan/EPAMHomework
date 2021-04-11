import gorestAPI.APIRequest;
import gorestAPI.User;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GorestAPITest {
    private Header header = new Header("Authorization", "Bearer 53b517c08020390a239e02b19714d9e6665715878869e69f811d3e81e8a0029a");
    private APIRequest apiRequest;

    @BeforeClass
    public void setBase() {
        RestAssured.baseURI = "https://gorest.co.in/";
        RestAssured.basePath = "public-api/";
        apiRequest = new APIRequest(header, "users/");
    }

    @AfterClass
    public void deleteUser() {
        apiRequest.delete();
    }

    @Test
    public void APITestGorest() {
        User user = User.getRandomUser();

        // Post a user and obtain id
        apiRequest.post(user);

        // Get user by id
        Response getResponse = apiRequest.get();

        // Assertions
        SoftAssert assertion = new SoftAssert();
        assertion.assertEquals(getField(getResponse, "data.name"), user.getName(), "Name doesn't match.");
        assertion.assertEquals(getField(getResponse, "data.email"), user.getEmail(), "Email doesn't match.");
        assertion.assertEquals(getField(getResponse, "data.gender"), user.getGender(), "Gender doesn't match.");
        assertion.assertEquals(getField(getResponse, "data.status"), user.getStatus(), "Status doesn't match.");
        assertion.assertAll();
    }

    public String getField(Response response, String path) {
        return response.jsonPath().getString(path);
    }
}
