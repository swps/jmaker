package net.diaperrush.jmaker.tools.ant;

import java.net.MalformedURLException;
import java.net.URL;

import net.diaperrush.jmaker.FileMakerResponseException;
import net.diaperrush.jmaker.FileMakerServerConfiguration;
import net.diaperrush.jmaker.schemas.FmpXmlResult;
import net.diaperrush.jmaker.schemas.FmpXmlResultRequest;
import net.diaperrush.jmaker.utils.URLHelper;

import org.apache.log4j.Logger;

public class FmpXmlResultMetadataRequest extends FmpXmlResultRequest {
	
	private static final Logger logger = Logger.getLogger( FmpXmlResultMetadataRequest.class );

	public FmpXmlResultMetadataRequest(
			FileMakerServerConfiguration configuration) {
		super(configuration);
	}
	
	public FmpXmlResult dbnames()
	{
	    String urlString = this.configuration.getFileMakerServerUrlBase() + "?";
	    urlString += URL_PARM_DBNAMES;
	    URL url;
	    try
	    {
	      url = new URL( urlString );
	    }
	    catch (MalformedURLException e)
	    {
	      throw new FileMakerResponseException( "Malformed URL for FM dbnames request: " + urlString );
	    }
	    
	    FmpXmlResult result = new FmpXmlResult( url );
	    if( logger.isDebugEnabled() )
	    {
	    	logger.debug( "Fetched Database names.  " + result.size() + " of them: " );
	    	for( int i = 0; i < result.size(); i++ )
	    	{
	    		logger.debug("\t " + (i+1) + ": " + result.columnAsString( i, "DATABASE_NAME" ));
	    	}
	    }
	    return result;
	}
	
	public FmpXmlResult layoutnames( String db )
	{
	    String urlString = this.configuration.getFileMakerServerUrlBase() + "?";
	    urlString += URL_PARM_DB + "=" + URLHelper.encode( db );
	    urlString += "&" + URL_PARM_LAYOUT_NAMES;
	    URL url;
	    try
	    {
	      url = new URL( urlString );
	    }
	    catch (MalformedURLException e)
	    {
	      throw new FileMakerResponseException( "Malformed URL for FM layout request: " + urlString );
	    }
	    return new FmpXmlResult( url );
	}

	  public FmpXmlResultMetadata findany( String db, String layout ) throws FileMakerResponseException
	  {
	    String urlString = this.configuration.getFileMakerServerUrlBase();
	    urlString +=  "?" + URL_PARM_DB + "=" + URLHelper.encode(db);
	    urlString +=  "&" + URL_PARM_LAYOUT + "=" + URLHelper.encode(layout);
	    urlString += "&" + URL_PARM_FINDANY;
	    URL url;
	    try
	    {
	      url = new URL( urlString );
	    }
	    catch (MalformedURLException e)
	    {
	      throw new FileMakerResponseException( "Malformed URL for FM find request: " + urlString, e );
	    }
	    return new FmpXmlResultMetadata( url );
	  }
	  
	  private static final String URL_PARM_FINDANY = "-findany";
	  private static final String URL_PARM_DBNAMES = "-dbnames";
	  private static final String URL_PARM_LAYOUT_NAMES = "-layoutnames";
	  
	  public static final String COLUMN_FILEMAKER_DATABASE_NAME = "DATABASE_NAME";
	  public static final String COLUMN_FILEMAKER_LAYOUT_NAME = "LAYOUT_NAME";
}
