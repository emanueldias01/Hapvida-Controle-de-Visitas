package br.com.hapvida.controledevisitas.ControleDeVisitas.exception;

import java.util.SplittableRandom;

public class VisitanteJaExisteException extends RuntimeException{

    private final String lancamento = "O visitante ja existe";

    public VisitanteJaExisteException(String message) {
        super(message);
    }

    public String getLancamento() {
        return lancamento;
    }
}
