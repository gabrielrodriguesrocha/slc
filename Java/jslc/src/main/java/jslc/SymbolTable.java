package jslc;

import java.util.*;

import jslc.AST.NamedTypeable;

public class SymbolTable {
	public SymbolTable() {
		globalTable = new Hashtable<String, NamedTypeable>();
		localTable = new Hashtable<String, NamedTypeable>();
	}
	
	public NamedTypeable putInGlobal( String key, NamedTypeable value ) {
		return globalTable.put(key, value);
	}

	public void putAllInGlobal( Hashtable<String, NamedTypeable> table ) {
		globalTable.putAll(table);
	}
	
	public NamedTypeable putInLocal( String key, NamedTypeable value ) {
		return localTable.put(key, value);
	}

	public void putAllInLocal( Hashtable<String, NamedTypeable> table ) {
		localTable.putAll(table);
	}
	
	public NamedTypeable getInLocal( String key ) {
		return localTable.get(key);
	}
	
	public NamedTypeable getInGlobal( String key ) {
		return globalTable.get(key);
	}

	public void moveLocalToGlobal () {
		globalTable.putAll(localTable);
		removeLocalIdent();
	}
	
	public NamedTypeable get( String key ) {
	// returns the object corresponding to the key.
		NamedTypeable result;
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
	
	private Hashtable<String, NamedTypeable> globalTable, localTable;
}

