package net.diaperrush.jmaker.layouts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.diaperrush.jmaker.FileMakerParameterizable;
import net.diaperrush.jmaker.FileMakerResponseException;
import net.diaperrush.jmaker.FileMakerServerConfiguration;
import net.diaperrush.jmaker.schemas.FmpXmlResult;
import net.diaperrush.jmaker.schemas.FmpXmlResultRequest;
import net.diaperrush.jmaker.utils.ImmutablePair;

import org.apache.log4j.Logger;

public abstract class AbstractFileMakerQuery<T> implements FileMakerParameterizable
{
  @SuppressWarnings("unused")
  private static final Logger logger = Logger.getLogger(AbstractFileMakerQuery.class);

  protected FileMakerServerConfiguration fmServer;
  private FmpXmlResultRequest fmRequest;
  private List<Map.Entry<String, String>> entries;
  private List<Map.Entry<String, String>> ranges;

  protected AbstractFileMakerQuery(FileMakerServerConfiguration fmServer, String fmDbName, String layoutName)
  {
    this.fmServer = fmServer;
    this.entries = new ArrayList<Map.Entry<String, String>>();
    this.ranges = new ArrayList<Map.Entry<String, String>>();
    this.fmRequest = new FmpXmlResultRequest( this.fmServer );
    
    this.fmRequest.setParameterizable( this );
    this.addEntry( FmpXmlResultRequest.URL_PARM_DB, fmDbName );
    this.addEntry( FmpXmlResultRequest.URL_PARM_LAYOUT, layoutName );
  }
  
  abstract protected T createRecord( FmpXmlResult xmlResult, int recNum );

  public final void addRange(Map.Entry<String, String> range)
  {
    this.ranges.add(range);
  }

  public final void addEntry(Map.Entry<String, String> entry)
  {
    this.entries.add(entry);
  }

  protected final void addEntry(String column, String value)
  {
    Map.Entry<String, String> entry = new ImmutablePair<String, String>(column, value);
    this.addEntry(entry);
  }
  
  protected final void addEntry( String column, Integer intvalue )
  {
    String value = "";
    if( intvalue != null ) value = intvalue.toString();
    this.addEntry( column, value );
  }

  protected final void addEntry( String column, Double doublevalue )
  {
    String value = "";
    if( doublevalue != null ) value = doublevalue.toString();
    this.addEntry( column, value );
  }

  protected final void addEntry( String column, Boolean bvalue )
  {
    String value = this.getBooleanStringValue(bvalue);
    this.addEntry( column, value );
  }

  protected String getBooleanStringValue( Boolean b )
  {
    if( b == null || !b )
      return "false";
    return "true";
  }

  public final List<Map.Entry<String, String>> getParameters()
  {
    List<Map.Entry<String, String>> parms = new ArrayList<Map.Entry<String, String>>();
    parms.addAll(this.entries);
    parms.addAll(this.ranges);
    return parms;
  }
  
  /* --------- try this on for size ---------------- */
  public final List<T> find()
  {
      FmpXmlResult xmlResult = this.fmRequest.find();;
      List<T> result = new ArrayList<T>();
      for( int i = 0; i < xmlResult.size (); i++ )
      {
          result.add( this.createRecord( xmlResult, i ) );
      }
     
      return result;
  }
 
  public final T edit( int recNum )
  {
      FmpXmlResult xmlResult = this.fmRequest.edit (recNum);
      return this.createRecord( xmlResult, 0 );
  }
 
  public final List<T> findall()
  {
      FmpXmlResult xmlResult = this.fmRequest.findall();
      List<T> result = new ArrayList<T>();
      for( int i = 0; i < xmlResult.size(); i++ )
      {
          result.add( this.createRecord( xmlResult, i ) );
      }
     
      return result;
  }

  
}
