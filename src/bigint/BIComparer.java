package bigint;

public class BIComparer {

	public Integer compareABSValue(BigInt b1, BigInt b2){
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
	
	public Integer compareABSValue(BigInt b1, BigInt b2, Integer offset){
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
	
	
}
