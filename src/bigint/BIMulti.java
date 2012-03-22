package bigint;

import java.io.File;
import java.util.ArrayList;

import util.BigIntFileUtil;

public class BIMulti {
	BigInt b1;
	BigInt b2;
	
	BigInt biResult;

	BigInt preTmpAccRst;
	BigInt postTmpAccRst;
	
	Integer globalIndexB1 = 0;
	Integer globalIndexB2 = 0;
	Integer offset = 0;
	Integer resultSign;
	Integer lastBlockEndBoundary = -1;
	
	BIPlus pluser;
	
	String tablePath;
	String tempAccumulatingResultPath;
	
	
	ParaBuilder pb;
	BigIntFileUtil fileOpt;
	
	public BIMulti(String tablePath, String tempResultPath, BigInt biResult){
		this.biResult = biResult;
		this.tablePath = tablePath;
		this.tempAccumulatingResultPath = tempResultPath;

		preTmpAccRst = new BigInt(tempAccumulatingResultPath+"/1", biResult.fixedBlockSize);
		postTmpAccRst = new BigInt(tempAccumulatingResultPath+"/2", biResult.fixedBlockSize);
		fileOpt = new BigIntFileUtil();
		
		for(int i=0; i<8; i++){
			if(getTableSize(i+2) > 0){
				System.out.println("Pre calculate check FAIL, table contains file. table:"+(i+2));
			}
		}
	}
	
	public int getTableSize(int tableNum){
		File dir = new File(tablePath+"/M"+tableNum);
		String [] list = dir.list();
		return list.length - 1;
	}
	
	
	public BigInt getTableRow(int tableNum){
		File dir = new File(tablePath+"/M"+tableNum);
		String [] list = dir.list();
		
		if(list.length < 2){
			System.out.println("not hit, tableNum:"+tableNum);
			return null;
		} else {
			System.out.println("hit, tableNum:"+tableNum);
			return new BigInt(tablePath+"/M"+tableNum, biResult.fixedBlockSize);
		}
	}
	
	
	public void multiply(BigInt b1, BigInt b2){
		ArrayList<String> list = new ArrayList<String>();
		Integer tmp = 0;
		Integer carryBit = 0;
		Integer bit = 0;
		resultSign = b1.crtBlockData.sign * b2.crtBlockData.sign;
		
		
		for(; globalIndexB1 < b1.totalSize; globalIndexB1++){
			Integer b1Factor = b1.get(globalIndexB1);
			BigInt bi = getTableRow(b1Factor);
			
			if(bi == null){//calculate
				bi = new BigInt(tablePath+"/M"+b1Factor, biResult.fixedBlockSize);

				for(globalIndexB2 = 0; globalIndexB2 < b2.totalSize; globalIndexB2++){
					tmp = b2.get(globalIndexB2) * b1Factor + carryBit;
					if(tmp > 9){
						carryBit = tmp / 10;
						bit = tmp % 10;
					} else {
						carryBit = 0;
						bit = tmp;
					}
					list.add(bit.toString());
					
					if(list.size() == bi.fixedBlockSize){
						//to disk, table path
						pb = new ParaBuilder();
						String paras = pb.buildPara(globalIndexB2, lastBlockEndBoundary, resultSign);
						bi.blockWriter(paras, list);
						list.clear();
						this.lastBlockEndBoundary = globalIndexB2;
					}
					
				}//end for
				
				if(carryBit != 0){
					list.add(carryBit.toString());
					globalIndexB2++;
					carryBit = 0;
				}
				
				if(list.size() != 0){
					globalIndexB2--;
					pb = new ParaBuilder();
					String paras = pb.buildPara(globalIndexB2, lastBlockEndBoundary, resultSign);
					bi.blockWriter(paras, list);
					list.clear();
					this.lastBlockEndBoundary = globalIndexB2;
				}
				//shit! bi files modified have to update the object
				bi = new BigInt(tablePath+"/M"+b1Factor, biResult.fixedBlockSize);
			}//the missing line in table was created
			
			if(globalIndexB1 == 0){//initial accumulating result
				//TODO copy bi(in the table) to postTmpAccRst path
				fileOpt.copyFiles(bi.path, postTmpAccRst.path);
				
				//shit! files added, have to update the object
				postTmpAccRst = new BigInt(this.tempAccumulatingResultPath+"/2", biResult.fixedBlockSize);
				
			} else {
				//pre + bi -> post, then pre and post will exchange
				pluser = new BIPlus(postTmpAccRst);
				pluser.plus(preTmpAccRst, bi, offset);
				//shit! files under post path updated, have to update the object
				postTmpAccRst = new BigInt(postTmpAccRst.path, postTmpAccRst.fixedBlockSize);
			}
			
			offset++;
			swapPrePost();
			lastBlockEndBoundary = -1;
		}
		
		biResult = preTmpAccRst;
	}
	
	public void swapPrePost(){
		String tmpPath = preTmpAccRst.path;
		//TODO pre remove everything 
		fileOpt.deleteFiles(preTmpAccRst.path);
		preTmpAccRst = postTmpAccRst;
		postTmpAccRst = new BigInt(tmpPath, biResult.fixedBlockSize);
		
	}
	
	
}
