package net.diaperrush.jmaker;

import java.util.Properties;

import net.diaperrush.utils.properties.AbstractMonitoredEnhancedProperties;
import net.diaperrush.utils.properties.TypedProperty;

import org.apache.log4j.Logger;

public class FileMakerServerConfiguration extends AbstractMonitoredEnhancedProperties
{
  @SuppressWarnings("unused")
  private static final Logger logger = Logger.getLogger(FileMakerServerConfiguration.class);

  public static final String FILEMAKER_SERVER_FILENAME = "filemaker.server.properties";
  
  private static final String KEY_FM_WEB_URL = "filemaker.server.url";
  private static final String KEY_DATABASE_NAME_PREFIX = "filemaker.server.fmpxmlresult.database-name";
  private static final String KEY_LAYOUT_NAME_PREFIX = "filemaker.server.fmpxmlresult.layouts";

  private FileMakerServerConfiguration()
  {
    super(FILEMAKER_SERVER_FILENAME);
  }
  
  private FileMakerServerConfiguration( ClassLoader resourceLoader )
  {
    super(FILEMAKER_SERVER_FILENAME, resourceLoader );
  }
  
  public static final FileMakerServerConfiguration createInstance()
  {
    return new FileMakerServerConfiguration();
  }

  public static final FileMakerServerConfiguration createInstance( ClassLoader resourceClassLoader )
  {
    return new FileMakerServerConfiguration( resourceClassLoader );
  }

  public String getFileMakerDatabaseName( String keyNamespace )
  {
    String key = KEY_DATABASE_NAME_PREFIX + "." + keyNamespace;
    String value = TypedProperty.getString(this.properties, key );
    if( value == null )
    {
      throw new RuntimeException( "Property: " + key + " in file: " + FILEMAKER_SERVER_FILENAME + " wasn't set." );
    }
    
    return value;
  }
  
  public String getLayoutName( String keyNamespace )
  {
    String key = KEY_LAYOUT_NAME_PREFIX + "." + keyNamespace;
    String value = TypedProperty.getString(this.properties, key );
    if( value == null )
    {
      throw new RuntimeException( "Property: " + key + " in file: " + FILEMAKER_SERVER_FILENAME + " wasn't set." );
    }
    
    return value;
  }
  
  public String getFileMakerServerUrlBase()
  {
    String key = KEY_FM_WEB_URL;
    String value = TypedProperty.getString(this.properties, key );
    if( value == null )
    {
      throw new RuntimeException( "Property: " + key + " in file: " + FILEMAKER_SERVER_FILENAME + " wasn't set." );
    }
    
    return value;
  }
  
  public Properties getProperties()
  {
    return new Properties( this.properties );
  }
}
