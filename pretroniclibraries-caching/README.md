# Introduction

The caching module is part of the PretronicLibraries projects and offers 
many functionalities for caching objects in memory. The cache is not a 
simple key value store, you are able to query objects by every attribute or method. 
The cache manages also the lifetime, expiry, refresh and loading of objects.

### How to include in your project?

#### Gradle

```groovy
maven {
    url "https://repository.pretronic.net/repository/pretronic/"
}

compile group: 'net.pretronic.libraries', name: 'pretroniclibraries-caching', version: '1.0.0'
```

#### Maven

```xml
<repository>
    <id>pretronic</id>
    <url>https://repository.pretronic.net/repository/pretronic/</url>
</repository>

<dependency>
    <groupId>net.pretronic.libraries</groupId>
    <artifactId>pretroniclibraries-caching</artifactId>
    <version>${version}</version>
    <scope>compile</scope>
</dependency>
```

### Javadoc
For complete API documentation, see Javadoc.
@Todo add link

### Example

```java
public class Example {

    public static void main(String[] args) {
        /* Setup the cache */
        Cache<Employee> employees = new ArrayCache<>();

        employees.setMaxSize(500);//The maximum size of the cache
        employees.setExpireAfterAccess(30, TimeUnit.MINUTES);//The expiry time after the last access
        employees.setRefresh(2,TimeUnit.HOURS);//The maximum lifetime of an object


        /* Insert static data */
        employees.insert(new Employee("Aidan Macleod","aidan.macleod@example.com","+ 1 000 000 00 00"));
        employees.insert(new Employee("Frederick Hooper","frederick.hooper@example.com","+ 1 000 000 00 00"));
        employees.insert(new Employee("Huma Norris","huma.norris@example.com","+ 1 000 000 00 00"));


        /* Query by predicate */
        Employee result = employees.get(employee -> employee.getName().equalsIgnoreCase("Frederick Hooper"));


        /* Query by a cache query */
        Employee result2 = employees.get(QUERY_BY_NAME,"Aidan Macleod");


        /* Query async */
        CompletableFuture<Employee> result3 = employees.getAsync(QUERY_BY_NAME,"Huma Norris");
        result3.thenAccept(employee -> {
            //Do something
        });
    }

    //Define a static query
    public static CacheQuery<Employee> QUERY_BY_NAME = new CacheQuery<Employee>() {

        @Override //Validate the query
        public void validate(Object[] identifiers) {
            if(!(identifiers.length == 1 && identifiers[0] instanceof String)){
                throw new IllegalArgumentException("invalid identifier");
            }
        }

        @Override
        public boolean check(Employee item, Object[] identifiers) {
            return item.getName().equalsIgnoreCase((String) identifiers[0]);
        }

        @Override
        public Employee load(Object[] identifiers) {
            return null;//Load data from database (Is executed if the object is not available in the cache)
        }
    };

    public static class Employee {

        private final String name;
        private final String email;
        private final String phone;

        public Employee(String name, String email, String phone) {
            this.name = name;
            this.email = email;
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }
    }
}
```
