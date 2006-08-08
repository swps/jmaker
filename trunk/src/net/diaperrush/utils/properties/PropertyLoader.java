package net.diaperrush.utils.properties;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

// ----------------------------------------------------------------------------
/**
 * A simple class for loading java.util.Properties backed by .properties files
 * deployed as classpath resources. See individual methods for details.
 * 
 * @author (C) <a
 *         href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad
 *         Roubtsov</a>, 2003
 */
public class PropertyLoader {
	public static final Logger logger = Logger.getLogger(PropertyLoader.class);

	/**
	 * @param name
	 *            classpath resource name [may not be null]
	 * @param loader
	 *            classloader through which to load the resource [null is
	 *            equivalent to the application loader]
	 * @return resource converted to java.util.Properties [may be null if the
	 *         resource was not found and THROW_ON_LOAD_FAILURE is false]
	 * @throws IllegalArgumentException
	 *             if the resource was not found and THROW_ON_LOAD_FAILURE is
	 *             true
	 */
	public static EnhancedProperties loadProperties(String name,
			ClassLoader loader) {
		if (name == null)
			throw new IllegalArgumentException("null input: name");

		if (name.startsWith("/"))
			name = name.substring(1);

		// if (name.endsWith (SUFFIX))
		// name = name.substring (0, name.length () - SUFFIX.length ());

		EnhancedProperties result = null;

		InputStream in = null;
		try {
			if (loader == null) {
				logger.warn( "Gave Property Loader a null ClassLoader to use." );
				System.err.println( "Gave Property Loader a null ClassLoader to use.  Defaulting to current thread's." );
				loader = Thread.currentThread().getContextClassLoader();
			}

			if (!name.endsWith(SUFFIX))
				name = name.concat(SUFFIX);

			// returns null on lookup failures:
			URL url = loader.getResource(name);
			System.err.println( "Resource Name: " + name + ", url: " + url );
			in = url.openStream();
			if (in != null) {
				result = new EnhancedProperties();
				result.load(in);
				in.close();
			}
		} catch (Exception e) {
			logger.error("Error loading " + name + ": ", e);
			result = null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Throwable ignore) {
				}
			}
		}

		if (THROW_ON_LOAD_FAILURE && (result == null)) {
			throw new IllegalArgumentException("Could not load [" + name + "]"
					+ " as a classloader resource");
		}

		return result;
	}

	/**
	 * A convenience overload of {@link #loadProperties(String, ClassLoader)}
	 * that uses the current thread's context classloader. A better strategy
	 * would be to use techniques shown in
	 * http://www.javaworld.com/javaworld/javaqa/2003-06/01-qa-0606-load.html
	 */
	public static EnhancedProperties loadProperties(final String name) {
		return loadProperties(name, Thread.currentThread()
				.getContextClassLoader());
	}

	/**
	 * This dude takes a set of properties and a propertyNamespaceKey. The
	 * property defined as propertyNamespaceKey has a comma-separated list of
	 * properties to examine and returns the subset of self-referenced
	 * properties. <br>
	 * <br>
	 * It is best examined. Consider a properties file containing among other
	 * stuff the following:<br>
	 * <br>
	 * <code>titles.dave.favorite.hobbies = cooking, rowing, crocheting</code>
	 * <code>titles.dave.favorite.hobbies.cooking = Cooking in the nude.</code>
	 * <code>titles.dave.favorite.hobbies.rowing = Rowing in the nude.</code>
	 * <code>titles.dave.favorite.hobbies.crocheting = Crocheting in the nude.</code>
	 * <br>
	 * <br>
	 * This method will return a <code>Properties</code> object that contains:
	 * <br>
	 * <code>titles.dave.favorite.hobbies.cooking = Cooking in the nude.</code>
	 * <code>titles.dave.favorite.hobbies.rowing = Rowing in the nude.</code>
	 * <code>titles.dave.favorite.hobbies.crocheting = Crocheting in the nude.</code>
	 * <br>
	 * Begs the question why dave likes to do so much in the nude. Any comments?
	 * A couple, you rat bastard. <br>
	 * 
	 * @param properties
	 * @param propertyNamespaceKey
	 * @return newProperties The subset of <code>properties</code> that has
	 *         the properties elements starting at propertyNamespaceKey, and
	 *         ending with each of the elements enumerated in that property.
	 */
	public static final Properties selfReferencedProperties(
			Properties properties, String propertyNamespaceKey) {
		logger.debug("Property namespace: " + propertyNamespaceKey);
		String propertyNameList = properties.getProperty(propertyNamespaceKey);
		Properties newProperties = new Properties();

		if (propertyNameList == null) {
			return newProperties;
		}

		logger.debug("Property Names list: " + propertyNameList);
		String[] propertyNames = propertyNameList.split(",");

		for (String name : propertyNames) {
			String key = propertyNamespaceKey + "." + name.trim();
			String value = properties.getProperty(key);
			logger.debug("Adding self-referenced property:  " + key + " | "
					+ value);

			newProperties.setProperty(key, value);
		}

		return newProperties;
	}

	/**
	 * This guy basically takes a set of properties ( properties of the type
	 * Properties<String,String> ) and returns a list of their values. Because
	 * Properties doesn't guarantee order, this doesn't either unless you
	 * specify <code>sortList</code>, which sorts on the key name.<br>
	 * <br>
	 * This is useful after getting a subset of Properties.
	 * 
	 * @see selfReferencedProperties
	 * 
	 * @param properties
	 * @return list list of strings contained in the properies object
	 */
	public static final List<String> getPropertyStrings(Properties properties,
			boolean sortList) {
		List<String> propertyStringsList = new ArrayList<String>();

		String[] keys = new String[properties.keySet().size()];
		properties.keySet().toArray(keys);

		if (sortList) {
			Arrays.sort(keys);
		}

		for (Object key : keys) {
			propertyStringsList.add(properties.getProperty(key.toString()));
		}

		return propertyStringsList;
	}

	private PropertyLoader() {
	} // this class is not extendable

	private static final boolean THROW_ON_LOAD_FAILURE = true;

	private static final String SUFFIX = ".properties";

}