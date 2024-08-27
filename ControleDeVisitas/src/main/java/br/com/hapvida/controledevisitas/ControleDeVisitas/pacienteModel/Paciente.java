package br.com.hapvida.controledevisitas.ControleDeVisitas.pacienteModel;

import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteRequestDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.dto.PacienteUpdateDTO;
import br.com.hapvida.controledevisitas.ControleDeVisitas.visitanteModel.Visitante;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tab_pacientes")
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String nome;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String cpf;

    @NotNull
    private int numeroLeito;

    private LocalDateTime dataEntrada;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Visitante> visitantes = new ArrayList<>();


    public Paciente(PacienteRequestDTO data) {
        this.nome = data.nome();
        this.cpf = data.cpf();
        this.numeroLeito = data.numeroLeito();
        this.dataEntrada = LocalDateTime.now();
    }

    public Paciente(String cpf, LocalDateTime dataEntrada, Long id, String nome, int numeroLeito) {
        this.cpf = cpf;
        this.dataEntrada = dataEntrada;
        this.id = id;
        this.nome = nome;
        this.numeroLeito = numeroLeito;
    }

    public void updateInfo(PacienteUpdateDTO data) {
        if (data.leito() != 0) {
            this.numeroLeito = data.leito();
        }
    }

}
