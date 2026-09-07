package rkd.com.response;

import rkd.com.type.AttributeType;
import java.time.LocalDateTime;

public class SearchAttributeResponse {
    private Long id;
    private String code;
    private String description;
    private AttributeType type;
    private Boolean mandatory;
    private Long domainId;
    private SearchOptionResponse option;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean status;

    public SearchAttributeResponse() {
    }

    public SearchAttributeResponse(Long id, String code, String description, AttributeType type,
                                   Boolean mandatory, Long domainId, SearchOptionResponse option,
                                   LocalDateTime createdAt, LocalDateTime updatedAt, Boolean status) {
        this.id = id; this.code = code; this.description = description; this.type = type;
        this.mandatory = mandatory; this.domainId = domainId; this.option = option;
        this.createdAt = createdAt; this.updatedAt = updatedAt; this.status = status;
    }
    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public AttributeType getType() { return type; }
    public Boolean getMandatory() { return mandatory; }
    public Long getDomainId() { return domainId; }
    public SearchOptionResponse getOption() { return option; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public Boolean getStatus() { return status; }
    public void setId(Long id) { this.id = id; }
    public void setCode(String code) { this.code = code; }
    public void setDescription(String description) { this.description = description; }
    public void setType(AttributeType type) { this.type = type; }
    public void setMandatory(Boolean mandatory) { this.mandatory = mandatory; }
    public void setDomainId(Long domainId) { this.domainId = domainId; }
    public void setOption(SearchOptionResponse option) { this.option = option; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setStatus(Boolean status) { this.status = status; }
}
