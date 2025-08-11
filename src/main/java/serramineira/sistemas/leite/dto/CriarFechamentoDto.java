package serramineira.sistemas.leite.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CriarFechamentoDto(
        @NotNull
        Long produtorId,

        @NotNull
        @Min(1) @Max(12)
        int mes,

        @NotNull
        int ano,

        @NotNull
        @Positive
        BigDecimal precoLitro
) {}