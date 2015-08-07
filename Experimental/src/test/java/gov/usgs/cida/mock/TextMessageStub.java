package gov.usgs.cida.mock;

import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 *
 * @author eeverman
 */
public class TextMessageStub extends MessageStub implements TextMessage {

	String text;
	
	public TextMessageStub() {
	}

	public TextMessageStub(String text) {
		this.text = text;
	}
	
	
	@Override
	public void setText(String string) throws JMSException {
		this.text = string;
	}

	@Override
	public String getText() throws JMSException {
		return text;
	}
	
}
