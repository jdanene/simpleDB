package simpledb;

import java.io.*;
import java.util.*;

/**
 * HeapFile is an implementation of a DbFile that stores a collection of tuples
 * in no particular order. Tuples are stored on pages, each of which is a fixed
 * size, and the file is simply a collection of those pages. HeapFile works
 * closely with HeapPage. The format of HeapPages is described in the HeapPage
 * constructor.
 * 
 * @see simpledb.HeapPage#HeapPage
 * @author Sam Madden
 */
public class HeapFile implements DbFile {

    /**
     * Constructs a heap file backed by the specified file.
     * 
     * @param f
     *            the file that stores the on-disk backing store for this heap
     *            file.
     */
	
	private File file;
	private TupleDesc tupleDesc;
	private Integer id;
	
    public HeapFile(File f, TupleDesc td) {
    	this.file = f;
        this.tupleDesc = td;
        this.id = this.file.getAbsoluteFile().hashCode();
    }

    /**
     * Returns the File backing this HeapFile on disk.
     * 
     * @return the File backing this HeapFile on disk.
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Returns an ID uniquely identifying this HeapFile. Implementation note:
     * you will need to generate this tableid somewhere to ensure that each
     * HeapFile has a "unique id," and that you always return the same value for
     * a particular HeapFile. We suggest hashing the absolute file name of the
     * file underlying the heapfile, i.e. f.getAbsoluteFile().hashCode().
     * 
     * @return an ID uniquely identifying this HeapFile.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the TupleDesc of the table stored in this DbFile.
     * 
     * @return TupleDesc of this DbFile.
     */
    public TupleDesc getTupleDesc() {
        return this.tupleDesc;
    }

  
    
    
//     see DbFile.java for javadocs
    public Page readPage(PageId pid) {
        try {
        	RandomAccessFile raf = new RandomAccessFile(file, "r");
        	int pageSize = BufferPool.getPageSize();
        	int pageNum = pid.getPageNumber();
        	int offSet = pageSize * pageNum;
        	byte[] data = new byte[pageSize];
        	raf.seek(offSet);
        	raf.readFully(data);
        	raf.close();
        	HeapPageId hpid = new HeapPageId(pid.getTableId(), pid.getPageNumber());
        	return new HeapPage(hpid, data);
        	
        }
        catch (IOException ex) {
        	ex.printStackTrace();
        	return null;
        }
    }
    
    

    // see DbFile.java for javadocs
    public void writePage(Page page) throws IOException {
        // some code goes here
        // not necessary for lab1
    }

    /**
     * Returns the number of pages in this HeapFile.
     */
    public int numPages() {
        return (int) Math.ceil((double) file.length() / BufferPool.getPageSize());
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> insertTuple(TransactionId tid, Tuple t)
            throws DbException, IOException, TransactionAbortedException {
        // some code goes here
        return null;
        // not necessary for lab1
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> deleteTuple(TransactionId tid, Tuple t) throws DbException,
            TransactionAbortedException {
        // some code goes here
        return null;
        // not necessary for lab1
    }

    
    // see DbFile.java for javadocs
    public DbFileIterator iterator(TransactionId tid) {
    	return new HeapFileIterator(this, tid);
    }
    
    
    

	public class HeapFileIterator extends AbstractDbFileIterator{
	    
		private Iterator<Tuple> tuple_iterator;
		private Integer currPage;
		private HeapFile hf;
		private TransactionId tid;
		
		public HeapFileIterator(HeapFile hf, TransactionId tid) {
	        this.tid = tid;
	        this.hf = hf;
	        //close();
	    }
		
		
		public void open() throws DbException, TransactionAbortedException{
	    	currPage = -1;

	    	 
	    }
        protected Tuple readNext() throws TransactionAbortedException, DbException {
            if (tuple_iterator != null){
            	if (!tuple_iterator.hasNext()) {
            		tuple_iterator = null;
            }
            }
            while (tuple_iterator == null && currPage+1 < hf.numPages() ) {
                currPage++;     
                tuple_iterator = ((HeapPage)Database.getBufferPool().getPage(tid,new HeapPageId(hf.getId(), currPage), Permissions.READ_ONLY)).iterator();
                if (!tuple_iterator.hasNext())
                    tuple_iterator = null;
            }
            if (tuple_iterator == null)
                return null;
            return tuple_iterator.next();
        }
        /**
         * Resets the iterator to the start.
         * @throws DbException When rewind is unsupported.
         */
        public void rewind() throws DbException, TransactionAbortedException{
        	if (tuple_iterator == null) throw new DbException("Iterator has not been opened.");
            close();
            open();
        }

        /**
         * Closes the iterator.
         */
        public void close() {
        	currPage = -1;
        	tuple_iterator = null;
        	super.close();

        }
	}
    
}

