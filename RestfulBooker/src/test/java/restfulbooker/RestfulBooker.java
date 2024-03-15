package restfulbooker;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RestfulBooker {

  public static String token;
  private final File authPayload = new File("src/main/resources/credentialsAuth.json");
  private final File updatePayload = new File("src/main/resources/updatePayload.json");
  private final File patchPayload = new File("src/main/resources/updatePartOfABookingPayload.json");
  private final File createPayload = new File("src/main/resources/createBookingPayload.json");
  public RequestSpecification requestSpec;

  @BeforeClass
  public void setBaseUriAndCreateCookieToken() {
    requestSpec = given().baseUri("http://localhost:3001");

  /*  could be made to be used as a default specification by using
    RestAssured.requestSpecification = requestSpec;*/

/*
   We can put JSON object in a Java map:

   Map jsonBodyUsingMap = new HashMap();
    jsonBodyUsingMap.put("firstname", "Jim");
    jsonBodyUsingMap.put("lastname", "Brown");
    jsonBodyUsingMap.put("totalprice", 111);
    jsonBodyUsingMap.put("depositpaid", true);

    Map bookingDatesMap = new HashMap<>();
    bookingDatesMap.put("checkin", "2021-07-01");
    bookingDatesMap.put("checkout", "2021-07-01");

    jsonBodyUsingMap.put("bookingdates", bookingDatesMap);
    jsonBodyUsingMap.put("additionalneeds", "Breakfast");*/

    token =
        given(requestSpec).
            contentType(ContentType.JSON).
            body(authPayload).
            log().
            all().
//            pathParam("resourcePath", "auth").
    when().
            post("/auth").
            //NOTE: if we're using baseUri in combination with path parameters, it won't work
//            post("http://localhost:3001/{resourcePath}"). -----> using pathParam
//            post("http://localhost:3001/{resourcePath}", "auth"). ----> using parameter directly
    then().
            assertThat().
            body(JsonSchemaValidator.matchesJsonSchemaInClasspath(
                "schemaValidator.json")).
            and().extract().path("token");

//    QueryableRequestSpecification queryRequest = SpecificationQuerier.query(requestSpec);
//    System.out.println(queryRequest.getContentType());
  }

  @Test
  public void createBookingRestful() {
    given(requestSpec).
        contentType(ContentType.JSON).
        body(createPayload).
        cookie("token", token).
        log().
        all().
        when().
        post("/booking").
        then().
        assertThat().
        statusCode(200).
        body("bookingid", not(isEmptyOrNullString())).
        body("booking.lastname", equalTo("Brown"));
  }

  @Test
  public void updateBookingRestful() {

    given(requestSpec).
        contentType(ContentType.JSON).
        body(updatePayload).
        cookie("token", token).
        pathParam("id", 8).
        log().
        all().
        when().
        put("/booking/{id}").
        then().
        assertThat().
        statusCode(200).
        body("lastname", equalTo("Brownie"));
  }

  @Test
  public void patchBookingRestful() {

    given(requestSpec).
        contentType(ContentType.JSON).
        body(patchPayload).
        cookie("token", token).
        pathParam("id", 8).
        log().
        all().
        when().
        patch("/booking/{id}").
        then().
        assertThat().
        statusCode(200).
        body("lastname", equalTo("Adams"));
  }

  @Test
  public void deleteBookingRestful() {
//in order to delete we would have to create first to not violate the existing data

    Integer bookingId =
        given(requestSpec).
            contentType(ContentType.JSON).
            body(createPayload).
            cookie("token", token).
            log().
            all().
            when().
            post("/booking").
            then().
            assertThat().
            statusCode(200).
            body("bookingid", not(isEmptyOrNullString())).
            and().extract().path("bookingid");

    given(requestSpec).
        contentType(ContentType.JSON).
        cookie("token", token).
        pathParam("id", bookingId).
        log().
        all().
        when().
        delete("/booking/{id}").
        then().
        assertThat().
        statusCode(201);

    //verifying the book is deleted

    given(requestSpec).
        contentType(ContentType.JSON).
        cookie("token", token).
        pathParam("id", bookingId).
        log().
        all().
        when().
        get("/booking/{id}").
        then().
        assertThat().
        statusCode(404);
  }


  //------ following tests are using ITestContext from TestNG
  @Test
  public void createBookingAndStoreIdInContext(ITestContext context) {

    int bookingId =
        given(requestSpec).
            contentType(ContentType.JSON).
            body(createPayload).
            cookie("token", token).
            log().
            all().
            when().
            post("/booking").
            then()
            .assertThat()
            .statusCode(200)
            .log()
            .all()
            .extract()
            .jsonPath()
            .get("bookingid");

    context.setAttribute("bookingId", bookingId);
  }

  @Test
  public void updateBookingUsingContext(ITestContext context) {
    int bookingId = (int) context.getAttribute("bookingId");
    given()
        .log()
        .all()
        .baseUri("https://restful-booker.herokuapp.com/")
        .basePath("booking/" + bookingId)
        .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
        .contentType(ContentType.JSON)
        .body("{\r\n" +
            "    \"firstname\" : \"Amod\",\r\n" +
            "    \"lastname\" : \"Mahajan\",\r\n" +
            "    \"totalprice\" : 222,\r\n" +
            "    \"depositpaid\" : true,\r\n" +
            "    \"bookingdates\" : {\r\n" +
            "        \"checkin\" : \"2022-01-01\",\r\n" +
            "        \"checkout\" : \"2022-01-01\"\r\n" +
            "    },\r\n" +
            "    \"additionalneeds\" : \"Breakfast\"\r\n" +
            "}")
        .when()
        .put()
        .then()
        .log()
        .all();

  }

}
