package rkd.com.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rkd.com.model.DomainModel;
import rkd.com.repository.AttributeRepository;
import rkd.com.repository.DomainRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class AttributeResourceTest {

    private static final String DOMAIN_CODE = "TEST_ATTRIBUTE_DOMAIN";
    private static final String ATTRIBUTE_CODE = "TEST_ATTRIBUTE";

    @Inject
    AttributeRepository attributeRepository;

    @Inject
    DomainRepository domainRepository;

    private Long domainId;

    @BeforeEach
    @Transactional
    void createDomainReference() {
        DomainModel domain = new DomainModel();
        domain.setCode(DOMAIN_CODE);
        domain.setDescription("Attribute test domain");
        domain.setStatus(true);
        domainRepository.persistAndFlush(domain);
        domainId = domain.getId();
    }

    @AfterEach
    @Transactional
    void cleanDatabase() {
        attributeRepository.delete("code", ATTRIBUTE_CODE);
        domainRepository.delete("code", DOMAIN_CODE);
    }

    @Test
    void shouldCreateFindUpdateAndDeleteAttributeUsingPostgres() {
        Long id = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {"code":"%s","description":"Text attribute","type":"TEXT","mandatory":true,"domainId":%d}
                        """.formatted(ATTRIBUTE_CODE, domainId))
                .when()
                .post("/attribute")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("code", is(ATTRIBUTE_CODE))
                .body("domainId", is(domainId.intValue()))
                .extract().jsonPath().getLong("id");

        given()
                .accept(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .get("/attribute/{id}")
                .then()
                .statusCode(200)
                .body("id", is(id.intValue()));

        given()
                .accept(ContentType.JSON)
                .queryParam("status", true)
                .when()
                .get("/attribute")
                .then()
                .statusCode(200)
                .body("id", hasItem(id.intValue()));

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("id", id)
                .body("""
                        {"code":"%s","description":"Updated attribute","type":"TEXT","mandatory":false,"status":false,"domainId":%d}
                        """.formatted(ATTRIBUTE_CODE, domainId))
                .when()
                .put("/attribute/{id}")
                .then()
                .statusCode(200)
                .body("description", is("Updated attribute"))
                .body("mandatory", is(false))
                .body("status", is(false));

        assertEquals(1L, attributeRepository.count("id", id));
        assertEquals("Updated attribute", attributeRepository.findById(id).getDescription());

        given().pathParam("id", id).when().delete("/attribute/{id}").then().statusCode(204);
        assertEquals(0L, attributeRepository.count("id", id));
    }
}
