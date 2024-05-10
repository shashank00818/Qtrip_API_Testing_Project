package Qtrip_API;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.UUID;

import org.json.JSONObject;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class test_case_03 {

    @Test
    public void TestCase03(){

        //Register
        RestAssured.baseURI="https://content-qtripdynamic-qa-backend.azurewebsites.net";
        RestAssured.basePath="/api/v1";

        String email = "testuser"+UUID.randomUUID()+"@abc.com";
        String password = "Abc@1234#";

        RequestSpecification req = RestAssured.given();

        JSONObject credentials = new JSONObject();
        credentials.put("email", email);
        credentials.put("password", password);
        credentials.put("confirmpassword", password);

        req.header("Content-Type","application/json");
        req.body(credentials.toString());

        Response resp = req.when().post("/register");
        //Asserting status code to be 201
        resp.then().assertThat().statusCode(201);

        //Login
        credentials.remove("confirmpassword");
        req.body(credentials.toString());

        resp = req.when().post("/login");

        //Asserting success is true
        resp.then().assertThat().body("success", equalTo(true));
        //Asserting token is generated
        resp.then().assertThat().body("data.token", notNullValue());
        //Asserting id is generated
        resp.then().assertThat().body("data.id", notNullValue());
        //Asserting status code to be 201
        resp.then().assertThat().statusCode(201);

        //Reservations

        JsonPath jp = new JsonPath(resp.getBody().asString());

        String userId = jp.getString("data.id");
        String token = jp.getString("data.token");

        req.header("Content-Type","application/json");
        req.header("Authorization", "Bearer "+ token);

        JSONObject reservation_details = new JSONObject();
        reservation_details.put("userId", userId);
        reservation_details.put("name", "TestUser");
        reservation_details.put("date", "2024-12-12");
        reservation_details.put("person", "2");
        reservation_details.put("adventure","2447910730");

        req.body(reservation_details.toString());

        Response reservation_details_response = req.when().post("/reservations/new");
        reservation_details_response.then().assertThat().statusCode(200);
        // System.out.println("Response is: "+reservation_details_response.asPrettyString());


        // Verifying reservation
        req.queryParam("id", userId);

        reservation_details_response = req.when().get("/reservations");
        // System.out.println("Verifying reservation "+reservation_details_response.asPrettyString());
        reservation_details_response.then().assertThat().body("isCancelled[0]", equalTo(false));
        reservation_details_response.then().assertThat().body("userId[0]", equalTo(userId));
        reservation_details_response.then().assertThat().body("name[0]", equalTo("Testuser"));

        // JsonPath jp2 = new JsonPath(reservation_details_response.getBody().asString());
        // System.out.println("..................."+jp2.getString("isCancelled"));

    }
    
}
