The application utilises JBoss API. It is almost certain that the application will have to be modified to remove this dependency. The use of the API should be examined
to determine how much effort will be required to remove it. In some cases, such as any use of org.jboss.mx.util.MBeanServerLocator, the removal will be trivial. In other
cases more effort will be required. 