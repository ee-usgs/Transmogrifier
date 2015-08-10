package gov.usgs.cida.tranmog.transport;

import gov.usgs.cida.tranmog.rowcol.Column;
import gov.usgs.cida.tranmog.rowcol.DocumentEnd;
import gov.usgs.cida.tranmog.rowcol.RowColElement;
import gov.usgs.cida.tranmog.rowcol.RowStart;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author eeverman
 */
public class RowColumnQueue extends ArrayBlockingQueue<RowColElement> {
	
	private int charCnt = 0;	//Count of characters in the queue.
	private int colCnt = 0;	//Count of columns in queue
	private int rowCnt = -1;	//Count of complete rows in que.  -1 = none in process.  0 = none complete.  1 = one complete, etc.
	private boolean complete = false;
	private boolean completelyRead = false;
	
	private final ReentrantLock lock = new ReentrantLock();
	
	public RowColumnQueue(int capacity) {
		super(capacity);
	}
	
	public int getCharCnt() {
		lock.lock();  // block until condition holds
		try {
			return charCnt;
		} finally {
			lock.unlock();
		}
	}

	public int getColCnt() {
		lock.lock();  // block until condition holds
		try {
			return colCnt;
		} finally {
			lock.unlock();
		}
	}

	public int getRowCnt() {
		lock.lock();  // block until condition holds
		try {
			return rowCnt;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * True if the DocumentEnd has been received from the sender, though
	 * it may not have been read yet by the poller of this queue.
	 * @return 
	 */
	public boolean isComplete() {
		lock.lock();  // block until condition holds
		try {
			return complete;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * True if the poller of this queue has read the entire document, including
	 * polling the DocumentEnd.
	 * @return 
	 */
	public boolean isCompletelyRead() {
		lock.lock();  // block until condition holds
		try {
			return completelyRead;
		} finally {
			lock.unlock();
		}
	}
	
	
	private void addElement(RowColElement rce) {
		if (rce != null) {
			lock.lock();  // block until condition holds
			try {
				if (rce instanceof Column) {
					colCnt++;
					charCnt+=rce.getCharacterCount();
				} else if (rce instanceof RowStart) {
					rowCnt++;
				} else if (rce instanceof DocumentEnd) {
					rowCnt++;	//indicates an additional completed row
					complete = true;
				}
			} finally {
				lock.unlock();
			}
		}
	}
	
	private void removeElement(RowColElement rce) {
		if (rce != null) {
			lock.lock();  // block until condition holds
			try {
				

				if (rce instanceof Column) {
					colCnt--;
					charCnt-=rce.getCharacterCount();
				} else if (rce instanceof RowStart) {
					rowCnt--;
				} else if (rce instanceof DocumentEnd) {
					rowCnt--;	//for consistency, bring back to -1 when no data exists
					completelyRead = true;
				}
			} finally {
				lock.unlock();
			}
		}
	}
	
	
	
	//
	//

	@Override
	public void clear() {
		lock.lock();  // block until condition holds
		try {
			super.clear();
			colCnt = 0;
			rowCnt = -1;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public RowColElement poll(long timeout, TimeUnit unit) throws InterruptedException {
		lock.lock();  // block until condition holds
		try {
			RowColElement rc = super.poll(timeout, unit);
			removeElement(rc);
			return rc;
		} finally {
			lock.unlock();
		}
	}
	


	@Override
	public RowColElement take() throws InterruptedException {
		lock.lock();  // block until condition holds
		try {
			RowColElement rc = super.take();
			removeElement(rc);
			return rc;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public RowColElement poll() {
		lock.lock();  // block until condition holds
		try {
			RowColElement rc = super.poll();
			removeElement(rc);
			return rc;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean offer(RowColElement e, long timeout, TimeUnit unit) throws InterruptedException {
		lock.lock();  // block until condition holds
		try {
			boolean ok = super.offer(e, timeout, unit);
			if (ok) {
				addElement(e);
			}
			return ok;
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public boolean offer(RowColElement e) {
		lock.lock();  // block until condition holds
		try {
			boolean ok = super.offer(e);
			if (ok) {
				addElement(e);
			}
			return ok;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void put(RowColElement e) throws InterruptedException {
		lock.lock();  // block until condition holds
		try {
			super.put(e);
			addElement(e);
		} finally {
			lock.unlock();
		}
	}




	
	//
	//
	// These methods are not implemented just to keep down the number of methods to test.
	
	/**
	 * Not implemented.
	 * 
	 * @param e
	 * @return 
	 */
	@Override
	public boolean add(RowColElement e) {
		throw new UnsupportedOperationException();
	}
	/**
	 * Not implemented.
	 * 
	 * @param c
	 * @param maxElements
	 * @return 
	 */
	@Override
	public int drainTo(Collection<? super RowColElement> c, int maxElements) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not implemented.
	 * 
	 * @param c
	 * @return 
	 */
	@Override
	public int drainTo(Collection<? super RowColElement> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not implemented.
	 * 
	 * @return 
	 */
	@Override
	public RowColElement remove() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Not implemented.
	 * 
	 * @param o
	 * @return 
	 */
	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not implemented.
	 * 
	 * @param c
	 * @return 
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not implemented.
	 * 
	 * @param c
	 * @return 
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	
}
