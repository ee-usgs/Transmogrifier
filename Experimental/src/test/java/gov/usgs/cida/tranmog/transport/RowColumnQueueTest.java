package gov.usgs.cida.tranmog.transport;


import gov.usgs.cida.tranmog.rowcol.*;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
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
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author eeverman
 */
public class RowColumnQueueTest {
	private static final Logger log = LoggerFactory.getLogger(RowColumnQueueTest.class);

	
	
	@Test
    public void testSimple() throws Exception {
		RowColumnQueue queue = new RowColumnQueue(30);
		
		stuffEvents(queue);
		
		assertEquals(5, queue.getRowCnt());
		assertEquals(20, queue.getColCnt());
		assertEquals(128, queue.getCharCnt());
		assertTrue(queue.isComplete());
		assertFalse(queue.isCompletelyRead());
		
		
		
    }



		
	public void stuffEvents(RowColumnQueue queue) throws Exception {

		CSVParser parser = null;

		try {
			URL fileUrl = Thread.currentThread().getContextClassLoader().getResource("samples/sampleA4x4.csv");
			parser = CSVParser.parse(fileUrl, Charset.forName("UTF-8"), CSVFormat.RFC4180);


			JMSAdaptor adapt = new JMSAdaptor();

			queue.offer(new DocumentStart());
			
			for (CSVRecord record : parser) {
				RowStart rs = new RowStart(record.getRecordNumber());
				queue.offer(rs);
				
				for (int colIdx = 0; colIdx < record.size(); colIdx++) {
					Column c = new Column(colIdx, record.get(colIdx));
					queue.offer(c);
				}
			}
			
			queue.offer(new DocumentEnd());


		} finally {
			try {
				parser.close();
			} catch (IOException ex) {
				//ignore
			}
		}
	}

 

	
}
