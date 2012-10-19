The application contains a number of classes that use DataSource transactions. Typically, no code
changes are required, however, during the migration, you may want to consider using
Spring's <a href="http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/jdbc.html#jdbc-core">JdbcTemplate</a>
and transaction management to simplify the code.