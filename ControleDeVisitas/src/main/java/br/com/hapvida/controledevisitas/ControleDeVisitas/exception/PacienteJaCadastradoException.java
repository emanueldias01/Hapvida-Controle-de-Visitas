package br.com.hapvida.controledevisitas.ControleDeVisitas.exception;

public class PacienteJaCadastradoException extends RuntimeException{

    private final String lancamento = "O nome do paciente ja esta cadastrado no sistema";

    public PacienteJaCadastradoException(String message) {
        super(message);
    }

    public String getLancamento() {
        return lancamento;
    }
}
