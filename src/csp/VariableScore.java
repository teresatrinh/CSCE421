package csp;

public class VariableScore {
    private int index;
    private int domainSize;
    private int degree;

    public VariableScore(int domainSize, int degree) {
        this.domainSize = domainSize;
        this.degree = degree;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public int getIndex() {
        return index;
    }

    public int getDomainSize() {
        return domainSize;
    }

    public int getDegree() {
        return degree;
    }

    public double getRatio() {
        if (degree == 0) {
            return Double.MAX_VALUE;
        } 
        return (double) domainSize / degree;
    }
}
