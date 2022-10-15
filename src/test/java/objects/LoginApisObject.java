package objects;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class LoginApisObject {
    private String endpoint = "https://restful-booker.herokuapp.com/auth/";
    RequestSpecification requestSpecification = null;

    public LoginApisObject(RequestSpecification requestSpecificationArg) {
        this.requestSpecification = requestSpecificationArg;
        this.requestSpecification.header("Content-Type", "application/json");
    }

    public String loginToApp ()
    {
        String body = """
                {
                    "username" : "admin",
                    "password" : "password123"
                }
                """;
        ValidatableResponse validatableResponse = requestSpecification.body(body)
            .when().post(endpoint).then();
        Response response = validatableResponse.extract().response();
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getString("token");
    }

}
