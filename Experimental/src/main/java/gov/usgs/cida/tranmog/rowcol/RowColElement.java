package gov.usgs.cida.tranmog.rowcol;

import java.io.Serializable;

/**
 * Marker interface for all Row / Column related domain objects.
 * @author eeverman
 */
public interface RowColElement extends Serializable {
	
	/**
	 * The size of the message element in characters.
	 * This is intended to be the size of the content only w/o any consideration
	 * of headers or overhead.
	 * 
	 * @return The number of content character in the element.
	 */
	public long getCharacterCount();
}
