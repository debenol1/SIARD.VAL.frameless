package column.validation.impl.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jdom.Element;

import column.validation.api.ValidationPlugin;
import column.validation.impl.bean.Table;
import column.validation.impl.context.ValidationContext;

public class AttributeSequenceValidator implements ValidationPlugin {
	
	private ValidationContext validationContext;
	private StringBuffer report;
	private Boolean passed;
	
	public AttributeSequenceValidator() {
		this.setPassed(true);
	}
	
	@Override
	public void execute() {
		
		ValidationContext context = this.getValidationContext();
		List<Table> tables = context.getTables();
		StringBuffer validationReport = new StringBuffer();
		Properties properties = context.getValidationProperties();
		
		validationReport.append(properties.getProperty("attribute.sequence.validator.title"));
		validationReport.append('\n');
		validationReport.append('\n');
		
		for (Table table : tables) {
			
			validationReport.append(table.getName());
			validationReport.append('\n');
			
			List<Element> xmlElements = table.getColumns();
			List<Element> xsdElements = table.getColumnsReference();
			
			List<String> xmlTypeSequence = new ArrayList<String>();
			List<String> xsdTypeSequence = new ArrayList<String>();
			
			if (xmlElements.size() == xsdElements.size()) {
			
				int i = 0;
			
				while ( i < xmlElements.size()) {
				
					Element xmlElement = xmlElements.get(i);
					Element xsdElement = xsdElements.get(i);
				
					String leftSide = xmlElement.getChild("type", context.getXmlNamespace()).getValue();
					String rightSide = xsdElement.getAttributeValue("type");
				
					//String elementName = xmlElement.getChild("name", context.getXmlNamespace()).getValue();
					String delimiter = properties.getProperty("attribute.sequence.validator.original.type.delimiter");
					String trimmedExpectedType = trimLeftSideType(leftSide, delimiter);
				
					//String expectedType = properties.getProperty(trimmedExpectedType);
				
					xmlTypeSequence.add(properties.getProperty(trimmedExpectedType));
					xsdTypeSequence.add(rightSide);
				
					validationReport.append(properties.getProperty(trimmedExpectedType));
					validationReport.append(": ");
					validationReport.append(rightSide);
				
					i = i + 1;
				
					if (properties.getProperty(trimmedExpectedType).equalsIgnoreCase(rightSide)) {
						validationReport.append(properties.getProperty("attribute.sequence.validator.passed"));
					} else {
						this.setPassed(false);
						validationReport.append(properties.getProperty("attribute.sequence.validator.failed"));
					}
					validationReport.append('\n');
				}
				validationReport.append('\n');
			} else {
				this.setPassed(false);
			}
		}
		if (this.getPassed() == true) {
			validationReport.append(properties.getProperty("attribute.sequence.validator.test.passed"));
		} else {
			validationReport.append(properties.getProperty("attribute.sequence.validator.test.failed"));
		}
		validationReport.append('\n');
		validationReport.append('\n');
		this.setReport(validationReport);
	}
	
	private String trimLeftSideType(String leftside, String delimiter) {
		int i = leftside.indexOf(delimiter);
		if (i > -1) {
			String trimmedLeftSideType = leftside.substring(0, i);
			return trimmedLeftSideType;
		} else {
			return leftside;
		}
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
		return this.report;
	}

	public Boolean getPassed() {
		return passed;
	}

	public void setPassed(Boolean passed) {
		this.passed = passed;
	}

	public void setReport(StringBuffer report) {
		this.report = report;
	}

}
