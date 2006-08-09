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

import net.diaperrush.jmaker.FileMakerServerConfiguration;
import net.diaperrush.jmaker.layouts.AbstractFileMakerQuery;
import net.diaperrush.jmaker.schemas.FmpXmlResult;

import ${basePackageName}.${layout.newClassName}Record;

public class ${layout.newClassName}Query  extends AbstractFileMakerQuery<${layout.newClassName}Record>
{
  public ${layout.newClassName}Query( FileMakerServerConfiguration fmServer )
  {
    super(fmServer, ${layout.newClassName}Record.Layout.getDatabaseName(), ${layout.newClassName}Record.Layout.getName() );
  }

  @Override
  protected ${layout.newClassName}Record createRecord( FmpXmlResult xmlResult, int recNum )
  {
      return new ${layout.newClassName}Record( xmlResult, recNum );
  }

<#list layout.fields as field><#if field.javaType?exists>
  public void set${field.setter}( ${field.javaType} ${field.setter} )
  {
    String value = "";
    if( ${field.setter} != null ) value = ${field.setter}.toString();
    this.addEntry( ${layout.newClassName}Record.Layout.${field.enumName}.fmName(), value );
  }
</#if></#list>

}
