package serramineira.sistemas.leite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import serramineira.sistemas.leite.dto.CriarFechamentoDto;
import serramineira.sistemas.leite.dto.AtualizarFechamentoDto;
import serramineira.sistemas.leite.model.ColetaDiaria;
import serramineira.sistemas.leite.model.FechamentoMensal;
import serramineira.sistemas.leite.model.Produtor;
import serramineira.sistemas.leite.repository.ColetaDiariaRepository;
import serramineira.sistemas.leite.repository.FechamentoMensalRepository;
import serramineira.sistemas.leite.repository.ProdutorRepository;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FechamentoMensalService {

    @Autowired
    private FechamentoMensalRepository fechamentoRepository;

    @Autowired
    private ColetaDiariaRepository coletaRepository;

    @Autowired
    private ProdutorRepository produtorRepository;

    public FechamentoMensal criarFechamento(CriarFechamentoDto dto) {
        Produtor produtor = produtorRepository.findById(dto.produtorId())
                .orElseThrow(() -> new EntityNotFoundException("Produtor não encontrado com o ID: " + dto.produtorId()));

        // 1. Calcular o total de litros no mês
        List<ColetaDiaria> coletasDoMes = coletaRepository.findByProdutorIdAndDataBetween(
                dto.produtorId(),
                dto.ano() + "-" + String.format("%02d", dto.mes()) + "-01",
                dto.ano() + "-" + String.format("%02d", dto.mes()) + "-31"
        );

        int totalLitros = coletasDoMes.stream().mapToInt(ColetaDiaria::getQuantidadeLitros).sum();

        if (totalLitros == 0) {
            throw new IllegalStateException("Não há coletas para este produtor no mês/ano especificado.");
        }

        // 2. Calcular valores
        BigDecimal totalBruto = dto.precoLitro().multiply(new BigDecimal(totalLitros));

        // 3. Criar a entidade
        FechamentoMensal fechamento = new FechamentoMensal();
        fechamento.setProdutor(produtor);
        fechamento.setMes(dto.mes());
        fechamento.setAno(dto.ano());
        fechamento.setTotalLitros(totalLitros);
        fechamento.setPrecoLitro(dto.precoLitro());
        fechamento.setTotalBruto(totalBruto);
        fechamento.setDescontos(BigDecimal.ZERO); // Inicia com zero
        fechamento.setTotalLiquido(totalBruto); // Inicialmente igual ao bruto
        fechamento.setStatusPagamento("Pendente");

        return fechamentoRepository.save(fechamento);
    }

    public FechamentoMensal atualizarPagamento(Long id, AtualizarFechamentoDto dto) {
        FechamentoMensal fechamento = buscarPorId(id);

        BigDecimal descontos = (dto.descontos() != null) ? dto.descontos() : fechamento.getDescontos();
        BigDecimal totalLiquido = fechamento.getTotalBruto().subtract(descontos);

        fechamento.setDescontos(descontos);
        fechamento.setTotalLiquido(totalLiquido);
        fechamento.setStatusPagamento(dto.statusPagamento());
        fechamento.setDataPagamento(dto.dataPagamento());

        return fechamentoRepository.save(fechamento);
    }

    public List<FechamentoMensal> listarTodos() {
        return fechamentoRepository.findAll();
    }

    public FechamentoMensal buscarPorId(Long id) {
        return fechamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fechamento não encontrado com o ID: " + id));
    }

    public void deletar(Long id) {
        FechamentoMensal fechamento = buscarPorId(id);
        fechamentoRepository.delete(fechamento);
    }
}