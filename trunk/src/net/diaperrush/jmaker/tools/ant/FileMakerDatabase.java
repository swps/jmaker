package net.diaperrush.jmaker.tools.ant;


public class FileMakerDatabase 
{
	private String realName;
	
	private static final String nameAsPackageizedName( String realname )
	{
		String name = realname.replaceAll( " ", "");
		
		return name.toLowerCase();
	}
	public FileMakerDatabase()
	{
	}
	public String getPackageizedName()
	{
		return nameAsPackageizedName( this.realName );
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
}
