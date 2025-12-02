package com.senai.conta_bancaria.domain.exception;

public class ContaDoMesmoTipoException extends RuntimeException {
    public ContaDoMesmoTipoException() {
        super("O cliente já possui uma conta deste tipo.");
    }
}
