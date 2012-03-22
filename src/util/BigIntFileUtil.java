package util;

import java.io.*;

public class BigIntFileUtil{
	
	public void copyFiles(String srDir, String dtDir) {
		try{
			File f1 = new File(srDir);
			File [] f1list = f1.listFiles();
			File f2 = new File(dtDir);
			
			if(!f1.isDirectory()){
				System.out.println("pre copy check, src isn't a dir:"+f1.getName());
			} else if (!f2.isDirectory()){
				System.out.println("pre copy check, dst isn't a dir:"+f2.getName());
			}
			
			
			for(int i=0; i<f1list.length ;i++){
				InputStream in = new FileInputStream(f1list[i]);
				
				  //For Append the file.
				  //OutputStream out = new FileOutputStream(f2,true);
			
				  //For Overwrite the file.
				String path = "/"+f1list[i].getName();
				OutputStream out = new FileOutputStream(f2+path);
			
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0){
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				
			}
		} catch(FileNotFoundException ex) {
			System.out.println(ex.getMessage() + " in the specified directory.");
			System.exit(0);
		} catch(IOException e) {
			System.out.println(e.getMessage());  
		}
	}
	
	public void deleteFiles(String dir){
		File filedir = new File(dir);
		File [] fileList = filedir.listFiles();
		System.out.print("deleting "+fileList.length+" files, ");
		int i=0;
		for(; i<fileList.length; i++){
			fileList[i].delete();
		}
		System.out.println(i+" files deleted path:"+dir);
		
	}
	
}
