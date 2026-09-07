package rkd.com.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import rkd.com.repository.OptionRepository;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class OptionResourceTest {

    private static final String CODE = "TEST_OPTION";

    @Inject
    OptionRepository optionRepository;

    @AfterEach
    @Transactional
    void cleanDatabase() {
        optionRepository.delete("code", CODE);
    }

    @Test
    void shouldCreateFindUpdateAndDeleteOptionUsingPostgres() {
        Long id = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {"code":"%s","description":"Option list","values":{"options":["A","B"]}}
                        """.formatted(CODE))
                .when()
                .post("/option")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("code", is(CODE))
                .body("values.options", hasItem("A"))
                .extract().jsonPath().getLong("id");

        given()
                .accept(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .get("/option/{id}")
                .then()
                .statusCode(200)
                .body("id", is(id.intValue()));

        given()
                .accept(ContentType.JSON)
                .queryParam("status", true)
                .when()
                .get("/option")
                .then()
                .statusCode(200)
                .body("id", hasItem(id.intValue()));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", id)
                .body("""
                        {"code":"%s","description":"Updated options","values":{"options":["A","B","C"]},"status":false}
                        """.formatted(CODE))
                .when()
                .put("/option/{id}")
                .then()
                .statusCode(200)
                .body("description", is("Updated options"))
                .body("status", is(false));

        assertEquals(1L, optionRepository.count("id", id));
        assertEquals(3, optionRepository.findById(id).getValues().get("options").size());

        given().pathParam("id", id).when().delete("/option/{id}").then().statusCode(204);
        assertEquals(0L, optionRepository.count("id", id));
    }
}
