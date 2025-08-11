package serramineira.sistemas.leite.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistroUsuarioDto(
        @NotBlank @Email String email,

        @NotBlank String senha) {
}