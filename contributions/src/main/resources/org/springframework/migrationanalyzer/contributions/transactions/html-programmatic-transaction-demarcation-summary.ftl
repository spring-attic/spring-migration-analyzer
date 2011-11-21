<#include "/org/springframework/migrationanalyzer/render/support/html/item-header.ftl"/>
<div>
    <@itemheader_link id="programmatic_transaction_demarcation_summary_body" title="Programmatic Transaction Demarcation" link="${link}"/>
    <div class="item-body" id="programmatic_transaction_demarcation_summary_body">
	    <#assign transactionTypes = summaries?keys>
	    <#list transactionTypes as transactionType>
            <ul>
                <li>${summaries[transactionType]}</li>
            </ul>
        </#list>
    </div>
</div>