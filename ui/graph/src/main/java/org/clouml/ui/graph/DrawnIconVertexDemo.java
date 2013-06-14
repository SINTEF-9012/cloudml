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
package org.clouml.ui.graph;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;

import org.apache.commons.collections15.Transformer;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.ArtefactPortInstance;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;


import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;


import java.io.Serializable;


public class DrawnIconVertexDemo implements Serializable {

	/**
	 * the graph
	 */
	Graph<Vertex,String> graph;

	/**
	 * the visual component and renderer for the graph
	 */
	VisualizationViewer<Vertex,String> vv;
	
	private CPIMTable cpimTable=null;
	private CPSMTable cpsmTable=null;
	private JTable properties;
	private JTable runtimeProperties;
	
	private transient DeploymentModel dm;

	public DrawnIconVertexDemo(DeploymentModel dm) {

		this.dm=dm;
		// create a simple graph for the demo
		graph = new DirectedSparseGraph<Vertex,String>();
		vv =  new VisualizationViewer<Vertex,String>(new SpringLayout2<Vertex,String>(graph));

		vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.cyan));
		vv.getRenderContext().setEdgeLabelRenderer(new DefaultEdgeLabelRenderer(Color.cyan));

		vv.getRenderContext().setVertexIconTransformer(new Transformer<Vertex,Icon>() {
			public Icon transform(final Vertex v) {
				return new Icon() {

					public int getIconHeight() {
						return 40;
					}

					public int getIconWidth() {
						return 40;
					}

					public void paintIcon(Component c, Graphics g,
							int x, int y) {
						ImageIcon img;
						if(v.getType() == "node"){
							img=new ImageIcon(this.getClass().getResource("/server.png"));
						} else if(v.getType() == "platform") {
							img=new ImageIcon(this.getClass().getResource("/dbms.png"));
						}else{
							img=new ImageIcon(this.getClass().getResource("/soft.png"));
						}
						ImageObserver io=new ImageObserver() {

							public boolean imageUpdate(Image img, int infoflags, int x, int y,
									int width, int height) {
								// TODO Auto-generated method stub
								return false;
							}
						};
						g.drawImage(img.getImage(), x, y, getIconHeight(), getIconWidth(), io);

						if(!vv.getPickedVertexState().isPicked(v)) {
							g.setColor(Color.black);
						} else {
							g.setColor(Color.red);
							cpimTable=new CPIMTable(v);
							cpsmTable=new CPSMTable(v);
							properties.setModel(cpimTable);
							runtimeProperties.setModel(cpsmTable);
						}
						g.drawString(v.getName(), x-10, y+50);
					}};
			}});

		vv.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<Vertex>(vv.getPickedVertexState(), Color.white,  Color.yellow));
		vv.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<String>(vv.getPickedEdgeState(), Color.black, Color.lightGray));

		vv.setBackground(Color.white);

		// add my listener for ToolTips
		vv.setVertexToolTipTransformer(new ToStringLabeller<Vertex>());

		// create a frome to hold the graph
		final JFrame frame = new JFrame();
		Container content = frame.getContentPane();
		final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
		content.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final ModalGraphMouse gm = new DefaultModalGraphMouse<Integer,Number>();
		vv.setGraphMouse(gm);

		final ScalingControl scaler = new CrossoverScalingControl();
		

		JButton plus = new JButton("+");
		plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1.1f, vv.getCenter());
			}
		});
		JButton save = new JButton("save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		JButton load = new JButton("load");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
			}
		});
		JButton minus = new JButton("-");
		minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1/1.1f, vv.getCenter());
			}
		});

		JPanel controls = new JPanel();
		controls.add(plus);
		controls.add(minus);
		controls.add(save);
		controls.add(((DefaultModalGraphMouse<Integer,Number>) gm).getModeComboBox());
		content.add(controls, BorderLayout.SOUTH);
		
		
		//right panel
		JPanel intermediary=new JPanel();
		intermediary.setLayout(new BoxLayout(intermediary, BoxLayout.PAGE_AXIS));
		intermediary.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JLabel jlCPIM=new JLabel();
		jlCPIM.setText("CPIM");
		
		
		JLabel jlCPSM=new JLabel();
		jlCPSM.setText("CPSM");
		
		properties=new JTable(null);
		//properties.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JTableHeader h=properties.getTableHeader();		
		JPanel props=new JPanel();
		props.setLayout(new BorderLayout());
		props.add(new JScrollPane(properties),BorderLayout.CENTER);
		props.add(h,BorderLayout.NORTH);
		
		runtimeProperties=new JTable(null);
		//runtimeProperties.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JTableHeader h2=runtimeProperties.getTableHeader();
		JPanel runProps=new JPanel();
		runProps.setLayout(new BorderLayout());
		runProps.add(h2,BorderLayout.NORTH);
		runProps.add(new JScrollPane(runtimeProperties),BorderLayout.CENTER);
		
		intermediary.add(jlCPIM);
		intermediary.add(props);
		intermediary.add(jlCPSM);
		intermediary.add(runProps);
		
		
		content.add(intermediary,BorderLayout.EAST);
		
		//Left panel
		JPanel selection=new JPanel();
		JLabel nodes=new JLabel();
		nodes.setText("Types of nodes");
		JLabel artefacts=new JLabel();
		artefacts.setText("Types of artefacts");
		selection.setLayout(new BoxLayout(selection, BoxLayout.PAGE_AXIS));
		DefaultListModel lm=new DefaultListModel();
		for(Node n:dm.getNodeTypes().values()){
			lm.addElement(n.getName());
		}
		JList nodeTypes=new JList(lm);
		nodeTypes.setLayoutOrientation(JList.VERTICAL);
		nodeTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		nodeTypes.setVisibleRowCount(10);
		JScrollPane  types = new JScrollPane (nodeTypes);
		types.setPreferredSize(new Dimension(150, 80));
		selection.add(nodes);
		selection.add(types);
		
		DefaultListModel lmArtefacts=new DefaultListModel();
		for(Artefact n:dm.getArtefactTypes().values()){
			lmArtefacts.addElement(n.getName());
		}
		JList artefactsTypes=new JList(lmArtefacts);
		nodeTypes.setLayoutOrientation(JList.VERTICAL);
		nodeTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		nodeTypes.setVisibleRowCount(10);
		JScrollPane  arteTypes = new JScrollPane (artefactsTypes);
		types.setPreferredSize(new Dimension(150, 80));
		selection.add(artefacts);
		selection.add(arteTypes);
		
		content.add(selection,BorderLayout.WEST);
		
		frame.pack();
		frame.setVisible(true);
	}


	public ArrayList<Vertex> drawVerticesFromDeploymentModel(DeploymentModel dm){
		ArrayList<Vertex> V=new ArrayList<Vertex>();
		for(NodeInstance n : dm.getNodeInstances()){
			Vertex v= new Vertex(n.getName(), "node",n);
			V.add(v);
			createVertice(v);
		}
		for(ArtefactInstance x : dm.getArtefactInstances()){
			if(x.getDestination() == null){
				Vertex v= new Vertex(x.getName(), "platform",x);
				V.add(v);
				createVertice(v);
			}else{
				Vertex v= new Vertex(x.getName(), "soft",x);
				V.add(v);
				createVertice(v);	
			}
			properties.setModel(new CPIMTable(V.get(0)));
			runtimeProperties.setModel(new CPSMTable(V.get(0)));
		}
		return V;
	}

	public void drawEdgesFromDeploymentModel(DeploymentModel dm, ArrayList<Vertex> v){
		for(ArtefactInstance x : dm.getArtefactInstances()){
			if(x.getDestination() != null){
				Vertex v1=findVertex(x.getName(), v);
				Vertex v2=findVertex(x.getDestination().getName(), v);
				createEdge(v1.getName()+" on "+v2.getName(), v1, v2);
			}
			if(x.getRequired().size() > 0){
				for(ArtefactPortInstance p : x.getRequired()){
					Vertex v1=findVertex(x.getName(), v);
					Vertex v2=findVertex(p.getOwner().getName(), v);
					createEdge(v1.getName()+" requires "+v2.getName(), v1, v2);
				}
			}
		}
		for(BindingInstance bi: dm.getBindingInstances()){
			Vertex v1=findVertex(bi.getClient().getOwner().getName(), v);
			Vertex v2=findVertex(bi.getServer().getOwner().getName(), v);
			createEdge(v1.getName()+" requires "+v2.getName(), v1, v2);
		}
	}


	private Vertex findVertex(String name, ArrayList<Vertex> a){
		for(Vertex v : a){
			if(v.getName().equals(name))
				return v;
		}
		return null;
	}

	private void createEdge(String annotation, Vertex v1, Vertex v2){
		graph.addEdge(annotation, v1, v2);
	}

	private void createVertice(Vertex v) {
		graph.addVertex(v);
	}

}