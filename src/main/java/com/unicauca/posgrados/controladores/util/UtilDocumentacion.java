/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicauca.posgrados.controladores.util;

import com.unicauca.posgrados.entidades.Archivo;
import com.unicauca.posgrados.entidades.Documentacion;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.pdf.PdfConformanceLevel;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.openkm.sdk4j.OKMWebservices;
import com.openkm.sdk4j.OKMWebservicesFactory;
import com.openkm.sdk4j.bean.Folder;
import com.openkm.sdk4j.bean.QueryParams;
import com.openkm.sdk4j.bean.QueryResult;
import com.openkm.sdk4j.bean.form.FormElement;
import com.openkm.sdk4j.bean.form.Input;
import com.openkm.sdk4j.exception.AccessDeniedException;
import com.openkm.sdk4j.exception.AutomationException;
import com.openkm.sdk4j.exception.DatabaseException;
import com.openkm.sdk4j.exception.ExtensionException;
import com.openkm.sdk4j.exception.FileSizeExceededException;
import com.openkm.sdk4j.exception.ItemExistsException;
import com.openkm.sdk4j.exception.LockException;
import com.openkm.sdk4j.exception.NoSuchGroupException;
import com.openkm.sdk4j.exception.NoSuchPropertyException;
import com.openkm.sdk4j.exception.ParseException;
import com.openkm.sdk4j.exception.PathNotFoundException;
import com.openkm.sdk4j.exception.RepositoryException;
import com.openkm.sdk4j.exception.UnknowException;
import com.openkm.sdk4j.exception.UnsupportedMimeTypeException;
import com.openkm.sdk4j.exception.UserQuotaExceededException;
import com.openkm.sdk4j.exception.VirusDetectedException;
import com.openkm.sdk4j.exception.WebserviceException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author debian
 */
public class UtilDocumentacion {

    public static final String VISADO_EN_ESPERA = "EN ESPERA";
    public static final String VISADO_NO_APROBADO = "NO APROBADO";
    public static final String VISADO_APROBADO = "APROBADO";
    public static final String[] LISTA_TIPO_VISADO = {VISADO_EN_ESPERA, VISADO_NO_APROBADO, VISADO_APROBADO};

    public static final String SEPARADOR_AUTORES_SECUNDARIOS = ", ";

    public String formatearFecha(Date fecha) {
        SimpleDateFormat format = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es_ES"));
        return format.format(fecha);
    }

