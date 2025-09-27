package Assignment1;

public class Metrics {
    public long comparisons = 0;
    public long writes = 0;
    public int curDepth = 0;
    public int maxDepth = 0;
    public long allocations = 0;
    public void reset() { comparisons = writes = curDepth = maxDepth = (int) (allocations = 0); }
    public void incComp(){ comparisons++; }
    public void addComps(long v){ comparisons += v; }
    public void incWrite(){ writes++; }
    public void addWrites(long v){ writes += v; }
    public void enter(){ curDepth++; if(curDepth>maxDepth) maxDepth = curDepth; }
    public void exit(){ curDepth--; if(curDepth<0) curDepth=0; }
    public void alloc(){ allocations++; }
}
