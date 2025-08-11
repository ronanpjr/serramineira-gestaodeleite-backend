package serramineira.sistemas.leite.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fechamentos_mensais")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FechamentoMensal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "O mês deve ser no mínimo 1.")
    @Max(value = 12, message = "O mês deve ser no máximo 12.")
    @Column(nullable = false)
    private int mes;

    @Column(nullable = false)
    private int ano;

    @Column(name = "total_litros", nullable = false)
    private int totalLitros;

    @Column(name = "preco_litro", precision = 10, scale = 4)
    private BigDecimal precoLitro;

    @Column(name = "total_bruto", precision = 10, scale = 2)
    private BigDecimal totalBruto;

    @Column(precision = 10, scale = 2)
    private BigDecimal descontos;

    @Column(name = "total_liquido", precision = 10, scale = 2)
    private BigDecimal totalLiquido;

    @Column(name = "status_pagamento")
    private String statusPagamento; // Ex: "Pendente", "Pago"

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produtor_id", nullable = false)
    private Produtor produtor;
}