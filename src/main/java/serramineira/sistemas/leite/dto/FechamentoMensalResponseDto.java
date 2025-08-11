package serramineira.sistemas.leite.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FechamentoMensalResponseDto(
        Long id,
        int mes,
        int ano,
        int totalLitros,
        BigDecimal precoLitro,
        BigDecimal totalBruto,
        BigDecimal descontos,
        BigDecimal totalLiquido,
        String statusPagamento,
        LocalDate dataPagamento,
        Long produtorId,
        String produtorNome
) {}