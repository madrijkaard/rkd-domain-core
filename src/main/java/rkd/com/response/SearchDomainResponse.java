package rkd.com.response;

import java.time.LocalDateTime;
import java.util.List;

public class SearchDomainResponse {
    private Long id;
    private String code;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean status;
    private Long projectId;
    private List<SearchAttributeResponse> attributes;
    private List<SearchSubdomainResponse> relatedDomains;

    public SearchDomainResponse() {
    }

    public SearchDomainResponse(Long id, String code, String description, LocalDateTime createdAt,
                                LocalDateTime updatedAt, Boolean status, List<SearchAttributeResponse> attributes) {
        this.id = id; this.code = code; this.description = description;
        this.createdAt = createdAt; this.updatedAt = updatedAt; this.status = status;
        this.attributes = attributes;
    }
    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public Boolean getStatus() { return status; }
    public Long getProjectId() { return projectId; }
    public List<SearchAttributeResponse> getAttributes() { return attributes; }
    public List<SearchSubdomainResponse> getRelatedDomains() { return relatedDomains; }
    public void setId(Long id) { this.id = id; }
    public void setCode(String code) { this.code = code; }
    public void setDescription(String description) { this.description = description; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setStatus(Boolean status) { this.status = status; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public void setAttributes(List<SearchAttributeResponse> attributes) { this.attributes = attributes; }
    public void setRelatedDomains(List<SearchSubdomainResponse> relatedDomains) { this.relatedDomains = relatedDomains; }
}
