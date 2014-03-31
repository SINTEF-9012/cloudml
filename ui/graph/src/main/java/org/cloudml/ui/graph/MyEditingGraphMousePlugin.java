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

import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.Random;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.collections15.Factory;
import org.cloudml.codecs.Edge;
import org.cloudml.codecs.Vertex;
import org.cloudml.core.Artefact;
import org.cloudml.core.ArtefactInstance;
import org.cloudml.core.Binding;
import org.cloudml.core.BindingInstance;
import org.cloudml.core.ClientPort;
import org.cloudml.core.ClientPortInstance;
import org.cloudml.core.DeploymentModel;
import org.cloudml.core.Node;
import org.cloudml.core.NodeInstance;
import org.cloudml.core.ServerPort;
import org.cloudml.core.ServerPortInstance;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractGraphMousePlugin;

public class MyEditingGraphMousePlugin extends AbstractGraphMousePlugin implements
        MouseListener, MouseMotionListener {

    private VisualizationViewer<Vertex, Edge> vv;
    private Graph<Vertex, Edge> graph;
    private JList nodeTypes;
    private DeploymentModel dm;

    public MyEditingGraphMousePlugin(int modifiers, VisualizationViewer<Vertex, Edge> vv, Graph<Vertex, Edge> graph, JList nodeTypes, DeploymentModel dm) {
        super(modifiers);
        this.vv = vv;
        this.graph = graph;
        this.nodeTypes = nodeTypes;
        this.dm = dm;
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
        GraphElementAccessor<Vertex, Edge> pickSupport = vv.getPickSupport();
        if (e.getButton() == e.BUTTON1) {
            if (pickSupport != null && nodeTypes.getSelectedIndex() != -1) {
                String nodeType = (String) nodeTypes.getSelectedValue();
                Random r = new Random();
                int cnt = r.nextInt();
                final Vertex vertex = pickSupport.getVertex(vv.getModel().getGraphLayout(), p.getX(), p.getY());
                //Not on a vertex
                if (vertex == null) {
                    for (Artefact a : dm.getArtefactTypes()) {
                        if (a.getName().equals(nodeType)) {
                            ArtefactInstance ai = a.instanciates(nodeType + cnt);
                            ai.setDestination(selectDestination());
                            dm.getArtefactInstances().add(ai);
                            for (ClientPort c : a.getRequired()) {
                                ai.getRequired().add(new ClientPortInstance(c.getName() + cnt, c, ai));
                            }
                            for (ServerPort s : a.getProvided()) {
                                ai.getProvided().add(new ServerPortInstance(s.getName() + cnt, s, ai));
                            }
                            Vertex v = new Vertex(nodeType + cnt, "soft", ai);
                            graph.addVertex(v);
                            vv.getModel().getGraphLayout().setLocation(v, vv.getRenderContext().getMultiLayerTransformer().inverseTransform(e.getPoint()));
                            Edge newEdge = new Edge(ai.getDestination().getName() + cnt, "destination");
                            Vertex dest = null;
                            for (Vertex vDest : graph.getVertices()) {
                                if (vDest.getName().equals(ai.getDestination().getName())) {
                                    graph.addEdge(newEdge, v, vDest);
                                    break;
                                }
                            }
                        }
                    }
                    for (Node a : dm.getNodeTypes()) {
                        if (a.getName().equals(nodeType)) {
                            NodeInstance ai = a.instanciates(nodeType + cnt);
                            dm.getNodeInstances().add(ai);
                            Vertex v = new Vertex(nodeType + cnt, "node", ai);
                            graph.addVertex(v);
                            vv.getModel().getGraphLayout().setLocation(v, vv.getRenderContext().getMultiLayerTransformer().inverseTransform(e.getPoint()));
                        }
                    }
                    for (Binding b : dm.getBindingTypes()) {
                        if (b.getName().equals(nodeType)) {
                            BindingInstance bi = b.instanciates(nodeType + cnt);
                            dm.getBindingInstances().add(bi);
                            Edge edge;
                            if (b.getClient().getIsOptional()) {
                                edge = new Edge(nodeType + cnt, "optional");
                            }
                            else {
                                edge = new Edge(nodeType + cnt, "mandatory");
                            }

                            String client = selectClientPortInstance(bi);
                            String server = selectServerPortInstance(bi);

                            Vertex v1 = null, v2 = null;
                            if (!client.equals("")) {
                                for (Vertex v : graph.getVertices()) {
                                    if (v.getName().equals(client)) {
                                        v1 = v;
                                    }
                                }
                            }
                            if (!server.equals("")) {
                                for (Vertex v : graph.getVertices()) {
                                    if (v.getName().equals(server)) {
                                        v2 = v;
                                    }
                                }
                            }
                            if (v1 != null && v2 != null) {
                                graph.addEdge(edge, v1, v2);
                            }
                        }
                    }
                }
            }
        }
        else {
            if (pickSupport != null) {
                final Vertex vertex = pickSupport.getVertex(vv.getModel().getGraphLayout(), p.getX(), p.getY());
                final Edge edge = pickSupport.getEdge(vv.getModel().getGraphLayout(), p.getX(), p.getY());
                if (vertex != null) {
                    if (vertex.getType().equals("node")) {
                        //need to remove also the bindings and the requirements !!!
                        dm.getNodeInstances().remove(vertex);
                        graph.removeVertex(vertex);
                    }
                    else {
                        dm.getArtefactInstances().remove(vertex);
                        graph.removeVertex(vertex);
                    }
                }
                else {
                    dm.getBindingInstances().remove(edge);
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

    public String selectClientPortInstance(BindingInstance bi) {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Please make a selection:"));
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (ArtefactInstance ai : dm.getArtefactInstances()) {
            System.out.println(ai.getRequired());
            for (ClientPortInstance ci : ai.getRequired()) {
                System.out.println(bi.getType().getClient() + " #### " + ci.getType());
                if (ci.getType().equals(bi.getType().getClient())) {
                    model.addElement(ci);
                }
            }
        }
        JComboBox comboBox = new JComboBox(model);
        panel.add(comboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "ClientPort", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        switch (result) {
            case JOptionPane.OK_OPTION:
                bi.setClient((ClientPortInstance) comboBox.getSelectedItem());
                return ((ClientPortInstance) comboBox.getSelectedItem()).getOwner().getName();
        }
        return "";
    }

    public NodeInstance selectDestination() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Please make a selection:"));
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (NodeInstance n : dm.getNodeInstances()) {
            model.addElement(n);
        }
        JComboBox comboBox = new JComboBox(model);
        panel.add(comboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Destination", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        switch (result) {
            case JOptionPane.OK_OPTION:
                return ((NodeInstance) comboBox.getSelectedItem());
        }
        return null;
    }

    public String selectServerPortInstance(BindingInstance bi) {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Please make a selection:"));
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (ArtefactInstance ai : dm.getArtefactInstances()) {
            for (ServerPortInstance ci : ai.getProvided()) {
                if (ci.getType().equals(bi.getType().getServer())) {
                    model.addElement(ci);
                }
            }
        }
        JComboBox comboBox = new JComboBox(model);
        panel.add(comboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "ServerPort", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        switch (result) {
            case JOptionPane.OK_OPTION:
                bi.setServer((ServerPortInstance) comboBox.getSelectedItem());
                return ((ServerPortInstance) comboBox.getSelectedItem()).getOwner().getName();
        }
        return "";
    }
}