# Introduction

The logging module is part of the PretronicLibraries projects and offers 
many functionalities for logging messages to console, file or other outputs. 
It contains different implementations for every type of use.

### How to include in your project?

#### Gradle

```groovy
maven {
    url "https://repository.pretronic.net/repository/pretronic/"
}

compile group: 'net.pretronic.libraries', name: 'pretroniclibraries-logging', version: '1.0.0'
```

#### Maven

```xml
<repository>
    <id>pretronic</id>
    <url>https://repository.pretronic.net/repository/pretronic/</url>
</repository>

<dependency>
    <groupId>net.pretronic.libraries</groupId>
    <artifactId>pretroniclibraries-logging</artifactId>
    <version>${version}</version>
    <scope>compile</scope>
</dependency>
```

### Javadoc
For a complete API documentation, see the [Javadocs](https://pretronic.net/javadoc/PretronicLibraries/1.0.117.90-SNAPSHOT/overview-summary.html).

### Example

```java
import net.pretronic.libraries.logging.Debug;
import net.pretronic.libraries.logging.PretronicLogger;
import net.pretronic.libraries.logging.PretronicLoggerFactory;
import net.pretronic.libraries.logging.handler.file.FileHandler;import net.pretronic.libraries.logging.io.LoggingPrintStream;import net.pretronic.libraries.logging.level.LogLevel;

public class Example {

    public static void main(String[] args) {
        /* Create a new logger */
        PretronicLogger logger = PretronicLoggerFactory.getLogger();
        logger.setLevel(LogLevel.INFO);
        Debug.setLogger(logger);
        logger.addHandler(new FileHandler(new File("logs/")));

        /* Hook into the default java output stream */
        LoggingPrintStream.hook(logger);
       
        
        logger.info("System started");
        
        logger.info("Hi {}, what is going on?","Peter");

        logger.debug("Very important debug");
    
        logger.warn("This can cause issues.");


        Debug.print("A simple debug");


        logger.shutdown();
    }
}
```
