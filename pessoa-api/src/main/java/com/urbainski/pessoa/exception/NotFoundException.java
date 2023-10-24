package com.urbainski.pessoa.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }


    public static NotFoundException ofPessoaNaoEncontrada(String documento) {
        return new NotFoundException(String.format("Pessoa com documento '%s' não encontrada!", documento));
    }

}
