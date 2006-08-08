package net.diaperrush.jmaker.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

public class XmlHeaderStripperInputStream extends InputStream
{
  @SuppressWarnings("unused")
  private static final Logger logger = Logger.getLogger(XmlHeaderStripperInputStream.class);
  private boolean hasMarched = false;

  private InputStream inputStream;

  public XmlHeaderStripperInputStream(InputStream inputStream)
  {
    this.inputStream = inputStream;
  }

  public int available() throws IOException
  {
    this.march();
    return this.inputStream.available();
  }

  public void mark(int arg0)
  {
    this.inputStream.mark(arg0);
  }

  public boolean markSupported()
  {
    return this.inputStream.markSupported();
  }

  public int read() throws IOException
  {
    this.march();
    return this.inputStream.read();
  }

  public int read(byte[] arg0, int arg1, int arg2) throws IOException
  {
    this.march();
    int read = this.inputStream.read(arg0, arg1, arg2); 
    if( logger.isDebugEnabled() && read > 0 )
    {
    	logger.debug( new String( arg0, 0, read ));
    }
    return read;
  }

  public int read(byte[] arg0) throws IOException
  {
    this.march();
    int read = this.inputStream.read(arg0);
    if( logger.isDebugEnabled() && read > 0 )
    {
    	logger.debug( new String( arg0, 0, read ));
    }
    return read;
  }

  public void reset() throws IOException
  {
    this.inputStream.reset();
    this.hasMarched = false;
    this.march();
  }

  public long skip(long arg0) throws IOException
  {
    this.march();
    return this.inputStream.skip(arg0);
  }

  public String toString()
  {
    return this.inputStream.toString();
  }

  private void march() throws IOException
  {
    if ( !hasMarched )
    {
      byte[] bytes = new byte[1];
      while ( (this.inputStream.read(bytes)) != 0 && bytes[0] != '>')
      {
        logger.debug("Read byte: " + (char) bytes[0]);
      }
      logger.debug("Read byte: " + (char) bytes[0]);
      while ( (this.inputStream.read(bytes)) != 0 && bytes[0] != '>')
      {
        logger.debug("Read byte: " + (char) bytes[0]);
      }
      logger.debug("Read byte: " + (char) bytes[0]);
      hasMarched = true;
    }
  }

}
