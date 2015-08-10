package gov.usgs.cida.tranmog.transport;

import gov.usgs.cida.tranmog.rowcol.Column;
import gov.usgs.cida.tranmog.rowcol.DocumentEnd;
import gov.usgs.cida.tranmog.rowcol.DocumentStart;
import gov.usgs.cida.tranmog.rowcol.RowStart;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author eeverman
 */
public class RowColumnConsumerRunner implements Runnable, ExceptionListener {
	String queName;
	
	public RowColumnConsumerRunner(String queName) {
		this.queName = queName;
	}

	@Override
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
			Destination destination = session.createQueue(queName);

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
