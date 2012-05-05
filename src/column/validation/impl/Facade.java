package column.validation.impl;

import java.util.ArrayList;
import java.util.List;

import column.validation.api.PreValidator;
import column.validation.api.ValidationPlugin;
import column.validation.api.Validator;
import column.validation.impl.context.ValidationContext;
import column.validation.impl.plugin.AttributeCountValidator;

public class Facade {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String path = "/Users/odebenath/Eclipse/SIARD.VAL.0/SIARD.VAL.EXERCISE/data";
		PreValidator preValidator = new PreColumnValidator();
				
		List<ValidationPlugin> plugins = new ArrayList<ValidationPlugin>();
		ValidationPlugin attributeCountValidator = new AttributeCountValidator();
		plugins.add(attributeCountValidator);
		
		preValidator.loadValidationContext(path);
		preValidator.loadValidationSource();
		preValidator.loadValidationPlugins(plugins);
		
		ValidationContext validationContext = preValidator.getValidationContext();
		
		Validator validator = new ColumnValidator(validationContext);
		validator.validate();
		validator.printReport();
		
	}

}
