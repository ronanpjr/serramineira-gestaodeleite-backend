package serramineira.sistemas.leite.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutorRequestDto(
        @NotBlank(message = "O nome não pode estar em branco")
        String nome,

        String linha,

        String chavePix,

        @NotNull(message = "O status 'ativo' é obrigatório")
        Boolean ativo
) {}