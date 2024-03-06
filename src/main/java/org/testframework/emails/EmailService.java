package org.testframework.emails;

import com.sun.tools.javac.Main;
import jakarta.mail.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import jakarta.mail.internet.MimeMultipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EmailService {
    // waleko27@gmail.com
    // ximiuibzwqujhfjb

    private static Session session;
    private static final String IMAPS = "imaps";
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public EmailService() {
        // This class allows you to test email receipt
    }

    private String getLocalTime() {
        Calendar fechaActual = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH_mm_ss");
        return formatter.format(fechaActual.getTime());
    }


    /*
        To start new session you need email and application key
        This method is for gmail
     */

    private static Store startSession(String user, String key) {
        Store store = null;
        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", IMAPS);
            properties.put("mail.imaps.host", "imap.gmail.com");
            properties.put("mail.imaps.port", "993");
            properties.put("mail.imaps.starttls.enable", "true");

            session = Session.getDefaultInstance(properties, null);
            store = session.getStore(IMAPS);
            store.connect(properties.getProperty("mail.imaps.host"), user, key);
        } catch (Exception e) {
            logger.error("Error starting a new session ", e);
            // En caso de error, asigna null a la variable store
            store = null;
        }

        return store;
    }

    // End session
    private static void closeSession() {
        try {
            if (session != null && session.getStore(IMAPS).isConnected()) {
                session.getStore(IMAPS).close();
            }
        } catch (Exception e) {
            logger.error("Error closing the session", e);
        }
    }

    /*
        Get message by specific email address
        You need to pass the email address that you expect
     */

    public String getMessageBySpecificAddress(String user, String key, String specificAddress, boolean hasAttachment) {
        String messageContent = "";

        try {
            Store store = startSession(user, key);
            if (store != null) {
                Folder inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_WRITE);

                Message[] messages = inbox.getMessages();

                Message specificMessage = findMessageByAddress(messages, specificAddress);
                if (specificMessage != null) {
                    if (hasAttachment) {
                        downloadAttachment(specificMessage,  getLocalTime());
                    } else {
                        messageContent = getContentMessage(specificMessage);
                    }
                }

            }
        } catch (Exception e) {
            logger.error("Error getting a message", e);
        }

        return messageContent;
    }

    // Get email address

    private Message findMessageByAddress(Message[] messages, String specificAddress) throws MessagingException {
        int startIndex = Math.max(0, messages.length - 5);
        int endIndex = messages.length;

        for (int i = endIndex - 1; i >= startIndex; i--) {
            Address[] fromAddresses = messages[i].getFrom();
            for (Address address : fromAddresses) {
                if (!messages[i].isSet(Flags.Flag.SEEN) && address.toString().equals(specificAddress)) {
                    messages[i].setFlag(Flags.Flag.SEEN, true);
                    return messages[i];
                }
            }
        }

        return null;
    }

    /*
        Get message by subject
        You need to pass the subject, if it has many emails with the same subject it will get the latest
     */

    public String getMessageBySimilarSubject(String user, String key, String specificSubject, boolean hasAttachment) {
        String messageContent = "";

        try {
            Store store = startSession(user, key);
            if (store != null) {
                Folder inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_WRITE);

                Message[] messages = inbox.getMessages();

                if (messages.length > 0) {
                    Message latestMessageWithSubject = findLatestMessageBySubject(messages, specificSubject);
                    if (latestMessageWithSubject != null) {
                        if (hasAttachment) {
                            downloadAttachment(latestMessageWithSubject,  getLocalTime());
                        } else {
                            messageContent = getContentMessage(latestMessageWithSubject);
                        }
                    }
                }

                inbox.close(false);
                closeSession();
            }
        } catch (Exception e) {
            logger.error("Error getting a message", e);
        }

        return messageContent;
    }

    private Message findLatestMessageBySubject(Message[] messages, String specificSubject) throws MessagingException {
        int startIndex = Math.max(0, messages.length - 5);
        int endIndex = messages.length;

        Message latestMessageWithSubject = null;

        for (int i = endIndex - 1; i >= startIndex; i--) {
            String subject = messages[i].getSubject();
            if (!messages[i].isSet(Flags.Flag.SEEN) && subject != null && subject.contains(specificSubject)) {
                latestMessageWithSubject = messages[i];
                messages[i].setFlag(Flags.Flag.SEEN, true);
                break;
            }
        }

        return latestMessageWithSubject;
    }


    /*
        1- Handler Multipar (manage different structures of message)
        2- Get text plain
        3- Return content message
     */

    // 1
    private static String handleMultipart(Message message, Multipart multipart) {
        StringBuilder content = new StringBuilder();
        try {
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                // Verifica si la parte es de tipo multipart/alternative
                if (bodyPart.isMimeType("multipart/alternative")) {
                    // Procesa cada parte de la alternativa
                    content.append(handleMultipart(message, (Multipart) bodyPart.getContent()));
                } else {
                    content.append(getTextFromMimeMultipart(bodyPart));

                }
            }
        } catch (Exception e) {
            logger.error("Error handling Multipar");
        }
        return content.toString();
    }

    // 2
    private static String getTextFromMimeMultipart(BodyPart bodyPart) throws MessagingException, java.io.IOException {
        if (bodyPart.isMimeType("text/plain")) {
            // Si es un tipo de texto, devuelve el contenido directamente
            return (String) bodyPart.getContent();
        } else if (bodyPart.getContent() instanceof MimeMultipart mimeMultipart) {
            // Si es otra parte MimeMultipart, procesa recursivamente
            return handleMultipart(null, mimeMultipart);
        }

        return "";
    }

    // 3
    private String getContentMessage(Message message) {
        String content = null;
        try {
            Object messageContent = message.getContent();

            if (messageContent instanceof Multipart multipart) {
                content = handleMultipart(message, multipart);
            }

            closeSession();
        } catch (Exception e) {
            logger.error("Error getting content of the message", e);
        }
        return content;
    }

    /*
        Handler Attachments
        1. Download
        2. Save
     */

    // 1
    private static void downloadAttachment(Message message, String identifier) throws IOException {
        Properties properties = new Properties();

        try (InputStream fileInputStream = Main.class.getClassLoader().getResourceAsStream("Routes.properties")) {

            if (fileInputStream == null) {
                logger.error("file not found");
                return;
            }

            Object content = message.getContent();
            properties.load(fileInputStream);

            if (content instanceof Multipart multipart) {
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);

                    if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                        String fileName = bodyPart.getFileName();
                        InputStream inputStream = bodyPart.getInputStream();

                        String folderPath = properties.getProperty("ATTACHMENTS_ROUTE") + identifier + "/";

                        saveAttachment(fileName, inputStream, folderPath);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error handling Multipar");
        }
    }

    // 2
    private static void saveAttachment(String fileName, InputStream inputStream, String folderPath) {
        try {
            // Crea la carpeta si no existe
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Utiliza un try-with-resources para cerrar automáticamente el OutputStream
            try (OutputStream outputStream = new FileOutputStream(new File(folderPath + fileName))) {
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            logger.info("Archivo adjunto guardado en: {} {} ", folderPath,  fileName);
        } catch (Exception e) {
            logger.error("Error downloading attachments ", e);
        }
    }

    // Mark as read

    private static void markAsRead(Message message) throws MessagingException {
        // Marca el mensaje como leído
        message.setFlag(Flags.Flag.SEEN, true);
        logger.info("Correo marcado como leído.");
    }
}
