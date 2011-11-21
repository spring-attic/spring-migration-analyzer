<#include "/org/springframework/migrationanalyzer/render/support/html/item-header.ftl"/>
<div>
    <#assign names = usage?keys>
    <#list names as name>
        <@itemheader_link id="summary_spring_${name}_integration_body" title="Spring ${name} Integration" link="${links[name]}"/>
        <div class="item-body" id="summary_spring_${name}_integration_body">
            <ul>
                <li>${usage[name]}</li>
            </ul>
        </div>  
    </#list>
</div>
