<message-driven-beans>
	<#assign messageDrivenBeanNames = messageDrivenBeans?keys>
	<#list messageDrivenBeanNames as messageDrivenBeanName>
		<message-driven-bean name="${messageDrivenBeanName}">
			<#assign metadata = messageDrivenBeans[messageDrivenBeanName]>
			<#assign metadataNames = metadata?keys>
			<#list metadataNames as metadataName>
				<metadata>
					<name>${metadataName}</name>
					<value>${metadata[metadataName]}</value>
				</metadata>
			</#list>		
		</message-driven-bean>
	</#list>
</message-driven-beans>