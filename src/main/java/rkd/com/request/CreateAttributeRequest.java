package rkd.com.request;

import rkd.com.type.AttributeType;

public class CreateAttributeRequest {
    private String code;
    private String description;
    private AttributeType type;
    private Boolean mandatory;
    private Long domainId;
    private Long optionId;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public AttributeType getType() { return type; }
    public void setType(AttributeType type) { this.type = type; }
    public Boolean getMandatory() { return mandatory; }
    public void setMandatory(Boolean mandatory) { this.mandatory = mandatory; }
    public Long getDomainId() { return domainId; }
    public void setDomainId(Long domainId) { this.domainId = domainId; }
    public Long getOptionId() { return optionId; }
    public void setOptionId(Long optionId) { this.optionId = optionId; }
}
