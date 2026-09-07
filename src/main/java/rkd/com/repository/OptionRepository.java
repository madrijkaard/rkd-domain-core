package rkd.com.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import rkd.com.model.OptionModel;

@ApplicationScoped
public class OptionRepository implements PanacheRepository<OptionModel> {
}
