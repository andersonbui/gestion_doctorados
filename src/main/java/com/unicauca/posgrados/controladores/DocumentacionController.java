package com.unicauca.posgrados.controladores;

import com.itextpdf.text.DocumentException;
import com.openkm.sdk4j.exception.AccessDeniedException;
import com.openkm.sdk4j.exception.PathNotFoundException;
import com.unicauca.posgrados.entidades.Documentacion;
import com.unicauca.posgrados.controladores.util.JsfUtil;
import com.unicauca.posgrados.controladores.util.JsfUtil.PersistAction;
import com.unicauca.posgrados.controladores.util.VistasCoordinador;
import com.unicauca.posgrados.controladores.util.UtilDocumentacion;
import com.unicauca.posgrados.controladores.util.UtilDocumentacion.ArchivoPDF;
import com.unicauca.posgrados.entidades.Estudiante;
import com.unicauca.posgrados.facades.DocumentacionFacade;
import com.unicauca.posgrados.controladores.util.Utilidades;
import com.unicauca.posgrados.controladores.util.VistasEstudiante;
import com.unicauca.posgrados.entidades.Archivo;
import com.unicauca.posgrados.entidades.ArchivoPK;
import com.unicauca.posgrados.entidades.CapituloLibro;
import com.unicauca.posgrados.entidades.Congreso;
import com.unicauca.posgrados.entidades.Libro;
import com.unicauca.posgrados.entidades.Revista;
import com.unicauca.posgrados.entidades.TipoDocumento;
import com.unicauca.posgrados.entidades.Usuario;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.primefaces.model.UploadedFile;

@Named("documentacionController")
@ManagedBean
public class DocumentacionController implements Serializable {

    @EJB
    private com.unicauca.posgrados.facades.DocumentacionFacade ejbFacade;
    private List<Documentacion> items = null;
    private Documentacion selected;
    private UploadedFile archivoPDF;
    private UploadedFile TablaContenidoPDF;
    private UploadedFile cartaAprobacionPDF;
    private UtilDocumentacion utilDoc;

    public DocumentacionController() {
        utilDoc = new UtilDocumentacion();
        autorSecundario = "";
        autoresSecundarios = new ArrayList<>();

    }

    private String autorSecundario;
    private List<String> autoresSecundarios;

    public String reinit() {
        autorSecundario = "";
        return null;
    }

    public String getAutorSecundario() {
        return autorSecundario;
    }

    public void setAutorSecundario(String autorSecundario) {
        this.autorSecundario = autorSecundario;
    }

    public List<String> getAutoresSecundarios() {
        return autoresSecundarios;
    }

    public UploadedFile getArchivoPDF() {
        return archivoPDF;
    }

    public void setArchivoPDF(UploadedFile archivoPDF) {
        this.archivoPDF = archivoPDF;
    }

    public UploadedFile getTablaContenidoPDF() {
        return TablaContenidoPDF;
    }

    public void setTablaContenidoPDF(UploadedFile TablaContenidoPDF) {
        this.TablaContenidoPDF = TablaContenidoPDF;
    }

    public UploadedFile getCartaAprobacionPDF() {
        return cartaAprobacionPDF;
    }

    public void setCartaAprobacionPDF(UploadedFile cartaAprobacionPDF) {
        this.cartaAprobacionPDF = cartaAprobacionPDF;
    }

    public Documentacion getSelected() {
        return selected;
    }

    public void setSelected(Documentacion selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DocumentacionFacade getFacade() {
        return ejbFacade;
    }

    public String prepareCreate(Estudiante est) {
        selected = new Documentacion();
        selected.setDocFechaPublicacion(new Date());
        selected.setDocAutoresSecundarios("");
        selected.setLibro(new Libro(null, "titulo", "123-45-67891-23-4"));
        selected.setCapituloLibro(new CapituloLibro());
        selected.setRevista(new Revista("NOMBRE REVISTA", "TITULO ARTICULO", "A1", "10.1234/1234"));
        selected.setCongreso(new Congreso(1, "titulo ponencia", "nombre del evento", "evento nacional", "", ""));
        selected.setIdTipoDocumentacion(new TipoDocumento(1, ""));
        autoresSecundarios = new ArrayList();
        selected.setEstId(est);
        initializeEmbeddableKey();
        return VistasEstudiante.registrarPublicacion();
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DocumentacionCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DocumentacionUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DocumentacionDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Documentacion> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<Documentacion> getItems(Usuario user) {
        if (items == null) {
            items = getFacade().buscarPorEstudiante(user.getEstudiante().getEstId());
            user.getEstudiante().setDocumentacionList(items);
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Documentacion getDocumentacion(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Documentacion> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Documentacion> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Documentacion.class)
    public static class DocumentacionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DocumentacionController controller = (DocumentacionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "documentacionController");
            return controller.getDocumentacion(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Documentacion) {
                Documentacion o = (Documentacion) object;
                return getStringKey(o.getDocIdentificador());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Documentacion.class.getName()});
                return null;
            }
        }

    }

