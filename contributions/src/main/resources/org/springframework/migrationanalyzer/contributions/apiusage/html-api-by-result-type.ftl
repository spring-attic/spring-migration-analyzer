<#include "/org/springframework/migrationanalyzer/render/support/html/item-header.ftl"/>
<#assign apiNames = apiUsage?keys>
<#list apiNames as apiName>
<div>
<@itemheader id="${apiName_index}Body" title="${apiName}"/>
  <div class="item-body" id="${apiName_index}Body">
<#assign usages = apiUsage[apiName]>
    <ul>
<#list usages as usage>
      <li>${usage}</li>
</#list>
    </ul>
  </div>
</div>
</#list>
