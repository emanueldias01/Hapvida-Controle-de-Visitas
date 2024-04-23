package br.com.hapvida.controledevisitas.ControleDeVisitas.exception;

public class LeitoIndisponivelException  extends RuntimeException{

    private final String lancamento = "o leito fornecido está indisponível no momento";

    public LeitoIndisponivelException(String message) {
        super(message);
    }

    public String getLancamento() {
        return lancamento;
    }
}
