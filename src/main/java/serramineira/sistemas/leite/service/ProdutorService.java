package serramineira.sistemas.leite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serramineira.sistemas.leite.dto.ProdutorRequestDto;
import serramineira.sistemas.leite.dto.ProdutorResponseDto;
import serramineira.sistemas.leite.model.Produtor;
import serramineira.sistemas.leite.repository.ProdutorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutorService {

    @Autowired
    private ProdutorRepository produtorRepository;

    // Retorna uma lista de DTOs, não de entidades
    @Transactional(readOnly = true)
    public List<ProdutorResponseDto> listarTodos() {
        return produtorRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // Retorna um DTO
    @Transactional(readOnly = true)
    public ProdutorResponseDto buscarPorId(Long id) {
        Produtor produtor = findProdutorById(id);
        return toResponseDto(produtor);
    }

    // Recebe um DTO e retorna um DTO
    @Transactional
    public ProdutorResponseDto criar(ProdutorRequestDto dto) {
        Produtor novoProdutor = new Produtor();
        novoProdutor.setNome(dto.nome());
        novoProdutor.setLinha(dto.linha());
        novoProdutor.setChavePix(dto.chavePix());
        novoProdutor.setAtivo(dto.ativo());

        Produtor produtorSalvo = produtorRepository.save(novoProdutor);
        return toResponseDto(produtorSalvo);
    }

    // Recebe um DTO e retorna um DTO
    @Transactional
    public ProdutorResponseDto atualizar(Long id, ProdutorRequestDto dto) {
        Produtor produtor = findProdutorById(id);

        produtor.setNome(dto.nome());
        produtor.setLinha(dto.linha());
        produtor.setChavePix(dto.chavePix());
        produtor.setAtivo(dto.ativo());

        Produtor produtorAtualizado = produtorRepository.save(produtor);
        return toResponseDto(produtorAtualizado);
    }

    @Transactional
    public void deletar(Long id) {
        Produtor produtor = findProdutorById(id);
        produtorRepository.delete(produtor);
    }

    // --- Métodos Auxiliares ---

    // Método privado para buscar a entidade, reutilizado internamente
    private Produtor findProdutorById(Long id) {
        return produtorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produtor não encontrado com o ID: " + id));
    }

    // Método privado para mapear a Entidade para o DTO de Resposta
    private ProdutorResponseDto toResponseDto(Produtor produtor) {
        return new ProdutorResponseDto(
                produtor.getId(),
                produtor.getNome(),
                produtor.getLinha(),
                produtor.getChavePix(),
                produtor.isAtivo()
        );
    }
}