<#include "/org/springframework/migrationanalyzer/render/support/html/item-header.ftl"/>
<div id="by_result_type_spring_integration">
    <@itemheader id="spring_${name}_integration_body" title="${title}"/>
    <#assign userLocations = users?keys>
    <div class="item-body" id="spring_${name}_integration_body">
        <ul>
            <#list userLocations as userLocation>
                <li><a href="${userLocation}">${users[userLocation]}</a></li>
            </#list>
        </ul>
    </div>
</div>
