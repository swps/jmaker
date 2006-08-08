package net.diaperrush.utils.properties;

import java.util.Properties;

import org.apache.log4j.Logger;

public class TypedProperty
{
  private static final Logger logger = Logger.getLogger(TypedProperty.class);

  public static final boolean getBoolean( Properties properties, String key )
  {
      String prop = properties.getProperty( key );
      return Boolean.parseBoolean( prop );
  }
  
  public static final int getInt( Properties properties, String key ) throws ImproperPropertyException
  {
    int value;
    String prop = properties.getProperty(key);

    try
    {
      value = Integer.parseInt( prop );
    }
    catch (NumberFormatException e )
    {
      String error = "Improper (int) format for property " + key +".";
      logger.warn( error, e );
      throw new ImproperPropertyException( error, e );
    }
    catch ( NullPointerException e )
    {
      String error = "Insufficient properties.  Property " + key + " not set.";
      logger.warn( error, e );
      throw new ImproperPropertyException( error, e );
    }
    
    return value;
  }
  
  public static final long getLong( Properties properties, String key ) throws ImproperPropertyException
  {
    long value;
    String prop = properties.getProperty(key);

    try
    {
      value = Long.parseLong( prop );
    }
    catch (NumberFormatException e )
    {
      String error = "Improper (long) format for property " + key +".";
      logger.warn( error, e );
      throw new ImproperPropertyException( error, e );
    }
    catch ( NullPointerException e )
    {
      String error = "Insufficient properties.  Property " + key + " not set.";
      logger.warn( error, e );
      throw new ImproperPropertyException( error, e );
    }
    
    return value;
  }
  
  public static final String getString( Properties properties, String key, boolean required ) throws ImproperPropertyException
  {
    String value = properties.getProperty( key );
    if( value == null )
    {
      String error = "Insufficient properties.  Property " + key + " not set.";
      logger.warn( error );
      throw new ImproperPropertyException( error );
    }
    return value;
  }
  public static final String getString( Properties properties, String key )
  {
    try
    {
      return getString( properties, key, false );
    }
    catch (ImproperPropertyException e)
    {
      return null;
    }
  }
  
  private TypedProperty() {};
}
