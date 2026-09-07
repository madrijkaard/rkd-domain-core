package rkd.com.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import rkd.com.model.AttributeModel;

@ApplicationScoped
public class AttributeRepository implements PanacheRepository<AttributeModel> {
}
