package org.andrejs.cassandra;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.ReadTimeoutException;
import com.datastax.driver.core.exceptions.WriteTimeoutException;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.andrejs.cassandra.CassandraSpy.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CassandraSpyTest {

    @ClassRule
    public static CassandraSpy cassandra = new CassandraSpy();

    private App app = new App(cassandra.getHost(), cassandra.getPort());

    @Before
    public void startApp() {
        app.start();
    }

    @Test(expected = WriteTimeoutException.class)
    public void whenInsertPrimedThenThrowsExceptionOnAdd() throws Exception {
        cassandra.when(insert()).willThrow(writeTimeout());

        app.addUser(1, "user1");
    }

    @Test(expected = ReadTimeoutException.class)
    public void whenSelectPrimedThenThrowsExceptionOnGet() throws Exception {
        cassandra.when(select()).willThrow(readTimeout());

        app.getUser(1);
    }

    @Test
    public void whenResetThenNoLongerThrowsException() {
        cassandra.when(insert()).willThrow(writeTimeout());
        cassandra.when(select()).willThrow(readTimeout());

        cassandra.resetSpy();

        app.addUser(2, "user2");
        assertEquals("user2", app.getUser(2).get());
    }

    @Test
    public void whenSessionCreatedThenSessionIsOpen() throws Exception {
        Session session = cassandra.getSession();

        assertFalse(session.isClosed());
    }

    @After
    public void reset() {
        cassandra.resetSpy();
    }
}
