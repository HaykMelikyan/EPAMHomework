import gorestAPI.ApiRequest;
import gorestAPI.User;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GoRestApiTest {
    private User user;
    ApiRequest apiRequest;
    String endPoint;

    @BeforeMethod
    public void setup() {
        apiRequest = new ApiRequest();
        endPoint = "users/";
    }

    @Test
    public void goRestPostUserTest() {
        user = User.getRandomUser();

        // Post a user and obtain id
        Response postResponse = apiRequest.post(endPoint, user);
        String userId = getField(postResponse, "data.id");

        // Get user and check response
        Response getResponse = apiRequest.get(endPoint + userId);
        checkResponseMatch(getResponse);
    }

    @Test
    public void goRestDeleteTest() {
        user = User.getRandomUser();

        // Post a user and obtain id
        Response postResponse = apiRequest.post(endPoint, user);
        String userId = getField(postResponse, "data.id");

        // Delete user by id
        apiRequest.delete(endPoint + userId);

        // Get user and check response
        Response getResponse = apiRequest.get(endPoint + userId);
        SoftAssert assertion = new SoftAssert();
        if(getField(getResponse, "code").equals("404"))
            assertion.assertEquals(getField(getResponse, "data.message"), "Resource not found");
        else
            assertion.assertNotEquals(getField(getResponse, "data.email"), user.getEmail(), "User with that email hasn't been deleted.");
        assertion.assertAll();
    }

    private void checkResponseMatch(Response response) {
        SoftAssert assertion = new SoftAssert();
        assertion.assertEquals(getField(response, "data.name"), user.getName(), "Name doesn't match.");
        assertion.assertEquals(getField(response, "data.email"), user.getEmail(), "Email doesn't match.");
        assertion.assertEquals(getField(response, "data.gender"), user.getGender(), "Gender doesn't match.");
        assertion.assertEquals(getField(response, "data.status"), user.getStatus(), "Status doesn't match.");
        assertion.assertAll();
    }

    private String getField(Response response, String path) {
        return response.jsonPath().getString(path);
    }
}