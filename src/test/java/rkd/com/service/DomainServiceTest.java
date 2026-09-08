package rkd.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rkd.com.exception.DomainNotFoundException;
import rkd.com.model.DomainModel;
import rkd.com.repository.DomainRepository;
import rkd.com.repository.AttributeRepository;
import rkd.com.exception.InvalidActionException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static rkd.com.message.DomainMessage.DOMAIN_NOT_FOUND;
import static rkd.com.message.DomainMessage.DOMAIN_HAS_ATTRIBUTES;
import static rkd.com.message.DomainMessage.DOMAIN_HAS_RELATIONS;
import static rkd.com.message.DomainMessage.DOMAIN_CANNOT_BE_RELATED_TO_ITSELF;

class DomainServiceTest {

    private DomainRepository domainRepository;
    private AttributeRepository attributeRepository;
    private DomainService domainService;

    @BeforeEach
    void setUp() {
        domainRepository = mock(DomainRepository.class);
        attributeRepository = mock(AttributeRepository.class);
        domainService = new DomainService(domainRepository, attributeRepository);
    }

    @Test
    void shouldFindAllDomains() {
        List<DomainModel> domains = List.of(new DomainModel());
        when(domainRepository.listAll()).thenReturn(domains);

        assertSame(domains, domainService.findAll());
        verify(domainRepository).listAll();
    }

    @Test
    void shouldFindDomainById() {
        DomainModel domain = new DomainModel();
        when(domainRepository.findById(1L)).thenReturn(domain);

        assertSame(domain, domainService.findById(1L));
        verify(domainRepository).findById(1L);
    }

    @Test
    void shouldThrowWhenDomainDoesNotExist() {
        when(domainRepository.findById(1L)).thenReturn(null);

        DomainNotFoundException exception = assertThrows(
                DomainNotFoundException.class,
                () -> domainService.findById(1L)
        );

        assertEquals(DOMAIN_NOT_FOUND, exception.getMessage());
        verify(domainRepository).findById(1L);
    }

    @Test
    void shouldCreateDomainWithActiveStatus() {
        DomainModel domain = new DomainModel();

        DomainModel result = domainService.create(domain);

        assertSame(domain, result);
        assertEquals(Boolean.TRUE, domain.getStatus());
        verify(domainRepository).persist(domain);
    }

    @Test
    void shouldUpdateDomain() {
        DomainModel existing = new DomainModel();
        DomainModel input = new DomainModel();
        input.setCode("updated-code");
        input.setDescription("Updated description");
        input.setStatus(false);
        when(domainRepository.findById(1L)).thenReturn(existing);

        DomainModel result = domainService.update(1L, input);

        assertSame(existing, result);
        assertEquals("updated-code", existing.getCode());
        assertEquals("Updated description", existing.getDescription());
        assertEquals(Boolean.FALSE, existing.getStatus());
        verify(domainRepository).findById(1L);
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentDomain() {
        when(domainRepository.findById(1L)).thenReturn(null);

        assertThrows(DomainNotFoundException.class, () -> domainService.update(1L, new DomainModel()));

        verify(domainRepository).findById(1L);
        verifyNoMoreInteractions(domainRepository);
    }

    @Test
    void shouldDeleteDomain() {
        when(attributeRepository.count("domain.id", 1L)).thenReturn(0L);
        when(domainRepository.countRelations(1L)).thenReturn(0L);
        when(domainRepository.deleteById(1L)).thenReturn(true);

        assertEquals(true, domainService.delete(1L));
        verify(domainRepository).deleteById(1L);
    }

    @Test
    void shouldNotDeleteDomainWithRelations() {
        when(attributeRepository.count("domain.id", 1L)).thenReturn(0L);
        when(domainRepository.countRelations(1L)).thenReturn(1L);

        InvalidActionException exception = assertThrows(InvalidActionException.class, () -> domainService.delete(1L));

        assertEquals(DOMAIN_HAS_RELATIONS, exception.getMessage());
        verify(domainRepository).countRelations(1L);
        verify(attributeRepository).count("domain.id", 1L);
        verifyNoMoreInteractions(domainRepository, attributeRepository);
    }

    @Test
    void shouldNotDeleteDomainWithAttributes() {
        when(attributeRepository.count("domain.id", 1L)).thenReturn(1L);

        InvalidActionException exception = assertThrows(InvalidActionException.class, () -> domainService.delete(1L));

        assertEquals(DOMAIN_HAS_ATTRIBUTES, exception.getMessage());
        verify(attributeRepository).count("domain.id", 1L);
        verifyNoInteractions(domainRepository);
    }

    @Test
    void shouldRejectSelfRelation() {
        DomainModel input = new DomainModel();
        DomainModel self = new DomainModel();
        self.setId(1L);
        input.setRelatedDomains(List.of(self));
        when(domainRepository.findById(1L)).thenReturn(new DomainModel());

        InvalidActionException exception = assertThrows(InvalidActionException.class, () -> domainService.update(1L, input));

        assertEquals(DOMAIN_CANNOT_BE_RELATED_TO_ITSELF, exception.getMessage());
        verify(domainRepository).findById(1L);
    }
}
