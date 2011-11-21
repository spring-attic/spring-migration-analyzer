The application utilises the JNDI API. JNDI is available in Tomcat and therefore no work is required to migrate the application.
However, it may be worth considering using Spring's
<a href="http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/xsd-config.html#xsd-config-body-schemas-jee" target="_blank">&lt;jee-jndi-lookup&gt; support</a>
to perform any JNDI lookups, rather than using the JNDI API directly. 