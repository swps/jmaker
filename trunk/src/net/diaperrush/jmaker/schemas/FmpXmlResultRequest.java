package net.diaperrush.jmaker.schemas;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import net.diaperrush.jmaker.FileMakerParameterizable;
import net.diaperrush.jmaker.FileMakerResponseException;
import net.diaperrush.jmaker.FileMakerServerConfiguration;
import net.diaperrush.jmaker.utils.URLHelper;

import org.apache.log4j.Logger;


//I shouldn't need a shutdownImpl()
//http://xml:1mp0rt@sqldev.allegro.med/fmi/xml/FMPXMLRESULT.xml?-db=Incredible+Bulk&-lay=xml-sku&BATCH_ID=5001&-find";
public class FmpXmlResultRequest
{
  @SuppressWarnings("unused")
  private static final Logger logger = Logger.getLogger(FmpXmlResultRequest.class);

  protected FileMakerServerConfiguration configuration;
  private FileMakerParameterizable parameters;
  public FmpXmlResultRequest( FileMakerServerConfiguration configuration )
  {
    this.configuration = configuration;
  }
  
  public FmpXmlResult find() throws FileMakerResponseException
  {
    String urlString = this.createUrlBase();
    urlString += "&" + URL_PARM_FIND;
    URL url;
    try
    {
      url = new URL( urlString );
    }
    catch (MalformedURLException e)
    {
      throw new FileMakerResponseException( "Malformed URL for FM find request: " + urlString, e );
    }
    return new FmpXmlResult( url );
  }
  
  public FmpXmlResult findall() throws FileMakerResponseException
  {
	    String urlString = this.createUrlBase();
    urlString += "&" + URL_PARM_FINDALL;
    URL url;
    try
    {
      url = new URL( urlString );
    }
    catch (MalformedURLException e)
    {
      throw new FileMakerResponseException( "Malformed URL for FM findall request: " + urlString );
    }
    return new FmpXmlResult( url );
  }
  
  public FmpXmlResult edit( int recnum ) throws FileMakerResponseException
  {
	    String urlString = this.createUrlBase();
    urlString += "&" + URL_PARM_RECNUM + "=" + recnum;
    urlString += "&" + URL_PARM_EDIT;
    URL url;
    try
    {
      url = new URL( urlString );
    }
    catch (MalformedURLException e)
    {
      throw new FileMakerResponseException( "Malformed URL for FM edit request: " + urlString, e );
    }
    return new FmpXmlResult( url );
  }
  
  public final void setParameterizable( FileMakerParameterizable params )
  {
    this.parameters = params;
  }

  protected String createUrlBase()
  {
	    String urlString = this.configuration.getFileMakerServerUrlBase() + "?";
	    urlString += this.getColumnParametersAsUrlString();

	    return urlString;
  }
  
  private String getColumnParametersAsUrlString()
  {
    String parmString = "";
    if( this.parameters != null )
    {
      for( Map.Entry<String, String> entry: this.parameters.getParameters() )
      {
        parmString = parmString + "&";
        parmString = parmString + entry.getKey() + "=" + URLHelper.encode(entry.getValue());
      }
    }
    
    return parmString;
  }
  
  //these are name/value pairs
  public static final String URL_PARM_DB = "-db";
  public static final String URL_PARM_LAYOUT = "-lay";
  //these are more "command"-like
  private static final String URL_PARM_FIND = "-find";
  private static final String URL_PARM_FINDALL = "-findall";
  private static final String URL_PARM_EDIT = "-edit";
  private static final String URL_PARM_RECNUM = "-recid";
}
