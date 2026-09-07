package rkd.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rkd.com.exception.DomainNotFoundException;
import rkd.com.model.DomainModel;
import rkd.com.repository.DomainRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static rkd.com.message.DomainMessage.DOMAIN_NOT_FOUND;

class DomainServiceTest {

    private DomainRepository domainRepository;
    private DomainService domainService;

    @BeforeEach
    void setUp() {
        domainRepository = mock(DomainRepository.class);
        domainService = new DomainService(domainRepository);
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
        when(domainRepository.deleteById(1L)).thenReturn(true);

        assertEquals(true, domainService.delete(1L));
        verify(domainRepository).deleteById(1L);
    }
}
