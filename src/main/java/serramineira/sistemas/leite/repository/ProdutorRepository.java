package serramineira.sistemas.leite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import serramineira.sistemas.leite.model.Produtor;

import java.util.Date;

public interface ProdutorRepository extends JpaRepository<Produtor, Long> {

}