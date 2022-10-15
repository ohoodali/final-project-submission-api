import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;

public class Base {

    RequestSpecification requestSpecification ;
    @BeforeClass
    public void beforeClass ()
    {
        requestSpecification = given () ;
    }

}