    //--------------------------------- ACCIONES -------------------------------
    public String verDocumentacion(Documentacion doc) {
        selected = doc;
        return VistasCoordinador.verPublicacionEstudiante();
    }

    public String verDocumentacionEstudiante(Documentacion doc) {
        selected = doc;
        return VistasEstudiante.verPublicacion();
    }

    public UtilDocumentacion getUtilDoc() {
        return utilDoc;
    }

    public void agregarAutorSecundario(String usuario) {
        autoresSecundarios.add(usuario);
    }

    public void eliminarAutorSecundario(String usuario) {
        autoresSecundarios.remove(usuario);
    }

    //--------------------------------- UTILIDADES -------------------------------
    public String obtenerNombreDoc(Documentacion doc) {
        utilDoc = new UtilDocumentacion();
        return UtilDocumentacion.obtenerNombreDoc(doc);
    }

    public String buscarPorVisadoEnEspera() {
        String vasado = UtilDocumentacion.VISADO_EN_ESPERA;
        items = getFacade().buscarPorVisado(vasado);
        return VistasCoordinador.listarPublicaciones();
    }

    public String buscarPorVisadoRevisada() {
        String vasado = UtilDocumentacion.VISADO_APROBADO;
        items = getFacade().buscarPorVisado(vasado);
        vasado = UtilDocumentacion.VISADO_NO_APROBADO;
        items.addAll(getFacade().buscarPorVisado(vasado));
        return VistasCoordinador.listarPublicaciones();
    }

    public String buscarTodas() {
        items = getFacade().findAll();
        return VistasCoordinador.listarPublicaciones();
    }

    //--------------------------------- UTILIDADES -----------------------------
    /**
     * cambia el estado de visado de una publicacion en la base de datos
     */
    public void cambiarEstadoVisado() {
//        if (!visado.equals("")) {
//            selected.setPubVisado(visado);
//            dao.edit(selected);
//            String correo = selected.getPubEstIdentificador().getEstCorreo();
//
//            if (visado.equalsIgnoreCase("Aprobado")) {
//                Utilidades.enviarCorreo(correo, "Revisión de publicación", "Apreciado "
//                        + selected.getPubEstIdentificador().getEstNombre() + " "
//                        + selected.getPubEstIdentificador().getEstApellido()
//                        + "\n\nLe informamos que su publicación con nombre "
//                        + selected.obtenerNombrePub() + " fue aprobada !ENHORABUENA¡."
//                        + "\nNúmero de creditos: " + selected.getPubCreditos());
//            }
//            if (visado.equalsIgnoreCase("No Aprobado")) {
//                String mensaje = "Apreciado "
//                        + selected.getPubEstIdentificador().getEstNombre() + " "
//                        + selected.getPubEstIdentificador().getEstApellido()
//                        + "\n\nLe informamos que su publicación con nombre "
//                        + selected.obtenerNombrePub() + " no fue aprobada, lo sentimos.";
//                if (!valorObs.equals("")) {
//                    mensaje = mensaje + "\n\nObservaciones: " + valorObs;
//                    valorObs = "";
//                }
//                Utilidades.enviarCorreo(correo, "Revisión de publicación", mensaje);
//            }
//            if (visado.equalsIgnoreCase("espera")) {
//                for (Estudiante doc : selected.getEstudianteList()) {
//                    Utilidades.enviarCorreo(doc.getUsuId().getCorreo(), "Revisión de publicación", "Apreciado "
//                            + doc.getUsuId().getNombres() + " "
//                            + doc.getUsuId().getApellidos()
//                            + "\n\nLe informamos que su publicación con nombre "
//                            + this.obtenerNombreDoc(selected) + " está en espera.");
//                }
//            }
//        }
        //dao.cambia1rEstadoVisado(this.selected.getPubIdentificador(),this.visado);
    }

