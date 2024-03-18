package tnt_times.bteutilmod;

import java.io.*;
import java.util.ArrayList;

public class CommandsFileHandler {
    private static final String fileName = "commandsList.txt";
    public static void saveCommands(ArrayList<String> commands){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
            for (String s:commands) {
                writer.write(s);
                writer.newLine();
            }
            System.out.println("Commands saved!");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<String> loadCommands(){
        ArrayList<String> commandList = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;

            while ((line = reader.readLine()) != null){
                commandList.add(line);
            }
            System.out.println("Commands loaded!");
        }catch (IOException e){
            e.printStackTrace();
        }
        return commandList;
    }
}
