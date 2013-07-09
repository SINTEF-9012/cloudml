/**
 * This file is part of CloudML [ http://cloudml.org ]
 *
 * Copyright (C) 2012 - SINTEF ICT
 * Contact: Franck Chauvel <franck.chauvel@sintef.no>
 *
 * Module: root
 *
 * CloudML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * CloudML is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with CloudML. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.cloudml.ui.graph;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.WithProperties;

public class CPSMTable extends AbstractTableModel{
	private Vertex v;
	private final String[] heads = {"Property", "Value"};
	private final Field[]  properties;
	
	public CPSMTable(Vertex v){
		super();
		this.v=v;
		Class c;
		if(v.getType().equals("node"))
			c=((NodeInstance)v.getInstance()).getClass();
		else c=((ArtefactInstance)v.getInstance()).getClass();
		this.properties=c.getDeclaredFields();
	}
	
	public int getRowCount() {
		return properties.length;
	}
	
	@Override
	public String getColumnName(int pCol) {
		return heads[pCol];
	}

	public int getColumnCount() {
		return heads.length;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Object o=null;
		switch(columnIndex){
		case 0:
			return properties[rowIndex].getName();
		default:
			try {
				properties[rowIndex].setAccessible(true);
				if(v.getType().equals("node"))
					o=properties[rowIndex].get(((NodeInstance)v.getInstance()));
				else o=properties[rowIndex].get(((ArtefactInstance)v.getInstance()));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return o+"";
		}
	}

}
