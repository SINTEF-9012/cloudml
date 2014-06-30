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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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
import org.cloudml.codecs.DrawnIconVertexDemo;
import org.cloudml.codecs.Edge;
import org.cloudml.codecs.JsonCodec;
import org.cloudml.codecs.Vertex;
import org.cloudml.core.*;
import org.cloudml.core.Component;
import org.cloudml.facade.*;
import org.cloudml.facade.commands.CloudMlCommand;
import org.cloudml.facade.commands.CommandFactory;

import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;

public class Visu {
	private JTable properties;
	private JTable runtimeProperties;
	private JList nodeTypes;

	private Deployment dmodel;
	private DrawnIconVertexDemo v;
	private CloudML cml;

	public Visu(Deployment model, DrawnIconVertexDemo v){
		this.dmodel=model;
		this.v=v;
		cml=Factory.getInstance().getCloudML();
	}

	public void createFrame(){
		final VisualizationViewer<Vertex, Edge> vv= v.getVisualisationViewer();
		
		vv.getRenderContext().setVertexIconTransformer(new Transformer<Vertex,Icon>() {
			public Icon transform(final Vertex v) {
				return new Icon() {

					public int getIconHeight() {
						return 40;
					}

                    public int getIconWidth() {
						return 40;
					}

					public void paintIcon(java.awt.Component c, Graphics g,
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
						
							properties.setModel(new CPIMTable(v));
							runtimeProperties.setModel(new CPSMTable(v));
						}
						g.drawString(v.getName(), x-10, y+50);
					}};
			}});
		
		
		
		
		// create a frame to hold the graph
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
					codec.save(dmodel, streamResult);
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
				v.writeJPEGImage(result);
			}
		});
		//WE NEED TO UPDATE THE FACADE AND THE GUI
		JButton load = new JButton("load");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showDialog(frame, "load");
				File result = fc.getSelectedFile();
				JsonCodec codec=new JsonCodec();
				try {
					InputStream stream = new FileInputStream(result);
					Deployment model = (Deployment)codec.load(stream);
					dmodel=model;
					v.setDeploymentModel(dmodel);
					ArrayList<Vertex> V=v.drawFromDeploymentModel();
					nodeTypes.removeAll();
					nodeTypes.setModel(fillList());
					
					properties.setModel(new CPIMTable(V.get(0)));
					runtimeProperties.setModel(new CPSMTable(V.get(0)));
					
					CommandFactory fcommand=new CommandFactory();
					CloudMlCommand load=fcommand.loadDeployment(result.getPath());
					cml.fireAndWait(load);
					
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
				//cad.deploy(dmodel);
				System.out.println("deploy");
				CommandFactory fcommand=new CommandFactory();
				CloudMlCommand deploy=fcommand.deploy();
				cml.fireAndWait(deploy);
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

		((DefaultModalGraphMouse<Integer,Number>) gm).add(new MyEditingGraphMousePlugin(0, vv, v.getGraph(), nodeTypes,dmodel));
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
		for(ExternalComponent n:dmodel.getComponents().onlyExternals()){
			lm.addElement(n.getName());
		}
		for(Component n:dmodel.getComponents()){
			lm.addElement(n.getName());
		}
		for(Relationship b:dmodel.getRelationships()){
			lm.addElement(b.getName());
		}
		return lm;
	}

}
