package rkd.com.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import rkd.com.repository.DomainRepository;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class DomainResourceTest {

    private static final String ROOT_CODE = "TEST_DOMAIN_ROOT";
    private static final String CHILD_CODE = "TEST_DOMAIN_CHILD";

    @Inject
    DomainRepository domainRepository;

    @AfterEach
    @Transactional
    void cleanDatabase() {
        domainRepository.delete("code", CHILD_CODE);
        domainRepository.delete("code", ROOT_CODE);
    }

    @Test
    void shouldCreateFindUpdateAndDeleteDomainUsingPostgres() {
        Long rootId = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {"code":"%s","description":"Root domain"}
                        """.formatted(ROOT_CODE))
                .when()
                .post("/domain")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("code", is(ROOT_CODE))
                .extract().jsonPath().getLong("id");

        Long childId = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {"code":"%s","description":"Child domain","parentDomainId":%d}
                        """.formatted(CHILD_CODE, rootId))
                .when()
                .post("/domain")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("parentDomainId", is(rootId.intValue()))
                .extract().jsonPath().getLong("id");

        given()
                .accept(ContentType.JSON)
                .pathParam("id", childId)
                .when()
                .get("/domain/{id}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", is(childId.intValue()))
                .body("parentDomainId", is(rootId.intValue()));

        given()
                .accept(ContentType.JSON)
                .queryParam("status", true)
                .when()
                .get("/domain")
                .then()
                .statusCode(200)
                .body("id", hasItem(rootId.intValue()))
                .body("id", hasItem(childId.intValue()));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", childId)
                .body("""
                        {"code":"%s","description":"Updated child","status":false,"parentDomainId":%d}
                        """.formatted(CHILD_CODE, rootId))
                .when()
                .put("/domain/{id}")
                .then()
                .statusCode(200)
                .body("description", is("Updated child"))
                .body("status", is(false));

        given()
                .accept(ContentType.JSON)
                .queryParam("status", false)
                .when()
                .get("/domain")
                .then()
                .statusCode(200)
                .body("id", hasItem(childId.intValue()));

        assertEquals(1L, domainRepository.count("id", childId));
        assertEquals("Updated child", domainRepository.findById(childId).getDescription());

        given().pathParam("id", childId).when().delete("/domain/{id}").then().statusCode(204);
        given().pathParam("id", rootId).when().delete("/domain/{id}").then().statusCode(204);

        assertEquals(0L, domainRepository.count("id", childId));
        assertEquals(0L, domainRepository.count("id", rootId));
    }
}
