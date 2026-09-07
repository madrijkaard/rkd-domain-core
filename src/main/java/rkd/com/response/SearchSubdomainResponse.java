package rkd.com.response;

public class SearchSubdomainResponse {
    private Long id;
    private String code;
    private String description;
    private Boolean status;

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public Boolean getStatus() { return status; }
    public void setId(Long id) { this.id = id; }
    public void setCode(String code) { this.code = code; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(Boolean status) { this.status = status; }
}
