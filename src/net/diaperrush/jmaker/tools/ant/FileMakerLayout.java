package net.diaperrush.jmaker.tools.ant;

import java.util.ArrayList;
import java.util.Collection;

public class FileMakerLayout {
	private Collection<FileMakerField> fields;

	private String realName;

	private static final String layoutAsNewClassName( String realName )
	{
		//TODO:  come back and do this for real
		String name = realName;
		name = name.replaceAll("-", "_" );
		name = name.replaceAll( "^[a-zA-Z0-9_]", "" );
		while( name.indexOf(' ' ) > 0 )
		{
			name = name.replace( ' ', '_');
		}
		return name;
	}
	public String getNewClassName()
	{
		return layoutAsNewClassName( this.realName );
	}
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public FileMakerLayout() {
		this.fields = new ArrayList<FileMakerField>();
	}

	public Collection<FileMakerField> getFields() {
		return fields;
	}

	public void addField(FileMakerField field) {
		this.fields.add(field);
	}
}
