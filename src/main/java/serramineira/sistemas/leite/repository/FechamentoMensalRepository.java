package serramineira.sistemas.leite.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import serramineira.sistemas.leite.models.FechamentoMensal;

public interface FechamentoMensalRepository extends JpaRepository<FechamentoMensal, Long> {
}
