<html>
<head>
  <title>Result Types</title>
  <link rel="stylesheet" href="../css/style.css">
</head>
<body>
<div id="module"/>
<#assign names = resultUrls?keys>
  <ul>
    <li><a href="../summary.html" target="content">Summary</a></li>
  </ul>
  <ul>
<#list names as name>
    <li><a href="${resultUrls[name]}" target="content">${name}</a></li>
</#list>
  </ul>
</div>

</body>
</html>