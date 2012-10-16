<html>
	<head>
		<title>File System Entries</title>
		<link rel="stylesheet" type="text/css" href="../css/style.css">
	</head>
	<body>
		<div class="sidebar-content">
			<#assign names = entryUrls?keys>
			<ul>
				<#list names as name>
					<#assign urls = entryUrls[name]>
					<#list urls as url>
						<li><a href="${url}" target="content">${name}</a></li>
					</#list>
				</#list>
			</ul>
		</div>
	</body>
</html>
