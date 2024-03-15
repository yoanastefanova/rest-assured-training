package restassured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ZippopotamRestAssured {

  public RequestSpecification requestSpec;

  public ResponseSpecification responseSpec;

  @DataProvider(name = "Country codes and zip codes")
  public static Object[][] zippopotamusCodesAndExpectedValue() {
    return new Object[][]{
        {"us", "90210", "Beverly Hills"},
        {"ad", "ad100", "Canillo"},
        {"BG", "1000", "София / Sofija"},
        {"tr", "01000", "Dervişler Köyü"}
    };
  }

  @BeforeClass
  public void setupUrlAndBasicExpectations() {

    requestSpec =
        RestAssured.given().baseUri("http://zippopotam.us");

    responseSpec = new ResponseSpecBuilder().expectStatusCode(200)
        .expectContentType(ContentType.JSON).build();
//    requestSpec = new RequestSpecBuilder().setBaseUri("http://zippopotam.us")
//        .build();
//    baseUri = "http://zippopotam.us";
  }


  @Test(dataProvider = "Country codes and zip codes")
  public void checkIfPlaceIsCorrectInResponseBody(String countryCode, String zipCode,
      String expectedPlaceName) {
//    given(requestSpec).formParam("countryCode",countryCode).
//        formParam("zipCode", zipCode).
////        spec().
////        when().
////        pathParam("countryCode", countryCode).pathParam("zipCode", zipCode).
////        get(countryCode, zipCode).
////        get( requestSpecification + "/{countryCode}/{zipCode}").
////    get(baseUri, countryCode, zipCode).
//        then().
//        body("places[0].'place name'", equalTo(expectedPlaceName));

//    RequestSpecification requestSpecification = RestAssured.given();

    given(requestSpec).
        /* Setting the Base URI, Base Path, Content Type , Request body (Payload)
        , cookies, headers , authentication , params etc.*/
            when().
        /*Which HTTP request to hit i.e. HTTP verbs – GET, POST etc*/
            get(countryCode + "/" + zipCode).
        then().
        /*Verify status code, response data, log, extracting data etc.*/
            spec(responseSpec).
        and().
        assertThat().body("places[0].'place name'", equalTo(expectedPlaceName));
  }

  @Test
  public void checkIfPlaceIsCorrectInResponseBodyAndExtractionToAVariable() {

    String placeName =
        given(requestSpec).
            when().
            get("us/90210").
            then().
            spec(responseSpec).
            and().
            //extracts the value of the variable from the response
                extract().path("places[0].'place name'");

    String state =
        given(requestSpec).
            when().
            get("us/90210").
            then().
            spec(responseSpec).
            and().
            //extracts the value of the variable from the response
                extract().path("places[0].'state'");

    String country =
        given(requestSpec).
            when().
            get("us/90210").
            then().
            spec(responseSpec).
            and().
            //extracts the value of the variable from the response
                extract().path("country");

    //Soft Assert does not throw an exception when an assert
    //fails and would continue with the next step after the assert statement.
    //If there is any exception and you want to throw it then you need to use assertAll() method as a last statement
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertEquals(placeName, "Beverly Hills");
    softAssert.assertEquals(state, "California");
    softAssert.assertEquals(country, "United State");
    softAssert.assertAll();
  }

}
