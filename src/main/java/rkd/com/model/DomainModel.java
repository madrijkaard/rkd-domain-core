package rkd.com.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.ArrayList;
import java.util.List;

@jakarta.persistence.Entity
@Table(name = "domain")
public class DomainModel extends EntityModel {

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "project_id")
    private ProjectModel project;

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttributeModel> attributes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "domain_relation",
            joinColumns = @JoinColumn(name = "domain_id"),
            inverseJoinColumns = @JoinColumn(name = "related_domain_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"domain_id", "related_domain_id"})
    )
    private List<DomainModel> relatedDomains = new ArrayList<>();

    public List<AttributeModel> getAttributes() {
        return attributes;
    }

    public ProjectModel getProject() { return project; }
    public void setProject(ProjectModel project) { this.project = project; }

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

    public List<DomainModel> getRelatedDomains() {
        return relatedDomains;
    }

    public void setRelatedDomains(List<DomainModel> relatedDomains) {
        this.relatedDomains = relatedDomains == null ? new ArrayList<>() : relatedDomains;
    }

    public void addRelatedDomain(DomainModel relatedDomain) {
        relatedDomains.add(relatedDomain);
    }
}
