package bigint;

public class mainClass {
	public static void main(String args[]){
//		System.out.println("test");
		
		String path1 = "/Users/jiusi/Documents/workspace/testfield/BD1";
		String path2 = "/Users/jiusi/Documents/workspace/testfield/BD2";
		String resultPath = "/Users/jiusi/Documents/workspace/testfield/RST";
		BigInt b1 = new BigInt(path1, 4);
		BigInt b2 = new BigInt(path2, 4);
		BigInt rst = new BigInt(resultPath, 4);
		
		BIPlus operator = new BIPlus(rst);
		operator.plus(b1, b2);
		
	}
	
	
}