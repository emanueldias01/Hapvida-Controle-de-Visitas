package br.com.hapvida.controledevisitas.ControleDeVisitas.exception;

public class PacienteNotFoundException extends RuntimeException{

    private final String lancamento = "Nao encontramos o paciente em nosso sistema";

    public PacienteNotFoundException(String message) {
        super(message);
    }

    public String getLancamento() {
        return lancamento;
    }
}
