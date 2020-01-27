package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;


 
public class Server
{
 
    private static Socket socket;
   
static  int spflag=0;
    public static void main(String[] args)
    {
    String packName=new String();
        try
        {
       
            int port = 5010;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server Started and listening to the port 5010");
           
           
           
           
            while(true)
            {
                //Reading the message from the client
                socket = serverSocket.accept();
                ObjectInputStream is = new ObjectInputStream(socket.getInputStream());;
         
                packName=(String) is.readObject();
               
                System.out.println("package received from Android is "+packName);
               
                executeADB(packName);
                if(spflag==1)
                {
                String messageS="This App is Vulnerable due to exposed Shared Preferences File";
                ObjectOutputStream os=new ObjectOutputStream(socket.getOutputStream());
                os.writeObject(messageS);
                os.flush();
                spflag=0;
                    //os.close();
                }
                else
                {
                String messageF="This App is Not Vulnerable due to any exposed Shared Preferences File";
                ObjectOutputStream os=new ObjectOutputStream(socket.getOutputStream());
                os.writeObject(messageF);
                os.flush();
                }
            }
           
           
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
       
       
    }
   
    public static void executeADB(String pack)
    {
      ExecuteCommand ec  = new ExecuteCommand();
           
        String nameOfSharedPrefFile =  ec.command("adb shell run-as " + pack +" ls /data/data/" + pack +"/shared_prefs ");
        System.out.println(nameOfSharedPrefFile);  
        System.out.println("Name of SP File is "+nameOfSharedPrefFile);
        if(!nameOfSharedPrefFile.contentEquals(""))
        {
         String fileContent =  ec.command("adb shell run-as "+pack+" cat /data/data/" + pack +"/shared_prefs/"+nameOfSharedPrefFile);
       System.out.println("The file contents are "+fileContent);
       if(!fileContent.equals("") && !nameOfSharedPrefFile.equals(""))
       {
        spflag=1;
       }
        }
       if(nameOfSharedPrefFile.trim().equals("ls: /data/data/"+pack+"/shared_prefs: No such file or directory"  ))
        {
        spflag=0;
        }
       
    }
}