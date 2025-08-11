package serramineira.sistemas.leite.dto;

import java.time.LocalDate;

public record ColetaDiariaResponseDto(
        Long id,
        LocalDate data,
        int quantidadeLitros,
        Long produtorId
) {}