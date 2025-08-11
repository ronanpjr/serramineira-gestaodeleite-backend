package serramineira.sistemas.leite.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "produtores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produtor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String linha; // Rota de coleta

    private String chavePix;

    private String formaPagamento;

    @Column(nullable = false)
    private boolean ativo = true;

    // Um produtor pode ter várias coletas diárias
    @OneToMany(mappedBy = "produtor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ColetaDiaria> coletas;

    // Um produtor pode ter vários fechamentos mensais
    @OneToMany(mappedBy = "produtor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FechamentoMensal> fechamentos;
}