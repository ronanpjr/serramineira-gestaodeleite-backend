package serramineira.sistemas.leite.dto;

public record ProdutorResponseDto(
        Long id,
        String nome,
        String linha,
        String chavePix,
        boolean ativo
) {}