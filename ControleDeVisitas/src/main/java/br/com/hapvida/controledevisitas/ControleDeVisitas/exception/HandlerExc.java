package br.com.hapvida.controledevisitas.ControleDeVisitas.exception;

import br.com.hapvida.controledevisitas.ControleDeVisitas.exception.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerExc {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO trataPacienteNotFound(PacienteNotFoundException ex){

        return new ErrorDTO(
                ex.getLancamento(),
                ex.getMessage()

        );
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO trataLeitoIndisponivel(LeitoIndisponivelException ex){

        return new ErrorDTO(
                ex.getLancamento(),
                ex.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO trataCpfCadastrado(CpfJaCadastradoException ex){

        return new ErrorDTO(
                ex.getLancamento(),
                ex.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO trataNomePacienteJaCadastrado(PacienteJaCadastradoException ex){

        return new ErrorDTO(
                ex.getLancamento(),
                ex.getMessage()
        );
    }


    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO trataVisitanteNotFound(VisitanteNotFoundException ex){

        return new ErrorDTO(
                ex.getLancamento(),
                ex.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO trataNomeVisitanteJaExiste(NomeVisitanteJaExisteException ex){

        return new ErrorDTO(
                ex.getLancamento(),
                ex.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO trataLeitoCheioException(LeitoCheioException ex){

        return new ErrorDTO(
                ex.getLancamento(),
                ex.getMessage()
        );
    }

}
