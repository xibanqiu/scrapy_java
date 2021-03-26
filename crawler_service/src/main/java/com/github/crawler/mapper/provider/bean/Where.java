package com.github.crawler.mapper.provider.bean;

public class Where {

	private String field;
	private String op;
	private Object value;
	
	public enum OperatorE{
		equal("="),
		notequal("<>"),
		like("like"),
		less("<"),
		greater(">"),
		isNull("isNull");
		
		OperatorE(String symbol){
			this.symbol=symbol;
		}
		
		private String symbol;

		public String getSymbol() {
			return symbol;
		}

		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}
		
	}

	public Where(String field, OperatorE operator, Object value) {
		super();
		this.field = field;
		this.op = operator.getSymbol();
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public static Where equal(String field,Object value){
		return new Where(field, OperatorE.equal, value);
	}
	
	public static Where isNull(String field){
		return new Where(field, OperatorE.isNull, "is null");
	}
	
	public static Where greater(String field,Object value){
		return new Where(field, OperatorE.greater, value);
	}
	
	public static Where less(String field,Object value){
		return new Where(field, OperatorE.less, value);
	}
	
	public static Where notequal(String field,Object value){
		return new Where(field, OperatorE.notequal, value);
	}

	@Override
	public String toString() {
		return "FilterRule [field=" + field + ", op=" + op + ", value=" + value + "]";
	}

}
