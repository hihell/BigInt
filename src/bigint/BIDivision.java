package bigint;

import java.io.File;
import java.util.ArrayList;

import util.BigIntFileUtil;

public class BIDivision {

	public String tablePath = "";
	
	public Integer result = 0;
	
	public BigInt biResult;
	
	public BigInt preDecRst;
	public BigInt postDecRst;
	
	private BigIntFileUtil fu;
	
	public BigIntFileUtil fileOpt;
	
	public BIDivision(String tablePath, String tempDecreasePath, BigInt biResult){
		this.tablePath = tablePath;
		this.biResult = biResult;
		preDecRst = new BigInt(tempDecreasePath+"/1", biResult.fixedBlockSize);
		postDecRst = new BigInt(tempDecreasePath+"/2", biResult.fixedBlockSize);
		fileOpt = new BigIntFileUtil();
	}
	
	
	public void  divide(BigInt b1, BigInt b2){
		
		//if b2 == 0, foobar
		//if b1 == 0, value = 0, remainder = 0;
		int compareRst = compareAbsValue(b1,b2);
		
		if(compareRst == -1){// b2 > b1
			//remainder = b2, value = 0
		} else if (compareRst == 0){// b2 == b1
			//remainder = 0, value = 1;
		} else if (compareRst == 1){// b1 > b2
			//init
			fu = new BigIntFileUtil();
			fu.copyFiles(b1.path, preDecRst.path);
			preDecRst = new BigInt(preDecRst.path, biResult.fixedBlockSize);
			fileOpt.copyFiles(b2.path, tablePath+"/M1");
			
			
			
			while(compareAbsValue(preDecRst, b2) == 1){
				BIPlus pluser = new BIPlus(postDecRst);

				
				BigRow row = findMaxMatch(preDecRst, b2);
				if(row == null){
					System.out.println("foobar!");
				}
				
				result += (row.getRowNum() *  (int)Math.pow(10, row.getOffset()) );
				System.out.println("tmp result:"+result);
				
				pluser = new BIPlus(postDecRst);
				postDecRst = pluser.plusForDiv(preDecRst, row.getRow(), row.getOffset());
				//postDecRst = new BigInt(postDecRst.path,postDecRst.fixedBlockSize);
				
				swapPrePost();
			}
			
			System.out.println("divide finished, check the remainder in pre folder:"+preDecRst.path);
		}
	}
	
	public BigRow findMaxMatch(BigInt decRst, BigInt b2){//TODO what if the max factor is 1
		Integer offset = decRst.totalSize - b2.totalSize;// TODO what if the offset is negative
		Integer factor;
		ArrayList<String> list;
		Integer tmp;
		Integer bit;
		ParaBuilder pb;
		Integer lastBlockEndBoundary = -1;
		Integer resultSign = 1;//TODO no result sign setting logic in here
		
		
		Integer offsetCounter = offset;
		for(; offsetCounter > offset - 2; offsetCounter-- ){
		
			for(factor = 9; factor > 0; factor--){
				BigInt row = getTableRow(factor); 
				
				if(row == null){		
					//create the row
					list = new ArrayList<String>();
					row = new BigInt(tablePath+"/M"+factor, biResult.fixedBlockSize);
	
					Integer carryBit = 0;
					lastBlockEndBoundary = -1;
					for(int globalIndex = 0; globalIndex < b2.totalSize; globalIndex++){
						tmp = b2.get(globalIndex) * factor + carryBit;
						if(tmp > 9){
							carryBit = tmp / 10;
							bit = tmp % 10;
						} else {
							carryBit = 0;
							bit = tmp;
						}
						list.add(bit.toString());
						
						if(list.size() == row.fixedBlockSize){
							//to disk, table path
							pb = new ParaBuilder();
							String paras = pb.buildPara(globalIndex, lastBlockEndBoundary, resultSign);
							row.blockWriter(paras, list);
							list.clear();
							lastBlockEndBoundary = globalIndex;
						}
						
						if (globalIndex + 1 == b2.totalSize) {
							if(carryBit != 0){
								list.add(carryBit.toString());
								globalIndex++;
							}
							if(list.size() != 0){
								pb = new ParaBuilder();
								String paras = pb.buildPara(globalIndex, lastBlockEndBoundary, resultSign);
								row.blockWriter(paras, list);
							}
						}
					}
					//shit! bi files modified have to update the object
					row = new BigInt(tablePath+"/M"+factor, biResult.fixedBlockSize);
				}//new row created
				
				//compare value and return the value if necessary
				int compareRst = compareAbsValue(decRst, row, offsetCounter);
				
				if(compareRst == 1 || compareRst == 0){
					return new BigRow(row, factor, offsetCounter);
				} else {
					continue;
				}
			}//end inner for
			
		}//end for
		return null;
	}
	
	public BigInt getTableRow(int tableNum){
		File dir = new File(tablePath+"/M"+tableNum);
		String [] list = dir.list();
		
		if(list.length < 2){
			return null;
		} else {
			return new BigInt(tablePath+"/M"+tableNum, biResult.fixedBlockSize);
		}
	}
	
	public int compareAbsValue(BigInt b1, BigInt b2){
		if(b1.totalSize > b2.totalSize){
			return 1;
		} else if(b2.totalSize > b1.totalSize){
			return -1;
		} else {
			for(int i = b1.totalSize - 1; i >= 0; i--){
				if(b1.get(i) > b2.get(i)){
					return 1;
				} else if(b2.get(i) > b1.get(i)){
					return -1;
				}
			}
			return 0;
		}
	}
	
	public int compareAbsValue(BigInt b1, BigInt b2,  int offset){

		if(b1.totalSize > b2.totalSize + offset){
			return 1;
		} else if(b2.totalSize + offset > b1.totalSize){
			return -1;
		} else {
			for(int i = b1.totalSize - 1; i >= 0; i--){
				
				int v1 = b1.get(i);
				int v2;
				int b2Index = i - offset;
				
				if(b2Index < 0){
					v2 = 0;
				} else {
					v2 = b2.get(b2Index);
				}
				
				if(v1 > v2){
					return 1;
				} else if(v2 > v1){
					return -1;
				}
			}
			return 0;
		}
	}
	
	public void swapPrePost(){
		String tmpPath = preDecRst.path;
		fileOpt.deleteFiles(preDecRst.path);
		preDecRst = postDecRst;
		postDecRst = new BigInt(tmpPath, biResult.fixedBlockSize);
		
	}
	
}
