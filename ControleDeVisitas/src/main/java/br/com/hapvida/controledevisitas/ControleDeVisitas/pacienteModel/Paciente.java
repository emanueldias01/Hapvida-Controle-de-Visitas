package br.com.hapvida.controledevisitas.ControleDeVisitas.pacienteModel;

import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Visitante;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tab_pacientes")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String nome;
    @Column(unique = true, nullable = false)
    private String cpf;
    private int numeroLeito;
    private LocalDateTime dataEntrada;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Visitante> visitantes;
}
