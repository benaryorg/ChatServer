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

/**
 *
 * @author benaryorg
 */
public class Client extends Thread
{

    protected Socket client;
    private final BufferedReader reader;
    private final OutputStreamWriter writer;

    public Client(Socket client) throws IOException
    {
        this.client=client;
        reader=new BufferedReader(new java.io.InputStreamReader(client.getInputStream()));
        writer=new OutputStreamWriter(client.getOutputStream());
    }

    @Override
    public void run()
    {
        try
        {

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
