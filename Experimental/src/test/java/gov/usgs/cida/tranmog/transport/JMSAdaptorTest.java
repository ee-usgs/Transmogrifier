/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.usgs.cida.tranmog.transport;

import gov.usgs.cida.mock.MessageStub;
import gov.usgs.cida.mock.TextMessageStub;
import gov.usgs.cida.tranmog.rowcol.*;
import javax.jms.Message;
import javax.jms.Session;
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
		String testStr = JMSAdaptor.COLUMN_TYPE + "2,10" + JMSAdaptor.FIELD_DELIMITER +
				"99" + JMSAdaptor.FIELD_DELIMITER + "0123456789";
		TextMessageStub msg = new TextMessageStub(testStr);
		msg.setStringProperty(JMSAdaptor.TYPE_HEADER_NAME, JMSAdaptor.COLUMN_TYPE);
		
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
		String testStr = JMSAdaptor.COLUMN_TYPE + "10" + JMSAdaptor.FIELD_DELIMITER + "0123456789";
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
		TextMessageStub msg = new TextMessageStub("99");
		msg.setStringProperty(JMSAdaptor.TYPE_HEADER_NAME, JMSAdaptor.ROW_START_TYPE);
		
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

	@Test
	public void testMessagePartsSingleStringConstructor() throws Exception {

		JMSAdaptor adapt = new JMSAdaptor();
		
		String testStr = JMSAdaptor.COLUMN_TYPE + "2,10" + JMSAdaptor.FIELD_DELIMITER +
				"99" + JMSAdaptor.FIELD_DELIMITER + "0123456789";
		
		JMSAdaptor.MessageParts expect = new JMSAdaptor.MessageParts(
				JMSAdaptor.COLUMN_TYPE,
				new String[] {"2", "10"},
				new String[] {"99", "0123456789"}
		);
		
		JMSAdaptor.MessageParts result = new JMSAdaptor.MessageParts(testStr);
		
		
		assertEquals(expect.type, result.type);
		assertEquals(expect.fieldLengths[0], result.fieldLengths[0]);
		assertEquals(expect.fieldLengths[1], result.fieldLengths[1]);
		assertEquals(expect.fieldLengths.length, result.fieldLengths.length);
		assertEquals(expect.fieldValues[0], result.fieldValues[0]);
		assertEquals(expect.fieldValues[1], result.fieldValues[1]);
		assertEquals(expect.fieldValues.length, result.fieldValues.length);
	}

	/**
	 * Test of fromMessageText method, of class JMSAdaptor.
	 */
	@Test
	public void testFromMessageText() throws Exception {

		JMSAdaptor adapt = new JMSAdaptor();
		
		Column expect = new Column(99L, "value");
		
		String testStr = JMSAdaptor.COLUMN_TYPE + "2,5|99|value";
		
		Object oCol = adapt.fromMessageText(testStr);
		Column result = (Column)oCol;
		
		assertEquals(expect.getSourceIndex(), result.getSourceIndex());
		assertEquals(expect.getValue(), result.getValue());
		
	}
	
	@Test
	public void testToMessageText() throws Exception {

		JMSAdaptor adapt = new JMSAdaptor();
		
		Column col = new Column(99L, "value");
		
		String expect = JMSAdaptor.COLUMN_TYPE + "2,5" + 
				JMSAdaptor.FIELD_DELIMITER + "99" + JMSAdaptor.FIELD_DELIMITER + "value";
		
		CharSequence result = adapt.toMessageText(col);

		
		assertEquals(expect, result.toString());
		
	}
	




	
}
