package com.db.awmd.challenge.exception;

@SuppressWarnings("serial")
public class DuplicateAccountIdException extends RuntimeException {

  public DuplicateAccountIdException(String message) {
    super(message);
  }
}
