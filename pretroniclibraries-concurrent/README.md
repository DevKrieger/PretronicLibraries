# Introduction

The concurrent module is part of the PretronicLibraries projects and offers you different 
functionalities for working with different threads and tasks. The main this of this module is the task scheduler, 
you are able to create and manage different kind of tasks. 

### How to include in your project?

#### Gradle

```groovy
maven {
    url "https://repository.pretronic.net/repository/pretronic/"
}

compile group: 'net.pretronic.libraries', name: 'pretroniclibraries-concurrent', version: '1.0.0'
```

#### Maven

```xml
<repository>
    <id>pretronic</id>
    <url>https://repository.pretronic.net/repository/pretronic/</url>
</repository>

<dependency>
    <groupId>net.pretronic.libraries</groupId>
    <artifactId>pretroniclibraries-concurrent</artifactId>
    <version>${version}</version>
    <scope>compile</scope>
</dependency>
```

### Javadoc
For a complete API documentation, see the [Javadocs](https://pretronic.net/javadoc/PretronicLibraries/1.0.117.90-SNAPSHOT/overview-summary.html).

### Example

```java
public class Example {

    public static void main(String[] args) {
        TaskScheduler scheduler = new SimpleTaskScheduler();

        /* Create a task and attach runnables */
        Task task = scheduler.createTask(ObjectOwner.SYSTEM)
                .name("MyTask")
                .async()
                .delay(5,TimeUnit.SECONDS)
                .interval(1,TimeUnit.MINUTES)
                .create();

        task.append(()->{
            System.out.println("My first runnable");
        });

        task.append(()->{
            System.out.println("My second runnable");
        });

        /* Add a task feedback listener */
        task.addListener(future -> {
            if(future.isFailed()){
                future.getThrowable().printStackTrace();
            }
        });

        task.start();

        /* get all tasks with the name "MyTask" */
        Collection<Task> result = scheduler.getTasks("MyTask");

        /* Execute a task directly */
        scheduler.createTask(ObjectOwner.SYSTEM)
                .name("MySecondTask")
                .async()
                .delay(5,TimeUnit.SECONDS)
                .execute(() -> System.out.println("Delayed"));


        /* Shutdown */
        scheduler.shutdown();
    }
}
```
