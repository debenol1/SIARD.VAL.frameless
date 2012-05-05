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

import column.validation.impl.bean.Table;
import column.validation.impl.context.ValidationContext;

public class ColumnValidatorEngine {
				
	private ValidationContext validationContext;
	
	private String xmlPrefix;
	private String xsdPrefix;
	
	private Namespace xmlNamespace;
	private Namespace xsdNamespace;
	
	private Document metadataXML;
	private Document columnSchema;
	
	private List<Element> databaseSchemata;
	private List<Table> tables;
	
	private Table table;
	
	public void startup(String pathToSiardMountPoint) {
		
		setValidationContext(new ValidationContext(pathToSiardMountPoint));
		
		setMetadataXML(loadXMLRessource(getValidationContext().getMetadataXML()));
		
		setXmlPrefix(getValidationContext().getValidationProperties().getProperty("metadata.xml.prefix"));
		setXsdPrefix(getValidationContext().getValidationProperties().getProperty("table.xsd.prefix"));
		
		setXmlNamespace(createNamespace(getValidationContext().getValidationProperties().getProperty("metadata.xml.prefix"),getMetadataXML().getRootElement().getNamespaceURI()));
		setXsdNamespace(createNamespace(getValidationContext().getValidationProperties().getProperty("table.xsd.prefix"),getMetadataXML().getRootElement().getNamespaceURI()));
	
	}
	
	private Namespace createNamespace(String prefix, String namespaceURI) {
		Namespace namespace = Namespace.getNamespace(prefix, namespaceURI);
		return namespace;
	}
	
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
	
	//Extract XML Elements with XPath
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
	
	//Provides Section of Metadata.xml which is to be validated and stores it to a local variable	
	private void extractDatabaseSchemata() {
		
		String xpath = getValidationContext().getValidationProperties().getProperty("xpath.to.data");
		String prefix = getXmlPrefix();
		String namespaceURI = getMetadataXML().getRootElement().getNamespaceURI();
		
		Document document = getMetadataXML();
		
		setDatabaseSchemata(loadXMLElements(xpath, prefix, namespaceURI, document));
		
	}
	
	//Extract Tables as Children Elements of Schemas
	private void extractTables() {
		
		Namespace xmlNamespace = getXmlNamespace();
		
		ValidationContext context = getValidationContext();
		
		Properties properties = getValidationContext().getValidationProperties();
		
		List<Element> schemaElements = getDatabaseSchemata();
		
		List<Table> tables = new ArrayList<Table>();
		
		//Load <schema> Elements
		for (Element a : schemaElements) {
			
			StringBuffer schemaFolder = new StringBuffer();
			
			schemaFolder.append(context.getPathToContent());
			schemaFolder.append("/");
			schemaFolder.append(a.getChild("folder", xmlNamespace).getValue());
						
			//Load <tables> Element
			Element tablesElement = a.getChild("tables", xmlNamespace);
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
				String xsdPrefix = getXsdPrefix();
				String namespaceURI = columnSchema.getRootElement().getNamespaceURI();
			    
				Element columnsElement = tableElement.getChild("columns", xmlNamespace);
				List<Element> columns = columnsElement.getChildren("column", xmlNamespace);
						
				List<Element> xsdElements = loadXMLElements(xpath, xsdPrefix, namespaceURI, columnSchema);
					
				Table table = new Table();
					
				table.setColumns(columns);
				table.setColumnsReference(xsdElements);
				
				tables.add(table);
										
			}
		}
		setTables(tables);
	}
	
	private Document getColumnSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	private void setColumnSchema(Document createJDOMDocument) {
		// TODO Auto-generated method stub
		
	}

	public void validate() {
		StringBuffer report = new StringBuffer();
		
		extractDatabaseSchemata();
		extractTables();
		
		report.append(validateAttributeNumber());
		
		System.out.println(report.toString());
	}
	
	private StringBuffer validateAttributeNumber() {
		
		List<Table> tables = getTables();
		
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
		return validationReport;
	}
	
	private void validateAttributeSequence() {
	}
	
	private void validateAttributeType() {
	}
	
	private void validateNullableProperties() {
	}

	public ValidationContext getValidationContext() {
		return validationContext;
	}

	private void setValidationContext(ValidationContext validationContext) {
		this.validationContext = validationContext;
	}

	private Document getMetadataXML() {
		return metadataXML;
	}

	private void setMetadataXML(Document metadataXML) {
		this.metadataXML = metadataXML;
	}

	public String getXsdPrefix() {
		return xsdPrefix;
	}

	public void setXsdPrefix(String xsdPrefix) {
		this.xsdPrefix = xsdPrefix;
	}

	public String getXmlPrefix() {
		return xmlPrefix;
	}

	public void setXmlPrefix(String xmlPrefix) {
		this.xmlPrefix = xmlPrefix;
	}

	public Namespace getXmlNamespace() {
		return xmlNamespace;
	}

	public void setXmlNamespace(Namespace xmlNamespace) {
		this.xmlNamespace = xmlNamespace;
	}

	public Namespace getXsdNamespace() {
		return xsdNamespace;
	}

	public void setXsdNamespace(Namespace xsdNamespace) {
		this.xsdNamespace = xsdNamespace;
	}

	public List<Element> getDatabaseSchemata() {
		return databaseSchemata;
	}

	public void setDatabaseSchemata(List<Element> databaseSchemata) {
		this.databaseSchemata = databaseSchemata;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

}
