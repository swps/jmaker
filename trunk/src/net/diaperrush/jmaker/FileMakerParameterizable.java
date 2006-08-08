package net.diaperrush.jmaker;

import java.util.List;
import java.util.Map;

public interface FileMakerParameterizable
{
  public List<Map.Entry<String, String>> getParameters();
}
