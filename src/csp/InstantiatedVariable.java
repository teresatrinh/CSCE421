package csp;

import java.util.Comparator;

public class InstantiatedVariable implements Comparator<InstantiatedVariable>{

    public static final Comparator<InstantiatedVariable> ByName = Comparator.comparing(InstantiatedVariable::getVarName);

    public static final Comparator<InstantiatedVariable> ByDomain = Comparator.comparing(InstantiatedVariable::getDomainLength);

    public static final Comparator<InstantiatedVariable> ByNeighbor = Comparator.comparing(InstantiatedVariable::getNeighborLength);

    public static final Comparator<InstantiatedVariable> ByDegree = Comparator.comparing(InstantiatedVariable::getDegree);

    public Variable variable;
    public int value;

    public InstantiatedVariable(Variable var, int value) {
        this.variable = var;
        this.value = value;
    }

    public InstantiatedVariable(Variable var) {
        this.variable = var;
    }

    public void setValue(int a) {
        this.value = a;
    }

    public Variable getVar() {
        return this.variable;
    }

    public int getValue() {
        return this.value;
    }

    public String getVarName() {
        return this.variable.getName();
    }

    public int getDomainLength() {
        return this.variable.initialDomainLength();
    }

    public int getNeighborLength() {
        return this.variable.neighborLength();
    }
    
    public int getDegree() {
        return this.variable.degree();
    }

    @Override
    public int compare(InstantiatedVariable o1, InstantiatedVariable o2) {
        return o1.getVarName().compareTo(o2.getVarName());
    }
}
