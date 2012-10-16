<#include "/org/springframework/migrationanalyzer/render/support/html/item-header.ftl"/>
<div>
	<@itemheader id="messageDrivenBeansBody" title="${messageDrivenBeansTitle}"/>
	<div class="item-body indented" id="messageDrivenBeansBody">
		<#assign messageDrivenBeanNames = messageDrivenBeans?keys>
		<#list messageDrivenBeanNames as messageDrivenBeanName>
			<@itemheader id="${messageDrivenBeanName_index}Body" title="${messageDrivenBeanName}"/>
			<div class="item-body content" id="${messageDrivenBeanName_index}Body">
				<table>
					<#assign metadata = messageDrivenBeans[messageDrivenBeanName]>
					<#assign metadataNames = metadata?keys>
					<#list metadataNames as metadataName>
						<tr><td>${metadataName}</td><td>${metadata[metadataName]}</td></tr>
					</#list>
				</table>
			</div>
		</#list>
	</div>
</div>