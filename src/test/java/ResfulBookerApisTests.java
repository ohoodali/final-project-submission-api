import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
public class ResfulBookerApisTests {
    // login as precondition
    String accessToken;
    int bookingID;

    @BeforeClass
    public void setupPreconditon_loginToApp() {
        String endpoint = "https://restful-booker.herokuapp.com/auth";
        String body = """
                {
                    "username" : "admin",
                    "password" : "password123"
                }""";
        Response response = given().body(body).header("Content-Type", "application/json")
                .log().all()
                .when()
                .post(endpoint)
                .then().extract().response();

        JsonPath jsonPath = response.jsonPath();
        accessToken = jsonPath.getString("token");
        System.out.println(accessToken);
    }

    // create booking
    @Test(priority = 0)
    public void testCreateValidBooking() {
        String endpoint = "https://restful-booker.herokuapp.com/booking";
        String body = """
                {
                    "firstname" : "Jim",
                    "lastname" : "Brown",
                    "totalprice" : 111,
                    "depositpaid" : true,
                    "bookingdates" : {
                        "checkin" : "2018-01-01",
                        "checkout" : "2019-01-01"
                    },
                    "additionalneeds" : "Breakfast"
                }""";
        var responseToValidate = given().body(body).header("Content-Type", "application/json")
                .log().all().when().post(endpoint).then();

        responseToValidate.body("booking.firstname", equalTo("Jim"));
        responseToValidate.statusCode(200);

        Response response = responseToValidate.extract().response();
        JsonPath jsonPath = response.jsonPath();
        bookingID = jsonPath.getInt("bookingid");

        responseToValidate.log().all();
    }

    // edit booking
    @Test(priority = 1)
    public void testEditBooking() {
        String endpoint = "https://restful-booker.herokuapp.com/booking/" +bookingID;
        String body = """
                {
                    "firstname" : "James",
                    "lastname" : "Brown",
                    "totalprice" : 111,
                    "depositpaid" : true,
                    "bookingdates" : {
                        "checkin" : "2018-01-01",
                        "checkout" : "2019-01-01"
                    },
                    "additionalneeds" : "Breakfast"
                }""";
        var responseToValidate = given().body(body)
                .header("Content-Type", "application/json")
                .header("Accept" , "application/json")
                .header("cookie" , "token=" + accessToken)
                .log().all().when().put(endpoint).then();

        responseToValidate.body("firstname", equalTo("James"));
        responseToValidate.statusCode(200);

    }

    // read booking
    @Test(priority = 2)
    public void testGetBooking ()
    {
        String endpoint = "https://restful-booker.herokuapp.com/booking/" +bookingID;
        var responseToValidate = given()
                .header("Content-Type", "application/json")
                .log().all().when().get(endpoint).then();

        responseToValidate.body("firstname", equalTo("James"));
        responseToValidate.statusCode(200);
    }
    // delete booking
    @Test(priority = 3)
    public void testDeleteBooking ()
    {
        String endpoint = "https://restful-booker.herokuapp.com/booking/" +bookingID;
        var responseToValidate = given()
                .header("Content-Type", "application/json")
                .header("cookie" , "token=" + accessToken)
                .log().all().when().delete(endpoint).then();

        responseToValidate.statusCode(201);
        Response response = responseToValidate.extract().response();
        Assert.assertEquals(response.asString() , "Created");
    }
}
