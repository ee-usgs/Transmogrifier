package gov.usgs.cida.tranmog.rowcol;

import java.io.Serializable;

/**
 *
 * @author eeverman
 */
public class Column implements RowColElement {

	private long sourceIndex = -1;
	private String value;
	
	public Column(String value) {
		this.value = value;
	}

	public Column(long sourceIndex, String value) {
		this.sourceIndex = sourceIndex;
		this.value = value;
	}
	
	public long getSourceIndex() {
		return sourceIndex;
	}

	public String getValue() {
		return value;
	}
	
	@Override
	public long getCharacterCount() {
		if (value != null) {
			return value.length();
		} else {
			return 0L;
		}
	}
	
}
