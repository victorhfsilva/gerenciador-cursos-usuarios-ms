package com.example.usuariosms.mapper;

import com.example.usuariosms.model.Endereco;
import com.example.usuariosms.model.dto.EnderecoRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnderecoEnderecoRequestMapper {
    Endereco enderecoRequestToEndereco(EnderecoRequest registrarenderecoRequest);

}