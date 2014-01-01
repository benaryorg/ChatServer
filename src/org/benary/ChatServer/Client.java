/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.benary.ChatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author benaryorg
 */
public class Client extends Thread
{

    private final Socket socket;
    private final BufferedReader reader;
    private final OutputStreamWriter writer;
    protected final String name;

    public Client(Socket socket) throws IOException,Exception
    {
        this.socket=socket;
        reader=new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
        writer=new OutputStreamWriter(socket.getOutputStream());
        this.name=reader.readLine();
        if(name.equalsIgnoreCase("Server"))
        {
            throw new Exception("Wrong Username");
        }
    }

    public void sendMessage(String message,String from)
    {
        try
        {
            this.writer.write('\3'+from+'\1'+message+'\n');
            writer.flush();
        }
        catch(IOException ex)
        {
            System.out.println(String.format("[%11d] ",System.currentTimeMillis()/1000)+socket.getPort()+": Could not send Message!");
        }
    }

    @Override
    public void run()
    {
        try
        {
            String in;
            while((in=reader.readLine())!=null)
            {
                System.out.println(String.format("[%11d] ",System.currentTimeMillis()/1000)+socket.getPort()+": Message got (\""+in+"\")");
                switch(in.charAt(0))
                {
                    case 2:
                        System.out.println(String.format("[%11d] ",System.currentTimeMillis()/1000)+socket.getPort()+": Left!");
                        Server.clientLeave(this,in.substring(1));
                        break;
                    case 3:
                        Server.sendMessage(in,this.name);
                        break;
                }
            }
            System.out.println(String.format("[%11d] ",System.currentTimeMillis()/1000)+socket.getPort()+": Connection closed!");
            socket.close();
        }
        catch(IOException ex)
        {
            if(ex.getMessage().equalsIgnoreCase("Connection reset"))
            {
                System.out.println(String.format("[%11d] ",System.currentTimeMillis()/1000)+socket.getPort()+": Connection closed!");
            }
            else
            {
                System.out.println(ex.getMessage());
            }
        }
    }
}
