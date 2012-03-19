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
	
	public BIPlus(BigInt Rst){
		this.biResult = Rst;
	}
	
	public void plus(BigInt b1, BigInt b2){
		biLarger = b1;
		biSmaller = b2;
		
		if(b1.crtBlockData.sign != b2.crtBlockData.sign){//necessary to set the larger and smaller	
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
				String p = parasBuilder(globalIndex, resultSign);
				biResult.blockWriter(p, list);
				list.clear();
				lastBlockEndBoundary = globalIndex;
			}
		}
			
		if(carryBit != 0){
			list.add(carryBit.toString());
			globalIndex++;
			carryBit = 0;
		}
		
	 	if(list.size() != 0){
	 		String p = parasBuilder(globalIndex, resultSign);
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
	
	public String parasBuilder(int globalIndex, int sign){
		Integer from;
		Integer to;
		Integer size;
		
		
		StringBuilder sb = new StringBuilder();
		to = globalIndex;
		size = to - lastBlockEndBoundary;
		from = to - size + 1;
		
		sb.append(from);
		sb.append(",");
		sb.append(to);
		sb.append(",");
		sb.append(size);
		sb.append(",");
		sb.append(sign);

		return sb.toString();
	}
}
	
