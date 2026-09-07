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
    private List<SearchAttributeResponse> attributes;
    private Long parentDomainId;
    private List<SearchSubdomainResponse> subdomains;

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
    public List<SearchAttributeResponse> getAttributes() { return attributes; }
    public Long getParentDomainId() { return parentDomainId; }
    public List<SearchSubdomainResponse> getSubdomains() { return subdomains; }
    public void setId(Long id) { this.id = id; }
    public void setCode(String code) { this.code = code; }
    public void setDescription(String description) { this.description = description; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setStatus(Boolean status) { this.status = status; }
    public void setAttributes(List<SearchAttributeResponse> attributes) { this.attributes = attributes; }
    public void setParentDomainId(Long parentDomainId) { this.parentDomainId = parentDomainId; }
    public void setSubdomains(List<SearchSubdomainResponse> subdomains) { this.subdomains = subdomains; }
}
