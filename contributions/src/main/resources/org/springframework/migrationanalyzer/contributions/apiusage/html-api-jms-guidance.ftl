The application utilises the JMS API. Typically, little or no migration is required as JMS can be used within Tomcat.
During the migration, it may be worth considering using Spring's
<a href="http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/jms.html#jms-using" target="_blank">JMS support</a>,
for example
<a href="http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/jms.html#jms-jmstemplate" target="_blank">JmsTemplate</a>,
to simplify the code.