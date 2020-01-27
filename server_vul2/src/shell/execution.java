package shell;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.sql.*;
import java.io.*;

public class execution {
	private static Socket socket;
	
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

   
    	 int port = 5010;
    	 String packName=new String();
         ServerSocket serverSocket = new ServerSocket(port);
         while(true)
         {
        	 socket = serverSocket.accept();
             ObjectInputStream is = new ObjectInputStream(socket.getInputStream());;
      
             packName=(String) is.readObject();
             executeADB_encryption(packName);
         }
       
    }
    
    public static void executeADB_encryption(String pack) throws IOException
    {
    	String message="";
    	ExecuteCommand ec  = new ExecuteCommand();
        String nameOfDBFile =  ec.command("adb shell run-as "+pack+" ls /data/data/"+pack+"/databases");
        System.out.println(nameOfDBFile); 
        ec.command("adb pull /data/data/"+pack+"/databases/"+nameOfDBFile+" C:\\Users\\akash\\DBfiles");
        File file = new File("C:\\Users\\akash\\DBfiles");
        File[] files = file.listFiles();
        for(File f: files){
     	   String name=f.toString();
     	   
     	   if(name.toLowerCase().endsWith(".db")){
     		   System.out.println(name); 
     		   
     		   if(isValidSQLite(name))
     		   {
     			   message="This App has unencrypted DataBase File";
     		   }
     		   else
     		   {
     			  message="This App has encrypted DataBase File"; 
     		   }
     			  ObjectOutputStream os=new ObjectOutputStream(socket.getOutputStream());
                  os.writeObject(message);
                  os.flush();
     		   
            } 
            
        }	
    }
    
    public static boolean isValidSQLite(String dbPath) {
        File file = new File(dbPath);

        if (!file.exists() || !file.canRead()) {
            return false;
        }

        try {
            FileReader fr = new FileReader(file);
            char[] buffer = new char[16];

            fr.read(buffer, 0, 16);
            String str = String.valueOf(buffer);
            fr.close();

            return str.equals("SQLite format 3\u0000");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    

}
