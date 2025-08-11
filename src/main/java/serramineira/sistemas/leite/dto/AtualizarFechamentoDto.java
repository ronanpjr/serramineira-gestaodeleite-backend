package serramineira.sistemas.leite.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AtualizarFechamentoDto(
        BigDecimal descontos,

        @NotNull
        String statusPagamento, // Ex: "Pendente", "Pago"

        @PastOrPresent
        LocalDate dataPagamento
) {}