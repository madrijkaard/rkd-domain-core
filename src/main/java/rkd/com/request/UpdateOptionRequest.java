package rkd.com.request;

import com.fasterxml.jackson.databind.JsonNode;

public class UpdateOptionRequest {
    private String code;
    private String description;
    private JsonNode values;
    private Boolean status;
    private Long attributeId;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public JsonNode getValues() { return values; }
    public void setValues(JsonNode values) { this.values = values; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
    public Long getAttributeId() { return attributeId; }
    public void setAttributeId(Long attributeId) { this.attributeId = attributeId; }
}
