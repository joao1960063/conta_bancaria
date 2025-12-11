package com.senai.conta_bancaria.domain.exception;

public class TaxaInvalidaException extends RuntimeException {
  public TaxaInvalidaException(String message) {
    super(message);
  }
}