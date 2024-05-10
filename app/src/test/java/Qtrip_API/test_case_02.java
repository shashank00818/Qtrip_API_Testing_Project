package Qtrip_API;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class test_case_02 {

    @Test
    public void TestCase02(){

        RestAssured.baseURI="https://content-qtripdynamic-qa-backend.azurewebsites.net";
        RestAssured.basePath="/api/v1";

        RequestSpecification req = RestAssured.given();
        req.queryParam("q", "beng");
        Response resp = req.when().get("/cities");
        // System.out.println(resp.asPrettyString());

        //Asserting that size of result is 1
        resp.then().assertThat().body("$", hasSize(1));
        //Asserting that status code is 200
        resp.then().assertThat().statusCode(200);
        //Asserting that description contains "100+ Places"
        resp.then().assertThat().body("description[0]", equalTo("100+ Places"));

    }
    
}
