package br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel;

import br.com.hapvida.controledevisitas.ControleDeVisitas.pacienteModel.Paciente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tab_visitantes_ou_acompanhantes")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Visitante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    private LocalDateTime dataEntrada;

    @ManyToOne
    private Paciente paciente;

}
