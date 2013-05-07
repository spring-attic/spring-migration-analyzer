<programmatic-transaction-demarcation>
	<#assign transactionTypes = programmaticDemarcation?keys>
	<#list transactionTypes as transactionType>
		<transaction type="${transactionType}">
			<#assign demarcationTypes = programmaticDemarcation[transactionType]?keys>
			<#list demarcationTypes as demarcationType>
				<demarcation type="${demarcationType}">				
					<#assign usages = programmaticDemarcation[transactionType][demarcationType]>
					<#list usages as usage>
						<usage>${usage}</usage>
					</#list>					
				</demarcation>
			</#list>
		</transaction>
	</#list>	
</programmatic-transaction-demarcation>