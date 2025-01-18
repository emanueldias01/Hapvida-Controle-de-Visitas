package br.com.hapvida.controledevisitas.ControleDeVisitas.model.visitante;

import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.VisitanteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.model.paciente.Paciente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private LocalDateTime dataEntrada;

    @ManyToOne
    private Paciente paciente;

    public Visitante(VisitanteRequestDTO data) {
        this.nome = data.nome();
        this.cpf = data.cpf();
        this.categoria = data.categoria();
        this.dataEntrada = LocalDateTime.now();
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
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
