package net.diaperrush.jmaker.utils;

import java.util.Map;

import org.apache.log4j.Logger;

public class ImmutablePair<K, V> implements Map.Entry<K, V>
{
  @SuppressWarnings("unused")
  private static final Logger logger = Logger.getLogger(ImmutablePair.class);
  private K key;
  private V value;
  
  public ImmutablePair( K key, V value )
  {
    this.key = key;
    this.value = value;
  }

  public K getKey()
  {
    return this.key;
  }
  public V getValue()
  {
    return this.value;
  }
  public V setValue(V arg0)
  {
    throw new UnsupportedOperationException( "This is immutable" );
  }

}
