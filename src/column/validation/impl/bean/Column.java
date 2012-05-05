package column.validation.impl.bean;

public class Column {
	
	private String name;
	private String type;
	private String typeOriginal;
	private String nullable;
	public String getNullable() {
		return nullable;
	}
	public void setNullable(String nullable) {
		this.nullable = nullable;
	}
	public String getTypeOriginal() {
		return typeOriginal;
	}
	public void setTypeOriginal(String typeOriginal) {
		this.typeOriginal = typeOriginal;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
