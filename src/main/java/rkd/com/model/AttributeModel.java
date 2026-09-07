package rkd.com.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import rkd.com.type.AttributeType;

@jakarta.persistence.Entity
@Table(name = "attribute")
public class AttributeModel extends EntityModel {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttributeType type;

    @Column(nullable = false)
    private Boolean mandatory;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "domain_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private DomainModel domain;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "option_id")
    private OptionModel option;

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public DomainModel getDomain() {
        return domain;
    }

    public void setDomain(DomainModel domain) {
        this.domain = domain;
    }

    public OptionModel getOption() {
        return option;
    }

    public void setOption(OptionModel option) {
        this.option = option;
    }
}
