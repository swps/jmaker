package net.diaperrush.utils.properties;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * An enhanced version of {@link java.util.Properties}.
 * 
 * An implementation of the {@link java.util.Properties} class with the
 * following enhancements:
 * 
 * <ul>
 * <li>property values can reference other properties in the same object with
 * ${key} syntax. These references are automatically expanded when
 * {@link #getProperty(String) getProperty()} is called.</li>
 * </uL>
 * 
 * Sample .properties file: <pre>
 * # normal properties
 * first-name=Geoff
 * last-name=Coffey
 * 
 * #this property includes two property references
 * full-name=${first-name} ${last-name}
 * 
 * #this property references the 'full-name' property, which in
 * #turn includes references; you can nest properties like this 
 * #ad infinitum
 * salutation=Dear ${full-name}:
 * </pre>
 * 
 * Recureive property references are (obviously) not allowed. For example, given
 * this .properties file: <pre>
 * a=${b}
 * b=${a}
 * </pre>
 * the value of {@code a} would be {@code ${a}}.
 * 
 * @author gcoffey
 */
public class EnhancedProperties extends Properties 
{
	private static final Logger logger = Logger.getLogger(EnhancedProperties.class);
	private static final long serialVersionUID = 77;
	private static final Pattern propertyReferencePattern = Pattern.compile("\\$\\{([^\\}]+)\\}");

	/**
	 * returns the property value named by {@code key}.
	 * 
	 * @param key
	 *            the property key
	 * @return the property value with any nested property references expanded
	 */
	@Override
	public String getProperty(String key) 
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("top-level request for property '" + key + "'; value is '" + super.getProperty(key)+ "'");
		}
		return this.getProperty(key, new HashSet<String>());
	}
	
	private String getProperty(String key, Set<String>outerKeySet)
	{
		String value = super.getProperty(key);
		if (value == null)
		{
			return null;
		}
		
		Matcher matcher = propertyReferencePattern.matcher(value);
		StringBuffer resultBuffer = new StringBuffer();
		outerKeySet.add(key);

		while (matcher.find())
		{
			String nestedPropKey = matcher.group(1);
			
			// if this property references itself, skip the replacement so we
			// don't loop forever
			if (outerKeySet.contains(nestedPropKey))
			{
				logger.warn("found circular property reference in key '" + key + "'");
				continue;
			}

			if (logger.isDebugEnabled())
			{
				logger.debug("found property reference '" + nestedPropKey
						+ "' in key '" + key + "'; expanding...");
			}
			
			String replacement = this.getProperty(nestedPropKey, outerKeySet);
			if (replacement == null)
			{
				continue;
			}
			matcher.appendReplacement(resultBuffer, this.escape(replacement));
		}

		matcher.appendTail(resultBuffer);
		return resultBuffer.toString().trim();
	}

	private String escape(String value) {
		return value.replace("\\", "\\\\").replace("$", "\\$");
	}
}
