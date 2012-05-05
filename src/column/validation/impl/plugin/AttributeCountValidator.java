package column.validation.impl.plugin;

import java.util.List;

import column.validation.api.ValidationPlugin;
import column.validation.impl.bean.Table;
import column.validation.impl.context.ValidationContext;

public class AttributeCountValidator implements ValidationPlugin {
	
	private ValidationContext validationContext;
	private StringBuffer report;
	
	@Override
	public void execute() {
		
		ValidationContext context = this.getValidationContext();
		
		List<Table> tables = context.getTables();
		
		StringBuffer validationReport = new StringBuffer();
		
		for (Table table : tables) {
			
			int columnCount = table.getColumns().size();
			int columnReferenceCount = table.getColumnsReference().size();
			
			if (columnCount == columnReferenceCount) {
				validationReport.append("Validation of the attribute number successfully passed" + '\n');
				validationReport.append("Metadata.xml: ");
				validationReport.append(columnCount);
				validationReport.append(", ");
				validationReport.append("XSD Schema: ");
				validationReport.append(columnReferenceCount);
				validationReport.append('\n');
				validationReport.append('\n');
			} else {
				validationReport.append("Validation of the attribute number failed" + '\n');
				validationReport.append("Metadata.xml: ");
				validationReport.append(columnCount);
				validationReport.append(", ");
				validationReport.append("XSD Schema: ");
				validationReport.append(columnReferenceCount);
				validationReport.append('\n');
				validationReport.append('\n');
			}
		}
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

}
