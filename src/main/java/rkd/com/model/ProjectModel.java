package rkd.com.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@jakarta.persistence.Entity
@Table(name = "project")
public class ProjectModel extends EntityModel {

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<DomainModel> domains = new ArrayList<>();

    public List<DomainModel> getDomains() { return domains; }

    public void setDomains(List<DomainModel> domains) {
        this.domains = domains == null ? new ArrayList<>() : domains;
        this.domains.forEach(domain -> domain.setProject(this));
    }
}
