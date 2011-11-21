<#include "/org/springframework/migrationanalyzer/render/support/html/item-header.ftl"/>
<div id="summary-deployment-descriptor">
<@itemheader_link id="deploymentDescriptorBody" title="Deployment Descriptors" link="${link}"/>
  <div class="item-body" id="deploymentDescriptorBody">
<#assign categories = deploymentDescriptorCounts?keys>
    <ul>
<#list categories as category>
      <li>${deploymentDescriptorCounts[category]} ${category}</li>
</#list>
    </ul>
  </div>
</div>
