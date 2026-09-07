package rkd.com.service;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import rkd.com.exception.DomainNotFoundException;
import rkd.com.exception.InvalidDataException;
import rkd.com.message.OptionMessage;
import rkd.com.model.OptionModel;
import rkd.com.repository.OptionRepository;

import java.util.List;

import static rkd.com.message.OptionMessage.OPTION_NOT_FOUND;

@ApplicationScoped
public class OptionService {

    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
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
        option.setStatus(true);
        optionRepository.persist(option);
        return option;
    }

    @Transactional
    public OptionModel update(Long id, OptionModel input) {
        validateValues(input.getValues());

        OptionModel option = findById(id);

        option.setCode(input.getCode());
        option.setDescription(input.getDescription());
        option.setValues(input.getValues());
        option.setStatus(input.getStatus());
        return option;
    }

    @Transactional
    public boolean delete(Long id) {
        return optionRepository.deleteById(id);
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
}
