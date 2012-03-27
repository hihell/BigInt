package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import bigint.BigInt;
import bigint.BlockData;
import bigint.ParaBuilder;

public class ModifyBlock {
	String tmpPath;
	
	public void deleteBlock(String path){
		System.out.println("block modified, path:"+path);
		File file = new File(path);
		if(file.isFile()){
			file.delete();
		} else {
			System.out.println("error, path is incorrect:"+path);
		}
	}
	
	public void deleteZerosInBlock(String bigIntPath, int blockNum, int zeroGap, int blockSize){
		int keep = blockSize - zeroGap;
		BlockData modBlock = new BlockData(bigIntPath, blockNum); 
		int [] preArray = modBlock.arrayData;
		ArrayList<String> arrayData = new ArrayList<String>();
		
		int postFrom = modBlock.from;
		int postTo = modBlock.to - zeroGap;
		int postSize = modBlock.size - zeroGap; 
		int postSign = modBlock.sign;
		
		ParaBuilder pb = new ParaBuilder();
		String postPara = pb.buildPara(postFrom, postTo, postSize, postSign);
		
		for(int i = 0; i < keep ; i++){
			Integer bit = new Integer(preArray[i]);
			arrayData.add(bit.toString());
		}
		
		deleteBlock(modBlock.path+"/"+modBlock.blockNum);
		
		modBlock.singleBlockWriter(postPara, arrayData, modBlock.blockNum);
	}
	
	
	public BigInt deleteZeros(BigInt modBI){//block number in here is the last block
		int zeroEnd = modBI.totalSize - 1; 
		int zeroStart = zeroEnd;
		int firstBlockSize = modBI.totalSize % modBI.fixedBlockSize;
		
		
		if(firstBlockSize == 0){
			firstBlockSize = modBI.fixedBlockSize; //TODO in here, what if the modBI is empty
		}
		
		for(int globalIndex = modBI.totalSize - 1; globalIndex >= 0; globalIndex--){
			
			if(modBI.get(globalIndex) != 0){
				zeroEnd = globalIndex;
				break;
			}
		}
		
		while(zeroStart > zeroEnd){
			int gap = zeroStart - zeroEnd;
			int crtBlockNum = modBI.getBlockNum(zeroStart);
			int crtBlockSize;
			if(zeroStart == modBI.totalSize - 1){//first block
				crtBlockSize = firstBlockSize;
			} else {
				crtBlockSize = modBI.fixedBlockSize;
			}
			
			if(gap > crtBlockSize){
				deleteBlock(modBI.path+"/"+crtBlockNum);
				zeroStart = zeroStart - crtBlockSize;
			} else if (gap == crtBlockSize) {
				deleteBlock(modBI.path+"/"+crtBlockNum);
				break;
			} else {
				//TODO delete data
				deleteZerosInBlock(modBI.path, crtBlockNum, gap, crtBlockSize);
				break;
			}
		}
		return new BigInt(modBI.path, modBI.fixedBlockSize);
	}
	
	
	public ModifyBlock(){

	}
	
	
}
