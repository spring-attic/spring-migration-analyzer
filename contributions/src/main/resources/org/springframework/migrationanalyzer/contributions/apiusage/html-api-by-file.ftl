<#include "/org/springframework/migrationanalyzer/render/support/html/item-header.ftl"/>
<#assign apiNames = apiUsage?keys>
<#list apiNames as apiName>
<div>
<@itemheader_link id="api_${apiName_index}_body" title="${apiName} API Usage" link="${link}"/>
<div class="item-body" id="api_${apiName_index}_body">
<#assign usageByType = apiUsage[apiName]>
<#assign usageTypes = usageByType?keys>
<#list usageTypes as usageType>
<@itemheader id="api_${apiName_index}_usage_${usageType_index}_body" title="${usageType}"/>
<div class="item-body" id="api_${apiName_index}_usage_${usageType_index}_body">
<#assign usages = usageByType[usageType]>
<ul>
<#list usages as usage>
<li>${usage}</li>
</#list>
</ul>
</div>
</#list>
</div>
</div>
</#list>