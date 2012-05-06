package column.validation.impl.plugin;

import column.validation.api.ValidationPlugin;
import column.validation.impl.context.ValidationContext;

public class AttributeSequenceValidator implements ValidationPlugin {
	
	private ValidationContext validationContext;
	
	@Override
	public void execute() {
		
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

}
