package org.openjfx.DirecT;
import java.io.*; 
  
public class Test 
{ 
    public static void main(String[] args) 
    { 
        // creating a new file instance 
        File file = new File("C:\\Program Files (x86)\\DirecT\\Direct.manifest.MF"); 
          
        // check if file exists 
        boolean exists = file.exists(); 
        if(exists == true) 
        { 
            // changing the file permissions 
            file.setExecutable(true); 
            file.setReadable(true); 
            file.setWritable(true); 
            System.out.println("File permissions changed."); 
  
            // printing the permissions associated with the file currently 
            System.out.println("Executable: " + file.canExecute()); 
            System.out.println("Readable: " + file.canRead()); 
            System.out.println("Writable: "+ file.canWrite()); 
              
        } 
        else
        { 
            System.out.println("File not found."); 
        } 
    } 
} 