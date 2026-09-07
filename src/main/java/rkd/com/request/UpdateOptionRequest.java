package rkd.com.request;

import com.fasterxml.jackson.databind.JsonNode;

public class UpdateOptionRequest {
    private String code;
    private String description;
    private JsonNode values;
    private Boolean status;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public JsonNode getValues() { return values; }
    public void setValues(JsonNode values) { this.values = values; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
}
