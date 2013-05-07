<deployment-descriptors>
	<#list deploymentDescriptors as deploymentDescriptor>
		<deployment-descriptor category="${deploymentDescriptor.category}" name="${deploymentDescriptor.name}"/>
	</#list>
</deployment-descriptors>