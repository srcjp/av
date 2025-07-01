package dev.jpfurlan;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class IntervalResourceTest {

    @Test
    public void shouldReturnIntervalsFromDefaultFile() throws IOException {
        String expectedJson = Files.readString(Path.of("src/test/resources/expected-intervals.json"));
        Map<String, Object> expected = new JsonPath(expectedJson).getMap("$");

        String response = given()
                .when().get("/producers/intervals")
                .then()
                .statusCode(200)
                .extract().asString();

        Map<String, Object> actual = new JsonPath(response).getMap("$");
        Assertions.assertEquals(expected, actual);
    }
}
