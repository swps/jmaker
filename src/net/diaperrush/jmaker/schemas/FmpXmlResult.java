package net.diaperrush.jmaker.schemas;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import net.diaperrush.jmaker.FileMakerResponseException;
import net.diaperrush.jmaker.fmpxmlresult.FMPXMLRESULT;
import net.diaperrush.jmaker.fmpxmlresult.FMPXMLRESULTType.METADATAType.FIELDType;
import net.diaperrush.jmaker.fmpxmlresult.FMPXMLRESULTType.RESULTSETType.ROWType;
import net.diaperrush.jmaker.fmpxmlresult.FMPXMLRESULTType.RESULTSETType.ROWType.COLType;
import net.diaperrush.jmaker.utils.XmlHeaderStripperInputStream;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

/**
 * This class is the go-between amongst the java world and the data FileMaker
 * returns
 */
public class FmpXmlResult
{
  @SuppressWarnings("unused")
  private static final Logger logger = Logger.getLogger(FmpXmlResult.class);

  private URL url;
  private Map<String, Integer> columnOffsets;
  protected Map<String, String> columnTypes;
  private FMPXMLRESULT fmpResult;

  @SuppressWarnings("unchecked")
  public FmpXmlResult(URL url) throws FileMakerResponseException
  {
    this.url = url;
    this.columnOffsets = new HashMap<String, Integer>();
    this.columnTypes = new HashMap<String, String>();

    logger.info(" Going to open URL: " + url.toExternalForm());
    try
    {
      
    URLConnection urlConnection = url.openConnection();
    if ( url.getUserInfo() != null )
    {
      logger.debug("setting http Authorization header");
      urlConnection.setRequestProperty("Authorization", "Basic "
          + new BASE64Encoder().encode(url.getUserInfo().getBytes()));
    }

    JAXBContext jaxCtx = JAXBContext.newInstance(FMPXMLRESULT.class.getPackage().getName(), this.getClass().getClassLoader() );
    Unmarshaller unmarshaller = jaxCtx.createUnmarshaller();

    unmarshaller.setValidating(false);
    unmarshaller.setEventHandler(new ValidationEventHandler()
    {
      public boolean handleEvent(ValidationEvent event)
      {
        System.out.println("I'm ignoring you.");
        return true;
      }
    });

    InputStream is = new XmlHeaderStripperInputStream(urlConnection.getInputStream());

    this.fmpResult = (FMPXMLRESULT) unmarshaller.unmarshal(is);
    }
    catch( IOException e )
    {
      throw new FileMakerResponseException( "IO Exception", e );
    }
    catch (JAXBException e)
    {
      throw new FileMakerResponseException( "Jaxb Exception", e );
    }

    List<FIELDType> fields = (List<FIELDType>) this.fmpResult.getMETADATA().getFIELD();
    for ( int i = 0; i < fields.size(); i++ )
    {
      FIELDType field = fields.get(i);
      this.columnOffsets.put(field.getNAME(), i);
      this.columnTypes.put(field.getNAME(), field.getTYPE() );
    }
  }

  public Integer columnAsInteger(int record, String columnName)
  {
    String data = this.getColumnData(record, columnName);
    Integer result = null;

    try
    {
      result = Integer.parseInt(data);
    }
    catch (Exception e)
    {
      logger.debug("Unsuccessfully Tried to get column '" + columnName + "' from row " + record + " as an Integer: "
          + data, e);
    }
    return result;
  }

  public String columnAsString(int record, String columnName)
  {
    return this.getColumnData(record, columnName);
  }

  public Double columnAsDouble(int record, String columnName)
  {
    String data = this.getColumnData(record, columnName);
    Double result = null;

    try
    {
      result = Double.parseDouble(data);
    }
    catch (Exception e)
    {
      logger.debug("Unsuccessfully Tried to get column '" + columnName + "' from row " + record + " as a Double: "
          + data, e);
    }
    return result;
  }

  public Boolean columnAsBoolean(int record, String columnName)
  {
    String data = this.getColumnData(record, columnName);
    Boolean result = null;

    try
    {
      result = Boolean.parseBoolean(data);
    }
    catch (Exception e)
    {
      logger.debug("Unsuccessfully Tried to get column '" + columnName + "' from row " + record + " as a Boolean: "
          + data, e);
    }
    return result;
  }

  public int size()
  {
    return this.fmpResult.getRESULTSET().getFOUND().intValue();
  }

  private String getColumnData(int record, String columnName)
  {
    if ( record > this.size() - 1 )
    {
      throw new RuntimeException("Requesting an invalid result.");
    }

    ROWType row = (ROWType) this.fmpResult.getRESULTSET().getROW().get(record);
    int offset = this.columnOffsets.get(columnName);

    COLType col = (COLType) row.getCOL().get(offset);
    String data = col.getDATA();

    if ( data == null )
    {
      data = "";
    }
    return data;
  }

  public int getFilemakerRecordId(int record)
  {
    ROWType row = (ROWType) this.fmpResult.getRESULTSET().getROW().get(record);
    return row.getRECORDID().intValue();
  }

  public int getFileMakerResult()
  {
    return this.fmpResult.getERRORCODE().intValue();
  }

  @SuppressWarnings("unchecked")
  public void debugResultDump()
  {
    int result = this.getFileMakerResult();
    logger.debug("URL: " + this.url.toString());
    logger.debug("Result: " + result);
    if ( result != 0 )
    {
      logger.debug("Error in recordset.");
    }
    else
    {
      logger.debug("Records: " + this.size());

      String message = "";
      List<FIELDType> fields = (List<FIELDType>) this.fmpResult.getMETADATA().getFIELD();
      for ( int i = 0; i < fields.size(); i++ )
      {
        FIELDType field = fields.get(i);
        message += field.getNAME() + ", ";
      }
      logger.debug("Column Names: " + message);

      for ( int i = 0; i < this.size(); i++ )
      {
        this.debugRowDump(i);
      }
    }
  }

  public void debugRowDump(int resultRow)
  {
    logger.debug("Row " + (resultRow + 1) + " - - - - - - - -");
    for ( String colName : this.columnOffsets.keySet() )
    {
      String colValue = this.columnAsString(resultRow, colName);
      logger.debug("\t" + colName + ": " + colValue);
    }
  }

}