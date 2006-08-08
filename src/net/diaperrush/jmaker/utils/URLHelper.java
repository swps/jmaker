/*
 * Created on May 9, 2005
 * 
 * (c) 2005 AllegroMedical
 */

package net.diaperrush.jmaker.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Holds methods to help work with urls.
 * 
 * @author Supreme Chancellor (a.k.a. Geoff Coffey)
 */
public abstract class URLHelper
{
  /**
   * encodes the url for you, but handles the stupid exception handling View java.net.URLEncoder.encode(String,String)
   */
  public static String encode(String url)
  {
    try
    {
      return URLEncoder.encode(url, "UTF-8");
    }
    catch (UnsupportedEncodingException e)
    {
      // this can't really happen that java doesn't support UTF-8, but if it does we'll throw a runtime exception
      throw new RuntimeException("unable to encode because I do not  understand utf-8: " + e.getMessage());
    }
  }

  /**
   * decodes the url for you, but handles the stupid exception handling View java.net.URLDecoder.encode(String,String)
   */
  public static String decode(String url)
  {
    try
    {
      return URLDecoder.decode(url, "UTF-8");
    }
    catch (UnsupportedEncodingException e)
    {
      // this can't really happen that java doesn't support UTF-8, but if it does we'll throw a runtime exception
      throw new RuntimeException("unable to decode because I do not  understand utf-8: " + e.getMessage());
    }
  }
  
}
