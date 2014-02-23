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


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Random;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.cloudml.codecs.Edge;
import org.cloudml.codecs.Vertex;
import org.cloudml.core.*;
import org.cloudml.core.VM;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractGraphMousePlugin;


public class MyEditingGraphMousePlugin extends AbstractGraphMousePlugin implements
MouseListener, MouseMotionListener {

	private VisualizationViewer<Vertex,Edge> vv;
	private Graph<Vertex,Edge> graph;
	private JList nodeTypes;
	private CloudMLModel dm;

	public MyEditingGraphMousePlugin(int modifiers, VisualizationViewer<Vertex,Edge> vv, Graph<Vertex,Edge> graph,JList nodeTypes, CloudMLModel dm) {
		super(modifiers);
		this.vv=vv;
		this.graph=graph;
		this.nodeTypes=nodeTypes;
		this.dm=dm;
	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		final Point2D p = e.getPoint();
		GraphElementAccessor<Vertex,Edge> pickSupport = vv.getPickSupport();
		if (e.getButton() == e.BUTTON1){
			if(pickSupport != null && nodeTypes.getSelectedIndex() != -1){
				String nodeType=(String)nodeTypes.getSelectedValue();
				Random r = new Random();
				int cnt=r.nextInt();
				final Vertex vertex = pickSupport.getVertex(vv.getModel().getGraphLayout(), p.getX(), p.getY());
				//Not on a vertex
				if(vertex == null){
					for(Component a:dm.getComponents().values()){
                        if(a instanceof InternalComponent){
                            InternalComponent ic=(InternalComponent)a;
                            if(a.getName().equals(nodeType)){
                                InternalComponentInstance ai= ic.instantiates(nodeType + cnt);
                                ai.setDestination(selectDestination());
                                dm.getComponentInstances().add(ai);
                                for(RequiredPort c:ic.getRequiredPorts())
                                    ai.getRequiredPortInstances().add(new RequiredPortInstance(c.getName()+cnt, c, ai));
                                for(ProvidedPort s:ic.getProvidedPorts())
                                    ai.getProvidedPortInstances().add(new ProvidedPortInstance(s.getName()+cnt, s, ai));
                                Vertex v=new Vertex(nodeType+cnt, "soft", ai);
                                graph.addVertex(v);
                                vv.getModel().getGraphLayout().setLocation(v, vv.getRenderContext().getMultiLayerTransformer().inverseTransform(e.getPoint()));
                                Edge newEdge=new Edge(ai.getDestination().getName()+cnt, "destination");
                                Vertex dest=null;
                                for(Vertex vDest : graph.getVertices()){
                                    if(vDest.getName().equals(ai.getDestination().getName())){
                                        graph.addEdge(newEdge, v, vDest);
                                        break;
                                    }
                                }
                            }
                        }//TODO Else
					}
					for(ExternalComponent a:dm.getExternalComponents().values()){
						if(a.getName().equals(nodeType) && a instanceof ExternalComponent){
							VMInstance ai= ((VM)a).instantiates(nodeType + cnt);
							dm.getExternalComponentInstances().add(ai);
							Vertex v=new Vertex(nodeType+cnt, "node", ai);
							graph.addVertex(v);
							vv.getModel().getGraphLayout().setLocation(v, vv.getRenderContext().getMultiLayerTransformer().inverseTransform(e.getPoint()));
						}
					}
					for(Relationship b:dm.getRelationships().values()){
						if(b.getName().equals(nodeType)){
							RelationshipInstance bi=b.instantiates(nodeType + cnt);
							dm.getRelationshipInstances().add(bi);
							Edge edge;
							if(!b.getRequiredPort().getIsMandatory())
								edge=new Edge(nodeType+cnt, "optional");
							else edge=new Edge(nodeType+cnt, "mandatory");

							String client=selectClientPortInstance(bi);
							String server=selectServerPortInstance(bi);

							Vertex v1=null,v2=null;
							if(!client.equals("")){
								for(Vertex v:graph.getVertices())
									if(v.getName().equals(client))
										v1=v;
							}
							if(!server.equals("")){
								for(Vertex v:graph.getVertices())
									if(v.getName().equals(server))
										v2=v;
							}
							if(v1 != null && v2 != null)
								graph.addEdge(edge, v1, v2);
						}
					}
				}
			}
		}else{
			if(pickSupport != null){
				final Vertex vertex = pickSupport.getVertex(vv.getModel().getGraphLayout(), p.getX(), p.getY());
				final Edge edge = pickSupport.getEdge(vv.getModel().getGraphLayout(), p.getX(), p.getY());
				if(vertex != null){
					if(vertex.getType().equals("node")){
						//need to remove also the bindings and the requirements !!!
						dm.getExternalComponentInstances().remove(vertex);
						graph.removeVertex(vertex);
					}else{
						dm.getComponentInstances().remove(vertex);
						graph.removeVertex(vertex);
					}
				}else{
					dm.getRelationshipInstances().remove(edge);
					graph.removeEdge(edge);
				}
			}
		}
		nodeTypes.clearSelection();
		vv.repaint();
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}    
	
	public String selectClientPortInstance(RelationshipInstance bi){
		JPanel panel = new JPanel();
		panel.add(new JLabel("Please make a selection:"));
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for(ComponentInstance ai:dm.getComponentInstances()){
            if(ai instanceof InternalComponentInstance){
                for(RequiredPortInstance ci:((InternalComponentInstance)ai).getRequiredPortInstances()){
                    System.out.println(bi.getType().getRequiredPort() + " #### "+ ci.getType());
                    if(ci.getType().equals(bi.getType().getRequiredPort())){
                        model.addElement(ci);
                    }
                }
            }
		}
		JComboBox comboBox = new JComboBox(model);
		panel.add(comboBox);

		int result = JOptionPane.showConfirmDialog(null, panel, "RequiredPort", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		switch (result) {
		case JOptionPane.OK_OPTION:
			bi.setRequiredPortInstance((RequiredPortInstance) comboBox.getSelectedItem());
			return ((RequiredPortInstance)comboBox.getSelectedItem()).getOwner().getName();
		}
		return "";
	}
	
	public VMInstance selectDestination(){
		JPanel panel = new JPanel();
		panel.add(new JLabel("Please make a selection:"));
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for(ExternalComponentInstance n:dm.getExternalComponentInstances()){
			model.addElement(n);
		}
		JComboBox comboBox = new JComboBox(model);
		panel.add(comboBox);
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Destination", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		switch (result) {
		case JOptionPane.OK_OPTION:
			return ((VMInstance)comboBox.getSelectedItem());
		}
		return null;
	}
	
	public String selectServerPortInstance(RelationshipInstance bi){
		JPanel panel = new JPanel();
		panel.add(new JLabel("Please make a selection:"));
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for(ComponentInstance ai:dm.getComponentInstances()){
            Iterator<ProvidedPortInstance> it=ai.getProvidedPortInstances().iterator();
            while(it.hasNext()){//TODO: check issue with foreach on list<ProvidedPortInstances>
                ProvidedPortInstance ci=it.next();
                if(ci.getType().equals(bi.getType().getProvidedPort())){
                    model.addElement(ci);
                }
            }
		}
		JComboBox comboBox = new JComboBox(model);
		panel.add(comboBox);

		int result = JOptionPane.showConfirmDialog(null, panel, "ProvidedPort", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		switch (result) {
		case JOptionPane.OK_OPTION:
			bi.setProvidedPortInstance((ProvidedPortInstance) comboBox.getSelectedItem());
			return ((ProvidedPortInstance)comboBox.getSelectedItem()).getOwner().getName();
		}
		return "";
	}

}