package net.diaperrush.jmaker.tools.ant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.diaperrush.jmaker.FileMakerServerConfiguration;
import net.diaperrush.jmaker.schemas.FmpXmlResult;

import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class JFMTask extends Task 
{
	private static final Logger logger = Logger.getLogger( JFMTask.class );
	private String target;
	private String basePackage;
	private Configuration freemarkerConfiguration;
	private Collection<String> layoutPrefixes;
	
	public JFMTask() throws IOException
	{
		this.freemarkerConfiguration = new Configuration();
        //Specify the data source where the template files come from.
		this.freemarkerConfiguration.setDirectoryForTemplateLoading( new File("templates") );
		this.freemarkerConfiguration.setObjectWrapper(new DefaultObjectWrapper());
		this.layoutPrefixes = new ArrayList<String>();
	}
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public static void main( String [] args ) throws Exception
	{
		JFMTask task = new JFMTask();
		task.setBasePackage("com.abscraps.filemaker");
		task.setTarget( "gensrc" );
		task.setLayoutPrefix( "xml-dude" );
		
		task.execute();
	}
	
	public void execute()
	{
		try
		{
			ClassLoader loader = this.getClass().getClassLoader();
			FileMakerServerConfiguration fmConfig = FileMakerServerConfiguration.createInstance( loader );
			this.introspectFileMakerServer( fmConfig );
		}
		catch( Exception e )
		{
			logger.error( "Build Exception: ", e );
			throw new BuildException( "Build Failed.  " + e.getMessage(), e );
		}
	}
	
	private void introspectFileMakerServer( FileMakerServerConfiguration fmConfig ) throws Exception
	{
		FmpXmlResultMetadataRequest metaReq = new FmpXmlResultMetadataRequest(fmConfig);
		FmpXmlResult dbNames = metaReq.dbnames();
		
		dbNames.debugResultDump();
		for( int i = 0; i < dbNames.size(); i++ )
		{
			FileMakerDatabase db = new FileMakerDatabase();
			String dbName = dbNames.columnAsString(i, FmpXmlResultMetadataRequest.COLUMN_FILEMAKER_DATABASE_NAME );
			db.setRealName( dbName );
			
			FmpXmlResult layoutNames = metaReq.layoutnames( dbName );
			layoutNames.debugResultDump();
			for( int j = 0; j < layoutNames.size(); j++ )
			{
				String layoutName = layoutNames.columnAsString(j, FmpXmlResultMetadataRequest.COLUMN_FILEMAKER_LAYOUT_NAME );
				//see if this is one we're instructed to process
				for( String prefix: this.layoutPrefixes )
				{
					if( layoutName.startsWith(prefix) )
					{
						this.processLayout(fmConfig, db, layoutName );
						break;
					}
				}
			}
		}
	}
	
	private void processLayout( FileMakerServerConfiguration fmConfig, FileMakerDatabase fmdb, String layoutName ) throws Exception
	{
		FileMakerLayout layout = new FileMakerLayout();
		FmpXmlResultMetadataRequest layoutReq = new FmpXmlResultMetadataRequest( fmConfig );
		FmpXmlResultMetadata metaLayout = layoutReq.findany( fmdb.getRealName(), layoutName );

		layout.setRealName( layoutName );

		for( String realName: metaLayout.getColumnTypes().keySet() )
		{
			String type = metaLayout.getColumnTypes().get(realName);
			FileMakerField field = new FileMakerField();
			
			field.setFmName(realName);
			field.setJavaType( type );
			
			layout.addField(field);
		}
		
		this.generateLayoutJavaApi( fmdb, layout );
	}
	
	private void generateLayoutJavaApi( FileMakerDatabase db, FileMakerLayout layout ) throws Exception
	{
		Map root = new HashMap();
		String extendedPackageName = this.basePackage + ".layouts." + db.getPackageizedName();
		extendedPackageName = extendedPackageName.toLowerCase();
		root.put("basePackageName", extendedPackageName );
		root.put("currentDatabasePackageName", db.getPackageizedName() );
		root.put("currentDatabaseRealName", db.getRealName() );

		root.put("layout", layout );
			
		Template entity = this.freemarkerConfiguration.getTemplate("EntityTemplate.ftl");
		Template query = this.freemarkerConfiguration.getTemplate("QueryTemplate.ftl");
			
		String folder = extendedPackageName;
		while( folder.indexOf( '.') > 0 )
		{
			folder = folder.replace( '.', File.separatorChar );
		}

		File entityFileFolder = new File( this.target + File.separator + folder + File.separator  );
		File queryFileFolder = new File( this.target + File.separator + folder + File.separator + "queries" + File.separator );
		File entityFile = new File( this.target + File.separator + folder + File.separator + layout.getNewClassName()  + ".java" );
		File queryFile = new File( this.target + File.separator + folder + File.separator + "queries" + File.separator + layout.getNewClassName() + "Query.java" );
			
		entityFileFolder.mkdirs();
		queryFileFolder.mkdirs();

		Writer entityWriter = new OutputStreamWriter( new FileOutputStream(entityFile) );
		Writer queryWriter = new OutputStreamWriter( new FileOutputStream( queryFile ) );
		entity.process(root, entityWriter );
		query.process( root, queryWriter );

		entityWriter.flush();
		queryWriter.flush();
	}
	
	public String getTarget() {
		return target;
	}

	public void setTarget(String srcDir) {
		this.target = srcDir;
	}
	
	public void setLayoutPrefix( String prefix )
	{
		this.layoutPrefixes.add(prefix);
	}
}
