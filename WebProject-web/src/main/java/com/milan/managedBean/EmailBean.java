package com.milan.managedBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFileWrapper;

@ManagedBean
@RequestScoped
public class EmailBean {
    private String to;
    private String subject;
    private String message;
    private UploadedFile pdf;

    // ... [keep all your existing getters/setters] 

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UploadedFile getPdf() {
        return pdf;
    }

    public void setPdf(UploadedFile pdf) {
        this.pdf = pdf;
    }
    
    

    public void sendEmail() {
        try {
            // Validate first
            if (to == null || to.isEmpty()) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Recipient is required");
                return;
            }
            
            final String username = "infogothalo@gmail.com";
            final String password = "jpag iydl plsk bjxv";
            
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject(subject);

            Multipart multipart = new MimeMultipart();
            
            // Text part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(message);
            multipart.addBodyPart(textPart);
            
            // PDF attachment part if exists
            
            if (pdf != null && pdf.getSize() > 0) {
    MimeBodyPart pdfPart = new MimeBodyPart();
    pdfPart.setDataHandler(new javax.activation.DataHandler(
        new javax.mail.util.ByteArrayDataSource(pdf.getInputStream(), pdf.getContentType())
    ));
    pdfPart.setFileName(pdf.getFileName());
    multipart.addBodyPart(pdfPart);
}

         
            
            msg.setContent(multipart);
            Transport.send(msg);
            
            addMessage(FacesMessage.SEVERITY_INFO, "Email Sent Successfully!");
            
            // Reset the form
            resetForm();
            
        } catch (MessagingException | IOException e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void addMessage(FacesMessage.Severity severity, String detail) {
        FacesContext.getCurrentInstance()
            .addMessage(null, new FacesMessage(severity, detail, null));
    }
    
    private void resetForm() {
        to = null;
        subject = null;
        message = null;
        pdf = null;
    }
}



//package com.milan.managedBean;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;
//import javax.enterprise.context.RequestScoped;
//import javax.faces.application.FacesMessage;
//import javax.faces.bean.ManagedBean;
//import javax.faces.context.FacesContext;
//import javax.mail.Authenticator;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Multipart;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//import org.primefaces.model.file.UploadedFile;
//
//@ManagedBean
//@RequestScoped
//public class EmailBean {
//    private String to;
//    private String subject;
//    private String message;
//    private UploadedFile pdf;
//
//    // Getters and Setters
//    public String getTo() {
//        return to;
//    }
//
//    public void setTo(String to) {
//        this.to = to;
//    }
//
//    public String getSubject() {
//        return subject;
//    }
//
//    public void setSubject(String subject) {
//        this.subject = subject;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public UploadedFile getPdf() {
//        return pdf;
//    }
//
//    public void setPdf(UploadedFile pdf) {
//        this.pdf = pdf;
//    }
//
//    public void sendEmail() {
//        if (!validateFields()) {
//            return;
//        }
//
//        final String username = "infogothalo@gmail.com";
//        final String password = "jpag iydl plsk bjxv";
//        
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//        
//        Session session = Session.getInstance(props, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(username, password);
//            }
//        });
//
//        try {
//            Message msg = new MimeMessage(session);
//            msg.setFrom(new InternetAddress(username));
//            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
//            msg.setSubject(subject);
//
//            Multipart multipart = new MimeMultipart();
//            
//            // Text part
//            MimeBodyPart textPart = new MimeBodyPart();
//            textPart.setText(message);
//            multipart.addBodyPart(textPart);
//            
//            // PDF attachment part if exists
//            if (pdf != null && pdf.getSize() > 0) {
//                try (InputStream is = pdf.getInputStream()) {
//                    MimeBodyPart pdfPart = new MimeBodyPart();
//                    pdfPart.setContent(is, pdf.getContentType());
//                    pdfPart.setFileName(pdf.getFileName());
//                    multipart.addBodyPart(pdfPart);
//                }
//            }
//            
//            msg.setContent(multipart);
//            Transport.send(msg);
//            
//            addMessage(FacesMessage.SEVERITY_INFO, "Email Sent Successfully!");
//            
//            // Reset form after successful send
//            resetForm();
//            
//        } catch (MessagingException | IOException e) {
//            addMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//    
//    private boolean validateFields() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        
//        if (to == null || to.isEmpty()) {
//            addMessage(FacesMessage.SEVERITY_ERROR, "Recipient email is required");
//            return false;
//        }
//        
//        if (subject == null || subject.isEmpty()) {
//            addMessage(FacesMessage.SEVERITY_ERROR, "Subject is required");
//            return false;
//        }
//        
//        if (message == null || message.isEmpty()) {
//            addMessage(FacesMessage.SEVERITY_ERROR, "Message is required");
//            return false;
//        }
//        
//        return true;
//    }
//    
//    private void addMessage(FacesMessage.Severity severity, String message) {
//        FacesContext.getCurrentInstance().addMessage(null, 
//            new FacesMessage(severity, message, null));
//    }
//    
//    private void resetForm() {
//        to = null;
//        subject = null;
//        message = null;
//        pdf = null;
//    }
//}
//package com.milan.managedBean;
//
//import java.net.PasswordAuthentication;
//import java.util.Properties;
//import javax.enterprise.context.RequestScoped;
//import javax.faces.bean.ManagedBean;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//
//
///**
// *
// * @author dell
// */
//@ManagedBean
//@RequestScoped
//public class EmailBean {
//    private String to;
//    private String subject;
//    private String message;
//
//    public String getTo() {
//        return to;
//    }
//
//    public void setTo(String to) {
//        this.to = to;
//    }
//
//    public String getSubject() {
//        return subject;
//    }
//
//    public void setSubject(String subject) {
//        this.subject = subject;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//    
//    public void sendEmail(){
//        final String username ="infogothalo@gmail.com";
//        final String password = "jpag iydl plsk bjxv";
//        
//       Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");  // TLS
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
////        
////      Session session = Session.getInstance(props, new javax.mail.Authenticator()
////      {
////          protected PasswordAuthentication getPasswordAuthentiation(){
////            return new PasswordAuthentication(username,password );
////                     
////          }
////      }
////      );
//
//Session session = Session.getInstance(props, new javax.mail.Authenticator() {
//    @Override
//    protected PasswordAuthentication getPasswordAuthentication() {
//        return new PasswordAuthentication(username, password);
//    }
//});
//
//      try{
//          Message msg = new MimeMessage(session);
//          msg.setFrom(new InternetAddress(username));
//          msg.setRecipients(Message.RecipientType.TO,
//                InternetAddress.parse(to)
//                  );
//          msg.setSubject(subject);
//          msg.setText(message);
//          
//          Transport.send(msg);
//          
//          System.out.println("Email sent Sucessfully");
//          
//      }
//      catch(MessagingException e){
//          e.printStackTrace();
//      }
//
//    }
//    
//}
