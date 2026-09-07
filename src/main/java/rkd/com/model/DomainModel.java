package rkd.com.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@jakarta.persistence.Entity
@Table(name = "domain")
public class DomainModel extends EntityModel {

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttributeModel> attributes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_domain_id")
    private DomainModel parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<DomainModel> subdomains = new ArrayList<>();

    public List<AttributeModel> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeModel> attributes) {
        this.attributes = attributes;
        if (attributes != null) {
            attributes.forEach(attribute -> attribute.setDomain(this));
        }
    }

    public void addAttribute(AttributeModel attribute) {
        attributes.add(attribute);
        attribute.setDomain(this);
    }

    public DomainModel getParent() {
        return parent;
    }

    public void setParent(DomainModel parent) {
        this.parent = parent;
    }

    public List<DomainModel> getSubdomains() {
        return subdomains;
    }

    public void setSubdomains(List<DomainModel> subdomains) {
        this.subdomains = subdomains;
        if (subdomains != null) {
            subdomains.forEach(subdomain -> subdomain.setParent(this));
        }
    }

    public void addSubdomain(DomainModel subdomain) {
        subdomains.add(subdomain);
        subdomain.setParent(this);
    }
}
