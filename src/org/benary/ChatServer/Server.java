/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.benary.ChatServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author benaryorg
 */
public class Server extends Thread
{

    protected ServerSocket server;

    private Server(ServerSocket server)
    {
        this.server=server;
    }

    @Override
    public void run()
    {
        System.out.println(String.format("[%11d] ",System.currentTimeMillis()/1000)+"Server started!");
        Socket socket=null;
        try
        {
            System.out.println(String.format("[%11d] ",System.currentTimeMillis()/1000)+"Listening on port "+server.getLocalPort());
            while((socket=server.accept())!=null)
            {
                System.out.println(String.format("[%11d] ",System.currentTimeMillis()/1000)+socket.getPort()+": Connected from \""+socket.getInetAddress().getHostName()+"\"");
                new Client(socket).start();
            }
        }
        catch(IOException ex1)
        {
            if(socket!=null)
            {
                try
                {
                    socket.close();
                }
                catch(IOException ex2)
                {
                }
            }
            System.out.println(ex1.getMessage());
            System.exit(1);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            ServerSocket server=new ServerSocket(1337);
            System.out.println(String.format("[%11d] ",System.currentTimeMillis()/1000)+"New ServerSocket created!");
            new Server(server).start();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

}
