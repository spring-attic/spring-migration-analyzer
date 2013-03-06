The application contains a number of stateful session beans. Typically, the migration of each stateful session
bean will require a moderate amount of effort to ensure that its state is managed appropriately. Such beans
are usually migrated to a Spring
<a href="http://static.springsource.org/spring/docs/3.2.x/spring-framework-reference/html/beans.html#beans-classpath-scanning">managed component</a>
with an appropriately configured
<a href="http://static.springsource.org/spring/docs/3.2.x/spring-framework-reference/html/beans.html#beans-factory-scopes">scope</a>.