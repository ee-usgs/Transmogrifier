/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.usgs.cida.tranmog.transport;

import gov.usgs.cida.mock.MessageStub;
import gov.usgs.cida.mock.TextMessageStub;
import gov.usgs.cida.tranmog.rowcol.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author eeverman
 */
public class JMSAdaptorTest {
	
	public JMSAdaptorTest() {
	}
	
	@Test
	public void testFromMessageForColumnWithIndexMessage() throws Exception {
		
		JMSAdaptor adapt = new JMSAdaptor();
		
		//Create a message
		String testStr = "0123456789";
		TextMessageStub msg = new TextMessageStub(testStr);
		msg.setStringProperty(JMSAdaptor.TYPE_HEADER_NAME, JMSAdaptor.COLUMN_TYPE);
		msg.setLongProperty(JMSAdaptor.SOURCE_INDEX, 99L);
		
		//Read message to object
		Object resultObj = adapt.fromMessage(msg);
		
		assertTrue(resultObj instanceof Column);
		Column col = (Column)resultObj;
		assertEquals(99L, col.getSourceIndex());
		assertEquals("0123456789", col.getValue());
	}
	
	@Test
	public void testFromMessageForColumnWithoutIndexMessage() throws Exception {
		
		JMSAdaptor adapt = new JMSAdaptor();
		
		//Create a message
		String testStr = "0123456789";
		TextMessageStub msg = new TextMessageStub(testStr);
		msg.setStringProperty(JMSAdaptor.TYPE_HEADER_NAME, JMSAdaptor.COLUMN_TYPE);
		
		//Read message to object
		Object resultObj = adapt.fromMessage(msg);
		
		assertTrue(resultObj instanceof Column);
		Column col = (Column)resultObj;
		assertEquals(-1L, col.getSourceIndex());
		assertEquals("0123456789", col.getValue());
	}
	
	@Test
	public void testFromMessageForRowStartWithIndexMessage() throws Exception {
		
		JMSAdaptor adapt = new JMSAdaptor();
		
		//Create a message
		MessageStub msg = new MessageStub();
		msg.setStringProperty(JMSAdaptor.TYPE_HEADER_NAME, JMSAdaptor.ROW_START_TYPE);
		msg.setLongProperty(JMSAdaptor.SOURCE_INDEX, 99L);
		
		//Read message to object
		Object resultObj = adapt.fromMessage(msg);
		
		assertTrue(resultObj instanceof RowStart);
		RowStart row = (RowStart)resultObj;
		assertEquals(99L, row.getSourceIndex());
	}
	
	@Test
	public void testFromMessageForRowStartWithoutIndexMessage() throws Exception {
		
		JMSAdaptor adapt = new JMSAdaptor();
		
		//Create a message
		MessageStub msg = new MessageStub();
		msg.setStringProperty(JMSAdaptor.TYPE_HEADER_NAME, JMSAdaptor.ROW_START_TYPE);
		
		//Read message to object
		Object resultObj = adapt.fromMessage(msg);
		
		assertTrue(resultObj instanceof RowStart);
		RowStart row = (RowStart)resultObj;
		assertEquals(-1L, row.getSourceIndex());
	}
	
	@Test
	public void testFromMessageForDocumentStartMessage() throws Exception {
		
		JMSAdaptor adapt = new JMSAdaptor();
		
		//Create a message
		MessageStub msg = new MessageStub();
		msg.setStringProperty(JMSAdaptor.TYPE_HEADER_NAME, JMSAdaptor.DOCUMENT_START_TYPE);
		
		//Read message to object
		Object resultObj = adapt.fromMessage(msg);
		
		assertTrue(resultObj instanceof DocumentStart);
	}
	
	@Test
	public void testFromMessageForDocumentEndMessage() throws Exception {
		
		JMSAdaptor adapt = new JMSAdaptor();
		
		//Create a message
		MessageStub msg = new MessageStub();
		msg.setStringProperty(JMSAdaptor.TYPE_HEADER_NAME, JMSAdaptor.DOCUMENT_END_TYPE);
		
		//Read message to object
		Object resultObj = adapt.fromMessage(msg);
		
		assertTrue(resultObj instanceof DocumentEnd);
	}

}
