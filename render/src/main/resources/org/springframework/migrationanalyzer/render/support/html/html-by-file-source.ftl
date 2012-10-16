<#include "/org/springframework/migrationanalyzer/render/support/html/item-header.ftl"/>
<div>
	<@itemheader id="source_body" title="Source"/>
	<div class="item-body content" id="source_body">
		<div class="source">
			<pre>${source?html}</pre>
		</div>
	</div>
</div>
