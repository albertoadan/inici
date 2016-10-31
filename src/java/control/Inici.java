/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.text.NumberFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author albert.adan
 */
public class Inici extends HttpServlet {

    
    private static String p;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
          //  LogSupport.info("inici del control de memoria del servidor");
            Runtime runtime = Runtime.getRuntime();
            boolean result = false;

            long maxMemory = runtime.maxMemory();
            long allocatedMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            
            int x = (int) (allocatedMemory /1024)/1024;  
            
            
          //     if (x >= 2000) {
           //   if (x >= 400) {
   
            if (x >= 1400) {
         
           
            SendMail("Es reinicia el servidor de produccio");
                reinici(response);
                

            } else {
            //    response.sendRedirect("http://37.222.161.131:8090/eACTALogin/");
               response.sendRedirect("http://31.47.76.83:8080/eACTALogin/");
          
            }
        } catch (Exception e) {
         //   LogSupport.grava(e);
            SendMail("problema accedint a eacta "+ e.toString() );
            try (PrintWriter out = response.getWriter()) {
                /* TODO output your page here. You may use following sample code. */
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet principal</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>error: " + e.toString() + "</h1>");
                out.println("</body>");
                out.println("</html>");
            }
        }

    }
    
    public synchronized void reinici (HttpServletResponse response)  {
        try {
            Control c = new Control();
            boolean result = false;
         //   response.sendRedirect("http://31.47.76.83:8080/eACTALogin/");
           response.sendRedirect("http://37.222.161.131:8090/eACTALogin/");
            result = c.conexionSsh("prova");
            if (result==true ) {
             //   LogSupport.info("Servei wildfly reiniciat");
                SendMail("Servei wildfly produccio reiniciat correctament");
            }
            else {
            //    LogSupport.info("problema reiniciant servei");
                SendMail("Problema reiniciant servei wildfly de produccio");
            }
        } catch (IOException ex) {
            Logger.getLogger(Inici.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
   
     
 
       
        public void SendMail( String destino) {
        //Enviamos a mail arbitro y clubs
        System.out.println("eACTA - ENVIANDO MAIL A :");

    
        String To = "albert.adan@fecapa.cat, carles.altimiras@gmail.com";
        //FLAG 7 Envio del correo al cerrar el acta
        

       
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.fecapa.cat");
      //  props.put("mail.smtp.ssl.trust", "smtp.fecapa.cat");
   //       props.put("mail.smtp.port", "5022");
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.user", "aadan@fecapa.cat");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("aadan@fecapa.cat", "Fecapa2016");
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("aadan@fecapa.cat"));
            message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(To));
            message.setSubject("Servei Wildfly");
            message.setText(destino);
            
            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
