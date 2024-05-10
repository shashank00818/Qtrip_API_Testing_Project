package Qtrip_API;

import java.util.UUID;

import org.json.JSONObject;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static org.hamcrest.Matchers.*;

public class test_case_01 {
    
    @Test
    public void TestCase01(){

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
    }
}
