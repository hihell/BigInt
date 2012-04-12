package bigint;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import util.BigIntFileUtil;

public class BIDivision {

	public String tablePath = "";
	
	public Integer result = 0;
	public ArrayList<String> resultToDisk;
	
	public BigInt biResult;
	public BigInt preDecRst;
	public BigInt postDecRst;
	
	private BigIntFileUtil fileOpt;
	private Integer resultSign;
	
	public BIDivision(String tablePath, String tempDecreasePath, BigInt biResult){
		this.tablePath = tablePath;
		this.biResult = biResult;
		preDecRst = new BigInt(tempDecreasePath+"/1", biResult.fixedBlockSize);
		postDecRst = new BigInt(tempDecreasePath+"/2", biResult.fixedBlockSize);
		fileOpt = new BigIntFileUtil();
	}
	
	
	public void divide(BigInt b1, BigInt b2){
		this.resultSign = b1.crtBlockData.sign * b2.crtBlockData.sign;
		this.resultToDisk = new ArrayList<String>();
		//if b2 == 0, foobar
		//if b1 == 0, value = 0, remainder = 0;
		BIComparer compare = new BIComparer();
		int compareRst = compare.compareABSValue(b1,b2);
		
		if(compareRst == -1){// b2 > b1
			//remainder = b2, value = 0
			System.out.println("divide finished, remainder located:"+b2.path+" division result is 0");
		} else if (compareRst == 0){// b2 == b1
			//remainder = 0, value = 1;
			System.out.println("divide finished, remainder is 0, division result is 1");
		} else if (compareRst == 1){// b1 > b2
			//init
			fileOpt = new BigIntFileUtil();
			fileOpt.copyFiles(b1.path, preDecRst.path);
			preDecRst = new BigInt(preDecRst.path, biResult.fixedBlockSize);
			
			//set up table
			fileOpt.copyFiles(b2.path, tablePath+"/M1");
			calculateTableRows(b2);
			
			
			//TODO what if the first block is the last block
			//init loop, do the first calculate and setup parameters
			BigRow row = findMaxMatch(preDecRst, b2);
			BIPlus pluser = new BIPlus(postDecRst);
			postDecRst = pluser.plusForDiv(preDecRst, row.getRow(), row.getOffset());
			swapPrePost();
			Integer lastOffset = row.offset;
			Integer resultLength = row.offset + 1;
			Integer fixedBlockSize = biResult.fixedBlockSize;
			Integer firstBlockSize = resultLength % fixedBlockSize;
			Integer totalBlocks = resultLength / fixedBlockSize;
			
			if(firstBlockSize == 0){
				firstBlockSize = fixedBlockSize;
			} else {
				totalBlocks++;
			}
			biResult.crtBlockData.blockNum = totalBlocks;
			
			Result2Disk rst2disk = new Result2Disk(firstBlockSize);
			rst2disk.append(0, row.getRowNum());
			
			while(compare.compareABSValue(preDecRst, b2) == 1){//TODO condition should be ==1 || ==0
				pluser = new BIPlus(postDecRst);
				
				row = findMaxMatch(preDecRst, b2);
				if(row == null){
					System.out.println("foobar!");
				}
				
				int offsetGap = lastOffset - row.getOffset() - 1;
				rst2disk.append(offsetGap, row.getRowNum());
				lastOffset = row.getOffset();
				
				pluser = new BIPlus(postDecRst);//TODO integrate the swap function into big integer plus
				postDecRst = pluser.plusForDiv(preDecRst, row.getRow(), row.getOffset());
				
				swapPrePost();
			}
			
			
			
			System.out.println("divide finished, remainder located:"+preDecRst.path);
		}
	}
	
	private class Result2Disk{
		private ArrayList<String> list;
		private boolean firstBlockCreated = false;
		private Integer firstBlockSize;
		private Integer fixedBlockSize = biResult.fixedBlockSize;
		private int crtBlockSize;
		
		public void append(int zeroGap, Integer digit){
				
			int zeroCounter = zeroGap;
			while(zeroCounter > 0){
				list.add("0");
				zeroCounter--;
			
				if(list.size() == crtBlockSize){
					StringBuilder para = new StringBuilder();
					//to disk
					biResult.reverseBlockWriter(para.toString(), list);
					list.clear();
					if(!firstBlockCreated){
						firstBlockCreated = true;
						crtBlockSize = fixedBlockSize;
					}
				}
			}
			
			list.add(digit.toString());
			if(list.size() == crtBlockSize){
				StringBuilder para = new StringBuilder();
				//to disk
				biResult.reverseBlockWriter(para.toString(), list);
				list.clear();
				if(!firstBlockCreated){
					firstBlockCreated = true;
					crtBlockSize = fixedBlockSize;
				}
			}
		}
		
		public Result2Disk(Integer firstBlockSize){
			list = new ArrayList<String>();
			this.firstBlockSize = firstBlockSize;
			this.crtBlockSize = firstBlockSize;
		}
		
	}
	
	
	
	//note! before call this function, make sure decRst is larger than b2
	public BigRow findMaxMatch(BigInt decRst, BigInt b2){//TODO what if the max factor is 1
		Integer offset = decRst.totalSize - b2.totalSize;// TODO what if the offset is negative
		Integer factor;
		
		Integer offsetCounter = offset;
		for(; offsetCounter > offset - 2; offsetCounter-- ){
		
			for(factor = 9; factor > 0; factor--){
				BigInt row = getTableRow(factor); 
				
				BIComparer compare = new BIComparer();
				int compareRst = compare.compareABSValue(decRst, row, offsetCounter);
				
				if(compareRst == 1 || compareRst == 0){
					return new BigRow(row, factor, offsetCounter);
				}
			}	
			if(offset < 0){
				System.out.println("foobar! the offset is negative:"+offset);
			}
		}
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
	
	private void swapPrePost(){
		String tmpPath = preDecRst.path;
		fileOpt.deleteFiles(preDecRst.path);
		preDecRst = postDecRst;
		postDecRst = new BigInt(tmpPath, biResult.fixedBlockSize);
		
	}
	
	private void calculateTableRows(BigInt divisor){

		ParaBuilder pb = new ParaBuilder();
		Integer bit;
		
		for(int factor = 2; factor <= 9; factor++){
		
			ArrayList<String> list = new ArrayList<String>();
			BigInt row = new BigInt(tablePath+"/M"+factor, biResult.fixedBlockSize);
	
			Integer carryBit = 0;
			int lastBlockEndBoundary = -1;
			
			for(int globalIndex = 0; globalIndex < divisor.totalSize; globalIndex++){
				Integer tmp = divisor.get(globalIndex) * factor + carryBit;
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
				
				if (globalIndex + 1 == divisor.totalSize) {
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
		}
	}
}
