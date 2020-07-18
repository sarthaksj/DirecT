package org.openjfx.DirecT.SenderReceivingPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;

import org.openjfx.DirecT.Connection.Connection;
import org.openjfx.DirecT.Controller.ReceiverProgress;
import org.openjfx.DirecT.Controller.SenderProgress;

import javafx.application.Platform;


public class SendingReceivingLogic {
	
	private static int bufferSize=64*1024;
	
	public static long send(String path,Socket socket,long total,long sent,String parent) throws Exception {
		
		//trim path using parent
		String name="";
		if(parent.equals("no parent")) {
			name=path.substring(path.lastIndexOf("\\") + 1);
		}else {
			int i=path.indexOf(parent);
			name=path.substring(i);
		}
		//send the name
		Connection.dos.writeUTF(name);
		
		File f=new File(path);
		long size=f.length();
		String size2=Long.toString(size);
		Connection.dos.writeUTF(size2);
		
		
		System.out.println(name);
       try (// FileInputStream fileOutputStream = new FileInputStream(path);//
		DataInputStream fileInputStream = new DataInputStream(new FileInputStream(path))) {
			byte[] buffer=new byte[bufferSize];
			int bytesRead;
			
			while ( (bytesRead = /*fileOutputStream*/fileInputStream.read(buffer)) !=-1)
			{
				
			    Connection.bos.write(buffer, 0, bytesRead);
			    sent=sent+bytesRead;
			    
			    double percent=(sent*100)/(double)total;
			    
			    ProgressIndicatorHandler.setPorgress(SenderProgress.circularProgress, percent);
			    
			}
		}
        
		
		
		System.out.println("hello");
		
		 while (true) {
	            String str = Connection.dis.readUTF();//read to confirm that the file has been received
	            if (str.equals("file received")) {
	                break;
	            }
	        }
		
		return sent;
	}
	
	public static long receive(String path,Socket socket,long total,long prev) throws Exception {
		
		//path contains the folder path to save files
		
		String fileName=Connection.dis.readUTF();
		path=path+"//"+fileName;//store the fileName
		
		String size2=Connection.dis.readUTF();
		long size=Long.parseLong(size2);
		
		long countSize=0;
		
	
		System.out.println(fileName);
		//BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(path));
		DataOutputStream dos=new DataOutputStream(new FileOutputStream(path));
		
		
		File f=new File(path);
		
		
		byte[] buffer=new byte[bufferSize];
		int byteRead=0;
		
		
        while((byteRead=Connection.is.read(buffer))>0){
          
    
        	double percent=((double)(prev+f.length())*100)/(double)total;
        	
        	ProgressIndicatorHandler.setPorgress(ReceiverProgress.circularProgress, percent);
      
        	/*bos*/dos.write(buffer, 0, byteRead);
        	
        	countSize+=byteRead;
        	if(countSize==size) {
        		break;
        	}
        	
        }
        
        
        //tell sender that file have been received
        System.out.println("Yes");
        Connection.dos.writeUTF("file received");
        
        //bos.close();
        dos.close();
        prev=prev+f.length();
        System.out.println("Yes");
        
        String toAdd=fileName.substring(fileName.lastIndexOf("\\") + 1);//extract name of the file
        Platform.runLater(new Runnable() {
			@Override
			public void run() {
		        ReceiverProgress.fileList2.getItems().addAll(toAdd);



			}
		});
        return prev;
		
	}

}



















