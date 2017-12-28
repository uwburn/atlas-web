## Atlas archetype
Spring MVC + Hibernate JPA web project: use this project as a template, then create and share
Maven archetype from it.

### Archetype generation
`mvn archetype:create-from-project`

Check version number, 1.2.0 is there as an example.

### Create project from archetype
`mvn archetype:generate -DarchetypeGroupId=it.mgt.archetype -DarchetypeArtifactId=atlas-archetype -DarchetypeVersion=1.2.0
-DgroupId=it.mgt.archetype -DartifactId=atlas -Dpackage=it.mgt.atlas`

Check version number, 1.2.0 is there as an example.