package column.validation.impl.context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

import column.validation.api.ValidationPlugin;
import column.validation.impl.bean.Table;

public class ValidationContext {
	
	private String SiardMountPoint;
	private String pathToContent;
	private String pathToHeader;
	private String pathToMetadataXML;
		
	private File metadataXML;
	
	private String xmlPrefix;
	private String xsdPrefix;
	
	private String namespaceURI;
	
	private Namespace xmlNamespace;
	private Namespace xsdNamespace;
	
	private Properties validationProperties;
	
	private FileInputStream metadataXMLFis;
		
	private Document columns;
	private Document columnSchema;
	
	private Table validationData;
	
	private List<Element> databaseSchemata;
	private List<Table> tables;
	
	private List<ValidationPlugin> validationPlugins;
			
	public ValidationContext(String pathToSiardMountPoint) {
				
		try {
			
			Properties properties = new Properties();
	        properties.load(getClass().getResourceAsStream("ValidationContext.properties"));
	        
	        setValidationProperties(properties);	
			setSiardMountPoint(pathToSiardMountPoint);
			
			setPathToContent(pathToSiardMountPoint 
					+ getValidationProperties().getProperty("content.suffix"));
			
			setPathToHeader(pathToSiardMountPoint 
					+ getValidationProperties().getProperty("header.suffix"));
			
			setPathToMetadataXML(getPathToHeader() 
					+ getValidationProperties().getProperty("test.file"));
						
			setMetadataXML(new File(getPathToMetadataXML()));		
			setMetadataXMLFis(new FileInputStream(getMetadataXML()));
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Prints out the context information
	public void printValidationContext() {
		
		System.out.println(getValidationProperties().getProperty("mount.point") 
				+ getSiardMountPoint());
		
		System.out.println(getValidationProperties().getProperty("content") 
				+ getPathToContent());
		
		System.out.println(getValidationProperties().getProperty("header")
				+ getPathToHeader());
				
		System.out.println(getValidationProperties().getProperty("metadata.xml") 
				+ getPathToMetadataXML());
		
		System.out.println(getValidationProperties().getProperty("validation.properties") 
				+ getValidationProperties().size());
		
		System.out.println(getValidationProperties().getProperty("metadata.xml.fis") 
				+ getMetadataXMLFis().toString());
		
	}
	
	//Setter and getter methods. Get methods are public, the setter methods are private
	public String getSiardMountPoint() {
		return SiardMountPoint;
	}
	
	private void setSiardMountPoint(String mountPoint) {
		this.SiardMountPoint = mountPoint;
	}
	
	public String getPathToContent() {
		return pathToContent;
	}
	
	private void setPathToContent(String pathToContent) {
		this.pathToContent = pathToContent;
	}
	
	public String getPathToHeader() {
		return pathToHeader;
	}
	
	private void setPathToHeader(String pathToHeader) {
		this.pathToHeader = pathToHeader;
	}
	
	public File getMetadataXML() {
		return metadataXML;
	}
	
	private void setMetadataXML(File metadataXML) {
		this.metadataXML = metadataXML;
	}
	
	public Properties getValidationProperties() {
		return validationProperties;
	}
	
	private void setValidationProperties(Properties validationProperties) {
		this.validationProperties = validationProperties;
	}
	
	public FileInputStream getMetadataXMLFis() {
		return metadataXMLFis;
	}
	
	private void setMetadataXMLFis(FileInputStream metadataXMLFis) {
		this.metadataXMLFis = metadataXMLFis;
	}
	
	public String getPathToMetadataXML() {
		return pathToMetadataXML;
	}

	private void setPathToMetadataXML(String pathToMetadataXML) {
		this.pathToMetadataXML = pathToMetadataXML;
	}

	public Table getValidationData() {
		return validationData;
	}

	public void setValidationData(Table validationData) {
		this.validationData = validationData;
	}

	public Document getColumns() {
		return columns;
	}

	public void setColumns(Document columns) {
		this.columns = columns;
	}

	public Document getColumnSchema() {
		return columnSchema;
	}

	public void setColumnSchema(Document columnSchema) {
		this.columnSchema = columnSchema;
	}

	public String getXmlPrefix() {
		return xmlPrefix;
	}

	public void setXmlPrefix(String xmlPrefix) {
		this.xmlPrefix = xmlPrefix;
	}

	public String getXsdPrefix() {
		return xsdPrefix;
	}

	public void setXsdPrefix(String xsdPrefix) {
		this.xsdPrefix = xsdPrefix;
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

	public String getNamespaceURI() {
		return namespaceURI;
	}

	public void setNamespaceURI(String namespaceURI) {
		this.namespaceURI = namespaceURI;
	}

	public List<Element> getDatabaseSchemata() {
		return databaseSchemata;
	}

	public void setDatabaseSchemata(List<Element> databaseSchemata) {
		this.databaseSchemata = databaseSchemata;
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public List<ValidationPlugin> getValidationPlugins() {
		return validationPlugins;
	}

	public void setValidationPlugins(List<ValidationPlugin> validationPlugins) {
		this.validationPlugins = validationPlugins;
	}

}
