package serramineira.sistemas.leite.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import serramineira.sistemas.leite.models.ColetaDiaria;

public interface ColetaDiariaRepository extends JpaRepository<ColetaDiaria, Long> {
}
