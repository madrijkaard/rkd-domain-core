package rkd.com.request;

import java.util.List;

public class CreateDomainRequest {
    private String code;
    private String description;
    private List<Long> relatedDomainIds;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<Long> getRelatedDomainIds() { return relatedDomainIds; }
    public void setRelatedDomainIds(List<Long> relatedDomainIds) { this.relatedDomainIds = relatedDomainIds; }
}
