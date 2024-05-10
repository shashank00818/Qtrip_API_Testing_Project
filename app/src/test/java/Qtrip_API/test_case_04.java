package Qtrip_API;

import java.util.UUID;

import org.json.JSONObject;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class test_case_04 {

    @Test
    public void TestCase04() {
    
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


        //Re-registering with same email id and password
        resp = req.when().post("/register");
        resp.then().assertThat().statusCode(400);

    }
    
}
