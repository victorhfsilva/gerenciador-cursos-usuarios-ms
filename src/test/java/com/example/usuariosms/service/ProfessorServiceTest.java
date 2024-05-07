package com.example.usuariosms.service;

import com.example.usuariosms.fixture.ProfessorFixture;
import com.example.usuariosms.fixture.ProfessorRequestFixture;
import com.example.usuariosms.fixture.ProfessorResourceFixture;
import com.example.usuariosms.mapper.EnderecoEnderecoRequestMapper;
import com.example.usuariosms.mapper.ProfessorProfessorRequestMapper;
import com.example.usuariosms.mapper.ProfessorProfessorResourceMapper;
import com.example.usuariosms.model.Professor;
import com.example.usuariosms.model.dto.EnderecoRequest;
import com.example.usuariosms.model.dto.ProfessorRequest;
import com.example.usuariosms.model.resources.ProfessorResource;
import com.example.usuariosms.repository.ProfessorRepository;
import com.example.usuariosms.service.impl.ProfessorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfessorServiceTest {

    @InjectMocks
    private ProfessorService professorService;
    @Mock
    private ProfessorRepository professorRepository;
    @Mock
    private ProfessorProfessorResourceMapper professorProfessorResourceMapper;
    @Mock
    private ProfessorProfessorRequestMapper professorProfessorRequestMapper;
    @Mock
    private EnderecoEnderecoRequestMapper enderecoEnderecoRequestMapper;

    @Test
    void saveTest(){
        ProfessorRequest professorRequest = ProfessorRequestFixture.buildValido();
        Professor professor = ProfessorFixture.buildValido();

        when(professorProfessorRequestMapper.professorRequestToProfessor(professorRequest)).thenReturn(professor);
        when(professorRepository.save(any(Professor.class))).thenReturn(professor);
        when(professorProfessorResourceMapper.professorToProfessorResource(professor)).thenReturn(ProfessorResourceFixture.buildValido());

        ProfessorResource professorResource = professorService.save(professorRequest);

        assertEquals(professorResource.getCpf(), professorRequest.cpf());
        assertNotNull(professorResource.getLink("self"));
        assertNotNull(professorResource.getLink("endereco"));
    }

    @Test
    void findByIdTest() {
        Professor professor = ProfessorFixture.buildValido();
        ProfessorResource professorResource = ProfessorResourceFixture.buildValido();

        when(professorRepository.findById(any(UUID.class))).thenReturn(Optional.of(professor));
        when(professorProfessorResourceMapper.professorToProfessorResource(professor)).thenReturn(professorResource);
        ProfessorResource professorRetornado = professorService.findById(UUID.randomUUID());

        assertEquals(professor.getCpf(), professorRetornado.getCpf());
        assertNotNull(professorRetornado.getLink("self"));
        assertNotNull(professorRetornado.getLink("endereco"));
    }

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Professor> professors = List.of(ProfessorFixture.buildValido());
        Page<Professor> paginaProfessors = new PageImpl<>(professors, pageable, professors.size());
        ProfessorResource professorResource = ProfessorResourceFixture.buildValido();

        when(professorRepository.findAll(pageable)).thenReturn(paginaProfessors);
        when(professorProfessorResourceMapper.professorToProfessorResource(any(Professor.class)))
                .thenReturn(professorResource);

        Page<ProfessorResource> paginaProfessorsRetornada = professorService.findAll(pageable);

        assertEquals(paginaProfessors.getTotalElements(), paginaProfessorsRetornada.getTotalElements());
        assertEquals(paginaProfessors.getTotalPages(), paginaProfessorsRetornada.getTotalPages());
        assertEquals(paginaProfessors.getContent().size(), paginaProfessorsRetornada.getContent().size());
        assertNotNull(paginaProfessorsRetornada.getContent().get(0).getLink("self"));
        assertNotNull(paginaProfessorsRetornada.getContent().get(0).getLink("endereco"));
    }

    @Test
    void updateTest() {
        Professor professor = ProfessorFixture.buildValido();
        ProfessorRequest professorRequest = ProfessorRequestFixture.buildValido();
        ProfessorResource professorResource = ProfessorResourceFixture.buildValido();

        when(professorRepository.findById(any(UUID.class))).thenReturn(Optional.of(professor));
        when(professorRepository.save(any(Professor.class))).thenReturn(professor);
        when(enderecoEnderecoRequestMapper.enderecoRequestToEndereco(any(EnderecoRequest.class))).thenReturn(professor.getEndereco());
        when(professorProfessorResourceMapper.professorToProfessorResource(professor)).thenReturn(professorResource);

        ProfessorResource professorRetornado = professorService.update(UUID.randomUUID(), professorRequest);

        assertEquals(professor.getCpf(), professorRetornado.getCpf());
        assertNotNull(professorRetornado.getLink("self"));
        assertNotNull(professorRetornado.getLink("endereco"));
    }

    @Test
    void deleteTest() {
        Professor professor = ProfessorFixture.buildValido();
        ProfessorResource professorResource = ProfessorResourceFixture.buildValido();

        when(professorRepository.findById(any(UUID.class))).thenReturn(Optional.of(professor));
        when(professorProfessorResourceMapper.professorToProfessorResource(professor)).thenReturn(professorResource);

        ProfessorResource professorRetornado = professorService.delete(UUID.randomUUID());

        assertEquals(professor.getCpf(), professorRetornado.getCpf());
        assertNotNull(professorRetornado.getLink("self"));
        assertNotNull(professorRetornado.getLink("endereco"));
    }
    

}