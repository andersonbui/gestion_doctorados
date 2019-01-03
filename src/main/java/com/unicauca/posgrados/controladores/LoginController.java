/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.posgrados.controladores;

import com.unicauca.posgrados.controladores.util.VistasCoordinador;
import com.unicauca.posgrados.controladores.util.VistasEstudiante;
import com.unicauca.posgrados.entidades.GrupoTipoUsuario;
import com.unicauca.posgrados.entidades.Usuario;
import com.unicauca.posgrados.facades.UsuarioFacade;
import com.unicauca.posgrados.controladores.util.Utilidades;
import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @hola unicauca
 */
@ManagedBean(name = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    private String username;
    private String password;
    private boolean logeado;
    protected Usuario usuario;

    @EJB
//    @Inject
    private UsuarioFacade ejbFacade;

    public LoginController() {
        logeado = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLogeado() {
        return logeado;
    }

    public void setLogeado(boolean logeado) {
        this.logeado = logeado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void sinAcceso() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Error", "Usuario y/o contraseña incorrecto(s)"));
    }

    public void login() throws ServletException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        FacesMessage msg;
        String ruta = "";
        if (req.getUserPrincipal() == null) {
            System.out.println("LOGUEO ACTIVADO----");
            try {
                req.login(this.username, this.password);
                System.out.println("username: "+this.username+"; password: "+this.password);
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenid@", this.username);
                logeado = true;
            } catch (ServletException e) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario o contraseña incorrectos.");
                logeado = false;
                context.addMessage(null, msg);
                return;
            }
            Principal principal = req.getUserPrincipal();
            String nombre = principal.getName();
            System.out.println("nombre: "+nombre+": facade: "+ejbFacade);
            usuario = ejbFacade.buscarPorNombreUsuario(nombre);
            ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
            Map<String, Object> sessionMap = external.getSessionMap();
            sessionMap.put("user", usuario);
            context.addMessage(null, msg);
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenid@", this.username);
            context.addMessage(null, msg);
            logeado = true;
            String nombreUsuario = req.getUserPrincipal().getName();
            usuario = ejbFacade.buscarPorNombreUsuario(nombreUsuario);
        }
        GrupoTipoUsuario gtu = usuario.getGrupoTipoUsuario();
        if (gtu != null) {
            String grupo = usuario.getGrupoTipoUsuario().getIdTipo().getIdTipo();
            switch (grupo) {
                case Utilidades.TIPO_USUARIO_COORDINADOR:
                    ruta = VistasCoordinador.getVistasCoordinador();
                    break;
                case Utilidades.TIPO_USUARIO_ESTUDIANTE:
                    ruta = VistasEstudiante.verVistaEstudiante();
                    break;
                default:
                    ruta = "";
            }
            RequestContext.getCurrentInstance().addCallbackParam("estaLogeado", logeado);
            RequestContext.getCurrentInstance().addCallbackParam("view", ruta);
        }
    }

    public String salir() throws IOException {

        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) fc.getExternalContext().getRequest();
        logeado = false;
        try {
            req.logout();
            req.getSession().invalidate();
            fc.getExternalContext().invalidateSession();

        } catch (ServletException e) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "FAILED", "Cerrar Sesion"));
        }
        return VistasCoordinador.getIndex();
    }

}
