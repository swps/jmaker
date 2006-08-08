package net.diaperrush.jmaker.layouts;

import net.diaperrush.jmaker.schemas.FmpXmlResult;

import org.apache.log4j.Logger;


public class FileMakerResult
{
  @SuppressWarnings("unused")
  private static final Logger logger = Logger.getLogger(FileMakerResult.class);

  private FmpXmlResult fmResult;
  private int resultRow;
  
  public FileMakerResult( FmpXmlResult fmResult, int resultRow )
  {
    this.fmResult = fmResult;
    this.resultRow = resultRow;
  }

  public Boolean columnAsBoolean( String columnName)
  {
    return this.fmResult.columnAsBoolean(resultRow, columnName);
  }

  public Double columnAsDouble( String columnName)
  {
    return this.fmResult.columnAsDouble(resultRow, columnName);
  }

  public Integer columnAsInteger( String columnName)
  {
    return this.fmResult.columnAsInteger(resultRow, columnName);
  }

  public String columnAsString( String columnName)
  {
    return this.fmResult.columnAsString(resultRow, columnName);
  }

  public void debugRowDump()
  {
    this.fmResult.debugRowDump(resultRow);
  }

  public int getFilemakerRecordId()
  {
    return this.fmResult.getFilemakerRecordId(resultRow);
  }
}
