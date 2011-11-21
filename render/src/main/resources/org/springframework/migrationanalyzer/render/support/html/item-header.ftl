<#macro itemheader_link id title link>
<div class="item-header">
  <span class="toggle" onClick="toggle('${id}', this)">[-]</span><a href="${link}">${title}</a>
</div>
</#macro>

<#macro itemheader id title>
<div class="item-header">
  <span class="toggle" onClick="toggle('${id}', this)">[-]</span>${title}
</div>
</#macro>