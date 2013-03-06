The application contains a number of message-driven beans (MDBs). Assuming that an external JMS provider is available, the migration of each MDB is
typically reasonably straightforward. Such beans are usually migrated to a Spring
<a href="http://static.springsource.org/spring/docs/3.2.x/spring-framework-reference/html/jms.html#jms-asynchronousMessageReception">message-driven POJO</a>
driven by a <a href="http://static.springsource.org/spring/docs/3.2.x/spring-framework-reference/html/jms.html#jms-mdp">message listener container</a>.