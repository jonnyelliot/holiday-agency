package com.jelliot.exception;

import java.nio.file.Path;

public class FileFormatException extends RuntimeException {

  public FileFormatException(Path filePath, Throwable cause) {
    super(getMessage(filePath), cause);
  }

  public FileFormatException(Path filePath) {
    super(getMessage(filePath));
  }

  private static String getMessage(Path filePath) {
    return String.format("Format of file %s is invalid", filePath);
  }
}
