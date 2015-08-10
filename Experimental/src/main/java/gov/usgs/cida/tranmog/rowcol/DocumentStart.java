package gov.usgs.cida.tranmog.rowcol;

import java.io.Serializable;

/**
 * Marks the beginning of a document.
 * 
 * @author eeverman
 */
public class DocumentStart implements RowColElement {
	
	public DocumentStart() {
	}
	
	@Override
	public long getCharacterCount() {
		return 0L;
	}
	
}
