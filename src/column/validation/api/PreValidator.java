package column.validation.api;

import java.util.List;

import column.validation.impl.context.ValidationContext;

public interface PreValidator {
	
	public void loadValidationContext(String entryPath);
	
	public void loadValidationSource();
		
	public void loadValidationPlugins(List<ValidationPlugin> plugins);
	
	public ValidationContext getValidationContext();
	
}
