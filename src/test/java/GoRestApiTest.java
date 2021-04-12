import gorestAPI.ApiRequest;
import gorestAPI.User;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GoRestApiTest {
    private ApiRequest apiRequest;

    @BeforeClass
    public void setBase() {
        apiRequest = new ApiRequest("users/");
    }

    @AfterClass
    public void deleteUser() {
        apiRequest.delete();
    }

    @Test
    public void goRestPostUserTest() {
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

    private String getField(Response response, String path) {
        return response.jsonPath().getString(path);
    }
}
