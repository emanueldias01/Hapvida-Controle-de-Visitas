package br.com.hapvida.controledevisitas.ControleDeVisitas.exception;

public class LeitoCheioException extends RuntimeException {
    private final String lancamento = "Este leito está cheio";

    public LeitoCheioException(String message) {
        super(message);
    }

    public String getLancamento() {
        return lancamento;
    }
}
