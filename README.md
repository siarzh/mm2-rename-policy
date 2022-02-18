# MirrorMaker v2 replication policy to rename target topics

Implements custom [**ReplicationPolicy**](https://github.com/apache/kafka/blob/trunk/connect/mirror-client/src/main/java/org/apache/kafka/connect/mirror/ReplicationPolicy.java) that allows you to set renaming mapping for required topics.

Setup
=====
In order to use this policy, please build the project with Maven:
```
mvn package
```
or take the .jar file directly from the repo _./dist_ folder.

Prerequisites
=====

To start using this policy you should first put the .jar to Kafka libs directory to all the other JARs used by Kafka services eg.
```
/opt/kafka/libs
```

or include the .jar file in your classpath:
``` bash
export CLASSPATH=<path_to_jar_folder>/mm2-rename-policy-1.0.jar
```

Usage
=====

Then you can add these two extra replication policy params for the required topics in your MirrorMaker v2 standalone or Kafka Connect config:

```
{
    "topics": "A,B,C,D"
    ...
    "replication.policy.class": "com.mirrormaker.RenameReplicationPolicy",
    "replication.policy.topics.mapping": "B,B_on_target;C,brand_new_C"
    ...
}
```

Specify only those topics you need to rename, for all the rest the default replication policy will be used.

