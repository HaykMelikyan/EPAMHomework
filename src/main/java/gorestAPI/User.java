package gorestAPI;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class User {
    private String name;
    private String email;
    private String gender;
    private String status;

    public static User getRandomUser() {
        return new User(
                "HaykPost Test" + System.currentTimeMillis(),
                "HaykPostn" + System.currentTimeMillis() + "@test.io",
                "Male",
                "Active"
        );
    }
}