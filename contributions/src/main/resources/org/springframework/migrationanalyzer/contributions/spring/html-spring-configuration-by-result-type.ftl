<#include "/org/springframework/migrationanalyzer/render/support/html/item-header.ftl"/>
<div id="by-result-type-spring-configuration">
	<@itemheader id="spring_configurations_body" title="Spring Configuration"/>
	<#assign names = springConfigurations?keys>
	<div class="item-body content" id="spring_configurations_body">
		<ul>
			<#list names as name>
				<li><a href="${outputPathGenerator.generatePathFor(springConfigurations[name])}">${name}</a></li>
			</#list>
		</ul>
	</div>
</div>
