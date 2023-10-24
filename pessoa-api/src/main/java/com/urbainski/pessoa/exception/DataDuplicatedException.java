package com.urbainski.pessoa.exception;

public class DataDuplicatedException extends RuntimeException {

    public DataDuplicatedException(String message) {
        super(message);
    }

    public static DataDuplicatedException ofPessoaDuplicata(String documento) {
        return new DataDuplicatedException(String.format("Já existe uma pessoa com o documento: '%s'.", documento));
    }

}
