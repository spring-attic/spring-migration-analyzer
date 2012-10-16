<#include "/org/springframework/migrationanalyzer/render/support/html/item-header.ftl"/>
<div id="summary-api-usage">
	<@itemheader_link id="apiUsageBody" title="API Usage" link="${link}"/>
	<div class="item-body content" id="apiUsageBody">
		<ul>
			<#list entries as entry>
				<li>${entry}</li>
			</#list>
		</ul>
	</div>
</div>
