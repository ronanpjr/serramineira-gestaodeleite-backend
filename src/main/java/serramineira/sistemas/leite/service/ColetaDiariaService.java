package serramineira.sistemas.leite.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serramineira.sistemas.leite.dto.ColetaDiariaDto;
import serramineira.sistemas.leite.dto.ColetaDiariaResponseDto;
import serramineira.sistemas.leite.dto.ColetaLoteDto;
import serramineira.sistemas.leite.model.ColetaDiaria;
import serramineira.sistemas.leite.model.Produtor;
import serramineira.sistemas.leite.repository.ColetaDiariaRepository;
import serramineira.sistemas.leite.repository.ProdutorRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColetaDiariaService {

    @Autowired
    private ColetaDiariaRepository coletaRepository;

    @Autowired
    private ProdutorRepository produtorRepository;

    // Retorna DTO de Resposta
    public List<ColetaDiariaResponseDto> listarTodas() {
        return coletaRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // Retorna DTO de Resposta
    public ColetaDiariaResponseDto buscarPorId(Long id) {
        ColetaDiaria coleta = findColetaById(id);
        return toResponseDto(coleta);
    }

    // Recebe DTO e retorna DTO de Resposta
    @Transactional
    public ColetaDiariaResponseDto criar(ColetaDiariaDto dto) {
        Produtor produtor = findProdutorById(dto.produtorId());

        ColetaDiaria novaColeta = new ColetaDiaria();
        novaColeta.setData(dto.data());
        novaColeta.setQuantidadeLitros(dto.quantidadeLitros());
        novaColeta.setProdutor(produtor);

        ColetaDiaria coletaSalva = coletaRepository.save(novaColeta);
        return toResponseDto(coletaSalva);
    }

    // Recebe DTO e retorna DTO de Resposta
    @Transactional
    public ColetaDiariaResponseDto atualizar(Long id, ColetaDiariaDto dto) {
        ColetaDiaria coletaExistente = findColetaById(id);
        Produtor produtor = findProdutorById(dto.produtorId());

        coletaExistente.setData(dto.data());
        coletaExistente.setQuantidadeLitros(dto.quantidadeLitros());
        coletaExistente.setProdutor(produtor);

        ColetaDiaria coletaAtualizada = coletaRepository.save(coletaExistente);
        return toResponseDto(coletaAtualizada);
    }

    @Transactional
    public void deletar(Long id) {
        ColetaDiaria coleta = findColetaById(id);
        coletaRepository.delete(coleta);
    }

    // Retorna DTO de Resposta
    public List<ColetaDiariaResponseDto> buscarPorProdutorEMes(Long produtorId, int ano, int mes) {
        if (!produtorRepository.existsById(produtorId)) {
            throw new EntityNotFoundException("Produtor não encontrado com o ID: " + produtorId);
        }
        return coletaRepository.findByProdutorAndMes(produtorId, ano, mes).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ColetaDiariaResponseDto> salvarColetasEmLote(List<ColetaLoteDto> coletasDto) {
        List<ColetaDiaria> coletasParaSalvar = coletasDto.stream().map(dto -> {
            Produtor produtor = findProdutorById(dto.produtorId());
            ColetaDiaria novaColeta = new ColetaDiaria();
            novaColeta.setProdutor(produtor);
            novaColeta.setData(dto.data());
            novaColeta.setQuantidadeLitros(dto.quantidadeLitros());
            return novaColeta;
        }).collect(Collectors.toList());

        List<ColetaDiaria> coletasSalvas = coletaRepository.saveAll(coletasParaSalvar);

        return coletasSalvas.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }


    private ColetaDiaria findColetaById(Long id) {
        return coletaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coleta não encontrada com o ID: " + id));
    }

    private Produtor findProdutorById(Long id) {
        return produtorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produtor não encontrado com o ID: " + id));
    }

    private ColetaDiariaResponseDto toResponseDto(ColetaDiaria coleta) {
        return new ColetaDiariaResponseDto(
                coleta.getId(),
                coleta.getData(),
                coleta.getQuantidadeLitros(),
                coleta.getProdutor().getId()
        );
    }
}