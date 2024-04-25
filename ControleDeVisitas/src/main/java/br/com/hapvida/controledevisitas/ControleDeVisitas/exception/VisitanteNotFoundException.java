package br.com.hapvida.controledevisitas.ControleDeVisitas.exception;

public class VisitanteNotFoundException extends RuntimeException{

    private final String lancamento = "Visitante nao encontrado";

    public VisitanteNotFoundException(String message) {
        super(message);
    }

    public String getLancamento() {
        return lancamento;
    }
}
