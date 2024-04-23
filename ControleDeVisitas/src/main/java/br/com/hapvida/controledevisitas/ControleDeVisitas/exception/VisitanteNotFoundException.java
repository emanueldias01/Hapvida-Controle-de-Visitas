package br.com.hapvida.controledevisitas.ControleDeVisitas.exception;

public class VisitanteNotFoundException extends RuntimeException{

    private final String lancamento = "O id do visitante fornecido é inválido, ele nao esta em nosso sistema";

    public VisitanteNotFoundException(String message) {
        super(message);
    }

    public String getLancamento() {
        return lancamento;
    }
}
