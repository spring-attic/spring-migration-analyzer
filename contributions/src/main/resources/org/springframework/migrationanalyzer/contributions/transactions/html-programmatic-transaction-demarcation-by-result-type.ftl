<#include "/org/springframework/migrationanalyzer/render/support/html/item-header.ftl"/>
<div>
	<@itemheader id="programmatic_transaction_demarcation_body" title="Programmatic Transaction Demarcation"/>
	<div class="item-body indented" id="programmatic_transaction_demarcation_body">
		<#assign transactionTypes = programmaticDemarcation?keys>
		<#list transactionTypes as transactionType>
			<@itemheader id="programmatic_${transactionType}_transaction_demarcation_body" title="${transactionType} Transactions"/>
			<div class="item-body indented" id="programmatic_${transactionType}_transaction_demarcation_body">
				<#assign demarcationTypes = programmaticDemarcation[transactionType]?keys>        
				<#list demarcationTypes as demarcationType>
					<@itemheader id="programmatic_${transactionType}_${demarcationType}_body" title="${demarcationType}"/>
					<div class="item-body content" id="programmatic_${transactionType}_${demarcationType}_body">
						<#assign usages = programmaticDemarcation[transactionType][demarcationType]>
						<#assign users = usages?keys>
						<ul>
							<#list users as user>
							<li>${user}</li>
								<ul>
									<#list usages[user] as description>
										<li>${description}</li>
									</#list>
								</ul>
							</#list>
						</ul>
					</div>
				</#list>
			</div>
		</#list>
	</div>
</div>