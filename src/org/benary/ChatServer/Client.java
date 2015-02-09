/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 benaryorg (binary@benary.org)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
