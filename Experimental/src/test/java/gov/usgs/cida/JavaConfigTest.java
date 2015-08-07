package gov.usgs.cida;

import gov.usgs.cida.tranmog.rowcol.Column;
import gov.usgs.cida.tranmog.transport.JMSAdaptor;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import junit.framework.TestCase;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.network.NetworkConnector;
import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.activemq.util.URISupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaConfigTest {
	private static final Logger LOG = LoggerFactory.getLogger(JavaConfigTest.class);

    BrokerService broker = null;



    @Before
    public void setup() throws Exception {
//		BrokerService broker = new BrokerService();
//		broker.setBrokerName("fred");
//		broker.setUseShutdownHook(false);
//		//Add plugin
//		//broker.setPlugins(new BrokerPlugin[]{new JaasAuthenticationPlugin()});
//		//Add a network connection
//		NetworkConnector connector = broker.addNetworkConnector("static://"+"tcp://somehost:61616");
//		connector.setDuplex(true);
//		broker.addConnector("tcp://localhost:61616");
//		broker.start();
		

		
		

		broker = new BrokerService();
		broker.setPersistent(false);
		broker.start();

    }
	
	@After
	public void tearDown() throws Exception {
		broker.stop();
		broker = null;
	}
	
	@Test
    public void testTest() throws Exception {
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldConsumer(), false);
        Thread.sleep(1000);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldProducer(), false);
        Thread.sleep(1000);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldProducer(), false);
        Thread.sleep(1000);
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldProducer(), false);
		Thread.sleep(1000);
    }


	
	public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }
	
    public static class HelloWorldProducer implements Runnable {
        public void run() {
            try {
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
 
                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();
 
                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue("TEST.FOO");
 
                // Create a MessageProducer from the Session to the Topic or Queue
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
 
                // Create a messages
//				String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
//              TextMessage message = session.createTextMessage(text);
				
				JMSAdaptor adapt = new JMSAdaptor();
				Column col = new Column("I am a column");
                Message message = adapt.toMessage(col, session);
				
                // Tell the producer to send the message
                System.out.println("Sent message: "+ col.getValue() + " : " + Thread.currentThread().getName());
                producer.send(message);
				
//				ObjectMessage om = session.createObjectMessage(new Column(0, "test"));
//				
//				// Tell the producer to send the message
//                System.out.println("Sent Obj message: "+ om.getObject().getClass().getCanonicalName() + " : " + Thread.currentThread().getName());
//                producer.send(om);
 
                // Clean up
                session.close();
                connection.close();
            }
            catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
        }
    }
 
    public static class HelloWorldConsumer implements Runnable, ExceptionListener {
        public void run() {
            try {
 
				JMSAdaptor adapt = new JMSAdaptor();
				
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");
 
                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();
 
                connection.setExceptionListener(this);
 
                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
 
                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue("TEST.FOO");
 
                // Create a MessageConsumer from the Session to the Topic or Queue
                MessageConsumer consumer = session.createConsumer(destination);
 
                // Wait for a message
                Message message = consumer.receive(1000);
				Object received = adapt.fromMessage(message);
 
                if (received instanceof Column) {
					Column c = (Column)(received);
					System.out.println("Received Column: " + c.getSourceIndex() + " : " + c.getValue());
                } else if (received instanceof Object) {
					System.out.println("Received: " + message);
//                    TextMessage textMessage = (TextMessage) message;
//                    String text = textMessage.getText();
//                    System.out.println("Received: " + text);
				} else {
                    System.out.println("Received: " + message);
                }
 
                consumer.close();
                session.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
        }
 
        public synchronized void onException(JMSException ex) {
            System.out.println("JMS Exception occured.  Shutting down client.");
        }
    }
}
