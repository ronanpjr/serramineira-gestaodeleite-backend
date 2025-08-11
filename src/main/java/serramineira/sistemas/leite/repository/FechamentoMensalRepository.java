package serramineira.sistemas.leite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import serramineira.sistemas.leite.model.FechamentoMensal;

public interface FechamentoMensalRepository extends JpaRepository<FechamentoMensal, Long> {
}
