package bigint;

public class ParaBuilder{
	
	
	public ParaBuilder(){
		
	}
	
	public String buildPara(int globalIndex, int lastBlockEndBoundary, int sign){
		Integer to, size, from;
		
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