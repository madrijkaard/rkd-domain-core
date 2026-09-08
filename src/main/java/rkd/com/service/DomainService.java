package rkd.com.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import rkd.com.exception.DomainNotFoundException;
import rkd.com.exception.InvalidActionException;
import rkd.com.model.DomainModel;
import rkd.com.repository.DomainRepository;
import rkd.com.repository.AttributeRepository;

import java.util.List;

import static rkd.com.message.DomainMessage.*;

@ApplicationScoped
public class DomainService {

    private final DomainRepository domainRepository;
    private final AttributeRepository attributeRepository;

    public DomainService(DomainRepository domainRepository, AttributeRepository attributeRepository) {
        this.domainRepository = domainRepository;
        this.attributeRepository = attributeRepository;
    }

    public List<DomainModel> findAll() {
        return domainRepository.listAll();
    }

    public List<DomainModel> findAll(Boolean status) {
        return status == null ? findAll() : domainRepository.list("status", status);
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
        domain.setRelatedDomains(resolveRelatedDomains(input.getRelatedDomains(), id));
        return domain;
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
