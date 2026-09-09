package rkd.com.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import rkd.com.exception.DomainNotFoundException;
import rkd.com.exception.InvalidActionException;
import rkd.com.model.DomainModel;
import rkd.com.repository.DomainRepository;
import rkd.com.repository.AttributeRepository;
import rkd.com.repository.ProjectRepository;
import rkd.com.model.ProjectModel;
import rkd.com.exception.ProjectNotFoundException;
import static rkd.com.message.ProjectMessage.PROJECT_NOT_FOUND;

import java.util.List;

import static rkd.com.message.DomainMessage.*;

@ApplicationScoped
public class DomainService {

    private final DomainRepository domainRepository;
    private final AttributeRepository attributeRepository;
    private final ProjectRepository projectRepository;

    @jakarta.inject.Inject
    public DomainService(DomainRepository domainRepository, AttributeRepository attributeRepository, ProjectRepository projectRepository) {
        this.domainRepository = domainRepository;
        this.attributeRepository = attributeRepository;
        this.projectRepository = projectRepository;
    }

    public DomainService(DomainRepository domainRepository, AttributeRepository attributeRepository) {
        this(domainRepository, attributeRepository, null);
    }

    public List<DomainModel> findAll() {
        return domainRepository.listAll();
    }

    public List<DomainModel> findAll(Boolean status) {
        return status == null ? findAll() : domainRepository.list("status", status);
    }

    public List<DomainModel> findAll(Boolean status, Long projectId) {
        if (projectId == null) return findAll(status);
        if (status == null) return domainRepository.list("project.id", projectId);
        return domainRepository.list("project.id = ?1 and status = ?2", projectId, status);
    }

    public DomainModel findById(Long id) {
        DomainModel domain = domainRepository.findById(id);
        if (domain == null) {
            throw new DomainNotFoundException(DOMAIN_NOT_FOUND);
        }
        return domain;
    }

    @Transactional
    public DomainModel create(DomainModel domain) {
        resolveProject(domain);
        domain.setRelatedDomains(resolveRelatedDomains(domain.getRelatedDomains(), null));
        domain.setStatus(true);
        domainRepository.persist(domain);
        return domain;
    }

    @Transactional
    public DomainModel update(Long id, DomainModel input) {
        DomainModel domain = findById(id);

        domain.setCode(input.getCode());
        domain.setDescription(input.getDescription());
        domain.setStatus(input.getStatus());
        resolveProject(input);
        domain.setProject(input.getProject());
        domain.setRelatedDomains(resolveRelatedDomains(input.getRelatedDomains(), id));
        return domain;
    }

    private void resolveProject(DomainModel domain) {
        if (domain.getProject() == null || domain.getProject().getId() == null || projectRepository == null) return;
        ProjectModel project = projectRepository.findById(domain.getProject().getId());
        if (project == null) throw new ProjectNotFoundException(PROJECT_NOT_FOUND);
        domain.setProject(project);
    }

    private List<DomainModel> resolveRelatedDomains(List<DomainModel> relatedDomains, Long currentId) {
        if (relatedDomains == null || relatedDomains.isEmpty()) {
            return new java.util.ArrayList<>();
        }

        List<DomainModel> resolved = new java.util.ArrayList<>();
        for (DomainModel related : relatedDomains) {
            if (related == null || related.getId() == null) {
                throw new DomainNotFoundException(RELATED_DOMAIN_NOT_FOUND);
            }
            if (currentId != null && currentId.equals(related.getId())) {
                throw new InvalidActionException(DOMAIN_CANNOT_BE_RELATED_TO_ITSELF);
            }
            DomainModel existing = domainRepository.findById(related.getId());
            if (existing == null) {
                throw new DomainNotFoundException(RELATED_DOMAIN_NOT_FOUND);
            }
            if (resolved.stream().noneMatch(item -> item.getId().equals(existing.getId()))) {
                resolved.add(existing);
            }
        }
        return resolved;
    }

    @Transactional
    public boolean delete(Long id) {
        if (attributeRepository.count("domain.id", id) > 0) {
            throw new InvalidActionException(DOMAIN_HAS_ATTRIBUTES);
        }
        if (domainRepository.countRelations(id) > 0) {
            throw new InvalidActionException(DOMAIN_HAS_RELATIONS);
        }
        return domainRepository.deleteById(id);
    }
}
