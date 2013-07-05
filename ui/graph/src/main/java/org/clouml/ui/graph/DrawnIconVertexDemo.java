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
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;

import org.apache.commons.collections15.Transformer;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.core.*;
import org.cloudml.deployer.CloudAppDeployer;



import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.EditingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;


public class DrawnIconVertexDemo implements Serializable {

	/**
	 * the graph
	 */
	Graph<Vertex,Edge> graph;

	/**
	 * the visual component and renderer for the graph
	 */
	VisualizationViewer<Vertex,Edge> vv;
	
	private CPIMTable cpimTable=null;
	private CPSMTable cpsmTable=null;
	private JTable properties;
	private JTable runtimeProperties;
	private JList nodeTypes;
	
	private transient DeploymentModel dmodel;
	private CloudAppDeployer cad;

	public DrawnIconVertexDemo(final DeploymentModel dm) {
		cad=new CloudAppDeployer();
		this.dmodel=dm;
		// create a simple graph for the demo
		graph = new DirectedSparseGraph<Vertex,Edge>();
		vv =  new VisualizationViewer<Vertex,Edge>(new SpringLayout2<Vertex,Edge>(graph));

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

        Transformer<Edge,Paint> edgeColor = new Transformer<Edge,Paint>() {
			public Paint transform(Edge e) {
				if(e.getType().equals("mandatory")) 
					return Color.RED;
				if(vv.getPickedEdgeState().isPicked(e))
					return Color.lightGray;
				return Color.BLACK;
			}
        };
		
		vv.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<Vertex>(vv.getPickedVertexState(), Color.white,  Color.yellow));
		vv.getRenderContext().setEdgeDrawPaintTransformer(edgeColor);
		
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
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showDialog(frame, "save");
				File result = fc.getSelectedFile();
				JsonCodec codec=new JsonCodec();
				OutputStream streamResult;
				try {
					streamResult = new FileOutputStream(result);
					codec.save(dm, streamResult);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		JButton saveImage = new JButton("save as image");
		saveImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showDialog(frame, "save");
				File result = fc.getSelectedFile();
				JsonCodec codec=new JsonCodec();
				writeJPEGImage(result);
			}
		});
		JButton load = new JButton("load");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showDialog(frame, "load");
				File result = fc.getSelectedFile();
				JsonCodec codec=new JsonCodec();
				try {
					InputStream stream = new FileInputStream(result);
					DeploymentModel model = (DeploymentModel)codec.load(stream);
					dmodel=model;
					drawFromDeploymentModel();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		JButton minus = new JButton("-");
		minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1/1.1f, vv.getCenter());
			}
		});
		JButton deploy=new JButton("Deploy!");
		deploy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cad.deploy(dmodel);
			}
		});
		
		
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
		nodes.setText("Types");
		selection.setLayout(new BoxLayout(selection, BoxLayout.PAGE_AXIS));		
		nodeTypes=new JList(fillList());
		nodeTypes.setLayoutOrientation(JList.VERTICAL);
		nodeTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		nodeTypes.setVisibleRowCount(10);
		JScrollPane  types = new JScrollPane (nodeTypes);
		types.setPreferredSize(new Dimension(150, 80));
		selection.add(nodes);
		selection.add(types);


		content.add(selection,BorderLayout.WEST);

		((DefaultModalGraphMouse<Integer,Number>) gm).add(new MyEditingGraphMousePlugin(0, vv, graph, nodeTypes,dm));
		JPanel controls = new JPanel();
		controls.add(plus);
		controls.add(minus);
		controls.add(save);
		controls.add(saveImage);
		controls.add(load);
		controls.add(deploy);
		controls.add(((DefaultModalGraphMouse<Integer,Number>) gm).getModeComboBox());
		content.add(controls, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);
	}


	public DefaultListModel fillList(){
		DefaultListModel lm=new DefaultListModel();
		for(Node n:dmodel.getNodeTypes().values()){
			lm.addElement(n.getName());
		}
		for(Artefact n:dmodel.getArtefactTypes().values()){
			lm.addElement(n.getName());
		}
		for(Binding b:dmodel.getBindingTypes().values()){
			lm.addElement(b.getName());
		}
		return lm;
	}

	public void drawFromDeploymentModel(){
		Collection<Edge> c = new ArrayList<Edge>(graph.getEdges());
		for(Edge e : c)
			graph.removeEdge(e);
		Collection<Vertex> vs =new ArrayList<Vertex>(graph.getVertices());
		for(Vertex ve : vs)
			graph.removeVertex(ve);
		
		ArrayList<Vertex> v = drawVerticesFromDeploymentModel(dmodel);
		drawEdgesFromDeploymentModel(dmodel, v);
		nodeTypes.removeAll();
		nodeTypes.setModel(fillList());
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
				Edge e=new Edge("dest"+x.getName(), "destination");
				createEdge(e, v1, v2);
			}
		}
		for(BindingInstance bi: dm.getBindingInstances()){
			Vertex v1=findVertex(bi.getClient().getOwner().getName(), v);
			Vertex v2=findVertex(bi.getServer().getOwner().getName(), v);
			Edge e;
			if(bi.getClient().getType().getIsOptional())
				e=new Edge(bi.getName(), "optional",bi);
			else e=new Edge(bi.getName(), "mandatory",bi);
			createEdge(e, v1, v2);
		}
	}


	private Vertex findVertex(String name, ArrayList<Vertex> a){
		for(Vertex v : a){
			if(v.getName().equals(name))
				return v;
		}
		return null;
	}

	private void createEdge(Edge e, Vertex v1, Vertex v2){
		graph.addEdge(e, v1, v2);
	}

	private void createVertice(Vertex v) {
		graph.addVertex(v);
	}
	
	public void writeJPEGImage(File file) {
		int width = vv.getWidth();
		int height = vv.getHeight();

		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bi.createGraphics();
		vv.paint(graphics);
		graphics.dispose();

		try {
			ImageIO.write(bi, "jpeg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}