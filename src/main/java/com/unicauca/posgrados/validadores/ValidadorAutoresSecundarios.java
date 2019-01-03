/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.posgrados.validadores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Juan
 */
@FacesValidator(value = "validadorAutoresSecundarios")
public class ValidadorAutoresSecundarios implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String texto = String.valueOf(value);

        if (!texto.equals("")) {
            if (!validarNombreAutores(texto)) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Nombres y apellidos deben contener solo letras y separados por espacio");
                throw new ValidatorException(msg);
            }
        }
    }
    public boolean validarNombreAutores(String texto) {
        return (texto).matches("^[a-zA-Z]{3,}([ ]+[a-zA-Z]{3,})*$");
    }
}
