package rkd.com.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import rkd.com.exception.DomainNotFoundException;
import rkd.com.exception.InvalidActionException;
import rkd.com.model.DomainModel;
import rkd.com.repository.DomainRepository;

import java.util.List;

import static rkd.com.message.DomainMessage.*;

@ApplicationScoped
public class DomainService {

    private final DomainRepository domainRepository;

    public DomainService(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
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
        domain.setParent(resolveParent(domain.getParent(), null));
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
        domain.setParent(resolveParent(input.getParent(), id));
        return domain;
    }

    private DomainModel resolveParent(DomainModel parent, Long currentId) {
        if (parent == null || parent.getId() == null) {
            return null;
        }

        if (currentId != null && currentId.equals(parent.getId())) {
            throw new InvalidActionException(DOMAIN_CANNOT_BE_ITS_OWN_PARENT);
        }

        DomainModel resolved = domainRepository.findById(parent.getId());
        if (resolved == null) {
            throw new DomainNotFoundException(PARENT_DOMAIN_NOT_FOUND);
        }

        if (currentId != null) {
            DomainModel ancestor = resolved;
            while (ancestor != null) {
                if (currentId.equals(ancestor.getId())) {
                    throw new InvalidActionException(CYCLIC_DOMAIN);
                }
                ancestor = ancestor.getParent();
            }
        }

        return resolved;
    }

    @Transactional
    public boolean delete(Long id) {
        if (domainRepository.count("parent.id", id) > 0) {
            throw new InvalidActionException(UNABLE_TO_DELETE_THE_DOMAIN);
        }
        return domainRepository.deleteById(id);
    }
}
