package column.validation.api;

import column.validation.impl.context.ValidationContext;

public interface ValidationPlugin {
			
	public void loadValidationContext(ValidationContext validationContext);
	
	public void execute();
	
	public StringBuffer getReport();

}
