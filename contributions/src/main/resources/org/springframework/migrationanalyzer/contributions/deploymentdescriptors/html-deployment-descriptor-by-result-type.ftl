<#include "/org/springframework/migrationanalyzer/render/support/html/item-header.ftl"/>
<div id="by-result-type-deployment-descriptor">
<#assign categories = deploymentDescriptors?keys>
<#list categories as category>
<@itemheader id="category_${category_index}_body" title="${category}"/>
  <div class="item-body" id="category_${category_index}_body">
<#assign descriptorTypes = deploymentDescriptors[category]>
<#assign descriptorTypeNames = descriptorTypes?keys>
<#list descriptorTypeNames as descriptorTypeName>
<@itemheader id="category_${category_index}_name_${descriptorTypeName_index}_body" title="${descriptorTypeName}"/>
    <div class="item-body" id="category_${category_index}_name_${descriptorTypeName_index}_body">
      <ul>
<#assign descriptors = descriptorTypes[descriptorTypeName]>
<#assign descriptorNames = descriptors?keys>
<#list descriptorNames as descriptorName>
        <li><a href="${descriptors[descriptorName]}">${descriptorName}</a></li>
</#list>
      </ul>
    </div>
</#list>
  </div>
</#list>
</div>
