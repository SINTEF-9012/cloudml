package org.cloudml.mrt.coord.cmd.abstracts;

public class Change implements Comparable<Change>{
    
    long timeStamp;
    public Change(){
        timeStamp = System.currentTimeMillis();
    }

    @Override
    public int compareTo(Change o) {
       return Long.compare(timeStamp, ((Change)o).timeStamp);
    }
    
    public Change obtainRepr(){
    	return this;
    }
    
    static final Change fakeChange = new Change();
    public static Change fakeChangeForSearch(long timeStamp){
        fakeChange.timeStamp = timeStamp;
        return fakeChange;
    }
}
