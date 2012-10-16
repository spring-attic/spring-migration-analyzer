<#include "/org/springframework/migrationanalyzer/render/support/html/item-header.ftl"/>
<div>
	<@itemheader id="entityBeansBody" title="${title}"/>
		<div class="item-body indented" id="entityBeansBody">
		<#assign entityBeanNames = entityBeans?keys>
		<#list entityBeanNames as entityBeanName>
			<@itemheader id="${entityBeanName_index}Body" title="${entityBeanName}"/>
			<div class="item-body content" id="${entityBeanName_index}Body">
				<table>
					<#assign metadata = entityBeans[entityBeanName]>
					<#assign metadataNames = metadata?keys>
					<#list metadataNames as metadataName>
						<tr><td>${metadataName}</td><td>${metadata[metadataName]}</td></tr>
					</#list>
				</table>
			</div>
		</#list>
	</div>
</div>
