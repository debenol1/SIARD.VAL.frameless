package column.validation.impl.plugin;

import java.util.List;
import java.util.Properties;

import column.validation.api.ValidationPlugin;
import column.validation.impl.bean.Table;
import column.validation.impl.context.ValidationContext;

public class AttributeCountValidator implements ValidationPlugin {
			
	private ValidationContext validationContext;
	private StringBuffer report;
	private Boolean passed;
	
	public AttributeCountValidator() {
		this.setPassed(true);
	}
	
	@Override
	public void execute() {
		
		ValidationContext context = this.getValidationContext();
		List<Table> tables = context.getTables();
		StringBuffer validationReport = new StringBuffer();
		Properties properties = context.getValidationProperties();
		
		validationReport.append(properties.getProperty("attribute.count.validator.title"));
		validationReport.append('\n');
		validationReport.append('\n');
		
		for (Table table : tables) {
			
			int columnCount = table.getColumns().size();
			int columnReferenceCount = table.getColumnsReference().size();
			
			validationReport.append(table.getName() + ": ");
			validationReport.append(columnCount);
			validationReport.append(",");
			validationReport.append(columnReferenceCount);
					
			if (columnCount == columnReferenceCount) {
				validationReport.append(properties.getProperty("attribute.count.validator.passed"));
			} else {
				this.setPassed(false);
				validationReport.append(properties.getProperty("attribute.count.validator.failed"));
			}
			validationReport.append('\n');
		}
		
		if (getPassed() == true) {
			validationReport.append('\n');
			validationReport.append(properties.getProperty("attribute.count.validator.test.passed"));
			validationReport.append('\n');
	    } else {
	    	validationReport.append('\n');
			validationReport.append(properties.getProperty("attribute.count.validator.test.failed"));
			validationReport.append('\n');
		}
		validationReport.append(properties.getProperty("attribute.count.validator.end"));
		validationReport.append('\n');
		this.setReport(validationReport);
	}

	@Override
	public void loadValidationContext(ValidationContext validationContext) {
		setValidationContext(validationContext);	
	}

	private ValidationContext getValidationContext() {
		return validationContext;
	}

	private void setValidationContext(ValidationContext validationContext) {
		this.validationContext = validationContext;
	}

	public StringBuffer getReport() {
		return report;
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
