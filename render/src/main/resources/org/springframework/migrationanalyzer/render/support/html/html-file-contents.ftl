<html>
<head>
  <title>File System Entries</title>
  <link rel="stylesheet" type="text/css" href="../css/style.css">
  <script type="text/javascript" src="../js/script.js"></script>
  </style>
</head>
<body>


<div id="module" class="treeIndex">
	<ul>
		<#list treeUrls.getChildren() as child>
			<@print tree=child/>
		</#list>
	</ul>
</div>

</body>
</html>

<#macro print tree>
	<#if tree.isLeaf() == false>
		<#if tree.isFirstChildLeaf() == true>
				<#list tree.getChildren() as child>
					  <li><a href="${child.head}" target="content">${tree.head}</a></li>
				</#list>
		<#else>
			<li> 
				<div class="archive">
					<span class="toggle" onClick="toggle('${tree.head}', this)">[-]</span>
					${tree.head} 
				</div>
				<div id="${tree.head}">							
					<ul>
						<#list tree.getChildren() as child>	
							<@print tree=child/>
						</#list>
					</ul>	
				</div>
			</li>
		</#if>
	</#if>
</#macro> 