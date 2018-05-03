package jslc;

import java.util.*;

public class SymbolTable {
	public SymbolTable() {
		globalTable = new Hashtable<String, Object>();
		localTable = new Hashtable<String, Object>();
	}
	
	public Object putInGlobal( String key, Object value ) {
		return globalTable.put(key, value);
	}

	public void putAllInGlobal( Hashtable<String, Object> table ) {
		globalTable.putAll(table);
	}
	
	public Object putInLocal( String key, Object value ) {
		return localTable.put(key, value);
	}

	public void putAllInLocal( Hashtable<String, Object> table ) {
		localTable.putAll(table);
	}
	
	public Object getInLocal( String key ) {
		return localTable.get(key);
	}
	
	public Object getInGlobal( String key ) {
		return globalTable.get(key);
	}
	
	public Object get( String key ) {
	// returns the object corresponding to the key.
		Object result;
		if ( (result = localTable.get(key)) != null ) {
			// found local identifier
			return result;
		}
		else {
			// global identifier, if it is in globalTable
			return globalTable.get(key);
		}
	}	
	
	public void removeLocalIdent() {
	// remove all local identifiers from the table
		localTable.clear();
	}
	
	private Hashtable<String, Object> globalTable, localTable;
}

