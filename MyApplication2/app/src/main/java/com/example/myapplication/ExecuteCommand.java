package com.example.myapplication;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecuteCommand {





    public String command(String cmd)
    {
        StringBuilder output = new StringBuilder();
        try {
            //Process process1 = Runtime.getRuntime().exec("adb root");

//            ProcessBuilder processBuilder = new ProcessBuilder();
//
//            // Run this on Windows, cmd, /c = terminate after this run
//            processBuilder.command("cmd.exe",cmd );



            Process process = Runtime.getRuntime().exec(cmd);
            if (process != null) {
                process.waitFor();
            }
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }


        }

        catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

}