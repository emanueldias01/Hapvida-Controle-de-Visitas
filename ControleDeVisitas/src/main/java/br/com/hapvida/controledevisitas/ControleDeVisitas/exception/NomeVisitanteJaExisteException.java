package br.com.hapvida.controledevisitas.ControleDeVisitas.exception;

public class NomeVisitanteJaExisteException extends RuntimeException{

    private final String lancamento = "O nome do visitante fornecido ja esta em nosso sistema";

    public NomeVisitanteJaExisteException(String message) {
        super(message);
    }

    public String getLancamento() {
        return lancamento;
    }
}
