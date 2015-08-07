package gov.usgs.cida.mock;

import java.util.Enumeration;
import java.util.HashMap;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
		
/**
 *
 * @author eeverman
 */
public class MessageStub implements Message {

	HashMap<String, String> properties = new HashMap();
	
	
	//
	//Property Related
	@Override
	public boolean propertyExists(String name) throws JMSException {
		return properties.containsKey(name);
	}

	@Override
	public boolean getBooleanProperty(String name) throws JMSException {
		String val = properties.get(name);
		return Boolean.valueOf(val);
	}

	@Override
	public byte getByteProperty(String name) throws JMSException {
		String val = properties.get(name);
		return Byte.valueOf(val);
	}

	@Override
	public short getShortProperty(String name) throws JMSException {
		String val = properties.get(name);
		return Short.valueOf(val);
	}

	@Override
	public int getIntProperty(String name) throws JMSException {
		String val = properties.get(name);
		return Integer.valueOf(val);
	}

	@Override
	public long getLongProperty(String name) throws JMSException {
		String val = properties.get(name);
		return Long.valueOf(val);
	}

	@Override
	public float getFloatProperty(String name) throws JMSException {
		String val = properties.get(name);
		return Float.valueOf(val);
	}

	@Override
	public double getDoubleProperty(String name) throws JMSException {
		String val = properties.get(name);
		return Double.valueOf(val);
	}

	@Override
	public String getStringProperty(String name) throws JMSException {
		String val = properties.get(name);
		return val;
	}

	@Override
	public Object getObjectProperty(String name) throws JMSException {
		String val = properties.get(name);
		return val;
	}

	@Override
	public Enumeration getPropertyNames() throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setBooleanProperty(String name, boolean value) throws JMSException {
		properties.put(name, Boolean.toString(value));
	}

	@Override
	public void setByteProperty(String name, byte value) throws JMSException {
		properties.put(name, Byte.toString(value));
	}

	@Override
	public void setShortProperty(String name, short value) throws JMSException {
		properties.put(name, Short.toString(value));
	}

	@Override
	public void setIntProperty(String name, int value) throws JMSException {
		properties.put(name, Integer.toString(value));
	}

	@Override
	public void setLongProperty(String name, long value) throws JMSException {
		properties.put(name, Long.toString(value));
	}

	@Override
	public void setFloatProperty(String name, float value) throws JMSException {
		properties.put(name, Float.toString(value));
	}

	@Override
	public void setDoubleProperty(String name, double value) throws JMSException {
		properties.put(name, Double.toString(value));
	}

	@Override
	public void setStringProperty(String name, String value) throws JMSException {
		properties.put(name, value);
	}

	@Override
	public void setObjectProperty(String name, Object value) throws JMSException {
		properties.put(name, value.toString());
	}

	
	
	
	//
	
	
	@Override
	public String getJMSMessageID() throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setJMSMessageID(String id) throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public long getJMSTimestamp() throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setJMSTimestamp(long timestamp) throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setJMSCorrelationIDAsBytes(byte[] correlationID) throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setJMSCorrelationID(String correlationID) throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String getJMSCorrelationID() throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Destination getJMSReplyTo() throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setJMSReplyTo(Destination replyTo) throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Destination getJMSDestination() throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setJMSDestination(Destination destination) throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public int getJMSDeliveryMode() throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setJMSDeliveryMode(int deliveryMode) throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean getJMSRedelivered() throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setJMSRedelivered(boolean redelivered) throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String getJMSType() throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setJMSType(String type) throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public long getJMSExpiration() throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setJMSExpiration(long expiration) throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public int getJMSPriority() throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setJMSPriority(int priority) throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void clearProperties() throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void acknowledge() throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void clearBody() throws JMSException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}
