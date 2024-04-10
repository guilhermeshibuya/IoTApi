package br.edu.utfpr.iotapi.dto;

import br.edu.utfpr.iotapi.models.Pessoa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GatewayDTO(
    @NotBlank @Size(min = 2) String nome,
    @NotBlank String descricao,
    String endereco,
    Pessoa pessoa) {

}
