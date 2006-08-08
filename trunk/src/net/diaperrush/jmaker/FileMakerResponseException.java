package net.diaperrush.jmaker;

import org.apache.log4j.Logger;

public class FileMakerResponseException extends RuntimeException
{
  /**
   * 
   */
  private static final long serialVersionUID = 77L;
  @SuppressWarnings("unused")
  private static final Logger logger = Logger.getLogger(FileMakerResponseException.class);
  
  public FileMakerResponseException()
  {
    super();
  }
  
  public FileMakerResponseException(String arg0)
  {
    super(arg0);
  }
  
  public FileMakerResponseException(String arg0, Throwable arg1)
  {
    super(arg0, arg1);
  }
  
  public FileMakerResponseException(Throwable arg0)
  {
    super(arg0);
  }

}
