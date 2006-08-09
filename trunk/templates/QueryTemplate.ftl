<#--
  This ftl needs the following in the freemarker context:
    * basePackageName - from the ant build file property
    * currentDatabasePackageName - the current database being introspected as a valid packageized string
    * layout.newClassName - the current Layout being introspected as a Class name camelized
    * layout.fields - a collection (probably list) of fields, each containing the following:
        - setter - the Xxx of setXxx.  will also be the name of the parameter of the setter
        - javaType - the type (String, Double, Integer, Boolean, and soon Calendar)
        - enumName - the name of the corresponding enum element of the entity type
-->
package ${basePackageName}.queries;

import java.util.ArrayList;
import java.util.List;

import net.diaperrush.jmaker.FileMakerServerConfiguration;
import net.diaperrush.jmaker.layouts.AbstractFileMakerQuery;
import net.diaperrush.jmaker.schemas.FmpXmlResult;

import ${basePackageName}.${layout.newClassName};

public class ${layout.newClassName}Query  extends AbstractFileMakerQuery
{
  public ${layout.newClassName}Query( FileMakerServerConfiguration fmServer )
  {
    super(fmServer, ${layout.newClassName}.Layout.getDatabaseName(), ${layout.newClassName}.Layout.getName() );
  }

  public List<${layout.newClassName}> find()
  {
      FmpXmlResult xmlResult = this.fmFind();
      List<${layout.newClassName}> result = new ArrayList<${layout.newClassName}>();
      for( int i = 0; i < xmlResult.size(); i++ )
      {
          result.add( new ${layout.newClassName}( xmlResult, i ) );
      }
      
      return result;
  }
  
  public ${layout.newClassName} edit( int recNum )
  {
      FmpXmlResult xmlResult = this.fmEdit( recNum );
      return new ${layout.newClassName}( xmlResult, 0 );
  }
  
  public List<${layout.newClassName}> findany()
  {
      FmpXmlResult xmlResult = this.fmFindall();
      List<${layout.newClassName}> result = new ArrayList<${layout.newClassName}>();
      for( int i = 0; i < xmlResult.size(); i++ )
      {
          result.add( new ${layout.newClassName}( xmlResult, i ) );
      }
      
      return result;
  }

<#list layout.fields as field><#if field.javaType?exists>
  public void set${field.setter}( ${field.javaType} ${field.setter} )
  {
    String value = "";
    if( ${field.setter} != null ) value = ${field.setter}.toString();
    this.addEntry( ${layout.newClassName}.Layout.${field.enumName}.fmName(), value );
  }
</#if></#list>

}
