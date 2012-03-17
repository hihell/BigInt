package bigint;

public class ParaParser {
	public Integer from;
	public Integer to;
	public boolean head;
	public boolean tail;
	public Integer size;

	
	public void parse(String line){
		String []tmp = line.split(",");
		from = Integer.parseInt(tmp[0]);
		to = Integer.parseInt(tmp[1]);
		head = Integer.parseInt(tmp[2]) == 1 ? true : false;
		tail = Integer.parseInt(tmp[3]) == 1 ? true : false;
		size = Integer.parseInt(tmp[4]);
	}
		
}
