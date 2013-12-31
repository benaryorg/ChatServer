/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.benary.echoserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *
 * @author benaryorg
 */
public class ConnectionHandler extends Thread
{

    protected Socket client;

    public ConnectionHandler(Socket client)
    {
        this.client=client;
    }

    @Override
    public void run()
    {
        try
        {
            BufferedReader reader=new BufferedReader(new java.io.InputStreamReader(client.getInputStream()));
            OutputStreamWriter writer=new OutputStreamWriter(client.getOutputStream());
            String in;
            while((in=reader.readLine())!=null)
            {
                System.out.println(String.format("[%11d] ",System.currentTimeMillis()/1000)+client.getPort()+": Message got (\""+in+"\")");
                String out=in;
                writer.write(out+'\n');
                writer.flush();
                System.out.println(String.format("[%11d] ",System.currentTimeMillis()/1000)+client.getPort()+": Answered (\""+out+"\")");
            }
            System.out.println(String.format("[%11d] ",System.currentTimeMillis()/1000)+client.getPort()+": Connection closed!");
            client.close();
        }
        catch(IOException ex)
        {
            if(ex.getMessage().equalsIgnoreCase("Connection reset"))
            {
                System.out.println(String.format("[%11d] ",System.currentTimeMillis()/1000)+client.getPort()+": Connection closed!");
            }
            else
            {
                System.out.println(ex.getMessage());
            }
        }
    }
}
