package net.diaperrush.utils.properties;

import org.apache.log4j.Logger;

public class ImproperPropertyException extends Exception
{
  private static final long serialVersionUID = 77;
  
  @SuppressWarnings("unused")
 private static final Logger logger = Logger.getLogger(ImproperPropertyException.class);
  
  public ImproperPropertyException()
  {
    super();
    // TODO Auto-generated constructor stub
  }
  
  public ImproperPropertyException(String arg0)
  {
    super(arg0);
    // TODO Auto-generated constructor stub
  }
  
  public ImproperPropertyException(String arg0, Throwable arg1)
  {
    super(arg0, arg1);
    // TODO Auto-generated constructor stub
  }
  
  public ImproperPropertyException(Throwable arg0)
  {
    super(arg0);
    // TODO Auto-generated constructor stub
  }

}
