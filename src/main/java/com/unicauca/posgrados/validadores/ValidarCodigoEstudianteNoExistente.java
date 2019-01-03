package com.unicauca.posgrados.validadores;

import com.unicauca.posgrados.controladores.EstudianteController;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "ValidarCodigoEstudianteNoExistente")
public class ValidarCodigoEstudianteNoExistente implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String codigo = String.valueOf(String.valueOf(value));

        if (value != null) {

            EstudianteController controller = (EstudianteController) context.getApplication().getELResolver().
                    getValue(context.getELContext(), null, "estudianteController");
            Integer identificador = controller.getSelected().getEstId();
            if (identificador == null) {
                identificador = -1;
            }
            if (controller.buscarPorCodigoExceptoConId(codigo, identificador) != null) {
                String message = "Codigo ya existe, Ingrese otro por favor.";
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
                throw new ValidatorException(msg);
            }
        }

    }

}
