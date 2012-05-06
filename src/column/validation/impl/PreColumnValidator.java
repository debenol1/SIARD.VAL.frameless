package column.validation.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jaxen.JaxenException;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import column.validation.api.PreValidator;
import column.validation.api.ValidationPlugin;
import column.validation.impl.bean.Table;
import column.validation.impl.context.ValidationContext;

public class PreColumnValidator implements PreValidator {
	
	private ValidationContext validationContext;

	@Override
	public void loadValidationContext(String entryPath) {
		
		ValidationContext context = new ValidationContext(entryPath);
		Properties properties = context.getValidationProperties();
		
		Document columns = loadXMLRessource(context.getMetadataXML());
		
		Element columnsRootElement = columns.getRootElement();
		
		String namespaceURI = columnsRootElement.getNamespaceURI();
				
		String xmlPrefix = properties.getProperty("metadata.xml.prefix");
		String xsdPrefix = properties.getProperty("table.xsd.prefix");
		
		Namespace xmlNamespace = createNamespace(xmlPrefix, namespaceURI);
		Namespace xsdNamespace = createNamespace(xsdPrefix, namespaceURI);
		
		context.setNamespaceURI(namespaceURI);
		
		context.setXmlPrefix(xmlPrefix);
		context.setXsdPrefix(xsdPrefix);
		
		context.setXmlNamespace(xmlNamespace);
		context.setXsdNamespace(xsdNamespace);
		
		context.setColumns(columns);
		
		this.setValidationContext(context);
	}

	@Override
	//Extracts the tables an the according XSD schema an stores them to the Validation Context
	public void loadValidationSource() {
		
		ValidationContext context = getValidationContext();
		Namespace xmlNamespace = context.getXmlNamespace();
		Properties properties = context.getValidationProperties();
		
		extractDatabaseSchemata();
		
		List<Element> schemaElements = context.getDatabaseSchemata();
		List<Table> tables = new ArrayList<Table>();
		
		//Load <schema> Elements
		for (Element schemaElement : schemaElements) {
			
			StringBuffer schemaFolder = new StringBuffer();
			
			schemaFolder.append(context.getPathToContent());
			schemaFolder.append("/");
			schemaFolder.append(schemaElement.getChild("folder", xmlNamespace).getValue());
						
			//Load <tables> Element
			Element tablesElement = schemaElement.getChild("tables", xmlNamespace);
			List<Element> tableElements = tablesElement.getChildren("table", xmlNamespace);
				
			//Load <table> Elements
			for (Element tableElement : tableElements) {
				
				
				
				//Load XML Elements
				StringBuffer tableFolder = new StringBuffer();
					
				tableFolder.append(schemaFolder.toString());
				tableFolder.append("/");
				tableFolder.append(tableElement.getChild("folder", xmlNamespace).getValue());
				tableFolder.append("/");
				tableFolder.append(tableElement.getChild("folder", xmlNamespace).getValue());
				tableFolder.append(".xsd");
					
				Document columnSchema = loadXMLRessource(new File(tableFolder.toString()));
					
				//Load XSD Elements
				String xpath = properties.getProperty("xpath.to.schema");
				String xsdPrefix = context.getXsdPrefix();
				
				/*NamespaceURI of each XSD Schema. Therefore it is not loaded from the Validation
				  Context but extracted from the XSD schemata*/
				String namespaceURI = columnSchema.getRootElement().getNamespaceURI();
			    
				Element columnsElement = tableElement.getChild("columns", xmlNamespace);
				List<Element> columns = columnsElement.getChildren("column", xmlNamespace);
						
				List<Element> xsdElements = loadXMLElements(xpath, xsdPrefix, namespaceURI, columnSchema);
					
				Table table = new Table();
				
				table.setName(tableElement.getChild("folder", xmlNamespace).getValue());
				table.setColumns(columns);
				table.setColumnsReference(xsdElements);
				
				tables.add(table);							
			}
		}
		context.setTables(tables);
		this.setValidationContext(context);
	}

	@Override
	public void loadValidationPlugins(List<ValidationPlugin> plugins) {
		
		ValidationContext context = getValidationContext();
		context.setValidationPlugins(plugins);
		
	}
	
	//Private Methods
	//Generates JDOM Document from File
	private Document loadXMLRessource(File file) {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(file);
			return document;
		} catch (JDOMException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	private Namespace createNamespace(String prefix, String namespaceURI) {
		Namespace namespace = Namespace.getNamespace(prefix, namespaceURI);
		return namespace;
	}
	
	private List<Element> loadXMLElements(String xpathString, 
			String prefix, String namespaceURI, Document document) {
		
		try {
			
			JDOMXPath xpath = new JDOMXPath(xpathString);
			
			SimpleNamespaceContext simpleNamespaceContext = new SimpleNamespaceContext();
			simpleNamespaceContext.addNamespace(prefix, namespaceURI);
			
			xpath.setNamespaceContext(simpleNamespaceContext);
			List<Element> elements = ((List<Element>) xpath.selectNodes(document));
			
			return elements;
			
		} catch (JaxenException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//Provides Section of Metadata.xml which is to be validated and stores it to the Validation Context
	private void extractDatabaseSchemata() {
		
		ValidationContext context = getValidationContext();
		Properties properties = context.getValidationProperties();
		
		String prefix = context.getXmlPrefix();
		String xpath = properties.getProperty("xpath.to.data");
		
		String namespaceURI = context.getNamespaceURI();
		
		Document document = context.getColumns();
		
		context.setDatabaseSchemata(loadXMLElements(xpath, prefix, namespaceURI, document));
	}
	
	//Getter and Setter
	public ValidationContext getValidationContext() {
		return validationContext;
	}

	public void setValidationContext(ValidationContext validationContext) {
		this.validationContext = validationContext;
	}

}
