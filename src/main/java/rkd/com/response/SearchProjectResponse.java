package rkd.com.response;

import java.time.LocalDateTime;
import java.util.List;

public class SearchProjectResponse {
    private Long id;
    private String code;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean status;
    private List<SearchDomainResponse> domains;
    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public Boolean getStatus() { return status; }
    public List<SearchDomainResponse> getDomains() { return domains; }
    public void setId(Long id) { this.id = id; }
    public void setCode(String code) { this.code = code; }
    public void setDescription(String description) { this.description = description; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setStatus(Boolean status) { this.status = status; }
    public void setDomains(List<SearchDomainResponse> domains) { this.domains = domains; }
}
