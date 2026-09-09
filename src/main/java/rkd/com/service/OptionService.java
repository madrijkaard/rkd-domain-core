package rkd.com.service;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import rkd.com.exception.DomainNotFoundException;
import rkd.com.exception.InvalidDataException;
import rkd.com.message.OptionMessage;
import rkd.com.model.OptionModel;
import rkd.com.repository.OptionRepository;
import rkd.com.repository.AttributeRepository;
import rkd.com.model.AttributeModel;

import java.util.List;

import static rkd.com.message.OptionMessage.OPTION_NOT_FOUND;

@ApplicationScoped
public class OptionService {

    private final OptionRepository optionRepository;
    private final AttributeRepository attributeRepository;

    public OptionService(OptionRepository optionRepository, AttributeRepository attributeRepository) {
        this.optionRepository = optionRepository;
        this.attributeRepository = attributeRepository;
    }

    public List<OptionModel> findAll() {
        return optionRepository.listAll();
    }

    public List<OptionModel> findAll(Boolean status) {
        return status == null ? findAll() : optionRepository.list("status", status);
    }

    public OptionModel findById(Long id) {
        OptionModel option = optionRepository.findById(id);
        if (option == null) {
            throw new DomainNotFoundException(OPTION_NOT_FOUND);
        }
        return option;
    }

    @Transactional
    public OptionModel create(OptionModel option) {
        validateValues(option.getValues());
        option.setAttribute(resolveAttribute(option.getAttribute()));
        option.setStatus(true);
        optionRepository.persist(option);
        if (option.getAttribute() != null) {
            option.getAttribute().setOption(option);
        }
        return option;
    }

    @Transactional
    public OptionModel update(Long id, OptionModel input) {
        validateValues(input.getValues());

        OptionModel option = findById(id);

        option.setCode(input.getCode());
        option.setDescription(input.getDescription());
        option.setValues(input.getValues());
        option.setStatus(input.getStatus() == null ? option.getStatus() : input.getStatus());
        if (input.getAttribute() != null && input.getAttribute().getId() != null) {
            option.setAttribute(resolveAttribute(input.getAttribute()));
        }
        if (option.getAttribute() != null) {
            option.getAttribute().setOption(option);
        }
        return option;
    }

    @Transactional
    public boolean delete(Long id) {
        OptionModel option = optionRepository.findById(id);
        if (option == null) {
            return false;
        }
        if (option.getAttribute() != null) {
            option.getAttribute().setOption(null);
        }
        optionRepository.delete(option);
        return true;
    }

    private void validateValues(JsonNode values) {
        if (values == null
                || !values.isObject()
                || !values.has("options")
                || !values.get("options").isArray()
                || values.get("options").size() < 2) {
            throw new InvalidDataException(OptionMessage.INVALID_OPTION);
        }
    }

    private AttributeModel resolveAttribute(AttributeModel attribute) {
        if (attribute == null || attribute.getId() == null) {
            throw new InvalidDataException("An option must be linked to an attribute.");
        }
        AttributeModel existing = attributeRepository.findById(attribute.getId());
        if (existing == null) {
            throw new DomainNotFoundException("Attribute not found.");
        }
        if (existing.getType() != rkd.com.type.AttributeType.OPTION) {
            throw new InvalidDataException("Only OPTION attributes can have an option list.");
        }
        return existing;
    }
}
