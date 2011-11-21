The application contains a number of session beans that use declarative JTA transactions. It should be noted that the default session bean configuration
is to use declarative transactions and it is frequently the case that while the use of declarative transactions is configured, it is not actually necessary.
<p />
If the transaction does not involve any resources, i.e. the transaction was not actually necessary, the bean can be easily migrated to a Spring managed component
with no declarative transaction configuration.
<p />
If the transaction only involves a single resource, it can be easily migrated to a Spring managed component that uses Spring's support for
<a href="http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/transaction.html#transaction-declarative">declarative transactions</a>.
<p />
If the transaction involves multiple resources, and the goal is to avoid the need for a JTA transaction manager, the migration effort may be higher as
some changes to the application's logic will almost certainly be necessary. If a JTA transaction manager will be available in the target tc Server environment, the
migration can easily be made to use Spring's declarative transaction support.