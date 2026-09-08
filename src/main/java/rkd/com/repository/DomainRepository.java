package rkd.com.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import rkd.com.model.DomainModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class DomainRepository implements PanacheRepository<DomainModel> {

    @PersistenceContext
    EntityManager entityManager;

    public long countRelations(Long domainId) {
        Number count = (Number) entityManager.createNativeQuery(
                        "SELECT COUNT(*) FROM domain_relation WHERE domain_id = :id OR related_domain_id = :id")
                .setParameter("id", domainId)
                .getSingleResult();
        return count.longValue();
    }
}
