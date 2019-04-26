package simpledb;

import java.util.*;

/**
 * SeqScan is an implementation of a sequential scan access method that reads
 * each tuple of a table in no particular order (e.g., as they are laid out on
 * disk).
 */
public class SeqScan implements OpIterator {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a sequential scan over the specified table as a part of the
     * specified transaction.
     *
     * @param tid
     *            The transaction this scan is running as a part of.
     * @param tableid
     *            the table to scan.
     * @param tableAlias
     *            the alias of this table (needed by the parser); the returned
     *            tupleDesc should have fields with name tableAlias.fieldName
     *            (note: this class is not responsible for handling a case where
     *            tableAlias or fieldName are null. It shouldn't crash if they
     *            are, but the resulting name can be null.fieldName,
     *            tableAlias.null, or null.null).
     */
    
    private TransactionId tid;
    private Integer tableid;
    private String tableAlias;
    private DbFileIterator db_iterator;
    
    public SeqScan(TransactionId tid, int tableid, String tableAlias) {
        this.tid = tid;
        this.tableid = tableid;
        this.tableAlias = tableAlias;
        this.db_iterator = Database.getCatalog().getDatabaseFile(this.tableid).iterator(this.tid);
    }

    /**
     * @return
     *       return the table name of the table the operator scans. This should
     *       be the actual name of the table in the catalog of the database
     * */
    public String getTableName() {
        return Database.getCatalog().getTableName(tableid);
    }

    /**
     * @return Return the alias of the table this operator scans.
     * */
    public String getAlias()
    {
        // some code goes here
        return this.tableAlias;
    }

    /**
     * Reset the tableid, and tableAlias of this operator.
     * @param tableid
     *            the table to scan.
     * @param tableAlias
     *            the alias of this table (needed by the parser); the returned
     *            tupleDesc should have fields with name tableAlias.fieldName
     *            (note: this class is not responsible for handling a case where
     *            tableAlias or fieldName are null. It shouldn't crash if they
     *            are, but the resulting name can be null.fieldName,
     *            tableAlias.null, or null.null).
     */
    public void reset(int tableid, String tableAlias) {
        this.tableid = tableid;
        this.tableAlias = tableAlias;
        this.db_iterator = null;
    }

    public SeqScan(TransactionId tid, int tableId) {
        this(tid, tableId, Database.getCatalog().getTableName(tableId));
    }

    public void open() throws DbException, TransactionAbortedException {
        try {
        	this.db_iterator = Database.getCatalog().getDatabaseFile(this.tableid).iterator(this.tid);
        	this.db_iterator.open();
        } catch (Exception ex){
        	throw (DbException) ex;
        }
    }

    /**
     * Returns the TupleDesc with field names from the underlying HeapFile,
     * prefixed with the tableAlias string from the constructor. This prefix
     * becomes useful when joining tables containing a field(s) with the same
     * name.  The alias and name should be separated with a "." character
     * (e.g., "alias.fieldName").
     *
     * @return the TupleDesc with field names from the underlying HeapFile,
     *         prefixed with the tableAlias string from the constructor.
     */
    public TupleDesc getTupleDesc() {
        TupleDesc myTD = Database.getCatalog().getTupleDesc(this.tableid);
        Type[] typeArray = new Type[myTD.numFields()];
        String[] fieldArray = new String[myTD.numFields()];
        for (int i = 0; myTD.numFields() > i; i++) {
        	typeArray[i] = myTD.getFieldType(i);
        	fieldArray[i] = tableAlias + "." + myTD.getFieldName(i);
        }
        return new TupleDesc(typeArray, fieldArray);
    }

    public boolean hasNext() throws TransactionAbortedException, DbException {
        if (this.db_iterator == null){
        	return false;
        }
        return this.db_iterator.hasNext();
    }

    public Tuple next() throws NoSuchElementException, TransactionAbortedException, DbException {
        if (!this.hasNext()) {
        	throw new NoSuchElementException("There is no next tuple");
        }
        return this.db_iterator.next();
    }

    public void close() {
        if (this.db_iterator != null) {
        	this.db_iterator.close();
        }
        this.db_iterator = null;
    }

    public void rewind() throws DbException, NoSuchElementException, TransactionAbortedException {
//        this.close();
//        this.open();
    	this.db_iterator.rewind();
    }
}
