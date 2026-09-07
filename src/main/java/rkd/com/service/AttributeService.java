package rkd.com.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import rkd.com.model.AttributeModel;
import rkd.com.model.DomainModel;
import rkd.com.model.OptionModel;
import rkd.com.repository.AttributeRepository;
import rkd.com.repository.DomainRepository;
import rkd.com.repository.OptionRepository;
import rkd.com.type.AttributeType;
import rkd.com.exception.DomainNotFoundException;

import java.util.List;

import static rkd.com.message.AttributeMessage.*;

@ApplicationScoped
public class AttributeService {

    private final AttributeRepository attributeRepository;
    private final DomainRepository domainRepository;
    private final OptionRepository optionRepository;

    public AttributeService(AttributeRepository attributeRepository,
                            DomainRepository domainRepository,
                            OptionRepository optionRepository) {
        this.attributeRepository = attributeRepository;
        this.domainRepository = domainRepository;
        this.optionRepository = optionRepository;
    }

    public List<AttributeModel> findAll() {
        return attributeRepository.listAll();
    }

    public List<AttributeModel> findAll(Boolean status) {
        return status == null ? findAll() : attributeRepository.list("status", status);
    }

    public AttributeModel findById(Long id) {
        AttributeModel attribute = attributeRepository.findById(id);
        if (attribute == null) {
            throw new DomainNotFoundException(ATTRIBUTE_NOT_FOUND);
        }
        return attribute;
    }

    @Transactional
    public AttributeModel create(AttributeModel attribute) {
        attribute.setStatus(true);
        attribute.setDomain(resolveDomain(attribute.getDomain()));
        attribute.setOption(resolveOption(attribute));
        attributeRepository.persist(attribute);
        return attribute;
    }

    @Transactional
    public AttributeModel update(Long id, AttributeModel input) {
        AttributeModel attribute = findById(id);

        attribute.setCode(input.getCode());
        attribute.setDescription(input.getDescription());
        attribute.setType(input.getType());
        attribute.setMandatory(input.getMandatory());
        attribute.setStatus(input.getStatus());
        attribute.setDomain(resolveDomain(input.getDomain()));
        attribute.setOption(resolveOption(input));
        return attribute;
    }

    @Transactional
    public boolean delete(Long id) {
        return attributeRepository.deleteById(id);
    }

    private DomainModel resolveDomain(DomainModel domain) {
        if (domain == null || domain.getId() == null) {
            throw new BadRequestException(INVALID_DOMAIN);
        }

        DomainModel existingDomain = domainRepository.findById(domain.getId());
        if (existingDomain == null) {
            throw new DomainNotFoundException(DOMAIN_NOT_FOUND);
        }
        return existingDomain;
    }

    private OptionModel resolveOption(AttributeModel attribute) {
        if (attribute.getType() != AttributeType.OPTION) {
            return null;
        }

        if (attribute.getOption() == null || attribute.getOption().getId() == null) {
            throw new BadRequestException(INVALID_OPTION_ATTRIBUTE);
        }

        OptionModel option = optionRepository.findById(attribute.getOption().getId());
        if (option == null) {
            throw new BadRequestException(OPTION_NOT_FOUND);
        }
        return option;
    }
}
