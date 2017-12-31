## Atlas archetype
Spring MVC + Hibernate JPA web project: use this project as a template, then 
create and share Maven archetype from it.

### Archetype generation
`mvn archetype:create-from-project`

Check version number, 1.2.0 is there as an example.

### Create project from archetype
`mvn archetype:generate -DarchetypeGroupId=it.mgt.archetype -DarchetypeArtifactId=atlas-archetype -DarchetypeVersion=1.2.0
-DgroupId=it.mgt.archetype -DartifactId=atlas -Dpackage=it.mgt.atlas`

Check version number, 1.2.0 is there as an example.

### Use generated project
Remember to add to the context of your container datasource and Hibernate 
relevant properties definition:
`<Resource name="jdbc/atlas" auth="Container" type="javax.sql.DataSource"
    maxTotal="2" maxIdle="2" maxWaitMillis="10000"
    username="yourUsr" password="yourPwd" driverClassName="com.mysql.jdbc.Driver"
    url="jdbc:mysql://localhost:3306/atlas"/>`
`<Environment name="it.mgt.atlas.hibernate.dialect" value="org.hibernate.dialect.MySQL57Dialect" type="java.lang.String" />`
`<Environment name="it.mgt.atlas.hibernate.dialect.storage_engine" value="org.hibernate.dialect.InnoDBStorageEngine" type="java.lang.String" />`

Check the properties names, you will need to change them according to your
project (see `BaseContext.java` and `JndiDataConfig.java`).