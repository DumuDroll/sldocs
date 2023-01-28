package com.dddd.sldocs.core.general.utils.email;

import com.dddd.sldocs.core.general.Dictionary;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

@Service
@Log4j2
public class Sender {

    public static void Send(String to, String filename) {
        String from = "isppd.semit@gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.user", "username");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "25");
        props.put("mail.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.EnableSSL.enable", "true");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, "xdgrwygtffgepgpy");
                    }
                });
        try {
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Mail Body");
            Multipart multipart = new MimeMultipart();
            DataSource source = new FileDataSource(Dictionary.RESULTS_FOLDER + filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(MimeUtility.encodeWord(filename));
            multipart.addBodyPart(messageBodyPart);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Study load documents");
            message.setText("");
            message.setContent(multipart);
            Transport.send(message);
        } catch (Exception e) {
            log.error(e);
        }
    }
    public static String rfc5987_encode(final String s) {
        final byte[] s_bytes = s.getBytes(StandardCharsets.UTF_8);
        final int len = s_bytes.length;
        final StringBuilder sb = new StringBuilder(len << 1);
        final char[] digits = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        final byte[] attr_char = {'!','#','$','&','+','-','.','0','1','2','3','4','5','6','7','8','9',           'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','^','_','`',                        'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','|', '~'};
        for (final byte b : s_bytes) {
            if (Arrays.binarySearch(attr_char, b) >= 0)
                sb.append((char) b);
            else {
                sb.append('%');
                sb.append(digits[0x0f & (b >>> 4)]);
                sb.append(digits[b & 0x0f]);
            }
        }

        return sb.toString();
    }
}