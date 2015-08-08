package gov.usgs.cida.tranmog.transport;


import gov.usgs.cida.mock.MessageStub;
import gov.usgs.cida.mock.TextMessageStub;
import gov.usgs.cida.tranmog.rowcol.*;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author eeverman
 */
public class EndToEndRowColumnUnitTest {
	private static final Logger log = LoggerFactory.getLogger(EndToEndRowColumnUnitTest.class);
    BrokerService broker = null;
	


    @Before
    public void setup() throws Exception {
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
    public void testSimple() throws Exception {
        thread(new HelloWorldProducer(), false);
		thread(new HelloWorldConsumer(), false);
		Thread.sleep(2000);
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
 

				stuffEvents(session, producer);
 
                // Clean up
                session.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
			}
        }
		
		public void stuffEvents(Session session, MessageProducer producer) throws Exception {
			
			CSVParser parser = null;
			
			try {
				URL fileUrl = Thread.currentThread().getContextClassLoader().getResource("samples/simpleNames.csv");
				parser = CSVParser.parse(fileUrl, Charset.forName("UTF-8"), CSVFormat.RFC4180);


				JMSAdaptor adapt = new JMSAdaptor();


				Message msg = adapt.toMessage(new DocumentStart(), session);
				producer.send(msg);
				System.out.println("Send Doc Start");

				for (CSVRecord record : parser) {
					RowStart rs = new RowStart(record.getRecordNumber());
					msg = adapt.toMessage(rs, session);
					producer.send(msg);

					for (int colIdx = 0; colIdx < record.size(); colIdx++) {
						Column c = new Column(colIdx, record.get(colIdx));
						msg = adapt.toMessage(c, session);
						producer.send(msg);
					}
				}

				msg = adapt.toMessage(new DocumentEnd(), session);
				producer.send(msg);
			} finally {
				try {
					parser.close();
				} catch (IOException ex) {
					//ignore
				}
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
				
				while (message != null) {
					Object received = adapt.fromMessage(message);

					if (received instanceof Column) {
						Column c = (Column)(received);
						System.out.println("Received Column: " + c.getSourceIndex() + " : " + c.getValue());
					} else if (received instanceof RowStart) {
						System.out.println("Received RowStart: " + message);

					} else if (received instanceof DocumentStart) {
						System.out.println("Received DocStart: " + message);
					} else if (received instanceof DocumentEnd) {
						System.out.println("Received DocEnd: " + message);
					} else {
						System.out.println("Received Unknown type");
					}
					
					message = consumer.receive(1000);
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
