package net.diaperrush.utils.config;

public interface ComponentConfig
{
  /** If any data is needed for shutdown, pass it in. */
  public void shutdown( Object...objects );
}
