package br.com.hapvida.controledevisitas.ControleDeVisitas.exception;

public class TempoLimiteAcompanhanteException extends RuntimeException{

    private final String lancamento = "Tempo limite de acompanhante";

    public TempoLimiteAcompanhanteException(String message) {
        super(message);
    }

    public String getLancamento() {
        return lancamento;
    }
}
