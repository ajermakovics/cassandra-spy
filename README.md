# Cassandra Spy [![Tests](https://circleci.com/gh/ajermakovics/cassandra-spy.svg?style=shield)](https://circleci.com/gh/ajermakovics/cassandra-spy) [![](https://jitpack.io/v/org.andrejs/cassandra-spy.svg)](https://jitpack.io/#org.andrejs/cassandra-spy)

Test error cases in code that uses Cassandra.

## Features

CassandraSpy is a library that helps you:

- Start embedded Cassandra
- Throw exceptions for certain queries

Implemented as a JUnit rule but can also be used in other contexts like Cucumber tests.

## Usage

```java
public class IntegrationTest {
    
    @ClassRule
    public static CassandraSpy cassandra = new CassandraSpy();
    
    @Before
    public void initApp() {
        String host = cassandra.getHost();
        int port = cassandra.getPort(); // started on random port
        // init app with host and port
    }
    
    @Test
    public void testInsertFails() throws Exception {
        cassandra.when(inserts("users")).willThrow(writeTimeout());
        
        app.addUser(1, "user"); // does an insert
        
        // assert app does the right thing
    }
    
    @Test
    public void testSelectFails() throws Exception {
        cassandra.when(selects("users")).willThrow(readTimeout());
        
        app.getUser(1); // does a select
        
        // assert app does the right thing
    }
}

```

Also see the tests for an example with more details.

# Download

https://jitpack.io/#org.andrejs/cassandra-spy

# About

The project is inspired by:
 - https://github.com/scassandra
 - https://github.com/jsevellec/cassandra-unit
 - https://github.com/chbatey/cassandra-killr
 - Mockito
    
It is written in Kotlin so is usable from other JVM languages.

## License

Apache 2.0
