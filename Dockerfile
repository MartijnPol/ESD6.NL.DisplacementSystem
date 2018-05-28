FROM oracle/glassfish

RUN curl https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.45/mysql-connector-java-5.1.45.jar -o $GLASSFISH_HOME/glassfish/lib/mysql-connector-java-5.1.45.jar

COPY glassfish-resources.xml /tmp/glassfish-resources.xml
RUN asadmin start-domain domain1 \
    && asadmin add-resources /tmp/glassfish-resources.xml \
	&& asadmin create-auth-realm --classname com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm --property="jaas-context=jdbcRealm:datasource-jndi=jdbc\\/AccountAdministrationSystem:user-table=USER:user-name-column=username:password-column=password:group-table=USERGROUP_USER:group-name-column=groupName:digest-algorithm=SHA-256" security_realm \
    && asadmin stop-domain domain1
COPY target/AccountAdministrationSystem.war $GLASSFISH_HOME/glassfish/domains/domain1/autodeploy/AccountAdministrationSystem.war