    public boolean renderizarRevista() {
        if (selected.getIdTipoDocumentacion().getNombre().equalsIgnoreCase(Utilidades.TIPO_DOCUMENTACION_REVISTA)) {
            selected.getIdTipoDocumentacion().setIdentificador(2);
            return true;
        }
        return false;
    }

    public boolean renderizarCongreso() {
        if (selected.getIdTipoDocumentacion().getNombre().equalsIgnoreCase(Utilidades.TIPO_DOCUMENTACION_CONGRESO)) {
            selected.getIdTipoDocumentacion().setIdentificador(3);
            return true;
        }
        return false;
    }

    public boolean renderizarLibro() {
        if (selected.getIdTipoDocumentacion().getNombre().equalsIgnoreCase(Utilidades.TIPO_DOCUMENTACION_LIBRO)) {
            selected.getIdTipoDocumentacion().setIdentificador(1);
            return true;
        }
        return false;
    }

    public boolean renderizarCapLibro() {
        if (selected.getIdTipoDocumentacion().getNombre().equalsIgnoreCase(Utilidades.TIPO_DOCUMENTACION_CAP_LIBRO)) {
            selected.getIdTipoDocumentacion().setIdentificador(4);
            return true;
        }
        return false;
    }

    //<editor-fold defaultstate="collapsed" desc="actualizar estado de publicacion">
        public String[] getTiposVisado(){
            return UtilDocumentacion.LISTA_TIPO_VISADO;
        }
    /**
     * cambia el estado de visado de una publicacion en la base de datos
     * @param evt
     */
    public void actualizarVisado(AjaxBehaviorEvent evt) {
        String comentario = "" + ((UIOutput) evt.getSource()).getValue();
        String visado = selected.getDocVisado();
        Integer  numCreditos = selected.getEstId().getEstCreditos();
        if (!visado.equals("")) {
            selected.getEstId().setEstCreditos(numCreditos+selected.getDocCreditos());
            selected.setDocFechaVisado(new Date());
            ejbFacade.edit(selected);
            String correo = selected.getEstId().getUsuId().getCorreo();
            String primeraParte = "Apreciado "
                    + selected.getEstId().getUsuId().getNombres() + " "
                    + selected.getEstId().getUsuId().getApellidos()
                    + "\n\nLe informamos que su publicación con nombre "
                    + UtilDocumentacion.obtenerNombreDoc(selected);
            String segundaParte = "";
            if (visado.equalsIgnoreCase(UtilDocumentacion.VISADO_APROBADO)) {
                segundaParte = " fue aprobada !ENHORABUENA¡.\nNúmero de creditos: " 
                        + numCreditos;
                segundaParte += "\n\nObservaciones: " + comentario;
            }
            if (visado.equalsIgnoreCase(UtilDocumentacion.VISADO_NO_APROBADO)) {
                segundaParte = " no fue aprobada, lo sentimos.";
                segundaParte += "\n\nObservaciones: " + comentario;
            }
            if (visado.equalsIgnoreCase(UtilDocumentacion.VISADO_EN_ESPERA)) {
                segundaParte = " está en espera.";
                segundaParte += "\n\nObservaciones: " + comentario;
            }
            Utilidades.enviarCorreo(correo, "Revisión de publicación", primeraParte + segundaParte);
            
        }
    }
    //</editor-fold>  

    //<editor-fold defaultstate="collapsed" desc="Descarga documentos openkm">
    public void pdfCartaAprob() throws FileNotFoundException, IOException, IOException, IOException {
        ArchivoPDF archivoPublic = utilDoc.descargaCartaAprobac("cartaAprobacion", selected);
        if (archivoPublic.getNombreArchivo().equals("")) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Para esta publicacion el Usuario no ha cargado un PDF de la carta de aprobacion  ", ""));

        } else {
            String[] nombreArchivo = archivoPublic.getNombreArchivo().split("\\.");
            InputStream fis = archivoPublic.getArchivo();

            HttpServletResponse response
                    = (HttpServletResponse) FacesContext.getCurrentInstance()
                            .getExternalContext().getResponse();

            response.setContentType("application/pdf");
            // response.setHeader("Content-Disposition", "inline;filename=" + archivoPublic.getNombreArchivo() + ".pdf");
            response.setHeader("Content-Disposition", "inline;filename=" + nombreArchivo[0] + ".pdf");
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }

