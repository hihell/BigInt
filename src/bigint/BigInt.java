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
	public ArrayList<String> arrayList;
	
	
	public BigInt(String path, int fixedBlockSize){//create big int by read file
		this.path = path;
		this.fixedBlockSize = fixedBlockSize;
		File dir = new File(path);
		String[] fileList = dir.list();
		System.out.println("path:"+this.path+" contains:"+fileList.length);
		if(fileList.length > 1){
			crtBlockData = new BlockData(path, 1);//constructor create the 1st block by reading file, so the number is 1			
			String lastBlock = fileList[fileList.length - 1];
			this.totalSize = this.getBlockEndBoundry(lastBlock) + 1;
		} else {
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
//				System.out.println("local index:"+localIndex+" array lenth:"+crtBlockData.arrayData.length);
		} else {//out of boundary
//			System.out.println("!! size:"+this.totalSize+" globalIndex:"+this.totalSize);
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
			br.write(data.toString());
			br.close();
			crtBlockData.blockNum++;
		} catch (IOException e) {
			e.printStackTrace();	
		}
	}
	
//	public void globalWriter(Integer bit, int globalIndex){
//		int blockNum = this.getBlockNum(globalIndex);
//		int localIndex = this.getLocalIndex(globalIndex);
//		
//		File dir = new File(path);
//		String[] fileList = dir.list();
//		
//		if(fileList.length - 1 < blockNum){//this block is not exist
//			
//			try {
//				FileWriter fstream = new FileWriter(this.path+"/"+blockNum);
//				BufferedWriter br = new BufferedWriter(fstream);
//				br.write(bit.toString());
//				br.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		
//		} else {
//			//block exist
//			try {
//				FileWriter fstream = new FileWriter(this.path+"/tmp"+blockNum);
//				BufferedWriter br = new BufferedWriter(fstream);
//				
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}

}
