# Introduction

The document module is part of the PretronicLibraries projects and offers 
many functionalities for serializing java objects, reading and wiring different 
file formats such as json, xml and yaml. This library is able to read different file 
formats in our document structure and afterwards convert the document structure in usable java objects.

### How to include in your project?

#### Gradle

```groovy
maven {
    url "https://repository.pretronic.net/repository/pretronic/"
}

compile group: 'net.pretronic.libraries', name: 'pretroniclibraries-document', version: '1.0.0'
```

#### Maven

```xml
<repository>
    <id>pretronic</id>
    <url>https://repository.pretronic.net/repository/pretronic/</url>
</repository>

<dependency>
    <groupId>net.pretronic.libraries</groupId>
    <artifactId>pretroniclibraries-document</artifactId>
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
        Employee first = new Employee("Aidan Macleod","aidan.macleod@example.com","+ 1 000 000 00 00");
        Employee second = new Employee("Frederick Hooper","frederick.hooper@example.com","+ 1 000 000 00 00");

        //Serialize the employee object into the document structure
        Document serialized = Document.newDocument(first);

        //Create a new document and append objects
        Document serialized2 = Document.newDocument();
        serialized2.set("date", LocalDateTime.now());
        serialized2.set("employee",second);

        //Write the document into a file format
        DocumentFileType.JSON.getWriter().write(new File("example.json"),serialized);
        DocumentFileType.YAML.getWriter().write(new File("example.yml"),serialized2);

        //Read the document from a file format
        Document document = DocumentFileType.JSON.getReader().read(new File("example.json"));

        //Create an object from a document
        Employee result = document.getAsObject(Employee.class);
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

#### Create an adapter for special objects
If you need a special serialisation for your object, you are able to create an adapter. See example
adapter in the net.pretronic.libraries.document.adapter.defaults package.

### Additional available annotations
| Annotation | Description |
| ------------- | ------------- |
| @DocumentKey | If the name of the field not equals the name of the serialized file in the document, you are able to specify the serialized name |
| @DocumentRequired | This annotation is used if this field is required .If the field is not contained in the document an exception wil be thrown |
| @DocumentAttribute | You are also able to store object information in the attribute section, use this annotation for defining a field which is located in the attribute section  |
| @DocumentIgnored | Use this annotation if your field should be ignored  |
| @DocumentIgnoreBooleanValue | This annotation is for boolean fields. If this annotation is represent, false value will be ignored |
| @DocumentIgnoreZeroValue | This annotation is for number fields. If this annotation is represent, 0 value will be ignored  |

