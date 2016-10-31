/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 *
 * @author albert.adan
 */
public class Control {

    public boolean conexionSsh(String comanda) throws IOException {
        boolean success = false;

        String username = "root";
     //   String host = "37.222.161.131";
        String host = "31.47.76.83";
        String pass = "y2Vx8ia8j6";

        JSch jsch = null;
        Session session = null;
        Channel channel = null;
        //  ChannelSftp c = null;
        try {
            boolean cond = false;
            jsch = new JSch();
            session = jsch.getSession(username, host, 5022);

            UserInfo ui = new SUserInfo(pass, null);
            session.setUserInfo(ui);
            session.setPassword(pass);

            session.connect();

            //  Channel channelex = session.openChannel("exec");
            ChannelExec channelex = (ChannelExec) session.openChannel("exec");
            BufferedReader in = new BufferedReader(new InputStreamReader(channelex.getInputStream()));

            DataOutputStream out = new DataOutputStream(channelex.getOutputStream());
//               channelex.setCommand("service wildfly status");           
//            channelex.connect();
//            String msg2 = null;
//            while ((msg2 = in.readLine()) != null) {
//                if (msg2.contains("running")) {
//                     cond= true;
//                }else  {
//                    cond= false;
//                }               
//                System.out.println(msg2);                 
//            } 

            channelex.setCommand("service wildfly restart");
            channelex.connect();

            String msg = null;
            while ((msg = in.readLine()) != null) {
                if (msg.contains("Starting")) {
                    success = true;
                } else if (msg.contains("hasn´t started")) {
                    success = false;
                }
                System.out.println(msg);
            }

            channelex.disconnect();
        } catch (Exception e) {
            Inici in = new Inici();
            in.SendMail("problema reiniciant servei en la conexio ssh");
            System.out.println(e.toString());
        }
        session.disconnect();
        return success;

    }

}
