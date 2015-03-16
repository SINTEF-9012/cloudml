package dsl;

import org.apache.commons.collections.list.FixedSizeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Maksym on 13.03.2015.
 */
public class Fork extends ControlNode {

    public Fork(ActivityEdge incoming, ArrayList<ActivityEdge> outgoing) throws Exception{
        super();
        this.setOutgoing(outgoing);
        // set incoming edge and protect incoming edges list from being expanded
        this.addEdge(incoming, "in");
        this.incoming = FixedSizeList.decorate(Arrays.asList(this.getIncoming()));
    }
}
