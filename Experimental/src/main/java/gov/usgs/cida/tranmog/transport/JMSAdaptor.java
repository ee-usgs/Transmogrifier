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
	

	/*
	message format
	|CC|40,205,23832|40 chars|205 chars|23832 chars
	*/
	
	///// Type designation
	public final static String TYPE_HEADER_NAME = "RC_TYPE";  //nmae use in message headers
	public final static int TYPE_HEADER_LENGTH = 4;	//ALL types must be this size
	public final static String COLUMN_TYPE = "|CC|";
	public final static String ROW_START_TYPE = "|RS|";
	public final static String DOCUMENT_START_TYPE = "|DS|";
	public final static String DOCUMENT_END_TYPE = "|DE|";
	
	//
	public final static String FIELD_DELIMITER = "|";	//Used to separate mutiple fields in a string
	
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
		TextMessage message = session.createTextMessage(toMessageText(column).toString());
		message.setStringProperty(TYPE_HEADER_NAME, COLUMN_TYPE);
		return message;
	}
	
	public Message toMessage(RowStart rowStart, Session session) throws JMSException {
		
		Message message;
		
		if (rowStart.getSourceIndex() > -1) {
			message = session.createTextMessage(Long.toString(rowStart.getSourceIndex()));
			message.setStringProperty(TYPE_HEADER_NAME, ROW_START_TYPE);
		} else {
			message = session.createMessage();
			message.setStringProperty(TYPE_HEADER_NAME, ROW_START_TYPE);
		}

		return message;
	}
	
	public Message toMessage(DocumentStart docStart, Session session) throws JMSException {
		Message message = session.createMessage();
		message.setStringProperty(TYPE_HEADER_NAME, DOCUMENT_START_TYPE);
		return message;
	}
	
	public Message toMessage(DocumentEnd docStart, Session session) throws JMSException {
		Message message = session.createMessage();
		message.setStringProperty(TYPE_HEADER_NAME, DOCUMENT_END_TYPE);
		return message;
	}
	
	public Column columnFromMessage(Message message) throws Exception {
		if (message instanceof TextMessage) {
			
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
	
			return (Column) fromMessageText(text);
			
		} else if (message instanceof ObjectMessage) {
			throw new JMSException("Unsupported message type of '" + message.getJMSType() + "' for the JMSAdaptor.");
		} else {
			throw new JMSException("Unsupported message type of '" + message.getJMSType() + "' for the JMSAdaptor.");
		}
	}
	
	public RowStart rowStartfromMessage(Message message) throws JMSException {
		if (message instanceof TextMessage) {
			
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			
			long idx = Long.parseLong(text);
	
			return new RowStart(idx);
			
		} else {
			return new RowStart();
		}
	}
	
	protected Object fromMessageText(String msgText) throws JMSException {
	
		MessageParts mps = new MessageParts(msgText);

		Object retObj = null;

		switch (mps.type) {
			case COLUMN_TYPE:
				
				if (mps.fieldValues.length == 2) {
					retObj = new Column(Long.parseLong(mps.fieldValues[0]), mps.fieldValues[1]);
				} else if (mps.fieldValues.length == 1) {
					retObj = new Column(mps.fieldValues[0]);
				} else {
					throw new JMSException("Unexpected number of fields for a Column");
				}
				
				break;
			case ROW_START_TYPE:
			case DOCUMENT_START_TYPE:
			case DOCUMENT_END_TYPE:	
		}

		return retObj;

	}
	

	

	
	protected CharSequence toMessageText(Column column) throws JMSException {
		
		String[] values = null;
		
		if (column.getSourceIndex() > -1) {
			values = new String[] {Long.toString(column.getSourceIndex()), column.getValue()};
		} else {
			values = new String[] {column.getValue()};
		}
		
		MessageParts mps = new MessageParts(COLUMN_TYPE, values);
		return mps.toCharSequence();

	}
	
	public static class MessageParts {
		public String type;
		public String[] fieldLengths;
		public String[] fieldValues;

		public MessageParts(String type, String[] fieldLengths, String[] fieldValues) {
			this.type = type;
			this.fieldLengths = fieldLengths;
			this.fieldValues = fieldValues;
		}
		
		public MessageParts(String type, String[] fieldValues) {
			this.type = type;
			this.fieldValues = fieldValues;
			this.fieldLengths = new String[fieldValues.length];
			
			for (int i = 0; i < fieldValues.length; i++) {
				if (fieldValues[i] != null) {
					this.fieldLengths[i] = Integer.toString(fieldValues[i].length());
				} else {
					this.fieldLengths[i] = "0";
				}
			}
		}
		
		public MessageParts(String msgText) {
			

			int headerEndIdx = msgText.indexOf(FIELD_DELIMITER, TYPE_HEADER_LENGTH);
			String header = msgText.substring(TYPE_HEADER_LENGTH, headerEndIdx);

			String[] lengths = header.split(",");
			String[] values = new String[lengths.length];

			int contextStartIdx = headerEndIdx + 1;
			int currentStartIdx = contextStartIdx;

			for (int i = 0; i < lengths.length; i++) {
				int len = Integer.parseInt(lengths[i]);
				values[i] = msgText.substring(currentStartIdx, currentStartIdx + len);

				//set for next value
				currentStartIdx+= len + (FIELD_DELIMITER.length());
			}

			this.type = msgText.substring(0, TYPE_HEADER_LENGTH);
			this.fieldLengths = lengths;
			this.fieldValues = values;

		}
		
		public CharSequence toCharSequence() {
			StringBuffer sb = new StringBuffer();
			sb.append(type);
			
			for (int i = 0; i < fieldLengths.length; i++) {
				sb.append(fieldLengths[i]);
				
				if (i < (fieldLengths.length - 1))
					sb.append(",");
			}
			
			sb.append(FIELD_DELIMITER);
			
			for (int i = 0; i < fieldValues.length; i++) {
				sb.append(fieldValues[i]);
				
				//The last value does not have a trailing delimiter
				if (i < (fieldValues.length - 1))
					sb.append(FIELD_DELIMITER);
			}
			
			return sb;
		}
		

	}
}
