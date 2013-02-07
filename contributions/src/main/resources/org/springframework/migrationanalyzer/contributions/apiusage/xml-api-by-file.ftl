<api-usage>
<#assign apiNames = apiUsage?keys>
	<#list apiNames as apiName>
		<api name="${apiName}">
			<#assign usageByType = apiUsage[apiName]>
			<#assign usageTypes = usageByType?keys>
			<#list usageTypes as usageType>
				<usageType type="${usageType}">
					<#assign usages = usageByType[usageType]>
					<#list usages as usage>
						<usage>${usage}</usage>
					</#list>
				</usageType>
			</#list>
		</api>
	</#list>
</api-usage>