    //<editor-fold defaultstate="collapsed" desc="descargaCartaAprobac">
    public ArchivoPDF descargaCartaAprobac(String tipoPDF, Documentacion doc) {
        ArchivoPDF archivo = new ArchivoPDF();

        String host = "http://localhost:8083/OpenKM";
        //String host = "http://wmyserver.sytes.net:8083/OpenKM";
        String username = "okmAdmin";
        String password = "admin";
        OKMWebservices ws = OKMWebservicesFactory.newInstance(host, username, password);
        Integer idDocumentacion = doc.getDocIdentificador();
        try {

            Map<String, String> properties = new HashMap();
            String tipo = doc.getIdTipoDocumentacion().getNombre();
            properties.put("okp:" + tipo + ".identPublicacion", "" + idDocumentacion);
            properties.put("okp:" + tipo + ".tipoPDFCargar", "" + tipoPDF);

            QueryParams qParams = new QueryParams();
            qParams.setProperties(properties);
            int posPub = 0;
            for (QueryResult qr : ws.find(qParams)) {
                if (posPub == 0) {
                    String auxDoc = qr.getDocument().getPath();
                    String[] arrayNombre = auxDoc.split("/");
                    int pos = arrayNombre.length;
                    String nombreDoc = arrayNombre[pos - 1];
                    System.out.println("nombreDocPUB: " + nombreDoc);
                    InputStream initialStream = ws.getContent(qr.getDocument().getPath());
                    archivo.setArchivo(initialStream);
                    archivo.setNombreArchivo(nombreDoc);
                }
                posPub = posPub + 1;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return archivo;
    }
    //</editor-fold>

    public String getAutoresSeundarios(Documentacion docu) {
        String autores_secun = docu.getDocAutoresSecundarios();
        return autores_secun;
    }

    public void agregarMetadatos(UploadedFile ArchivoPDF, UploadedFile TablaContenidoPDF,
            UploadedFile cartaAprobacionPDF, Documentacion documentacion)
            throws IOException, GeneralSecurityException, DocumentException,
            PathNotFoundException, AccessDeniedException {

        /*Nombre de los archivos que se almacenaran en el repositorio*/
        MetodosPDF mpdf = new MetodosPDF();
        String codigoEst = documentacion.getEstId().getEstCodigo();
        String codigoFirma = mpdf.codigoFirma(codigoEst);
        codigoFirma = codigoFirma.trim();
//        String tipoDoc = documentacion.getIdTipoPublicacion().getNombre();

        String nombreCartaAprob = Utilidades.TIPO_PDF_CARTA_DE_APROBACION + "-" + codigoFirma;
        String nombrePublicacion = obtenerNombreDoc(documentacion);
        String nombreTablaC = Utilidades.TIPO_PDF_TABLA_DE_CONTENIDO + "-" + codigoFirma;


        /*Obtiene la ruta de la ubicacion del servidor donde se almacenaran 
          temporalmente los archivos ,para luego subirlos al Gestor Documental OpenKm  */
        String realPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        //   String destCartaAprob = realPath + "WEB-INF\\temp\\Tabla de Contenido.pdf";
        String destCartaAprob = realPath + "WEB-INF\\temp\\" + nombreCartaAprob + ".pdf";
        String destArticulo = realPath + "WEB-INF\\temp\\" + nombrePublicacion + ".pdf";
        //  String destTablaC = realPath + "WEB-INF\\temp\\Tabla de Contenido.pdf";
        String destTablaC = realPath + "WEB-INF\\temp\\" + nombreTablaC + ".pdf";


        /*  Estampa de Tiempo
            Obtiene el dia y hora actual del servidor para posteriormente adicionarlo
            como Metadato en el documento PDF/A y el Gestor Documental*/
        Date date = new Date();
        DateFormat datehourFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String estampaTiempo = "" + datehourFormat.format(date);

        documentacion.setDocFechaRegistro(date);

        /*  Metodo para almacenar los metadatos de la Carte de Aprobacion , Articulo y Tabla de Contenido 
            para almacenarlo en formato PDFA */
        ArrayList<ArchivoPDF> subidaArchivos = new ArrayList<>();

        /* TipoPDF_cargar cartaAprobacion = new TipoPDF_cargar();
        cartaAprobacion.setNombreArchivo(nombreCartaAprob);
        cartaAprobacion.setRutaArchivo(destCartaAprob);
        cartaAprobacion.setTipoPDF("cartaAprobacion");
        cartaAprobacion.setArchivoIS(cartaAprobacionPDF.getInputstream());
        subidaArchivos.add(cartaAprobacion); */
        if (!cartaAprobacionPDF.getFileName().equalsIgnoreCase("")) {
            ArchivoPDF cartaAprobacion = new ArchivoPDF();
            cartaAprobacion.setNombreArchivo(nombreCartaAprob);
            cartaAprobacion.setRutaArchivo(destCartaAprob);
            cartaAprobacion.setTipoPDF(Utilidades.TIPO_PDF_CARTA_DE_APROBACION);
            cartaAprobacion.setArchivo(cartaAprobacionPDF.getInputstream());
            subidaArchivos.add(cartaAprobacion);
        }

        if (!ArchivoPDF.getFileName().equalsIgnoreCase("")) {
            ArchivoPDF articulo = new ArchivoPDF();
            articulo.setNombreArchivo(nombrePublicacion);
            articulo.setRutaArchivo(destArticulo);
            articulo.setTipoPDF(Utilidades.TIPO_PDF_DOCUMENTO);
            articulo.setArchivo(ArchivoPDF.getInputstream());
            subidaArchivos.add(articulo);
        }

        if (!TablaContenidoPDF.getFileName().equalsIgnoreCase("")) {
            ArchivoPDF tablaContenido = new ArchivoPDF();
            tablaContenido.setNombreArchivo(nombreTablaC);
            tablaContenido.setRutaArchivo(destTablaC);
            tablaContenido.setTipoPDF(Utilidades.TIPO_PDF_TABLA_DE_CONTENIDO);
            tablaContenido.setArchivo(TablaContenidoPDF.getInputstream());
            subidaArchivos.add(tablaContenido);
        }

        CrearPDFA_Metadata(subidaArchivos, estampaTiempo, documentacion);

        String hash = mpdf.obtenerHash(destArticulo);

        /*  Metodo para almacenar en el Gestor Documental(OPENKM), carta de aprobacion,
            el articulo en formato PDFA y la Tabla de Contenido del Articulo (formato PDFA) */
        //   SubirOpenKM(rutasArchivos, nombreArchivos, estampaTiempo, codigoFirma, hash);
        SubirOpenKM(subidaArchivos, estampaTiempo, codigoFirma, hash, documentacion);
    }

    public class GrupoRevista {

        List<String> lista;
    }

    //<editor-fold defaultstate="collapsed" desc="CrearPDFA_Metadata">
    private void CrearPDFA_Metadata(ArrayList<ArchivoPDF> subidaArchivos,
            String estampaTiempo, Documentacion doc) {

        List<Archivo> listaArchivos = doc.getArchivoList();
        String autoresSecundarios = getAutoresSeundarios(doc);
        Integer idDocumentacion = doc.getDocIdentificador();
        for (int i = 0; i < subidaArchivos.size(); i++) {
            Document document = new Document();
            PdfReader reader;

            try {
                String tipoPublicacion = doc.getIdTipoDocumentacion().getNombre();
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(
                        subidaArchivos.get(i).getRutaArchivo()));
                writer.setTagged();
                writer.setPDFXConformance(com.lowagie.text.pdf.PdfWriter.PDFA1B);
                
                SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
                String FechaPublicacion = formateador.format(doc.getDocFechaPublicacion());

                document.addHeader("Identificador Publicacion", "" + idDocumentacion);
                document.addHeader("TipoPDF_cargar", "" + subidaArchivos.get(i).getTipoPDF());
                document.addHeader("Estampa Tiempo", "" + estampaTiempo);
                document.addAuthor("" + doc.getEstId().getUsuId().getNombres());
                document.addCreator("" + doc.getEstId().getUsuId().getNombres());
                document.addHeader("Autores Secundarios", autoresSecundarios);
                document.addHeader("Fecha Publicacion", FechaPublicacion);
                document.addHeader("Tipo Publicacion", tipoPublicacion);
                /* Se Comprubea si los metadatos a llenar son para una revista
                    un congreso , un libro o un capitulo de un libro*/
                if (tipoPublicacion.equalsIgnoreCase(Utilidades.TIPO_DOCUMENTACION_REVISTA)) {
                    document.addTitle("" + doc.getRevista().getRevTituloArticulo());
                    document.addHeader("Nombre_Revista", doc.getRevista().getRevNombreRevista());
                    document.addHeader("Categoria", doc.getRevista().getRevCategoria());
                    document.addHeader("DOI", "" + doc.getRevista().getRevDoi());

                }
                if (tipoPublicacion.equalsIgnoreCase(Utilidades.TIPO_DOCUMENTACION_CONGRESO)) {
                    document.addTitle("" + doc.getCongreso().getCongTituloPonencia());
                    document.addHeader("Nombre_Evento", doc.getCongreso().getCongNombreEvento());
                    document.addHeader("Tipo_Congreso", doc.getCongreso().getCongTipoCongreso());
                    document.addHeader("DOI", "" + doc.getCongreso().getCongDoi());
                    document.addHeader("ISSN", "" + doc.getCongreso().getCongIssn());

                }
                if (tipoPublicacion.equalsIgnoreCase(Utilidades.TIPO_DOCUMENTACION_LIBRO)) {
                    document.addTitle("" + doc.getLibro().getLibTituloLibro());
                    document.addHeader("Titulo_Libro", doc.getLibro().getLibTituloLibro());
                }
                if (tipoPublicacion.equalsIgnoreCase(Utilidades.TIPO_DOCUMENTACION_CAP_LIBRO)) {
                    document.addTitle("" + doc.getCapituloLibro().getCaplibTituloLibro());
                    document.addHeader("Titulo_Libro", doc.getCapituloLibro().getCaplibTituloLibro());
                    document.addHeader("Titulo_Capitulo", doc.getCapituloLibro().getCaplibTituloCapitulo());
                    document.addHeader("ISBN", "" + doc.getCapituloLibro().getCaplibIsbn());
                }

                document.addCreationDate();

                writer.setTagged();
                writer.createXmpMetadata();
                writer.setCompressionLevel(9);
                document.open();
                PdfContentByte cb = writer.getDirectContent();
                reader = new PdfReader(subidaArchivos.get(i).getArchivo());
                PdfImportedPage page;
                int pageCount = reader.getNumberOfPages();
                for (int j = 0; j < pageCount; j++) {
                    document.newPage();
                    page = writer.getImportedPage(reader, j + 1);
                    cb.addTemplate(page, 0, 0);
                }
            } catch (IOException ex) {
                Logger.getLogger(UtilDocumentacion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(UtilDocumentacion.class.getName()).log(Level.SEVERE, null, ex);
            }
            document.close();

        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="obtenerNombreDoc">
    public static String obtenerNombreDoc(Documentacion doc) {
        String nombrePub = "";
        String tipoPublicacion = doc.getIdTipoDocumentacion().getNombre();

        if (tipoPublicacion.equalsIgnoreCase(Utilidades.TIPO_DOCUMENTACION_REVISTA)) {
            nombrePub = doc.getRevista().getRevTituloArticulo();
        }
        if (tipoPublicacion.equalsIgnoreCase(Utilidades.TIPO_DOCUMENTACION_CONGRESO)) {
            nombrePub = doc.getCongreso().getCongTituloPonencia();
        }
        if (tipoPublicacion.equalsIgnoreCase(Utilidades.TIPO_DOCUMENTACION_LIBRO)) {
            nombrePub = doc.getLibro().getLibTituloLibro();
        }
        if (tipoPublicacion.equalsIgnoreCase(Utilidades.TIPO_DOCUMENTACION_CAP_LIBRO)) {
            nombrePub = doc.getCapituloLibro().getCaplibTituloCapitulo();
        }
        return nombrePub;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SubirOpenKM">
    public void SubirOpenKM(ArrayList<ArchivoPDF> listaArchivos, String estampaTiempo,
            String codigoFirma, String hash, Documentacion documentacion) throws IOException {

        /* Inicia una instancia del Gestor Documental Openkm*/
        String autoresSecundarios = getAutoresSeundarios(documentacion);
        documentacion.setDocHash(hash);
        String autorDoc = documentacion.getEstId().getUsuId().getNombres();
        String host = "http://localhost:8083/OpenKM";
        //String host = "http://wmyserver.sytes.net:8083/OpenKM";
        String username = "okmAdmin";
        String password = "admin";
        Integer idDocumentacion = documentacion.getDocIdentificador();
        OKMWebservices okmWebServ = OKMWebservicesFactory.newInstance(host, username, password);
        try {
            boolean crearFolder = false;
            String rutaFolderCrear;
            /* codigoFirma - en este caso corresponde al nombre de la carpeta que contendra
                el articulo y su tabla de contenido en formato PDFA
                Ruta del folder a crear en el Gestor Openkm*/
            // rutaFolderCrear = "/okm:root/Doctorado_Electronica/" + codigoFirma;
            rutaFolderCrear = "/okm:root/Doctorado_Electronica/" + documentacion.getEstId().getEstCodigo();
            documentacion.setDocDiropkm(codigoFirma);
            try {
                /* Se valida si el forder a crear existe o no*/
                okmWebServ.isValidFolder(rutaFolderCrear);
                crearFolder = false;
            } catch (Exception e) {
                crearFolder = true;
            }
            if (crearFolder == true) {
                /* Si no existe el folder, se crea con la ruta(rutaFolderCrear)*/
                Folder fld = new Folder();
                fld.setPath(rutaFolderCrear);
                okmWebServ.createFolder(fld);
            }

            rutaFolderCrear = rutaFolderCrear + "/" + codigoFirma;
            try {
                /* Se valida si el forder a crear existe o no*/
                okmWebServ.isValidFolder(rutaFolderCrear);
                crearFolder = false;
            } catch (Exception e) {
                crearFolder = true;
            }
            if (crearFolder == true) {
                /* Si no existe el folder, se crea con la ruta(rutaFolderCrear)*/
                Folder fld = new Folder();
                fld.setPath(rutaFolderCrear);
                okmWebServ.createFolder(fld);
            }
            List<Archivo> archivos = documentacion.getArchivoList();
            for (int i = 0; i < listaArchivos.size(); i++) {
                String tipoDocumentacion = documentacion.getIdTipoDocumentacion().getNombre();
                SimpleDateFormat formateador = new SimpleDateFormat("MM-yyyy");
                String FechaPublicacion = formateador.format(documentacion.getDocFechaPublicacion());

                Archivo arch = archivos.get(i);
                /* Se Comprubea si los  Metadatos en OpenKm que se van  a llenar son para 
                 una revista, un congreso , un libro o un capitulo de un libro*/
                if (tipoDocumentacion.equalsIgnoreCase(Utilidades.TIPO_DOCUMENTACION_REVISTA)) {

                    InputStream is = new FileInputStream("" + listaArchivos.get(i).getRutaArchivo());
                    com.openkm.sdk4j.bean.Document doc = new com.openkm.sdk4j.bean.Document();
                    doc.setPath(rutaFolderCrear + "/" + listaArchivos.get(i).getNombreArchivo() + ".pdf");
                    okmWebServ.createDocument(doc, is);

                    IOUtils.closeQuietly(is);
                    List<FormElement> fElements = okmWebServ.getPropertyGroupProperties("" + rutaFolderCrear + "/" + listaArchivos.get(i).getNombreArchivo() + ".pdf", "okg:" + tipoDocumentacion + "");
                    for (FormElement fElement : fElements) {
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".identPublicacion")) {
                            Input name = (Input) fElement;
                            name.setValue("" + idDocumentacion);
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".tipoPDFCargar")) {
                            Input name = (Input) fElement;
                            name.setValue("" + listaArchivos.get(i).getTipoPDF());
                        }

                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".estampaTiempo")) {
                            Input name = (Input) fElement;
                            name.setValue("" + estampaTiempo);
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".nombreAutor")) {
                            Input name = (Input) fElement;
                            name.setValue("" + autorDoc);
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".autoresSecundarios")) {
                            Input name = (Input) fElement;
                            name.setValue("" + autoresSecundarios);
                        }

                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".fechaPublicacion")) {
                            Input name = (Input) fElement;
                            name.setValue("" + FechaPublicacion);
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".tipoPublicacion")) {
                            Input name = (Input) fElement;
                            name.setValue("" + documentacion.getIdTipoDocumentacion().getNombre());
                        }
                        //--------
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".tituloArticulo")) {
                            Input name = (Input) fElement;
                            name.setValue("" + documentacion.getRevista().getRevTituloArticulo());
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".nombreRevista")) {
                            Input name = (Input) fElement;
                            name.setValue("" + documentacion.getRevista().getRevNombreRevista());
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".categoria")) {
                            Input name = (Input) fElement;
                            name.setValue("" + documentacion.getRevista().getRevCategoria());
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".DOI")) {
                            Input name = (Input) fElement;
                            name.setValue("" + documentacion.getRevista().getRevDoi());
                        }

                    }
                    okmWebServ.setPropertyGroupProperties("" + rutaFolderCrear + "/" + listaArchivos.get(i).getNombreArchivo() + ".pdf", "okg:" + tipoDocumentacion + "", fElements);

                }

                if (tipoDocumentacion.equalsIgnoreCase(Utilidades.TIPO_DOCUMENTACION_CONGRESO)) {

                    InputStream is = new FileInputStream("" + listaArchivos.get(i).getRutaArchivo());
                    com.openkm.sdk4j.bean.Document doc = new com.openkm.sdk4j.bean.Document();

                    doc.setPath(rutaFolderCrear + "/" + listaArchivos.get(i).getNombreArchivo() + ".pdf");
                    okmWebServ.createDocument(doc, is);
                    IOUtils.closeQuietly(is);
                    List<FormElement> fElements = okmWebServ.getPropertyGroupProperties("" + rutaFolderCrear + "/" + listaArchivos.get(i).getNombreArchivo() + ".pdf", "okg:" + tipoDocumentacion + "");

                    for (FormElement fElement : fElements) {
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".identPublicacion")) {
                            Input name = (Input) fElement;
                            name.setValue("" + idDocumentacion);
                        }

                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".tipoPDFCargar")) {
                            Input name = (Input) fElement;
                            name.setValue("" + listaArchivos.get(i).getTipoPDF());
                        }

                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".estampaTiempo")) {
                            Input name = (Input) fElement;
                            name.setValue("" + estampaTiempo);
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".nombreAutor")) {
                            Input name = (Input) fElement;
                            name.setValue("" + autorDoc);
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".autoresSecundarios")) {
                            Input name = (Input) fElement;
                            name.setValue("" + autoresSecundarios);
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".fechaPublicacion")) {
                            Input name = (Input) fElement;
                            name.setValue("" + FechaPublicacion);
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".tipoPublicacion")) {
                            Input name = (Input) fElement;
                            name.setValue("" + documentacion.getIdTipoDocumentacion().getNombre());
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".tituloPonencia")) {
                            Input name = (Input) fElement;
                            name.setValue("" + documentacion.getCongreso().getCongTituloPonencia());
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".nombreEvento")) {
                            Input name = (Input) fElement;
                            name.setValue("" + documentacion.getCongreso().getCongNombreEvento());
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".tipoCongreso")) {
                            Input name = (Input) fElement;
                            name.setValue("" + documentacion.getCongreso().getCongTipoCongreso());
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".DOI")) {
                            Input name = (Input) fElement;
                            name.setValue("" + documentacion.getCongreso().getCongDoi());
                        }
                    }
                    okmWebServ.setPropertyGroupProperties("" + rutaFolderCrear + "/" + listaArchivos.get(i).getNombreArchivo() + ".pdf", "okg:" + tipoDocumentacion + "", fElements);

                }

                if (tipoDocumentacion.equalsIgnoreCase(Utilidades.TIPO_DOCUMENTACION_CAP_LIBRO)) {

                    InputStream is = new FileInputStream("" + listaArchivos.get(i).getRutaArchivo());
                    com.openkm.sdk4j.bean.Document doc = new com.openkm.sdk4j.bean.Document();

                    doc.setPath(rutaFolderCrear + "/" + listaArchivos.get(i).getNombreArchivo() + ".pdf");
                    okmWebServ.createDocument(doc, is);
                    IOUtils.closeQuietly(is);
                    List<FormElement> fElements = okmWebServ.getPropertyGroupProperties("" + rutaFolderCrear + "/" + listaArchivos.get(i).getNombreArchivo() + ".pdf", "okg:" + tipoDocumentacion + "");

                    for (FormElement fElement : fElements) {
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".identPublicacion")) {
                            Input name = (Input) fElement;
                            name.setValue("" + idDocumentacion);
                        }

                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".tipoPDFCargar")) {
                            Input name = (Input) fElement;
                            name.setValue("" + listaArchivos.get(i).getTipoPDF());
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".estampaTiempo")) {
                            Input name = (Input) fElement;
                            name.setValue("" + estampaTiempo);
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".nombreAutor")) {
                            Input name = (Input) fElement;
                            name.setValue("" + autorDoc);
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".autoresSecundarios")) {
                            Input name = (Input) fElement;
                            name.setValue("" + autoresSecundarios);
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".fechaPublicacion")) {
                            Input name = (Input) fElement;
                            name.setValue("" + FechaPublicacion);
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".tipoPublicacion")) {
                            Input name = (Input) fElement;
                            name.setValue("" + documentacion.getIdTipoDocumentacion().getNombre());
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".tituloLibro")) {
                            Input name = (Input) fElement;
                            name.setValue("" + documentacion.getCapituloLibro().getCaplibTituloLibro());
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".tituloCapitulo")) {
                            Input name = (Input) fElement;
                            name.setValue("" + documentacion.getCapituloLibro().getCaplibTituloCapitulo());
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".ISBN")) {
                            Input name = (Input) fElement;
                            name.setValue("" + documentacion.getCapituloLibro().getCaplibIsbn());
                        }
                    }
                    okmWebServ.setPropertyGroupProperties("" + rutaFolderCrear + "/" + listaArchivos.get(i).getNombreArchivo() + ".pdf", "okg:" + tipoDocumentacion + "", fElements);

                }

                if (tipoDocumentacion.equalsIgnoreCase(Utilidades.TIPO_DOCUMENTACION_LIBRO)) {

                    InputStream is = new FileInputStream("" + listaArchivos.get(i).getRutaArchivo());
                    com.openkm.sdk4j.bean.Document doc = new com.openkm.sdk4j.bean.Document();

                    doc.setPath(rutaFolderCrear + "/" + listaArchivos.get(i).getNombreArchivo() + ".pdf");
                    okmWebServ.createDocument(doc, is);
                    IOUtils.closeQuietly(is);
                    List<FormElement> fElements = okmWebServ.getPropertyGroupProperties("" + rutaFolderCrear + "/" + listaArchivos.get(i).getNombreArchivo() + ".pdf", "okg:" + tipoDocumentacion + "");

                    for (FormElement fElement : fElements) {
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".identPublicacion")) {
                            Input name = (Input) fElement;
                            name.setValue("" + idDocumentacion);
                        }

                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".tipoPDFCargar")) {
                            Input name = (Input) fElement;
                            name.setValue("" + listaArchivos.get(i).getTipoPDF());
                        }

                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".estampaTiempo")) {
                            Input name = (Input) fElement;
                            name.setValue("" + estampaTiempo);
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".nombreAutor")) {
                            Input name = (Input) fElement;
                            name.setValue("" + autorDoc);
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".autoresSecundarios")) {
                            Input name = (Input) fElement;
                            name.setValue("" + autoresSecundarios);
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".fechaPublicacion")) {
                            Input name = (Input) fElement;
                            name.setValue("" + FechaPublicacion);
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".tipoPublicacion")) {
                            Input name = (Input) fElement;
                            name.setValue("" + documentacion.getIdTipoDocumentacion().getNombre());
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".tituloLibro")) {
                            Input name = (Input) fElement;
                            name.setValue("" + documentacion.getLibro().getLibTituloLibro());
                        }
                        if (fElement.getName().equals("okp:" + tipoDocumentacion + ".ISBN")) {
                            Input name = (Input) fElement;
                            name.setValue("" + documentacion.getLibro().getLibIsbn());
                        }
                    }
                    okmWebServ.setPropertyGroupProperties("" + rutaFolderCrear + "/" + listaArchivos.get(i).getNombreArchivo() + ".pdf", "okg:" + tipoDocumentacion + "", fElements);

                }

                File fichero = new File(listaArchivos.get(i).getRutaArchivo());
                fichero.delete();

            }

        } catch (AccessDeniedException | AutomationException | DatabaseException | ExtensionException | FileSizeExceededException | ItemExistsException | LockException | NoSuchGroupException | NoSuchPropertyException | ParseException | PathNotFoundException | RepositoryException | UnknowException | UnsupportedMimeTypeException | UserQuotaExceededException | VirusDetectedException | WebserviceException | IOException e) {
            e.printStackTrace();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="tipoArchivoPDF">
    public class ArchivoPDF {

        private String nombreArchivo;
        private String rutaArchivo;
        private String tipoPDF;
        private InputStream archivoIS;

        public ArchivoPDF() {
        }

        public ArchivoPDF(String nombreArchivo, String rutaArchivo, String tipoPDF, InputStream archivoIS) {
            this.nombreArchivo = nombreArchivo;
            this.rutaArchivo = rutaArchivo;
            this.tipoPDF = tipoPDF;
            this.archivoIS = archivoIS;
        }

        public String getNombreArchivo() {
            return nombreArchivo;
        }

        public void setNombreArchivo(String nombreArchivo) {
            this.nombreArchivo = nombreArchivo;
        }

        public String getRutaArchivo() {
            return rutaArchivo;
        }

        public void setRutaArchivo(String rutaArchivo) {
            this.rutaArchivo = rutaArchivo;
        }

        public String getTipoPDF() {
            return tipoPDF;
        }

        public void setTipoPDF(String tipoPDF) {
            this.tipoPDF = tipoPDF;
        }

        public InputStream getArchivo() {
            return archivoIS;
        }

        public void setArchivo(InputStream archivoIS) {
            this.archivoIS = archivoIS;
        }

    }
    //</editor-fold>
}
