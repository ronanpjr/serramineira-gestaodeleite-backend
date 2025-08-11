package serramineira.sistemas.leite.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record ColetaLoteDto(
        @NotNull
        Long produtorId,

        @NotNull
        LocalDate data,

        @NotNull
        @Positive(message = "A quantidade de litros deve ser um valor positivo.")
        int quantidadeLitros
) {}