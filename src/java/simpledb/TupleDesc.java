package simpledb;

import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {

    /**
     * A help class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * */
        public final Type fieldType;
        
        /**
         * The name of the field
         * */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

    private final ArrayList<TDItem> tupleDescriptor; 
    
    /**
     * @return
     *        An iterator which iterates over all the field TDItems
     *        that are included in this TupleDesc
     * */
    public Iterator<TDItem> iterator() {
        return tupleDescriptor.iterator(); 
    	}

    
    private static final long serialVersionUID = 1L;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may
     *            be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        // some code goes here
    	this.tupleDescriptor = new ArrayList<TDItem>(); 
    	for (int i =0; typeAr.length > i && fieldAr.length > i; ++i) {
    		this.tupleDescriptor.add(new TDItem(typeAr[i],fieldAr[i]));
    	}
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
    	this.tupleDescriptor = new ArrayList<TDItem>(); 
    	for (int i =0; typeAr.length > i; ++i) {
    		this.tupleDescriptor.add(new TDItem(typeAr[i], ""));
    	}
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
    	return this.tupleDescriptor.size(); 
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     * 
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        // some code goes here
    	if (i< this.tupleDescriptor.size() && i>=0) {
    		return this.tupleDescriptor.get(i).fieldName; 
    	}else {
    		throw new NoSuchElementException();
    	}
      }

    /**
     * Gets the type of the ith field of this TupleDesc.
     * 
     * @param i
     *            The index of the field to get the type of. It must be a valid
     *            index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
    	if (i < this.tupleDescriptor.size() && i>=0) {
    		return this.tupleDescriptor.get(i).fieldType; 
    	}else {
    		throw new NoSuchElementException();
    	}
    }

    /**
     * Find the index of the field with a given name.
     * 
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {
        	for (int i =0; this.tupleDescriptor.size() > i; ++i) {
        		if (this.tupleDescriptor.get(i).fieldName.equals(name)) {
        			return i;
        		}
        	}
    		throw new NoSuchElementException();
    	}
   

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
    	int size_TupleDesc = 0;
    	for (int i =0; this.tupleDescriptor.size() > i; ++i) {
    		size_TupleDesc+= this.tupleDescriptor.get(i).fieldType.getLen();
    	}
    	return size_TupleDesc; 
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     * 
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
    	int size_of_both = td1.numFields()+td2.numFields(); 
    	Type[] typeAr = new Type[size_of_both];
    	String[] fieldAr = new String[size_of_both];
    	
    	for (int i = 0; i< td1.numFields(); ++i ) {
    		typeAr[i] = td1.tupleDescriptor.get(i).fieldType; 
    		fieldAr[i] = td1.tupleDescriptor.get(i).fieldName; 

    	}
    	
    	for (int i = td1.numFields(); i< size_of_both; ++i ) {
    		typeAr[i] = td2.tupleDescriptor.get(i-td1.numFields()).fieldType; 
    		fieldAr[i] = td2.tupleDescriptor.get(i-td1.numFields()).fieldName; 

    	}
    		

    	return new TupleDesc(typeAr,fieldAr ); 
    }
    

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they have the same number of items
     * and if the i-th type in this TupleDesc is equal to the i-th type in o
     * for every i.
     * 
     * @param o
     *            the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */

    public boolean equals(Object o) {

    	if (o instanceof TupleDesc) {
    		TupleDesc otherTupleDesc = (TupleDesc) o; 
    		if (otherTupleDesc.numFields() != this.numFields()) {
    			return false;
    		}
    		else {
    			String originalName; 
    			String otherName;
    			for (int i = 0; i< this.numFields(); ++i ) {
    				originalName = this.tupleDescriptor.get(i).fieldName; 
    				otherName = otherTupleDesc.tupleDescriptor.get(i).fieldName; 
    				if (!originalName.equals(otherName)) {
    					return false; 
    				}
    				
    			}
    			return true; 
    		}
    		
    	}else {
    		return false; 
    	}
    	
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * 
     * @return String describing this descriptor.
     */
    public String toString() {
    	String strDescribe = ""; 
    	String end; 
    	for (int i = 0; i< this.numFields(); ++i ) {
    		end = ",";
    		if (i == this.numFields() -1) {
    			end = "";
    		}
    		strDescribe += this.tupleDescriptor.get(i).fieldType+"("+this.tupleDescriptor.get(i).fieldName + ")"+end; 
    	}
    	
        return strDescribe;
    }
    
    
    
    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
    	return Integer.parseInt(toString()); 
 
    }

    
    
   
    
}















