package bigint;

import java.util.ArrayList;

import util.ModifyBlock;

public class BIPlus {
	BigInt biResult;
	
	Integer carryBit = 0;
	int itmp = 0;
	Integer bit = 0;
	int globalIndex = 0;
	ArrayList<String> list = new ArrayList<String>();
	
	int isLarger;
	BigInt biLarger;
	BigInt biSmaller;
	int operator = 1;
	StringBuilder paras;
	int resultSign = 1;
	int lastBlockEndBoundary = -1;
	
	ParaBuilder pb;
	
	
	public BIPlus(BigInt Rst){
		this.biResult = Rst;
	}
	
	public void plus(BigInt b1, BigInt b2){
		biLarger = b1;
		biSmaller = b2;
		
		if(b1.crtBlockData.sign != b2.crtBlockData.sign) {//necessary to set the larger and smaller	
			isLarger = compareAbsValue(b1,b2);
			operator = -1;
			if(isLarger == 0){
				System.out.print("the result is 0");
				System.exit(0);
				//finished, the result is 0
			} else if (isLarger == 1){
				biLarger = b1;
				biSmaller = b2;
				resultSign = b1.crtBlockData.sign;
			} else if (isLarger == -1){
				biLarger = b2;
				biSmaller = b1;
				resultSign = b2.crtBlockData.sign;
			}
		} else if(b1.crtBlockData.sign == -1) {
			resultSign = -1;
		}
			
		
			
		for(; globalIndex < biLarger.totalSize; globalIndex++){
			itmp = biLarger.get(globalIndex) + operator * biSmaller.get(globalIndex) + carryBit;
			if(itmp > 9){
				bit = itmp % 10;
				carryBit = (itmp - bit) / 10;
			} else if(itmp < 10 && itmp > -1){
				bit = itmp;
				carryBit =  0;
			} else {
				bit = 10 + itmp; 
				carryBit = -1;
			}
			list.add(bit.toString());
			
			if(list.size() == biResult.crtBlockData.size){
				pb = new ParaBuilder();
				String p = pb.buildPara(globalIndex, lastBlockEndBoundary, resultSign);
				biResult.blockWriter(p, list);
				list.clear();
				lastBlockEndBoundary = globalIndex;
			} 
			if(globalIndex + 1 == biLarger.totalSize) {
				
				if(carryBit!=0){
					list.add(carryBit.toString());
					globalIndex++;
				}
				if(list.size()!=0){
					pb = new ParaBuilder();
					String p = pb.buildPara(globalIndex, lastBlockEndBoundary, resultSign);
					biResult.blockWriter(p, list);
				}
			}
		}
		
	}
	
	public void plus(BigInt b1, BigInt b2, int offset){//only process same sign value
		Integer v1, v2;
		Integer offsetCounter = offset;
		this.resultSign = b1.crtBlockData.sign;
		
		for(globalIndex = 0; globalIndex < Math.max(b1.totalSize, b2.totalSize+offset); globalIndex++){
		
			if(offsetCounter > 0){
				v2 = 0;
				offsetCounter--;
			} else {
				v2 = b2.get(globalIndex - offset);
			}
			v1 = b1.get(globalIndex);
			
			itmp = v1 + v2 + carryBit;
			if(itmp > 9){
				carryBit = itmp/10;
				bit = itmp%10;
			} else {
				carryBit = 0;
				bit = itmp;
			}
			list.add(bit.toString());
			
			if(list.size() == biResult.crtBlockData.size){
				pb = new ParaBuilder();
				String p = pb.buildPara(globalIndex, lastBlockEndBoundary, resultSign);
				biResult.blockWriter(p, list);
				list.clear();
				lastBlockEndBoundary = globalIndex;
			} 
			if( globalIndex + 1 == Math.max(b1.totalSize, b2.totalSize+offset)){
				if(carryBit != 0){
					list.add(carryBit.toString());
					globalIndex++;
				}
				if(list.size() != 0){
					pb = new ParaBuilder();
					String p = pb.buildPara(globalIndex, lastBlockEndBoundary, resultSign);
					biResult.blockWriter(p, list);
				}
			}
		}
	}
	
	public BigInt plusForDiv(BigInt b1, BigInt b2, int offset){//in here b1 must larger than b2, so the resultSign is 1 (positive)
		biLarger = b1;
		biSmaller = b2;
		resultSign = 1;
		Integer v1,v2;
//		Integer firstBlockSize = biResult.fixedBlockSize;
//		boolean startWrite = false;
//		boolean firstBlockCreated = false;
//		boolean needDeleteZeros = false;
		
		for(globalIndex = 0; globalIndex < biLarger.totalSize; globalIndex++){
			
			v1 = biLarger.get(globalIndex);
			
			int v2Index = globalIndex - offset;
			if(v2Index >= 0){
				v2 = biSmaller.get(v2Index);
			} else {
				v2 = 0;
			}
			
			itmp = v1 - v2 + carryBit;
			if(itmp > 9){//TODO this branch can be deleted. because is a sub operation, the carry will always less than 1
				bit = itmp % 10;
				carryBit = (itmp - bit) / 10;
			} else if(itmp < 10 && itmp > -1){
				bit = itmp;
				carryBit =  0;
			} else {
				bit = 10 + itmp; 
				carryBit = -1;
			}
			list.add(bit.toString());
			
			
//			if(!startWrite && bit !=0){
//				startWrite = true;
//				firstBlockSize = (globalIndex + 1) % biResult.fixedBlockSize;						
//			}
//			
//			if(startWrite){
//				list.add(bit.toString());
//			}
//			
			
//			if(!firstBlockCreated && list.size() == firstBlockSize){//create the first block
//				pb = new ParaBuilder();
//				String p = pb.buildPara(globalIndex, globalIndex-list.size(), resultSign);
//				biResult.blockWriter(p, list);
//				list.clear();
//				
//				firstBlockCreated = true;
//			}
//			

			//TODO if list reach the last one, check if zero, if it is, use modify block to delete all zeros
			
			
			if(list.size() == biResult.crtBlockData.size){
				pb = new ParaBuilder();
				String p = pb.buildPara(globalIndex, globalIndex-list.size(), resultSign);
				biResult.blockWriter(p, list);
				list.clear();
			} 
			
			if(globalIndex + 1 == biLarger.totalSize){
				if(list.size() != 0){
					pb = new ParaBuilder();
					String p = pb.buildPara(globalIndex, globalIndex-list.size(), resultSign);
					biResult.blockWriter(p, list);
				}
				biResult = new BigInt(biResult.path, biResult.fixedBlockSize);
				
				if(bit == 0){// if the last bit is zero
					ModifyBlock modifier = new ModifyBlock();
					biResult = modifier.deleteZeros(biResult);
				}
			}
		}//end for
		return biResult;
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
	
	
}
	
