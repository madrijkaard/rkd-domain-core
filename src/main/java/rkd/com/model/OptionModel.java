package rkd.com.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@jakarta.persistence.Entity
@Table(name = "\"option\"")
public class OptionModel extends EntityModel {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attribute_id", unique = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AttributeModel attribute;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "\"values\"", columnDefinition = "jsonb", nullable = false)
    private JsonNode values;

    public JsonNode getValues() {
        return values;
    }

    public void setValues(JsonNode values) {
        this.values = values;
    }

    public AttributeModel getAttribute() {
        return attribute;
    }

    public void setAttribute(AttributeModel attribute) {
        this.attribute = attribute;
    }
}
