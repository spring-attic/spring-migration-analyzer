<#include "/org/springframework/migrationanalyzer/render/support/html/item-header.ftl"/>
<div id="by_result_type_spring_integration">
	<@itemheader id="spring_${name}_integration_body" title="${title}"/>
	<#assign userLocationNames = userLocationsByName?keys>
	<div class="item-body content" id="spring_${name}_integration_body">
		<ul>
			<#list userLocationNames as userLocationName>
				<li><a href="${outputPathGenerator.generatePathFor(userLocationsByName[userLocationName])}">${userLocationName}</a></li>
			</#list>
		</ul>
	</div>
</div>
