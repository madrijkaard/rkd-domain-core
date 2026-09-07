package rkd.com.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;

public class SearchOptionResponse {
    private Long id;
    private String code;
    private String description;
    private JsonNode values;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean status;

    public SearchOptionResponse() {
    }

    public SearchOptionResponse(Long id, String code, String description, JsonNode values,
                                LocalDateTime createdAt, LocalDateTime updatedAt, Boolean status) {
        this.id = id; this.code = code; this.description = description; this.values = values;
        this.createdAt = createdAt; this.updatedAt = updatedAt; this.status = status;
    }
    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public JsonNode getValues() { return values; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public Boolean getStatus() { return status; }
    public void setId(Long id) { this.id = id; }
    public void setCode(String code) { this.code = code; }
    public void setDescription(String description) { this.description = description; }
    public void setValues(JsonNode values) { this.values = values; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setStatus(Boolean status) { this.status = status; }
}
