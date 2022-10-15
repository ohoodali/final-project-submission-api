package objects;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;


public class BookingApisObject {
    private String endpoint = "https://restful-booker.herokuapp.com/booking/";
    RequestSpecification requestSpecification  = null;

    public BookingApisObject(RequestSpecification requestSpecificationArg, String loginToken) {
        this.requestSpecification = requestSpecificationArg;
        this.requestSpecification.header("Content-Type", "application/json");
        this.requestSpecification.header("Cookie", "token=" + loginToken);
    }

    public ValidatableResponse createBooking ()
    {
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
                }
                """;
        return requestSpecification.body(body).log().all().when().post(endpoint).then();
    }
    public ValidatableResponse editBooking (int bookingID, String loginToken)
    {

        String body = """
                {
                    "firstname" : "Ohood",
                    "lastname" : "Brown",
                    "totalprice" : 111,
                    "depositpaid" : true,
                    "bookingdates" : {
                        "checkin" : "2018-01-01",
                        "checkout" : "2019-01-01"
                    },
                    "additionalneeds" : "Breakfast"
                }
                """;

        return requestSpecification.body(body).header("Cookie", "token=" + loginToken).log().all()
                .when().put(endpoint+bookingID).then();
    }
    public ValidatableResponse getBooking (int bookingID)
    {
        return requestSpecification.log().all()
                .when().get(endpoint + bookingID).then();
    }
    public ValidatableResponse deleteBooking (int bookingID, String loginToken)
    {
        return requestSpecification.header("Cookie", "token=" + loginToken).log().all()
                .when().delete(endpoint+bookingID).then();
    }
}
