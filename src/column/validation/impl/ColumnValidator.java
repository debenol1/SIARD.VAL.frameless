package column.validation.impl;

import java.util.List;

import column.validation.api.ValidationPlugin;
import column.validation.api.Validator;
import column.validation.impl.context.ValidationContext;

public class ColumnValidator implements Validator {

	private ValidationContext validationContext;
	private StringBuffer validationReport;
	
	public ColumnValidator(ValidationContext validationContext) {
		setValidationContext(validationContext);
	}
	
	@Override
	public void validate() {
		
		ValidationContext validationContext = getValidationContext();
		List<ValidationPlugin> plugins = validationContext.getValidationPlugins();
		
		for (ValidationPlugin plugin : plugins) {
			plugin.loadValidationContext(validationContext);
			plugin.execute();
			setValidationReport(plugin.getReport());
		}

	}

	private ValidationContext getValidationContext() {
		return validationContext;
	}

	private void setValidationContext(ValidationContext validationContext) {
		this.validationContext = validationContext;
	}

	public StringBuffer getValidationReport() {
		return validationReport;
	}

	public void setValidationReport(StringBuffer validationReport) {
		this.validationReport = validationReport;
	}

	@Override
	public void printReport() {
		System.out.println(getValidationReport().toString());
	}

}
