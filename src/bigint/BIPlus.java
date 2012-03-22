package bigint;

import java.util.ArrayList;

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
		}
		
		if(carryBit != 0){
			list.add(carryBit.toString());
			carryBit = 0;
			globalIndex++;
		}
		
	 	if(list.size() != 0){
	 		globalIndex--;
	 		pb = new ParaBuilder();
	 		String p = pb.buildPara(globalIndex, lastBlockEndBoundary, resultSign);
			biResult.blockWriter(p, list);
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
		}
//		TODO carryBit add issue
		if(carryBit != 0){
			list.add(carryBit.toString());
			carryBit = 0;
			globalIndex++;
		}
		
	 	if(list.size() != 0){
	 		globalIndex--;
	 		pb = new ParaBuilder();
	 		String p = pb.buildPara(globalIndex, lastBlockEndBoundary, resultSign);
			biResult.blockWriter(p, list);
		}
	}
	
	public int compareAbsValue(BigInt b1, BigInt b2){
		if(b1.totalSize > b2.totalSize){
			return 1;
		} else if(b2.totalSize > b1.totalSize){
			return -1;
		} else {
			for(int i=0; i < b1.totalSize; i++){
				if(b1.get(i) > b2.get(i)){
					return 1;
				} else if(b2.get(i) < b1.get(i)){
					return -1;
				}
			}
			return 0;
		}
	}
	
	
}
	
