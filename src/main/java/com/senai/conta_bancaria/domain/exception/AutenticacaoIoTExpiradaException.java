package com.senai.conta_bancaria.domain.exception;

public class AutenticacaoIoTExpiradaException extends RuntimeException {
  public AutenticacaoIoTExpiradaException() {
    super("A autenticação expirou ou falhou.");
  }
}