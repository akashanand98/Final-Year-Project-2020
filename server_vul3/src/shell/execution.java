package shell;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.io.*;

public class execution {
	private static Socket socket;
	
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

    	
    	 int port = 5011;
    	 String packName=new String();
         ServerSocket serverSocket = new ServerSocket(port);
         while(true)
         {
        	 socket = serverSocket.accept();
             ObjectInputStream is = new ObjectInputStream(socket.getInputStream());;
      
             packName=(String) is.readObject();
             System.out.println("Data Leakage contents : \n");
             
             executeADB_dataleak(packName);
         }
       
    }
    
    public static void executeADB_dataleak(String pack) throws IOException
    {
    	String message="";
    	ExecuteCommand ec  = new ExecuteCommand();
    	String pid=ec.command("adb shell pidof "+pack);
    	pid = pid.replaceAll("\\n", "");
    	String log_content =  ec.command("cmd /c adb logcat -d > C:\\Users\\akash\\Desktop\\log_capture.txt");
    	
    	Path path = Paths.get("C:\\Users\\akash\\Desktop\\log_capture.txt");
    	String space=" ";
    	String match=pid+space+pid;
    	
    	//System.out.println(match);
    	List stringList = getLinesThatContain(path,match);
    	
    	for (int i = 0; i < stringList.size(); i++) {
			System.out.println(stringList.get(i));
		}
        
        
        ObjectOutputStream os=new ObjectOutputStream(socket.getOutputStream());
        if(stringList!=null)
        {
        	os.writeObject("Log Contents Leaked from App");
        }
        
        os.flush();
      	
    }
    
    
    public static List<String> getLinesThatContain(Path path, String match) {
        List<String> filteredList = null;

        try(Stream<String> stream = Files.lines(path)){
            // Filtering logic here
        	
             filteredList = stream.filter(line -> line.contains(match))
                                  .collect(Collectors.toList());
             System.out.println(filteredList);
        } catch (IOException ioe) {
            // exception handling here
        }
        return filteredList;
    }
    
   
    

}
