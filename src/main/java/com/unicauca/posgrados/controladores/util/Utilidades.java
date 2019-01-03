package com.unicauca.posgrados.controladores.util;

import com.unicauca.posgrados.entidades.Estudiante;
import com.unicauca.posgrados.entidades.Usuario;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Utilidades {

    public static final String TIPO_USUARIO_ADMINISTRADOR = "ADMINISTRADOR",
            TIPO_USUARIO_ESTUDIANTE = "ESTUDIANTE",
            TIPO_USUARIO_PROFESOR = "PROFESOR",
            TIPO_USUARIO_COORDINADOR = "COORDINADOR";

    public static final String TIPO_DOCUMENTACION_REVISTA = "revista",
            TIPO_DOCUMENTACION_LIBRO = "libro",
            TIPO_DOCUMENTACION_CAP_LIBRO = "capitulo_libro",
            TIPO_DOCUMENTACION_CONGRESO = "congreso";

    public static final String TIPO_PDF_DOCUMENTO = "documento",
            TIPO_PDF_TABLA_DE_CONTENIDO = "tabla_de_contenido",
            TIPO_PDF_CARTA_DE_APROBACION = "carta_de_aprobacion";
    public static final String ESTADO_INACTIVO = "INACTIVO",
            ESTADO_ACTIVO = "ACTIVO";
    public static final String ESTADO_VISADO_EN_ESPERA = "EN ESPERA",
            ESTADO_VISADO_APROBADO = "APROBADO",
            ESTADO_VISADO_NO_APROBADO = "NO APROBADO";

    public static void enviarCorreoCreacion(String correo, String usuario, String contrasena) {
        Utilidades.enviarCorreo(correo,
                "Registro en Doctorados de Ciencias de la Elecrónica ",
                "Cordial Saludo \nEl registro en el sistema de Doctorados"
                + " de Ciencias de la Electrónica fue exitoso,para ingresar "
                + "sírvase usar los siguientes datos: \nNombre de Usuario: "
                + usuario + "\n" + "Clave Ingreso: " + contrasena);

    }

    public static void enviarCorreoEdicion(Estudiante actual) {
        Usuario usu = actual.getUsuId();
        if (usu.getEstado().equalsIgnoreCase(ESTADO_ACTIVO)) {
            enviarCorreo("" + usu.getCorreo(),
                    "Sistema de doctorado en ciencias de la electronica - Eliminacion de cuenta de estudiante ",
                    "Cordial Saludo. " + "\n" + "La eliminacion de sus Datos en el sistema de doctorado en ciencias "
                    + "de la electrónica se ha completado correctamente");
        } else {
            enviarCorreo("" + usu.getCorreo(),
                    "Sistema de doctorado en ciencias de la electronica - Edición de Datos en cuenta de estudiante ",
                    "Cordial Saludo. " + "\n" + "\n" + "La edición de datos en el sistema de doctorado "
                    + "en ciencias de la electrónica se ha completado correctamente." + "\n"
                    + "Los detalles de su cuenta son los siguientes: " + "\n" + "\n" + "Datos: "
                    + "\n" + "Codigo: " + actual.getEstCodigo() + "\n" + "Nombres: " + usu.getNombres()
                    + "\n" + "Apellidos: " + usu.getApellidos() + "\n" + "Correo Institucional: "
                    + usu.getCorreo() + "\n" + "Cohorte: " + actual.getEstCohorte() + "\n"
                    + "Nombre del Tutor: " + actual.getEstTutor() + "\n" + "Semestre: "
                    + actual.getEstSemestre() + "\n" + "Estado: " + usu.getEstado() + "\n" + "\n"
                    + "Datos para iniciar sesión: " + "\n" + "Usuario: " + usu.getCorreo() + "\n"
                    + "Contrasenia: " + actual.getEstCodigo());

        }

    }

    public static boolean enviarCorreo(String destinatario, String asunto, String mensaje) {
        // String de = "elcorreodelprofe@gmail.com";
        // String clave = "lacontraseñadelcorreodelprofe";
        final String de = "posgradoselectunic@gmail.com";
        final String clave = "posgrados22";
        String para = destinatario;

        boolean resultado = false;

        try {
            Properties prop = new Properties();
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", 587);

            Session session = Session.getInstance(prop,
                    new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(de, clave);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(de));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(para));
            message.setSubject(asunto);
            message.setText(mensaje);
            Transport.send(message);
            resultado = true;
            System.out.println("========== CORREO ENVIADO CON ÉXITO ============");
        } catch (Exception e) {
            System.out.println("========== ERROR AL ENVIAR CORREO ============ " + e.getMessage());
        }

        return resultado;
    }

    public static String sha256(String cadena) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(cadena.getBytes());

            byte[] mb = md.digest();
            for (int i = 0; i < mb.length; i++) {
                sb.append(Integer.toString((mb[i] & 0xff) + 0x100, 16).substring(1));
            }

        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error-Utilidades -- " + ex.getMessage());
        }
        return sb.toString();
    }

    public static int[] getListaAnios() {
        Calendar cal = Calendar.getInstance();
        int anioInicio = 1999;
        int anioActual = cal.get(Calendar.YEAR);
        int[] vectorA = new int[anioActual - anioInicio];
        for (int i = 0; i < vectorA.length; i++) {
            vectorA[i] = anioActual--;
        }
        return vectorA;
    }

}
