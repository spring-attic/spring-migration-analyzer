<#include "/org/springframework/migrationanalyzer/render/support/html/item-header.ftl"/>

<#macro sessionBeanItem sessionBeans type title>
    <@itemheader id="${type}Body" title="${title}"/>
    <div class="item-body" id="${type}Body">
        <#assign sessionBeanNames = sessionBeans?keys>
        <#list sessionBeanNames as sessionBeanName>
            <@itemheader id="${type}${sessionBeanName_index}Body" title="${sessionBeanName}"/>
            <div class="item-body" id="${type}${sessionBeanName_index}Body">
                <table>
                    <#assign metadata = sessionBeans[sessionBeanName]>
                    <#assign metadataNames = metadata?keys>
                    <#list metadataNames as metadataName>
                        <tr><td>${metadataName}</td><td>${metadata[metadataName]}</td></tr>
                    </#list>
                </table>
            </div>
        </#list>
    </div>
</#macro>

<div>
    <@itemheader id="sessionBeansBody" title="${sessionBeansTitle}"/>
    <div class="item-body" id="sessionBeansBody">
        <@sessionBeanItem statelessSessionBeans "stateless" "${statelessSessionBeansTitle}"/>
        <@sessionBeanItem statefulSessionBeans "stateful" "${statefulSessionBeansTitle}"/>
    </div>
</div>