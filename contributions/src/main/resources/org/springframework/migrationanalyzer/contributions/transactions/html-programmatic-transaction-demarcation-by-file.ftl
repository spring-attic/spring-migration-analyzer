<#include "/org/springframework/migrationanalyzer/render/support/html/item-header.ftl"/>
<div>
	<@itemheader_link id="programmatic_transaction_demarcation_body" title="Programmatic Transaction Demarcation" link="${link}"/>
	<div class="item-body indented" id="programmatic_transaction_demarcation_body">
		<#assign transactionTypes = programmaticDemarcation?keys>
		<#list transactionTypes as transactionType>
			<@itemheader_link id="programmatic_${transactionType}_transaction_demarcation_body" title="${transactionType} Transactions" link="${link}"/>
			<div class="item-body indented" id="programmatic_${transactionType}_transaction_demarcation_body">
				<#assign demarcationTypes = programmaticDemarcation[transactionType]?keys>
				<#list demarcationTypes as demarcationType>
					<@itemheader id="programmatic_${transactionType}_${demarcationType}_body" title="${demarcationType}"/>
					<div class="item-body content" id="programmatic_${transactionType}_${demarcationType}_body">
						<#assign usages = programmaticDemarcation[transactionType][demarcationType]>
						<ul>
							<#list usages as usage>
								<li>${usage}</li>
							</#list>
						</ul>
					</div>
				</#list>
			</div>
		</#list>
	</div>
</div>