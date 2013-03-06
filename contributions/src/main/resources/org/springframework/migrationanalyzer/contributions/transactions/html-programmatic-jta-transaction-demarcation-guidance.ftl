The application contains a number of classes that programatically manage JTA transactions.
<p />
If the transaction does not involve any resources, i.e. the transaction was not actually necessary, the programmatic use of JTA can be easily and
safely removed during the migration.
<p />
If the transaction only involves a single resource, the programmatic use of JTA can easily be replaced with Spring's support for
<a href="http://static.springsource.org/spring/docs/3.2.x/spring-framework-reference/html/transaction.html#transaction-programmatic">programmatic transactions</a>.
During the migration is may also be worth considering replacing the programmatic transactions with
<a href="http://static.springsource.org/spring/docs/3.2.x/spring-framework-reference/html/transaction.html#transaction-declarative">declarative transactions</a>
in order to simply the application's logic.
<p />
If the transaction involves multiple resources, and the goal is to avoid the need for a JTA transaction manager, the migration effort may be higher as
some changes to the application's logic will almost certainly be necessary. If a JTA transaction manager will be available in the target tc Server environment, the
migration can easily be made to use Spring's
<a href="http://static.springsource.org/spring/docs/3.2.x/spring-framework-reference/html/transaction.html#transaction-programmatic">programmatic transaction</a> support.