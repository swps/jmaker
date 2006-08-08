<#--
  This ftl needs the following in the freemarker context:
    * basePackageName - from the ant build file property
    * currentDatabasePackageName - the current database being introspected as a valid packageized string
    * currentDatabaseRealName - value of the -db parm. the current database being introspected
    * layout.realName - value of the -lay parm
    * layout.newClassName - the current Layout being introspected as a Class name camelized
    * layout.fields - a collection (probably list) of fields, each containing the following:
        - getter - the Xxx of getXxx.
        - javaType - the type (String, Double, Integer, Boolean, and soon Calendar)
        - enumName - the name of the corresponding enum element of the entity type
-->
package ${basePackageName};

import net.diaperrush.jmaker.layouts.FileMakerResult;
import net.diaperrush.jmaker.schemas.FmpXmlResult;

public class ${layout.newClassName}
{
  public enum Layout
  {
<#assign first = true>
    <#list layout.fields as field><#if first >${field.enumName}<#assign first = false><#else>, ${field.enumName}</#if></#list>;

    public static final String getDatabaseName()
    {
      return "${currentDatabaseRealName}";
    }
    public static final String getName()
    {
      return "${layout.realName}";
    }

    public String fmName()
    {
      switch( this )
      {
<#list layout.fields as field><#if field.fmName != field.enumName>
          case ${field.enumName}: return "${field.fmName}";</#if></#list>
          default:
            return this.name();
      }
    }
  }

  private FileMakerResult fmResult;

  public ${layout.newClassName}(FmpXmlResult fmResult, int resultRow)
  {
    this.fmResult = new FileMakerResult(fmResult, resultRow);
  }

<#list layout.fields as field><#if field.javaType?exists>
  public ${field.javaType} get${field.getter}()
  {
    return this.fmResult.columnAs${field.javaType}(Layout.${field.enumName}.fmName());
  }

</#if></#list>

  public Integer getFileMakerRecNum()
  {
    return this.fmResult.getFilemakerRecordId();
  }
}