            // response.getOutputStream().write(buf);
            response.getOutputStream().flush();
            response.getOutputStream().close();
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void pdfPub() throws FileNotFoundException, IOException, IOException, IOException {
        ArchivoPDF archivoPublic = utilDoc.descargaCartaAprobac("tipoPublicacion", selected);

        if (archivoPublic.getNombreArchivo().equals("")) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario no ha cargado un PDF para esta publicacion", ""));

        } else {

            String[] nombreArchivo = archivoPublic.getNombreArchivo().split("\\.");
            InputStream fis = archivoPublic.getArchivo();

            HttpServletResponse response
                    = (HttpServletResponse) FacesContext.getCurrentInstance()
                            .getExternalContext().getResponse();

            response.setContentType("application/pdf");
            // response.setHeader("Content-Disposition", "inline;filename=" + archivoPublic.getNombreArchivo() + ".pdf");
            response.setHeader("Content-Disposition", "inline;filename=" + nombreArchivo[0] + ".pdf");
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }

            // response.getOutputStream().write(buf);
            response.getOutputStream().flush();
            response.getOutputStream().close();
            FacesContext.getCurrentInstance().responseComplete();

        }

    }

    public void pdfPubTC() throws FileNotFoundException, IOException, IOException, IOException {
        ArchivoPDF archivoPublic = utilDoc.descargaCartaAprobac("tablaContenido", selected);

        if (archivoPublic.getNombreArchivo().equals("")) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario no ha cargado un PDF de la tabla de contenido", ""));

        } else {
            String[] nombreArchivo = archivoPublic.getNombreArchivo().split("\\.");
            InputStream fis = archivoPublic.getArchivo();

            HttpServletResponse response
                    = (HttpServletResponse) FacesContext.getCurrentInstance()
                            .getExternalContext().getResponse();

            response.setContentType("application/pdf");
            // response.setHeader("Content-Disposition", "inline;filename=" + archivoPublic.getNombreArchivo() + ".pdf");
            response.setHeader("Content-Disposition", "inline;filename=" + nombreArchivo[0] + ".pdf");
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }

            // response.getOutputStream().write(buf);
            response.getOutputStream().flush();
            response.getOutputStream().close();
            FacesContext.getCurrentInstance().responseComplete();

        }
    }

    public void descargarCartaAprobac() throws FileNotFoundException, IOException {
        ArchivoPDF archivoPublic = utilDoc.descargaCartaAprobac("cartaAprobacion", selected);
        if (archivoPublic.getNombreArchivo().equals("")) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Para esta publicacion el Usuario no ha cargado un PDF de la carta de aprobacion  ", ""));

        } else {

            InputStream fis = archivoPublic.getArchivo();
            String[] nombreArchivo = archivoPublic.getNombreArchivo().split("\\.");
            HttpServletResponse response
                    = (HttpServletResponse) FacesContext.getCurrentInstance()
                            .getExternalContext().getResponse();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename=" + nombreArchivo[0] + ".pdf");

            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }

            // response.getOutputStream().write(buf);
            response.getOutputStream().flush();
            response.getOutputStream().close();
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void descargarPublicacion() throws FileNotFoundException, IOException {
        ArchivoPDF archivoPublic = utilDoc.descargaCartaAprobac("tipoPublicacion", selected);
        if (archivoPublic.getNombreArchivo().equals("")) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario no ha cargado un PDF para esta publicacion", ""));

        } else {
            InputStream fis = archivoPublic.getArchivo();
            String[] nombreArchivo = archivoPublic.getNombreArchivo().split("\\.");
            HttpServletResponse response
                    = (HttpServletResponse) FacesContext.getCurrentInstance()
                            .getExternalContext().getResponse();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename=" + nombreArchivo[0] + ".pdf");

            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }

            // response.getOutputStream().write(buf);
            response.getOutputStream().flush();
            response.getOutputStream().close();
            FacesContext.getCurrentInstance().responseComplete();
        }

    }

    public void descargarPubTC() throws FileNotFoundException, IOException {
        ArchivoPDF archivoPubTC = utilDoc.descargaCartaAprobac("tablaContenido", selected);
        if (archivoPubTC.getNombreArchivo().equals("")) {
            FacesContext.getCurrentInstance().addMessage("error", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario no ha cargado un PDF de la tabla de contenido", ""));

        } else {
            byte[] buf;
            InputStream fis = archivoPubTC.getArchivo();
            String[] nombreArchivo = archivoPubTC.getNombreArchivo().split("\\.");

            HttpServletResponse response
                    = (HttpServletResponse) FacesContext.getCurrentInstance()
                            .getExternalContext().getResponse();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename=" + nombreArchivo[0] + ".pdf");

            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }

            // response.getOutputStream().write(buf);
            response.getOutputStream().flush();
            response.getOutputStream().close();
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    //</editor-fold>  
    
    //<editor-fold defaultstate="collapsed" desc="agregar nueva publicacion al estudiante">
    public String agregar(Estudiante est) {
        System.out.println("Registrando publicacion");
        /* formatoValido -> se utiliza para verificar que el usario
           suba unicamente archivos en formato pdf*/
        boolean formatoValido = true;
        System.out.println("nombre archivo pdf:" + archivoPDF.getFileName());
        if (!archivoPDF.getFileName().equalsIgnoreCase("") && !"application/pdf".equals(archivoPDF.getContentType())) {

            FacesContext.getCurrentInstance().addMessage("valPublicacion", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe subir la publicación o la evidencia de la publicación en formato PDF", ""));
            formatoValido = false;
        }
        System.out.println("TablaContenidoPDF:" + TablaContenidoPDF.getFileName());
        if (!TablaContenidoPDF.getFileName().equalsIgnoreCase("") && !"application/pdf".equals(TablaContenidoPDF.getContentType())) {

            FacesContext.getCurrentInstance().addMessage("valTContenido", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe subir la Tabla de Contenido en formato PDF", ""));
            formatoValido = false;
        }
        System.out.println("nombre cartaAprobacionPDF:" + cartaAprobacionPDF.getFileName());
        if (!cartaAprobacionPDF.getFileName().equalsIgnoreCase("") && !"application/pdf".equals(cartaAprobacionPDF.getContentType())) {

            FacesContext.getCurrentInstance().addMessage("cartaAprobacion", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe subir la carta de aprobación en formato PDF", ""));
            formatoValido = false;
        }

        if (formatoValido == true) {
            /* puedeSubir  ->  se utiliza para comprobar que el usuario ha seleccionado 
                el PDF de la publicacion o en su defecto la carta de aprobacion*/
            boolean puedeSubir = false;
            if (archivoPDF.getFileName().equalsIgnoreCase("")) {
                if (cartaAprobacionPDF.getFileName().equalsIgnoreCase("")) {

                    FacesContext.getCurrentInstance().addMessage("cartaAprobacion", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe subir la publicación o la evidencia de la publicación", ""));
                } else {
                    puedeSubir = true;

                }
            } else {
                puedeSubir = true;
            }

            if (puedeSubir == true) {
                //else {
                System.out.println("agregar");
                try {
                    selected.setEstId(est);
                    selected.setDocCreditos(2);
                    int idDocument = ejbFacade.getnumFilasPubRev();
                    selected.setDocIdentificador(idDocument);
                    // optener autores secundarios
                    String autSecundarios = "";
                    String separador = UtilDocumentacion.SEPARADOR_AUTORES_SECUNDARIOS;
                    for (int i = 0; i < autoresSecundarios.size(); i++) {
                        if (i == autoresSecundarios.size() - 1) {
                            separador = "";
                        }
                        autSecundarios += autoresSecundarios.get(i) + separador;
                    }
                    selected.setDocAutoresSecundarios(autSecundarios);
                    /* Dependiendo de si se adiciona una revista, congreso, libro o   
                    capitulo de un libro se mantiena el objeto respectivo y se eliminan
                    los objetos residuales correspondientes a los demas tipos*/
                    if (renderizarRevista()) {
                        System.out.println("revista:" + selected.getIdTipoDocumentacion().getNombre());
//                        selected.getRevista().setDocumentacion(selected);
                        selected.getRevista().setDocIdentificador(idDocument);
                        selected.setCongreso(null);
                        selected.setCapituloLibro(null);
                        selected.setLibro(null);

                    }
                    if (renderizarCongreso()) {

                        System.out.println("congreso:" + selected.getIdTipoDocumentacion().getNombre());
                        selected.getCongreso().setDocIdentificador(idDocument);
                        selected.setRevista(null);
                        selected.setCapituloLibro(null);
                        selected.setLibro(null);
                    }

                    if (renderizarLibro()) {
                        System.out.println("libro:" + selected.getIdTipoDocumentacion().getNombre());
                        selected.getLibro().setDocIdentificador(idDocument);
                        selected.setRevista(null);
                        selected.setCongreso(null);
                        selected.setCapituloLibro(null);
                    }

                    if (renderizarCapLibro()) {
                        System.out.println("capitulo libro:" + selected.getIdTipoDocumentacion().getNombre());
                        selected.getCapituloLibro().setDocIdentificador(idDocument);
                        selected.setRevista(null);
                        selected.setCongreso(null);
                        selected.setLibro(null);
                    }

                    ArrayList<Archivo> CollArchivo = new ArrayList<>();

                    Archivo archCartaAprob = new Archivo();
                    archCartaAprob.setArchivoPK(new ArchivoPK());
//                    archCartaAprob.setDocumentacion(selected);
                    archCartaAprob.getArchivoPK().setArctipoPDFcargar(Utilidades.TIPO_PDF_CARTA_DE_APROBACION);
                    archCartaAprob.getArchivoPK().setDocIdentificador(selected.getDocIdentificador());
                    CollArchivo.add(archCartaAprob);

                    if (!TablaContenidoPDF.getFileName().equalsIgnoreCase("")) {
                        Archivo arcTablaC = new Archivo();
                        arcTablaC.setArchivoPK(new ArchivoPK());
                        arcTablaC.getArchivoPK().setArctipoPDFcargar(Utilidades.TIPO_PDF_TABLA_DE_CONTENIDO);
                        arcTablaC.getArchivoPK().setDocIdentificador(selected.getDocIdentificador());
                        CollArchivo.add(arcTablaC);
                    }
                    //int numArchivos = numPubRevis;
                    if (!archivoPDF.getFileName().equalsIgnoreCase("")) {
                        Archivo archArt = new Archivo();
                        archArt.setArchivoPK(new ArchivoPK());
//                        archArt.setDocumentacion(selected);
                        archArt.getArchivoPK().setArctipoPDFcargar(Utilidades.TIPO_PDF_DOCUMENTO);
                        archArt.getArchivoPK().setDocIdentificador(selected.getDocIdentificador());
                        CollArchivo.add(archArt);
                    }
                    selected.setArchivoList(CollArchivo);
                    utilDoc.agregarMetadatos(archivoPDF, TablaContenidoPDF, cartaAprobacionPDF, selected);
                    selected.setArchivoList(null);
                    selected.setPasantia(null);

                    selected.setDocEstado("Activo");
                    /* Asigna espera como estado del visado la publicacion */
                    selected.setDocVisado("espera");
                    items = null;
                    this.ejbFacade.edit(selected);
//                    this.create();
//                    ejbFacade.flush();
//                    mensajeconfirmarRegistro();

                    Date date = new Date();
                    DateFormat datehourFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String estampaTiempo = "" + datehourFormat.format(date);
                    Utilidades.enviarCorreo("posgradoselectunic@gmail.com", "Mensaje Sistema Doctorados - Registro Publicación", "Estudiante " + est.getUsuId().getNombres() + " ha regitrado una publicación del tipo " + selected.getIdTipoDocumentacion().getNombre() + " en la siguiente f y hora: " + estampaTiempo);
//                    limpiarCampos();
//                    redirigirAlistar(est.getEstUsuario());
                    // redirigirAlistar(est.getEstUsuario());
                } catch (EJBException e) {
                    @SuppressWarnings("ThrowableResultIgnored")
                    Exception cause = e.getCausedByException();
                    if (cause instanceof ConstraintViolationException) {
                        @SuppressWarnings("ThrowableResultIgnored")
                        ConstraintViolationException cve = (ConstraintViolationException) e.getCausedByException();
                        for (Iterator<ConstraintViolation<?>> it = cve.getConstraintViolations().iterator(); it.hasNext();) {
                            ConstraintViolation<? extends Object> v = it.next();
                            System.err.println(v);
                            System.err.println("==>>" + v.getMessage());
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(DocumentacionController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (GeneralSecurityException ex) {
                    Logger.getLogger(DocumentacionController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DocumentException ex) {
                    Logger.getLogger(DocumentacionController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (PathNotFoundException ex) {
                    Logger.getLogger(DocumentacionController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (AccessDeniedException ex) {
                    Logger.getLogger(DocumentacionController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        return VistasEstudiante.verPublicaciones();

    }
    //</editor-fold>  
}
