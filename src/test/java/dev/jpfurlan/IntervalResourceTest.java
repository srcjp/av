package dev.jpfurlan;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
public class IntervalResourceTest {

    @Test
    public void shouldReturnIntervals() {
        given()
                .when().get("/producers/intervals")
                .then()
                .statusCode(200)
                .body("min.size()", greaterThan(0))
                .body("max.size()", greaterThan(0));
    }
}