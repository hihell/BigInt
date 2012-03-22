package bigint;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BlockData {
	public int from;
	public int to;
	public int size;
	public int blockNum;
	public Integer totalBlocks;
	
	public int[] arrayData;
	public int sign;
	public String path;
	public BufferedReader br; 
	
	public BlockData(String path, int blockNum) {

		//set block number
		this.blockNum = blockNum;
		this.path = path;
		//read the file
		try {
			br = new BufferedReader(new FileReader(this.path+"/"+this.blockNum));
			String tmp = br.readLine();
			ParaParser ps = new ParaParser();
			ps.parse(tmp);
			this.from = ps.from;
			this.to = ps.to;

			this.size = ps.size;
			this.sign = ps.sign;
		
			//change data from string to int
			processData();
			//close reader flow
			br.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public BlockData(){
		
	}
	
	public void processData(){
		String tmp;
		try {
			tmp = br.readLine();
			char [] ctmp = tmp.toCharArray();
			this.arrayData = new int[this.size];
			
			for(int i=0; i<tmp.length(); i++){
				try{
					this.arrayData[i] = Character.getNumericValue(ctmp[i]);
				} catch (java.lang.ArrayIndexOutOfBoundsException e){
					System.out.println("SB! i:"+i+"the array lenth is:"+arrayData.length+" path is:"+this.path+"/"+this.blockNum);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("shabile");
		}	
	}
}
