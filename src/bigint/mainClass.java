package bigint;

import java.io.File;

import util.BigIntFileUtil;
import util.FolderCleaner;


public class mainClass {
	public static void main(String args[]){
		System.out.println("test");
		
		String base = "/Users/jiusi/Documents/workspace/testfield/";
		
		String path1 = base+"BD1";
		String path2 = base+"BD2";
		String resultPath = base+"RST";
		String tablePath = base+"TABLE";
		String tempaccumulation = base+"TempAccumulation";
		String tempdecrease = base+"TempDecrease";
		
		BigInt b1 = new BigInt(path1, 4);
		BigInt b2 = new BigInt(path2, 4);
		
		BigInt rst = new BigInt(resultPath, 4);
	
		FolderCleaner fc = new FolderCleaner(base);
		fc.preCalClean();

//		BIPlus operator = new BIPlus(rst);
//		rst = operator.plusForDiv(b1, b2, 0);
		
//		System.out.println("finished");
		
//		BIMulti opr = new BIMulti(tablePath, tempaccumulation, rst);
//		opr.multiply(b1, b2);

		BIDivision div = new BIDivision(tablePath, tempdecrease, rst);
		
//		div.compareAbsValue(b1, b2);
		
		div.divide(b2, b1);
		
		
	}
	
	
}