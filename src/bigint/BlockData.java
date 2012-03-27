package bigint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
			e.printStackTrace();
		} catch (IOException e) {
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
					System.out.println("SB! i:"+i+"the array length is:"+arrayData.length+" path is:"+this.path+"/"+this.blockNum);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("shabile");
		}	
	}
	
	public void singleBlockWriter(String paras, ArrayList<String> data, int blockNum){
		try {
//			System.out.println("paras are:"+paras);
			FileWriter fstream = new FileWriter(this.path+"/"+blockNum);
			BufferedWriter br = new BufferedWriter(fstream);
			br.write(paras);
			br.newLine();
			StringBuilder sb = new StringBuilder();
			for(int i=0; i<data.size(); i++){
				sb.append(data.get(i));
			}
			br.write(sb.toString());
			br.close();
		} catch (IOException e) {
			e.printStackTrace();	
		}
	}
}
