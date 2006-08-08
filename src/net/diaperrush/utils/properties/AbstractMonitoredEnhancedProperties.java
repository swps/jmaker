package net.diaperrush.utils.properties;

import java.io.FileNotFoundException;

import net.diaperrush.utils.config.ComponentConfig;
import net.diaperrush.utils.io.FileChangeListener;
import net.diaperrush.utils.io.FileMonitor;
import net.diaperrush.utils.properties.EnhancedProperties;
import net.diaperrush.utils.properties.PropertyLoader;

import org.apache.log4j.Logger;

public abstract class AbstractMonitoredEnhancedProperties implements FileChangeListener, ComponentConfig
{
  @SuppressWarnings("unused")
  private static final Logger logger = Logger.getLogger(AbstractMonitoredEnhancedProperties.class);

  private final static long FILE_MONITOR_PERIOD = 5000;
  private FileMonitor fileMonitor;
  private String filename;
  private ClassLoader resourceLoader;
  
  protected EnhancedProperties properties;

  protected AbstractMonitoredEnhancedProperties( String filename )
  {
    this( filename, FILE_MONITOR_PERIOD );
  }
  
  protected AbstractMonitoredEnhancedProperties( String filename, long monitorPeriod )
  {
    this( filename, monitorPeriod, Thread.currentThread().getContextClassLoader() );
  }
  
  protected AbstractMonitoredEnhancedProperties( String filename, ClassLoader loader )
  {
	  this( filename, FILE_MONITOR_PERIOD, loader );
  }
  
  protected AbstractMonitoredEnhancedProperties( String filename, long monitorPeriod, ClassLoader loader )
  {
    this.filename = filename;
    this.resourceLoader = loader;
    this.fileMonitor = new FileMonitor();
    try
    {
      this.fileMonitor.addFileChangeListener(this, this.filename, monitorPeriod );
    }
    catch (FileNotFoundException e)
    {
      logger.error("Error finding file '" + this.filename
          + "' in class '"+this.getClass().getName()+"'.  You have bigger problems than simply not having a valid listener running, friend...", e);
    }
    this.reload( loader );
  }
  
  private final void reload( ClassLoader loader )
  {
    this.properties = PropertyLoader.loadProperties(this.filename, loader );
  }
  
  public final void fileChanged(String fileName)
  {
    this.reload( this.resourceLoader );
  }
  
  public final void shutdown(Object... objects)
  {
    this.shutdownImpl(objects);
    this.fileMonitor.close();
  }
  
  //if you need to do stuff, do it here.  I must close my listener down w/o
  //interferance
  protected void shutdownImpl(Object... objects)
  {
    
  }
}