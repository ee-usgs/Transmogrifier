package gov.usgs.cida.tranmog.transport;

import gov.usgs.cida.tranmog.rowcol.*;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * @author eeverman
 */
public class JMSAdaptor {
	

	///// Type designation
	public final static String TYPE_HEADER_NAME = "RC_TYPE";  //header name to indicate msg type
	public final static int TYPE_HEADER_LENGTH = 4;	//ALL types must be this size
	public final static String COLUMN_TYPE = "|CC|";
	public final static String ROW_START_TYPE = "|RS|";
	public final static String DOCUMENT_START_TYPE = "|DS|";
	public final static String DOCUMENT_END_TYPE = "|DE|";
	
	//Other headers
	public final static String SOURCE_INDEX = "srcIdx";
	
	//
	//public final static String FIELD_DELIMITER = "|";	//Used to separate mutiple fields in a string
	
	public Object fromMessage(Message message) throws Exception {
		
		String type = message.getStringProperty(TYPE_HEADER_NAME);
		
		
		
		if (type == null) {
			throw new JMSException("No type header specified for message.  Expecting a value for the String property '" + TYPE_HEADER_NAME + "'");
		}
		
		Object ret;
		
		switch (type) {
			case COLUMN_TYPE:
				ret = columnFromMessage(message);
				break;
			case ROW_START_TYPE:
				ret = rowStartfromMessage(message);
				break;
			case DOCUMENT_START_TYPE:
				ret = new DocumentStart();	//nothing else to look at
				break;
			case DOCUMENT_END_TYPE:
				ret = new DocumentEnd();	//nothing else to look at
				break;
			default:
				ret = null;
		}
		
		return ret;
		
	}
	
	public Message toMessage(Column column, Session session) throws JMSException {
		Message msg;
		
		if (column.getValue() == null || "".equals(column.getValue())) {
			msg = session.createMessage();
		} else {
			msg = session.createTextMessage(column.getValue());
		}
		
		
		msg.setStringProperty(TYPE_HEADER_NAME, COLUMN_TYPE);
		if (column.getSourceIndex() > -1) {
			msg.setLongProperty(SOURCE_INDEX, column.getSourceIndex());
		}
		
		return msg;
	}
	
	public Message toMessage(RowStart rowStart, Session session) throws JMSException {
		
		Message msg =session.createMessage();
		msg.setStringProperty(TYPE_HEADER_NAME, ROW_START_TYPE);
		
		if (rowStart.getSourceIndex() > -1) {
			msg.setLongProperty(SOURCE_INDEX, rowStart.getSourceIndex());
		}

		return msg;
	}
	
	public Message toMessage(DocumentStart docStart, Session session) throws JMSException {
		Message message = session.createMessage();
		message.setStringProperty(TYPE_HEADER_NAME, DOCUMENT_START_TYPE);
		return message;
	}
	
	public Message toMessage(DocumentEnd docEnd, Session session) throws JMSException {
		Message message = session.createMessage();
		message.setStringProperty(TYPE_HEADER_NAME, DOCUMENT_END_TYPE);
		return message;
	}
	
	public Column columnFromMessage(Message message) throws Exception {
		
		Column col;
		long srcIdx = -1L;	//equivalant to null for this prop
		
		if (message.propertyExists(SOURCE_INDEX)) {
			srcIdx = message.getLongProperty(SOURCE_INDEX);
		}
		
		if (message instanceof TextMessage) {
			
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
	
			col = new Column(srcIdx, text);
			
		} else {
			col = new Column(srcIdx, null);
		}
		
		return col;
	}
	
	public RowStart rowStartfromMessage(Message message) throws JMSException {
		
		RowStart rs;
		long srcIdx = -1L;	//equivalant to null for this prop
		
		if (message.propertyExists(SOURCE_INDEX)) {
			srcIdx = message.getLongProperty(SOURCE_INDEX);
		}
		
		rs = new RowStart(srcIdx);
		
		return rs;
	}

}
