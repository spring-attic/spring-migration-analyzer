<entity-beans>
	<#assign entityBeanNames = entityBeans?keys>
	<#list entityBeanNames as entityBeanName>
		<entity-bean name="${entityBeanName}">
			<#assign metadata = entityBeans[entityBeanName]>
			<#assign metadataNames = metadata?keys>
			<#list metadataNames as metadataName>
				<metadata>
					<name>${metadataName}</name>
					<value>${metadata[metadataName]}</value>
				</metadata>
			</#list>
		</entity-bean>
	</#list>
</entity-beans>
	
