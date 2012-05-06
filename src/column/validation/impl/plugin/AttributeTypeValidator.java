package column.validation.impl.plugin;

import java.util.List;
import java.util.Properties;

import org.jdom.Element;

import column.validation.api.ValidationPlugin;
import column.validation.impl.bean.Table;
import column.validation.impl.context.ValidationContext;

public class AttributeTypeValidator implements ValidationPlugin {
	
	private ValidationContext validationContext;
	private StringBuffer report;
	private Boolean passed;
	
	public AttributeTypeValidator() {
		this.setPassed(true);
	}
	
	@Override
	public void execute() {
		
		ValidationContext context = this.getValidationContext();
		List<Table> tables = context.getTables();
		StringBuffer validationReport = new StringBuffer();
		Properties properties = context.getValidationProperties();
		
		validationReport.append(properties.getProperty("attribute.occurrence.validator.title"));
		validationReport.append('\n');
		validationReport.append('\n');
		
		for (Table table : tables) {
			
			validationReport.append(table.getName());
			validationReport.append('\n');
			
			List<Element> xmlElements = table.getColumns();
			List<Element> xsdElements = table.getColumnsReference();
			
			int i = 0;
			
			while ( i < xmlElements.size()) {
				
				Element xmlElement = xmlElements.get(i);
				Element xsdElement = xsdElements.get(i);
				
				//Check the nullable Element
				String leftSide = xmlElement.getChild("type", context.getXmlNamespace()).getValue();
				
				//Check the minOccurs Attribute
				String rightSide = xsdElement.getAttributeValue("type");
				
				String elementName = xmlElement.getChild("name", context.getXmlNamespace()).getValue();
				
				validationReport.append(elementName);
				validationReport.append(": ");
				validationReport.append(" ");
				validationReport.append(leftSide);
				validationReport.append(", ");
				validationReport.append(" ");
				validationReport.append(rightSide);
				
				if (leftSide.equalsIgnoreCase("true") && rightSide.equalsIgnoreCase("0")) {
					validationReport.append(properties.getProperty("attribute.type.validator.passed"));
				} else if (leftSide.equalsIgnoreCase("false") && rightSide == null) {
					validationReport.append(properties.getProperty("attribute.type.validator.passed"));
				} else {
					this.setPassed(false);
					validationReport.append(properties.getProperty("attribute.type.validator.failed"));
				}
				i = i + 1;
				validationReport.append('\n');
			}
			
		}
		//this.setReport(validationReport);
		System.out.println(validationReport.toString());
		
		
	}

	@Override
	public void loadValidationContext(ValidationContext validationContext) {
		setValidationContext(validationContext);
	}

	public ValidationContext getValidationContext() {
		return validationContext;
	}

	public void setValidationContext(ValidationContext validationContext) {
		this.validationContext = validationContext;
	}

	@Override
	public StringBuffer getReport() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setReport(StringBuffer report) {
		this.report = report;
	}

	public Boolean getPassed() {
		return passed;
	}

	public void setPassed(Boolean passed) {
		this.passed = passed;
	}

}
