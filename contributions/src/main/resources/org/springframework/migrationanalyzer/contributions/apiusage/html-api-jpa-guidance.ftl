The application utilises the JPA API. Spring provides first-class
<a href="http://static.springsource.org/spring/docs/3.2.x/spring-framework-reference/html/orm.html#orm-jpa" target="_blank">support for JPA</a>
and the required migration effort will be very low. Spring applications using JPA in a tc Server environment should utilize a
<a href=http://static.springsource.org/spring/docs/3.2.x/spring-framework-reference/html/orm.html#orm-jpa-setup-lcemfb" target="_blank">LocalContainerEntityManagerFactoryBean</a>.