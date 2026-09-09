package rkd.com.service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rkd.com.exception.DomainNotFoundException;
import rkd.com.exception.InvalidDataException;
import rkd.com.model.OptionModel;
import rkd.com.model.AttributeModel;
import rkd.com.repository.AttributeRepository;
import rkd.com.repository.OptionRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static rkd.com.message.OptionMessage.INVALID_OPTION;
import static rkd.com.message.OptionMessage.OPTION_NOT_FOUND;

class OptionServiceTest {

    private OptionRepository optionRepository;
    private AttributeRepository attributeRepository;
    private OptionService optionService;

    @BeforeEach
    void setUp() {
        optionRepository = mock(OptionRepository.class);
        attributeRepository = mock(AttributeRepository.class);
        optionService = new OptionService(optionRepository, attributeRepository);
    }

    @Test
    void shouldFindAllOptions() {
        List<OptionModel> options = List.of(new OptionModel());
        when(optionRepository.listAll()).thenReturn(options);

        assertSame(options, optionService.findAll());
        verify(optionRepository).listAll();
    }

    @Test
    void shouldFindOptionById() {
        OptionModel option = new OptionModel();
        when(optionRepository.findById(1L)).thenReturn(option);

        assertSame(option, optionService.findById(1L));
        verify(optionRepository).findById(1L);
    }

    @Test
    void shouldThrowWhenOptionDoesNotExist() {
        when(optionRepository.findById(1L)).thenReturn(null);

        DomainNotFoundException exception = assertThrows(
                DomainNotFoundException.class,
                () -> optionService.findById(1L)
        );

        assertEquals(OPTION_NOT_FOUND, exception.getMessage());
        verify(optionRepository).findById(1L);
    }

    @Test
    void shouldCreateOptionWithActiveStatus() {
        OptionModel option = new OptionModel();
        option.setValues(validValues());
        AttributeModel attribute = new AttributeModel();
        attribute.setId(10L);
        attribute.setType(rkd.com.type.AttributeType.OPTION);
        option.setAttribute(attribute);
        when(attributeRepository.findById(10L)).thenReturn(attribute);

        OptionModel result = optionService.create(option);

        assertSame(option, result);
        assertEquals(Boolean.TRUE, option.getStatus());
        verify(optionRepository).persist(option);
    }

    @Test
    void shouldRejectOptionWithInvalidValuesOnCreate() {
        OptionModel option = new OptionModel();

        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> optionService.create(option)
        );

        assertEquals(INVALID_OPTION, exception.getMessage());
        verifyNoMoreInteractions(optionRepository);
    }

    @Test
    void shouldUpdateOption() {
        OptionModel existing = new OptionModel();
        OptionModel input = new OptionModel();
        input.setCode("updated-code");
        input.setDescription("Updated description");
        input.setValues(validValues());
        input.setStatus(false);
        when(optionRepository.findById(1L)).thenReturn(existing);

        OptionModel result = optionService.update(1L, input);

        assertSame(existing, result);
        assertEquals("updated-code", existing.getCode());
        assertEquals("Updated description", existing.getDescription());
        assertSame(input.getValues(), existing.getValues());
        assertEquals(Boolean.FALSE, existing.getStatus());
        verify(optionRepository).findById(1L);
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentOption() {
        OptionModel input = new OptionModel();
        input.setValues(validValues());
        when(optionRepository.findById(1L)).thenReturn(null);

        assertThrows(DomainNotFoundException.class, () -> optionService.update(1L, input));

        verify(optionRepository).findById(1L);
        verifyNoMoreInteractions(optionRepository);
    }

    @Test
    void shouldRejectInvalidValuesOnUpdate() {
        OptionModel input = new OptionModel();

        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> optionService.update(1L, input)
        );

        assertEquals(INVALID_OPTION, exception.getMessage());
        verifyNoMoreInteractions(optionRepository);
    }

    @Test
    void shouldDeleteOption() {
        OptionModel option = new OptionModel();
        when(optionRepository.findById(1L)).thenReturn(option);

        assertEquals(true, optionService.delete(1L));
        verify(optionRepository).findById(1L);
        verify(optionRepository).delete(option);
    }

    private ObjectNode validValues() {
        ObjectNode values = JsonNodeFactory.instance.objectNode();
        values.set("options", JsonNodeFactory.instance.arrayNode().add("A").add("B"));
        return values;
    }
}
