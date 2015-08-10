package gov.usgs.cida.tranmog.rowcol;

import java.io.Serializable;

/**
 * Marks the end of a document.
 * 
 * @author eeverman
 */
public class DocumentEnd implements RowColElement {
	
	public DocumentEnd() {
	}
	
	@Override
	public long getCharacterCount() {
		return 0L;
	}
	
}
