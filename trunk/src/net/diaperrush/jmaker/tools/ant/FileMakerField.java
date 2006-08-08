package net.diaperrush.jmaker.tools.ant;

public class FileMakerField {
	private String fmName;

	private String javaType;
	
	private enum JavaType
	{
		NUMBER, TEXT, TIMESTAMP;
		public static final JavaType from( String string )
		{
			JavaType type = null;
			try
			{
                type = JavaType.valueOf( string );
			}
			catch( Exception e )
			{
				//ignore
			}
			
			return type;
		}
	}

	private static final String fieldAsAccessorMutator( String fieldName )
	{
		String name = fieldName.replaceAll( ":", "_" );
		return name;
	}
	private static final String fieldAsEnumName( String fieldName )
	{
		String name = fieldName.replaceAll( ":", "_" );
		return name;
	}
	public String getGetter()
	{
		return fieldAsAccessorMutator( this.fmName );
	}
	
	public String getSetter()
	{
		return fieldAsAccessorMutator( this.fmName );
	}
	public String getEnumName() {
		return fieldAsEnumName( this.fmName );
	}

	public String getJavaType() {
		JavaType type = JavaType.from( this.javaType );
		switch( type )
		{
		case NUMBER:
			return "Double";
		case TEXT:
			return "String";
		default:
			return null;
		}
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getFmName() {
		return fmName;
	}

	public void setFmName(String fmName) {
		this.fmName = fmName;
	}
}
