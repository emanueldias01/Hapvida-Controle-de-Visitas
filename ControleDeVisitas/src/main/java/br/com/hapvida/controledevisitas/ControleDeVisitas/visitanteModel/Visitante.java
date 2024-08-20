package br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel;

import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.VisitanteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.pacienteModel.Paciente;
import br.com.hapvida.controledevisitas.ControleDeVisitas.repository.PacienteRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Entity
@Table(name = "tab_visitantes_ou_acompanhantes")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Visitante {

    @Autowired
    PacienteRepository pacienteRepository;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private LocalDateTime dataEntrada;

    @ManyToOne
    private Paciente paciente;

    public Visitante(VisitanteRequestDTO data) {
        this.nome = data.nome();
        this.cpf = data.cpf();
        this.categoria = data.categoria();
        this.paciente = pacienteRepository.findById(data.pacienteId()).get();
        this.dataEntrada = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Visitante{" +
                "categoria=" + categoria +
                ", id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", dataEntrada=" + dataEntrada +
                ", paciente=" + paciente +
                '}';
    }
}
