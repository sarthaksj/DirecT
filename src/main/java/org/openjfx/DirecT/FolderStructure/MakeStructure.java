package org.openjfx.DirecT.FolderStructure;

import java.io.File;
import java.util.ArrayList;

import org.openjfx.DirecT.Controller.FileSelection;

public class MakeStructure {
	
	 static String parent="Pics";
	 
	static void RecursivePrint(File[] arr,int index,int level)  
     { 
        
         if(index == arr.length) 
             return; 
           
         
         if(arr[index].isFile()) {
        	 String path=arr[index].getAbsolutePath();
        	
       
        	FileSelection.fileChecker.add(arr[index].getAbsolutePath());
				
			FileSelection.fileList.add(arr[index]);
        	 
        	try {
        			Object ob=FileSelection.fileListWithParent.get(parent);
        			FileSelection.fileChecker.add(arr[index].getAbsolutePath());
        			if(ob==null) {
        				ArrayList<String> ar=new ArrayList<>();
        				ar.add(arr[index].getAbsolutePath());
        				FileSelection.fileListWithParent.put(parent, ar);
        				
        			}
        			else {
        				
        				ArrayList<String> ar=(ArrayList<String>)ob;
        				ar.add(path);
        				FileSelection.fileListWithParent.put(parent, ar);
        			}
        			
        	}catch(Exception e) {
        		e.printStackTrace();
        	} 
         }
            
           
         // for sub-directories 
         else if(arr[index].isDirectory()) 
         { 
         
        	 String path=arr[index].getAbsolutePath();
         	
        	 int i=path.indexOf(parent);
        	 FileSelection.dirList.add(path.substring(i));
               
             // recursion for sub-directories 
             RecursivePrint(arr[index].listFiles(), 0, level + 1); 
         } 
            
         // recursion for main directory 
         RecursivePrint(arr,++index, level); 
    } 
	 
	public static void senderDirHandler(String path,String p) {
		
		parent=p;
		File maindir = new File(path); 
		File arr[] = maindir.listFiles(); 
		RecursivePrint(arr,0,0);  
		
	}
	
}
