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

    public List<ColetaDiaria> listarTodas() {
        return coletaRepository.findAll();
    }

    public ColetaDiaria buscarPorId(Long id) {
        return coletaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coleta não encontrada com o ID: " + id));
    }

    public ColetaDiaria criar(ColetaDiariaDto dto) {
        Produtor produtor = produtorRepository.findById(dto.produtorId())
                .orElseThrow(() -> new EntityNotFoundException("Produtor não encontrado com o ID: " + dto.produtorId()));

        ColetaDiaria novaColeta = new ColetaDiaria();
        novaColeta.setData(dto.data());
        novaColeta.setQuantidadeLitros(dto.quantidadeLitros());
        novaColeta.setProdutor(produtor);

        return coletaRepository.save(novaColeta);
    }

    public ColetaDiaria atualizar(Long id, ColetaDiariaDto dto) {
        ColetaDiaria coletaExistente = buscarPorId(id);

        Produtor produtor = produtorRepository.findById(dto.produtorId())
                .orElseThrow(() -> new EntityNotFoundException("Produtor não encontrado com o ID: " + dto.produtorId()));

        coletaExistente.setData(dto.data());
        coletaExistente.setQuantidadeLitros(dto.quantidadeLitros());
        coletaExistente.setProdutor(produtor);

        return coletaRepository.save(coletaExistente);
    }

    public void deletar(Long id) {
        ColetaDiaria coleta = buscarPorId(id);
        coletaRepository.delete(coleta);
    }


    public List<ColetaDiaria> buscarPorProdutorEMes(Long produtorId, int ano, int mes) {
        if (!produtorRepository.existsById(produtorId)) {
            throw new EntityNotFoundException("Produtor não encontrado com o ID: " + produtorId);
        }
        return coletaRepository.findByProdutorAndMes(produtorId, ano, mes);
    }


    @Transactional
    public List<ColetaDiariaResponseDto> salvarColetasEmLote(List<ColetaLoteDto> coletasDto) {
        List<ColetaDiaria> coletasParaSalvar = coletasDto.stream().map(dto -> {
            Produtor produtor = produtorRepository.findById(dto.produtorId())
                    .orElseThrow(() -> new EntityNotFoundException("Produtor não encontrado com o ID: " + dto.produtorId()));

            ColetaDiaria novaColeta = new ColetaDiaria();
            novaColeta.setProdutor(produtor);
            novaColeta.setData(dto.data());
            novaColeta.setQuantidadeLitros(dto.quantidadeLitros());
            return novaColeta;
        }).collect(Collectors.toList());

        List<ColetaDiaria> coletasSalvas = coletaRepository.saveAll(coletasParaSalvar);

        // Converte a lista de entidades salvas para uma lista de DTOs de resposta
        return coletasSalvas.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private ColetaDiariaResponseDto toResponseDto(ColetaDiaria coleta) {
        return new ColetaDiariaResponseDto(
                coleta.getId(),
                coleta.getData(),
                coleta.getQuantidadeLitros(),
                coleta.getProdutor().getId() // Apenas o ID do produtor
        );
    }


}