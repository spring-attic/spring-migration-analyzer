The application uses Spring's JTA integration. Application code should be examined to determine how JTA is being used, and how many resources are
involved in each transaction.
<p />
If a transaction does not involve any resources, i.e. the transaction was not actually necessary, it can be safely removed.
<p />
If the transaction only involves a single resource, it can be easily migrated to use Spring's support for local transactions with a transaction manager that's
appropriate for the resource that is involved, e.g. a DataSourceTransactionManager for a JDBC DataSource.
<p />
If the transaction involves multiple resources, and the goal is to avoid the need for a JTA transaction manager, the migration effort may be higher as
some changes to the application's logic will almost certainly be necessary. If a JTA transaction manager will be available in the target tc Server environment, few
changes will typically be necessary.