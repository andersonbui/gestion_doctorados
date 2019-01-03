/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.posgrados.validadores;

import javax.ejb.EJB;
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
@FacesValidator(value = "validadorNombreArticulo")
public class ValidadorNombreArticulo implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String nombre = String.valueOf(value);
        System.out.println("Nombre" + nombre);

        /*Validando que el campo no este vacio*/
        if (nombre.length() == 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El título del artículo es obligatorio");
            throw new ValidatorException(msg);
        }

        /*Validando el tamaño del campo*/
        if (nombre.length() < 10 || nombre.length() > 200) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "El título del artículo debe contener entre 10 y 200 caracteres");
            throw new ValidatorException(msg);
        }

        /*Validando que solo halla espacios entre palabras*/
        if (nombre.length() != 0) {
            if (nombre.charAt(0) == ' ' || nombre.charAt(nombre.length() - 1) == ' ') {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Solo se permiten espacios entre las palabras");
                throw new ValidatorException(msg);
            }

        }

    }

}
