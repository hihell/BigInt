package util;

public class FolderCleaner {
	String testFieldPath;
	static String name1 = "BD1";
	static String name2 = "BD2";
	static String result = "RST";
	static String table  = "TABLE";
	static String tempAccumulatingResult = "TempAccumulation";
	
	BigIntFileUtil fu;
	
	public FolderCleaner(String path){
		this.testFieldPath = path;
		fu = new BigIntFileUtil();
	}
	
	public void preCalClean(){
		//delete result;
		fu.deleteFiles(this.testFieldPath+"/"+this.result);
		
		//delete table
		for(int i = 2; i < 10; i++){
			fu.deleteFiles(this.testFieldPath+"/"+this.table+"/M"+i);
		}
		
		//delete temp accumulating files
		fu.deleteFiles(testFieldPath+"/"+this.tempAccumulatingResult+"/1");
		fu.deleteFiles(this.testFieldPath+"/"+this.tempAccumulatingResult+"/2");
		
	}
	
	
}
