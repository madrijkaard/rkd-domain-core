package rkd.com.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import rkd.com.model.DomainModel;

@ApplicationScoped
public class DomainRepository implements PanacheRepository<DomainModel> {
}
