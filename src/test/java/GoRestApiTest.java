import gorestAPI.ApiRequest;
import gorestAPI.User;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GoRestApiTest {
    private User user;

    @Test
    public void goRestPostUserTest() {
        ApiRequest apiRequest = new ApiRequest();
        String endPoint = "users/";
        user = User.getRandomUser();

        // Post a user and obtain id
        Response postResponse = apiRequest.post(endPoint, user);
        checkResponse(postResponse);
        String userId = getField(postResponse, "data.id");

        // Get user by id
        Response getResponse = apiRequest.get(endPoint + userId);
        checkResponse(getResponse);

        // Delete user by id
        Response deleteResponse = apiRequest.delete(endPoint + userId);
        checkDelete(deleteResponse);
    }

    private void checkResponse(Response response) {
        SoftAssert assertion = new SoftAssert();
        assertion.assertEquals(getField(response, "data.name"), user.getName(), "Name doesn't match.");
        assertion.assertEquals(getField(response, "data.email"), user.getEmail(), "Email doesn't match.");
        assertion.assertEquals(getField(response, "data.gender"), user.getGender(), "Gender doesn't match.");
        assertion.assertEquals(getField(response, "data.status"), user.getStatus(), "Status doesn't match.");
        assertion.assertAll();
    }

    private void checkDelete(Response response) {
        SoftAssert assertion = new SoftAssert();
        assertion.assertEquals(getField(response, "code"), "204", "Delete status code is not 204.");
        assertion.assertEquals(getField(response, "data"), null, "Deleted data isn't null.");
        assertion.assertAll();
    }

    private String getField(Response response, String path) {
        return response.jsonPath().getString(path);
    }
}