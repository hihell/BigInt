package bigint;

public class ParaParser {
	public Integer from;
	public Integer to;
	public Integer size;
	public Integer sign;
	
	public void parse(String line){
		String []tmp = line.split(",");
		from = Integer.parseInt(tmp[0]);
		to = Integer.parseInt(tmp[1]);
		size = Integer.parseInt(tmp[2]);
		sign = Integer.parseInt(tmp[3]);
	}
}
