package bigint;

public class BigRow {
	BigInt row;
	Integer rowNum;
	Integer offset;
	
	public BigRow(BigInt row, Integer rowNum, Integer offset){
		this.row = row;
		this.rowNum = rowNum;
		this.offset = offset;
	}
	
	public Integer getRowNum(){
		return rowNum;
	}
	
	public BigInt getRow(){
		return row;
	}
	
	public Integer getOffset(){
		return offset;
	}
	
}
