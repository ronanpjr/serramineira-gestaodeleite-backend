package serramineira.sistemas.leite.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import serramineira.sistemas.leite.models.Produtor;

public interface ProdutorRepository extends JpaRepository<Produtor, Long> {
}