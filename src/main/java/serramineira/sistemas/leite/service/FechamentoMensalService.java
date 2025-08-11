package serramineira.sistemas.leite.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serramineira.sistemas.leite.dto.CriarFechamentoDto;
import serramineira.sistemas.leite.dto.AtualizarFechamentoDto;
import serramineira.sistemas.leite.dto.FechamentoMensalResponseDto; // Importar
import serramineira.sistemas.leite.model.ColetaDiaria;
import serramineira.sistemas.leite.model.FechamentoMensal;
import serramineira.sistemas.leite.model.Produtor;
import serramineira.sistemas.leite.repository.ColetaDiariaRepository;
import serramineira.sistemas.leite.repository.FechamentoMensalRepository;
import serramineira.sistemas.leite.repository.ProdutorRepository;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FechamentoMensalService {

    @Autowired
    private FechamentoMensalRepository fechamentoRepository;

    @Autowired
    private ColetaDiariaRepository coletaRepository;

    @Autowired
    private ProdutorRepository produtorRepository;

    @Transactional
    public FechamentoMensalResponseDto criarFechamento(CriarFechamentoDto dto) {
        Produtor produtor = findProdutorById(dto.produtorId());

        List<ColetaDiaria> coletasDoMes = coletaRepository.findByProdutorAndMes(dto.produtorId(), dto.ano(), dto.mes());
        int totalLitros = coletasDoMes.stream().mapToInt(ColetaDiaria::getQuantidadeLitros).sum();
        if (totalLitros == 0) {
            throw new IllegalStateException("Não há coletas para este produtor no mês/ano especificado.");
        }

        BigDecimal totalBruto = dto.precoLitro().multiply(new BigDecimal(totalLitros));

        FechamentoMensal fechamento = new FechamentoMensal();
        // ... (configurações do fechamento)
        fechamento.setProdutor(produtor);
        fechamento.setMes(dto.mes());
        fechamento.setAno(dto.ano());
        fechamento.setTotalLitros(totalLitros);
        fechamento.setPrecoLitro(dto.precoLitro());
        fechamento.setTotalBruto(totalBruto);
        fechamento.setDescontos(BigDecimal.ZERO);
        fechamento.setTotalLiquido(totalBruto);
        fechamento.setStatusPagamento("Pendente");

        FechamentoMensal fechamentoSalvo = fechamentoRepository.save(fechamento);
        return toResponseDto(fechamentoSalvo);
    }

    @Transactional
    public FechamentoMensalResponseDto atualizarPagamento(Long id, AtualizarFechamentoDto dto) {
        FechamentoMensal fechamento = findFechamentoById(id);
        BigDecimal descontos = (dto.descontos() != null) ? dto.descontos() : fechamento.getDescontos();
        BigDecimal totalLiquido = fechamento.getTotalBruto().subtract(descontos);

        fechamento.setDescontos(descontos);
        fechamento.setTotalLiquido(totalLiquido);
        fechamento.setStatusPagamento(dto.statusPagamento());
        fechamento.setDataPagamento(dto.dataPagamento());

        FechamentoMensal fechamentoAtualizado = fechamentoRepository.save(fechamento);
        return toResponseDto(fechamentoAtualizado);
    }

    public List<FechamentoMensalResponseDto> listarTodos() {
        return fechamentoRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public FechamentoMensalResponseDto buscarPorId(Long id) {
        FechamentoMensal fechamento = findFechamentoById(id);
        return toResponseDto(fechamento);
    }

    @Transactional
    public void deletar(Long id) {
        // Verifica se existe antes de deletar para lançar a exceção padrão
        if (!fechamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Fechamento não encontrado com o ID: " + id);
        }
        fechamentoRepository.deleteById(id);
    }

    // --- Métodos Auxiliares ---
    private FechamentoMensal findFechamentoById(Long id) {
        return fechamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fechamento não encontrado com o ID: " + id));
    }

    private Produtor findProdutorById(Long id) {
        return produtorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produtor não encontrado com o ID: " + id));
    }

    private FechamentoMensalResponseDto toResponseDto(FechamentoMensal fechamento) {
        return new FechamentoMensalResponseDto(
                fechamento.getId(),
                fechamento.getMes(),
                fechamento.getAno(),
                fechamento.getTotalLitros(),
                fechamento.getPrecoLitro(),
                fechamento.getTotalBruto(),
                fechamento.getDescontos(),
                fechamento.getTotalLiquido(),
                fechamento.getStatusPagamento(),
                fechamento.getDataPagamento(),
                fechamento.getProdutor().getId(),
                fechamento.getProdutor().getNome()
        );
    }
}