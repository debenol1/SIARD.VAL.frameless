package column.validation.impl;

import java.util.ArrayList;
import java.util.List;

import column.validation.api.ValidationPlugin;
import column.validation.api.Validator;
import column.validation.impl.context.ValidationContext;

public class ColumnValidator implements Validator {

	private ValidationContext validationContext;
	private List<StringBuffer> validationReport;
	
	public ColumnValidator(ValidationContext validationContext) {
		setValidationContext(validationContext);
	}
	
	@Override
	public void validate() {
		
		ValidationContext validationContext = getValidationContext();
		List<ValidationPlugin> plugins = validationContext.getValidationPlugins();
		List<StringBuffer> reports = new ArrayList<StringBuffer>();
		
		for (ValidationPlugin plugin : plugins) {
			plugin.loadValidationContext(validationContext);
			plugin.execute();
			reports.add(plugin.getReport());
		}
		
		this.setValidationReport(reports);
	}

	private ValidationContext getValidationContext() {
		return validationContext;
	}

	private void setValidationContext(ValidationContext validationContext) {
		this.validationContext = validationContext;
	}

	public List<StringBuffer> getValidationReport() {
		return validationReport;
	}

	public void setValidationReport(List<StringBuffer> validationReport) {
		this.validationReport = validationReport;
	}

	@Override
	public void printReport() {
		
		List<StringBuffer> reports = this.getValidationReport();
		
		for (int i = 0; i < reports.size(); i++) {
			System.out.println(reports.get(i).toString());
		}
	}
}
