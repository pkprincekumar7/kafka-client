# kafka-client 

# configuring repository
  The component snapshot artifact is presently managed into in-house configured nexus private repository.
To enable distribution management, developers should add the following tag(s) into the pom. [N.B. The repository can only be accessessable by the users in the Tricon network.]. The below pom settings can be changed for more customizations as per the needs. 

```
<distributionManagement>
<!-- Publish the versioned releases here -->
  <repository>
    <id>tricon-nexus</id>
    <name>maven-releases</name>
    <url>http://172.16.16.51:8081/repository/maven-releases</url>
  </repository>
  <snapshotRepository>
    <id>tricon-nexus</id>
    <name>maven-snapshots</name>
    <url>http://172.16.16.51:8081/repository/maven-snapshots</url>
  </snapshotRepository>
</distributionManagement>

<!-- download plugins from this repo -->
<pluginRepositories>
  <pluginRepository>
    <id>tricon-nexus</id>
    <name>maven-public</name>
    <url>http://172.16.16.51:8081/repository/maven-public</url>
    <releases>
      <enabled>true</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </pluginRepository>
</pluginRepositories>
```

# configuring settings.xml
Please configure the repository credentials into the settings.xml as per the convension. Please add server under the servers tag in settings.xml found in the configured maven .m2 path
```
<server>
<!-- this id should match the id of the repo server in pom.xml -->
  <id>tricon-nexus</id>
  <username>tricon</username>
  <password>tricon123</password>
</server>
 ```
