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
package org.cloudml.codecs;
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
import java.awt.geom.Point2D;
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
import org.cloudml.core.*;



import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
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
	
	private transient DeploymentModel dmodel;

	private Transformer<Edge,Paint> edgeColor = new Transformer<Edge,Paint>() {
		public Paint transform(Edge e) {
			if(e.getType().equals("mandatory")) 
				return Color.RED;
			if(vv.getPickedEdgeState().isPicked(e))
				return Color.lightGray;
			return Color.BLACK;
		}
    };
    
    private Transformer<Vertex,Icon> vertexColor= new Transformer<Vertex,Icon>() {
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
					}
					g.drawString(v.getName(), x-10, y+50);
				}};
		}};
    
	
	public DrawnIconVertexDemo(final DeploymentModel dm) {
		this.dmodel=dm;
		// create a simple graph for the demo
		graph = new DirectedSparseGraph<Vertex,Edge>();
		vv =  new VisualizationViewer<Vertex,Edge>(new SpringLayout2<Vertex,Edge>(graph));

		vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.cyan));
		vv.getRenderContext().setEdgeLabelRenderer(new DefaultEdgeLabelRenderer(Color.cyan));

		vv.getRenderContext().setVertexIconTransformer(vertexColor);

		
		vv.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<Vertex>(vv.getPickedVertexState(), Color.white,  Color.yellow));
		vv.getRenderContext().setEdgeDrawPaintTransformer(edgeColor);
		
		vv.setBackground(Color.white);

		// add my listener for ToolTips
		vv.setVertexToolTipTransformer(new ToStringLabeller<Vertex>());
	}
	
	

	public ArrayList<Vertex> drawFromDeploymentModel(){
		Collection<Edge> c = new ArrayList<Edge>(graph.getEdges());
		for(Edge e : c)
			graph.removeEdge(e);
		Collection<Vertex> vs =new ArrayList<Vertex>(graph.getVertices());
		for(Vertex ve : vs)
			graph.removeVertex(ve);
		
		ArrayList<Vertex> v = drawVerticesFromDeploymentModel(dmodel);
		drawEdgesFromDeploymentModel(dmodel, v);
		System.out.println(vv);
		return v;
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
	
	public void writeServerJPEGImage(File file) {
		VisualizationImageServer<Vertex, Edge> vis = new VisualizationImageServer<Vertex, Edge>(vv.getGraphLayout(), vv.getGraphLayout().getSize());
		vis.setBackground(Color.WHITE);
		vis.getRenderContext().setEdgeDrawPaintTransformer(edgeColor);
		vis.getRenderContext().setVertexIconTransformer(vertexColor);
		
		BufferedImage image = (BufferedImage) vis.getImage(
			    new Point2D.Double(vis.getWidth(),vis.getHeight()),
			    new Dimension(vv.getGraphLayout().getSize()));

		try {
			ImageIO.write(image, "jpeg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public VisualizationViewer<Vertex,Edge> getVisualisationViewer(){
		return vv;
	}
	
	public Graph<Vertex,Edge> getGraph(){
		return graph;
	}
	
	public void setDeploymentModel(DeploymentModel dm){
		this.dmodel=dm;
	}
	
}