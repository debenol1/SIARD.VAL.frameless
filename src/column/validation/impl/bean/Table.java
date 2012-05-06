package column.validation.impl.bean;

import java.util.List;

import org.jdom.Element;

public class Table {
	
	private String name;
	private List<Element> columns;
	private List<Element> columnsReference;
	
	public List<Element> getColumns() {
		return columns;
	}
	public void setColumns(List<Element> columns) {
		this.columns = columns;
	}
	
	public List<Element> getColumnsReference() {
		return columnsReference;
	}
	public void setColumnsReference(List<Element> columnsReference) {
		this.columnsReference = columnsReference;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
