package bigint;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class BIPlus {
	BigInt biResult;
	
	
	public BIPlus(BigInt Rst){
		this.biResult = Rst;
	}
	
	
	
	public void plus(BigInt b1, BigInt b2){
		Integer carryBit = 0;
		int itmp = 0;
		Integer bit = 0;
		int globalIndex = 0;
		ArrayList<String> list = new ArrayList<String>();
		
		System.out.println("b1.size:"+b1.totalSize+" b2.size:"+b2.totalSize);
		
		for(; globalIndex < Math.max(b1.totalSize, b2.totalSize); globalIndex++){
			itmp = b1.get(globalIndex) + b2.get(globalIndex) + carryBit;
			if(itmp > 9){
				bit = itmp % 10;
				carryBit = (itmp - bit) / 10;
			} else {
				bit = itmp;
				carryBit =  0;
			}
			list.add(bit.toString());
			
			if(list.size() == biResult.crtBlockData.size){
				biResult.blockWriter(list);
				list.clear();
			}
		}
		
		if(carryBit != 0){
			list.add(carryBit.toString());
			carryBit = 0;
		}
		
	 	if(list.size() != 0){
			biResult.blockWriter(list);
		} 
	
	}
		
}
	
