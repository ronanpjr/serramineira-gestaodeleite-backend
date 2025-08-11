package serramineira.sistemas.leite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import serramineira.sistemas.leite.model.ColetaDiaria;

import java.util.List;

public interface ColetaDiariaRepository extends JpaRepository<ColetaDiaria, Long> {

    @Query("SELECT c FROM ColetaDiaria c WHERE c.produtor.id = :produtorId AND STR_TO_DATE(c.data, '%Y-%m-%d') BETWEEN STR_TO_DATE(:startDate, '%Y-%m-%d') AND STR_TO_DATE(:endDate, '%Y-%m-%d')")
    List<ColetaDiaria> findByProdutorIdAndDataBetween(Long produtorId, String startDate, String endDate);

    @Query("SELECT c FROM ColetaDiaria c WHERE c.produtor.id = :produtorId AND YEAR(c.data) = :ano AND MONTH(c.data) = :mes")
    List<ColetaDiaria> findByProdutorAndMes(Long produtorId, int ano, int mes);

}
