package rkd.com.request;

public class UpdateDomainRequest {
    private String code;
    private String description;
    private Boolean status;
    private Long parentDomainId;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
    public Long getParentDomainId() { return parentDomainId; }
    public void setParentDomainId(Long parentDomainId) { this.parentDomainId = parentDomainId; }
}
