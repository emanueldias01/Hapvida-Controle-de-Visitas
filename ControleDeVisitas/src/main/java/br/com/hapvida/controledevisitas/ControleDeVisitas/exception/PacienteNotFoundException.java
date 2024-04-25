package br.com.hapvida.controledevisitas.ControleDeVisitas.exception;

public class PacienteNotFoundException extends RuntimeException{

    private final String lancamento = "Paciente nao encontrado";

    public PacienteNotFoundException(String message) {
        super(message);
    }

    public String getLancamento() {
        return lancamento;
    }
}
