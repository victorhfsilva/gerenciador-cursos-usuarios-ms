package com.example.usuariosms.service.impl;

import com.example.usuariosms.controller.EnderecoController;
import com.example.usuariosms.controller.AlunoController;
import com.example.usuariosms.mapper.EnderecoEnderecoRequestMapper;
import com.example.usuariosms.mapper.AlunoAlunoRequestMapper;
import com.example.usuariosms.mapper.AlunoAlunoResourceMapper;
import com.example.usuariosms.model.Aluno;
import com.example.usuariosms.model.dto.AlunoRequest;
import com.example.usuariosms.model.resources.AlunoResource;
import com.example.usuariosms.repository.AlunoRepository;
import com.example.usuariosms.service.IAlunoService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@AllArgsConstructor
public class AlunoService implements IAlunoService {
    private AlunoRepository alunoRepository;
    private AlunoAlunoRequestMapper alunoAlunoRequestMapper;
    private AlunoAlunoResourceMapper alunoAlunoResourceMapper;
    private EnderecoEnderecoRequestMapper enderecoEnderecoRequestMapper;

    @Transactional
    public AlunoResource save(AlunoRequest alunoDto) {
        Aluno aluno = alunoAlunoRequestMapper.alunoRequestToAluno(alunoDto);

        Aluno alunoSalvo = alunoRepository.save(aluno);

        AlunoResource alunoResource = alunoAlunoResourceMapper.alunoToAlunoResource(alunoSalvo);

        alunoResource.add(linkTo(methodOn(AlunoController.class).registrarAluno(alunoDto)).withSelfRel());
        alunoResource.add(linkTo(methodOn(EnderecoController.class).buscarEnderecosPorUsuarioId(alunoSalvo.getId())).withRel("endereco"));

        return alunoResource;
    }

    public AlunoResource findById(UUID id) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow();

        AlunoResource alunoResource = alunoAlunoResourceMapper.alunoToAlunoResource(aluno);

        alunoResource.add(linkTo(methodOn(AlunoController.class).buscarAlunoPorId(id)).withSelfRel());
        alunoResource.add(linkTo(methodOn(EnderecoController.class).buscarEnderecosPorUsuarioId(id)).withRel("endereco"));

        return alunoResource;
    }

    public Page<AlunoResource> findAll(Pageable pageable) {
        Page<Aluno> alunos = alunoRepository.findAll(pageable);

        Page<AlunoResource> alunoResources = alunos
                .map(aluno ->
                        alunoAlunoResourceMapper
                                .alunoToAlunoResource(aluno)
                                .add(linkTo(methodOn(AlunoController.class).buscarAlunoPorId(aluno.getId())).withSelfRel())
                                .add(linkTo(methodOn(EnderecoController.class).buscarEnderecosPorUsuarioId(aluno.getId())).withRel("endereco")));

        return alunoResources;
    }

    @Transactional
    public AlunoResource update(UUID id, AlunoRequest alunoDto) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow();

        atualizarAluno(aluno, alunoDto);

        Aluno alunoSalvo = alunoRepository.save(aluno);

        AlunoResource alunoResource = alunoAlunoResourceMapper.alunoToAlunoResource(alunoSalvo);

        alunoResource.add(linkTo(methodOn(AlunoController.class).atualizarAluno(id, alunoDto)).withSelfRel());
        alunoResource.add(linkTo(methodOn(EnderecoController.class).buscarEnderecosPorUsuarioId(id)).withRel("endereco"));

        return alunoResource;
    }

    @Transactional
    public AlunoResource delete(UUID id) {
        Aluno aluno = alunoRepository.findById(id).orElseThrow();

        alunoRepository.delete(aluno);

        AlunoResource alunoResource = alunoAlunoResourceMapper.alunoToAlunoResource(aluno);

        alunoResource.add(linkTo(methodOn(AlunoController.class).deletarAluno(id)).withSelfRel());

        return alunoResource;
    }

    private void atualizarAluno(Aluno aluno, AlunoRequest novoAluno) {
        aluno.setNome(novoAluno.nome());
        aluno.setSobrenome(novoAluno.sobrenome());
        aluno.setCpf(novoAluno.cpf());
        aluno.setSenha(novoAluno.senha());
        aluno.setPapel(novoAluno.papel());
        aluno.setDataNascimento(novoAluno.dataNascimento());
        aluno.setEmail(novoAluno.email());
        aluno.setCelular(novoAluno.celular());
        aluno.setEndereco(enderecoEnderecoRequestMapper
                .enderecoRequestToEndereco(novoAluno.endereco()));
        aluno.setIdEstudantil(novoAluno.idEstudantil());
    }
}