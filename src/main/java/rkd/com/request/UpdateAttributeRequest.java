package rkd.com.request;

import rkd.com.type.AttributeType;

public class UpdateAttributeRequest {
    private String code;
    private String description;
    private AttributeType type;
    private Boolean mandatory;
    private Boolean status;
    private Long domainId;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public AttributeType getType() { return type; }
    public void setType(AttributeType type) { this.type = type; }
    public Boolean getMandatory() { return mandatory; }
    public void setMandatory(Boolean mandatory) { this.mandatory = mandatory; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
    public Long getDomainId() { return domainId; }
    public void setDomainId(Long domainId) { this.domainId = domainId; }
}
