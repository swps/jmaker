package net.diaperrush.jmaker.tools.ant;

import java.net.URL;
import java.util.Map;

import net.diaperrush.jmaker.FileMakerResponseException;
import net.diaperrush.jmaker.schemas.FmpXmlResult;

public class FmpXmlResultMetadata extends FmpXmlResult {

	public FmpXmlResultMetadata(URL url) throws FileMakerResponseException {
		super(url);
		// TODO Auto-generated constructor stub
	}

	public Map<String, String> getColumnTypes()
	{
		return this.columnTypes;
	}
}
