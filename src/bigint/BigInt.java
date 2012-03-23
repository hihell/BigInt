package bigint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class BigInt {
	public BlockData crtBlockData;
	public String path;
	public int totalSize;
	public int fixedBlockSize;
	
	
	public BigInt(String path, int fixedBlockSize){//create big int by reading file
		this.path = path;
		this.fixedBlockSize = fixedBlockSize;
		File dir = new File(path);
		String[] fileNames = dir.list();
		ArrayList<String>  fileNameList = new ArrayList<String>();
		
		for(int i=0; i<dir.list().length; i++){
			if(fileNames[i] != ".DS_Store" ){
				fileNameList.add(fileNames[i]);	
			} else {
				System.out.println("haha, ds store found!");
			}
		}
		
		
		if(fileNameList.contains("1")){
			crtBlockData = new BlockData(path, 1);
			
			String lastBlockNum = fileNameList.get(fileNameList.size() - 1);
			
			this.totalSize = this.getBlockEndBoundry(lastBlockNum) + 1;
		} else {
			System.out.print("file not found");
			crtBlockData = new BlockData();
			crtBlockData.size = fixedBlockSize;
			crtBlockData.blockNum = 1;//set 1st file number, create and fill it afterward
			this.path = path;
		}

	}

	
	public int get(int globalIndex){
		if(globalIndex < this.totalSize){
			
			if(getBlockNum(globalIndex) != crtBlockData.blockNum){//change block				
				crtBlockData = new BlockData(path, getBlockNum(globalIndex));
			}
			
			int localIndex = getLocalIndex(globalIndex);
			int a = crtBlockData.arrayData[localIndex];				
				
			return a;
		} else {//out of boundary
			return 0;
		}
	}
	
	public int getBlockNum(int globalIndex){
		int num = globalIndex / this.fixedBlockSize + 1;
		return num;
	}
	
	public int getLocalIndex(int globalIndex){
		int localIndex = globalIndex - this.crtBlockData.from;
		return localIndex;
	}

	public int getBlockEndBoundry(String blockName){
		BufferedReader tmpbr;
		try {
			
			tmpbr = new BufferedReader(new FileReader(this.path+"/"+blockName));
			String stmp = tmpbr.readLine();
			ParaParser ps = new ParaParser();
			ps.parse(stmp);
			int to = ps.to;
//			System.out.println("from:"+ps.from+" to:"+ps.to);
			return to;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	
	public void blockWriter(String paras, ArrayList<String> data){
		try {
			System.out.println("paras are:"+paras);
			FileWriter fstream = new FileWriter(this.path+"/"+crtBlockData.blockNum);
			BufferedWriter br = new BufferedWriter(fstream);
			br.write(paras);
			br.newLine();
			StringBuilder sb = new StringBuilder();
			for(int i=0; i<data.size(); i++){
				sb.append(data.get(i));
			}
			br.write(sb.toString());
			br.close();
			crtBlockData.blockNum++;
		} catch (IOException e) {
			e.printStackTrace();	
		}
	}
}
