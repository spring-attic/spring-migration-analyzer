<#macro sessionBeanItem sessionBeans type>
	<${type}>	
		<#assign sessionBeanNames = sessionBeans?keys>
		<#list sessionBeanNames as sessionBeanName>
			<session-bean name="${sessionBeanName}">
				<#assign metadata = sessionBeans[sessionBeanName]>
				<#assign metadataNames = metadata?keys>
				<#list metadataNames as metadataName>
					<metadata>
						<name>${metadataName}</name>
						<value>${metadata[metadataName]}</value>
					</metadata>
				</#list>			
			</session-bean>		
		</#list>
	</${type}>
</#macro>
<session-beans>
	<@sessionBeanItem statelessSessionBeans "stateless"/>
	<@sessionBeanItem statefulSessionBeans "stateful"/>
</session-beans>