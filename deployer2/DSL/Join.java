package dsl;

import org.apache.commons.collections.list.FixedSizeList;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Maksym on 13.03.2015.
 */
public class Join extends ControlNode {

    public Join(ArrayList<ActivityEdge> incoming, ActivityEdge outgoing) throws Exception{
        super();
        this.setIncoming(incoming);
        // set outgoing edge and protect outgoing edges list from being expanded
        this.addEdge(outgoing, Direction.OUT);
        this.outgoing = FixedSizeList.decorate(Arrays.asList(this.getOutgoing()));
    }

}
