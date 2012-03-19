package bigint;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BlockData {
	public int from;
	public int to;
	public boolean head;
	public boolean tail;
	public int size;
	public int blockNum;
	public int[] arrayData;
	public int sign;
	public String path;
	public BufferedReader br; 
	
	public BlockData(String path, int blockNum){
		try {
			//set block number
			this.blockNum = blockNum;
			this.path = path;
			//read the file
			br = new BufferedReader(new FileReader(this.path+"/"+this.blockNum));
			String tmp = br.readLine();
			ParaParser ps = new ParaParser();
			ps.parse(tmp);
			this.from = ps.from;
			this.to = ps.to;
			this.head = ps.head;
			this.tail = ps.tail;
			this.size = ps.size;
			this.sign = ps.sign;
			//print some log
			System.out.println("|block data initialized, Num:"+this.blockNum);
			System.out.println("|path:"+path);
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
				this.arrayData[i] = Character.getNumericValue(ctmp[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("shabile");
		}	
	}

	
}
