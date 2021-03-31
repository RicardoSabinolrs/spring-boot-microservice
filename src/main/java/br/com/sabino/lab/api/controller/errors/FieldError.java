package br.com.sabino.lab.api.controller.errors;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


public class FieldError implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private final String objectName;
    @Getter
    @Setter
    private final String field;
    @Getter
    @Setter
    private final String message;

    public FieldError(String dto, String field, String message) {
        this.objectName = dto;
        this.field = field;
        this.message = message;
    }
}
