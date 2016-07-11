package com.me.webapp.modelo;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 * Clase encargada del envio de correos electronicos.
 */
public class EnvioCorreo {

    /**
     * Metodo estatico que envia un mail, si no hubo errores en el envio retorna true, caso contrario false.
     * @param origen String que indica el correo de quien envia.
     * @param destinatario String que indica el correo a quien será enviado.
     * @param asunto String asunto del correo.
     * @param mensaje String mensaje.
     * @return true si se envio correctamente, sino false.
     */
    public static boolean enviarCorreo(String origen, String destinatario, String asunto, String mensaje) {
        Properties props = new Properties();
        /** parametros de conexion al servidor */
        props.put("mail.transport.protocol", "smtp");

        // hotmail
        //props.put("mail.smtp.host", "smtp.live.com");

        // gmail
        props.put("mail.smtp.host", "smtp.gmail.com");

        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication("prueba.tp.pp@gmail.com", "instituto189");
                    }
                });

        session.setDebug(true);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(origen)); //de

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(destinatario)); // destinatario(s)
            message.setSubject(asunto); //asunto
            message.setText(mensaje);

            Transport.send(message);

            System.out.println("Mensaje enviado.");
            return true;
        } catch (MessagingException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }


    public static void main(String[] args) {
        enviarCorreo("Admin pp", "f.taramasco@hotmail.com.ar", "asunto simple", "123 probando mensaje");
    }
}

