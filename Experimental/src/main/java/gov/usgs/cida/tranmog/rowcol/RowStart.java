package gov.usgs.cida.tranmog.rowcol;

import java.io.Serializable;

/**
 * Marks the beginning of a row.
 * 
 * There is no corresponding row end, since another RowStart or document end
 can indicate that.
 * 
 * @author eeverman
 */
public class RowStart implements Serializable {

	private long sourceIndex = -1;
	
	public RowStart() {
	}

	public RowStart(long sourceIndex) {
		this.sourceIndex = sourceIndex;
	}
	
	public long getSourceIndex() {
		return sourceIndex;
	}
	
}
