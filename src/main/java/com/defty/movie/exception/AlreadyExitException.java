package com.defty.movie.exception;

public class AlreadyExitException extends RuntimeException {
  public AlreadyExitException(String message) {
    super(message);
  }
}