package rkd.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rkd.com.exception.DomainNotFoundException;
import rkd.com.exception.InvalidActionException;
import rkd.com.model.AttributeModel;
import rkd.com.model.DomainModel;
import rkd.com.model.OptionModel;
import rkd.com.repository.AttributeRepository;
import rkd.com.repository.DomainRepository;
import rkd.com.repository.OptionRepository;
import rkd.com.type.AttributeType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static rkd.com.message.AttributeMessage.ATTRIBUTE_NOT_FOUND;
import static rkd.com.message.AttributeMessage.OPTION_ATTRIBUTE_CANNOT_BE_DELETED;

class AttributeServiceTest {

    private AttributeRepository attributeRepository;
    private DomainRepository domainRepository;
    private OptionRepository optionRepository;
    private AttributeService attributeService;

    @BeforeEach
    void setUp() {
        attributeRepository = mock(AttributeRepository.class);
        domainRepository = mock(DomainRepository.class);
        optionRepository = mock(OptionRepository.class);
        attributeService = new AttributeService(attributeRepository, domainRepository, optionRepository);
    }

    @Test
    void shouldFindAllAttributes() {
        List<AttributeModel> attributes = List.of(new AttributeModel());
        when(attributeRepository.listAll()).thenReturn(attributes);

        assertSame(attributes, attributeService.findAll());
        verify(attributeRepository).listAll();
    }

    @Test
    void shouldFindAttributeById() {
        AttributeModel attribute = new AttributeModel();
        when(attributeRepository.findById(1L)).thenReturn(attribute);

        assertSame(attribute, attributeService.findById(1L));
        verify(attributeRepository).findById(1L);
    }

    @Test
    void shouldThrowWhenAttributeDoesNotExist() {
        when(attributeRepository.findById(1L)).thenReturn(null);

        DomainNotFoundException exception = assertThrows(
                DomainNotFoundException.class,
                () -> attributeService.findById(1L)
        );

        assertEquals(ATTRIBUTE_NOT_FOUND, exception.getMessage());
        verify(attributeRepository).findById(1L);
    }

    @Test
    void shouldCreateOptionAttributeWithoutOption() {
        AttributeModel input = new AttributeModel();
        DomainModel domainReference = new DomainModel();
        domainReference.setId(10L);
        input.setType(AttributeType.OPTION);
        input.setDomain(domainReference);

        DomainModel existingDomain = new DomainModel();
        when(domainRepository.findById(10L)).thenReturn(existingDomain);

        AttributeModel result = attributeService.create(input);

        assertSame(input, result);
        assertEquals(Boolean.TRUE, input.getStatus());
        assertSame(existingDomain, input.getDomain());
        assertNull(input.getOption());
        verify(attributeRepository).persist(input);
        verify(domainRepository).findById(10L);
        verifyNoInteractions(optionRepository);
    }

    @Test
    void shouldCreateNonOptionAttributeWithoutOption() {
        AttributeModel input = new AttributeModel();
        DomainModel domainReference = new DomainModel();
        domainReference.setId(10L);
        input.setType(AttributeType.TEXT);
        input.setDomain(domainReference);
        input.setOption(new OptionModel());
        DomainModel existingDomain = new DomainModel();
        when(domainRepository.findById(10L)).thenReturn(existingDomain);

        attributeService.create(input);

        assertSame(existingDomain, input.getDomain());
        assertNull(input.getOption());
        verify(attributeRepository).persist(input);
        verifyNoInteractions(optionRepository);
    }

    @Test
    void shouldUpdateAttribute() {
        AttributeModel existing = new AttributeModel();
        AttributeModel input = new AttributeModel();
        input.setCode("updated-code");
        input.setDescription("Updated description");
        input.setType(AttributeType.TEXT);
        input.setMandatory(true);
        input.setStatus(false);
        DomainModel domainReference = new DomainModel();
        domainReference.setId(10L);
        input.setDomain(domainReference);
        DomainModel existingDomain = new DomainModel();
        when(attributeRepository.findById(1L)).thenReturn(existing);
        when(domainRepository.findById(10L)).thenReturn(existingDomain);

        AttributeModel result = attributeService.update(1L, input);

        assertSame(existing, result);
        assertEquals("updated-code", existing.getCode());
        assertEquals("Updated description", existing.getDescription());
        assertEquals(AttributeType.TEXT, existing.getType());
        assertEquals(Boolean.TRUE, existing.getMandatory());
        assertEquals(Boolean.FALSE, existing.getStatus());
        assertSame(existingDomain, existing.getDomain());
        assertNull(existing.getOption());
        verify(attributeRepository).findById(1L);
        verify(domainRepository).findById(10L);
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentAttribute() {
        when(attributeRepository.findById(1L)).thenReturn(null);

        assertThrows(DomainNotFoundException.class, () -> attributeService.update(1L, new AttributeModel()));

        verify(attributeRepository).findById(1L);
        verifyNoMoreInteractions(attributeRepository);
        verifyNoInteractions(domainRepository, optionRepository);
    }

    @Test
    void shouldDeleteAttribute() {
        AttributeModel attribute = new AttributeModel();
        attribute.setType(AttributeType.TEXT);
        when(attributeRepository.findById(1L)).thenReturn(attribute);
        when(attributeRepository.deleteById(1L)).thenReturn(true);

        assertEquals(true, attributeService.delete(1L));
        verify(attributeRepository).deleteById(1L);
    }

    @Test
    void shouldNotDeleteOptionAttributeWhileOptionIsLinked() {
        AttributeModel attribute = new AttributeModel();
        attribute.setType(AttributeType.OPTION);
        attribute.setOption(new OptionModel());
        when(attributeRepository.findById(1L)).thenReturn(attribute);

        InvalidActionException exception = assertThrows(InvalidActionException.class, () -> attributeService.delete(1L));

        assertEquals(OPTION_ATTRIBUTE_CANNOT_BE_DELETED, exception.getMessage());
        verify(attributeRepository).findById(1L);
        verifyNoMoreInteractions(attributeRepository);
    }
}
