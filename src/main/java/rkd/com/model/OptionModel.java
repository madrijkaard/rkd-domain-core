package rkd.com.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@jakarta.persistence.Entity
@Table(name = "\"option\"")
public class OptionModel extends EntityModel {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "\"values\"", columnDefinition = "jsonb", nullable = false)
    private JsonNode values;

    public JsonNode getValues() {
        return values;
    }

    public void setValues(JsonNode values) {
        this.values = values;
    }
}
