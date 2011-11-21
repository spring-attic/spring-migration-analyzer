<#include "/org/springframework/migrationanalyzer/render/support/html/item-header.ftl"/>
<div id="summary-session-beans">
<@itemheader_link id="sessionBeansBody" title="Session Beans" link="${link}"/>
  <div class="item-body" id="sessionBeansBody">
    <ul>    
      <li>${statelessValue}</li>
      <li>${statefulValue}</li>
    </ul>
  </div>
</div>
