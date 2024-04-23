package br.com.hapvida.controledevisitas.ControleDeVisitas.exception;

public class CpfJaCadastradoException extends RuntimeException{

    private final String lancamento = "o cpf fornecido ja esta cadastrado no sistema";

    public CpfJaCadastradoException(String message) {
        super(message);
    }

    public String getLancamento() {
        return lancamento;
    }
}
