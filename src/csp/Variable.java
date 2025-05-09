package csp;

import abscon.instance.components.PVariable;
import java.util.ArrayList;
import java.util.Comparator;

public class Variable implements Comparable<Variable> {

    public static final Comparator<Variable> ByName = Comparator.comparing(Variable::getName);

    public static final Comparator<Variable> ByDomain = Comparator.comparing(Variable::initialDomainLength);

    public static final Comparator<Variable> ByNeighbor = Comparator.comparing(Variable::neighborLength);

    public static final Comparator<Variable> ByDegree = Comparator.comparing(Variable::degree);

    //Keep reference to original variable, just in case it is needed later
    protected PVariable varRef;

    //Best to create *deep copy* of the data-structures that are needed for the homework
    protected String name;

    protected Domain initDomain;

    protected Domain currDomain;

    protected ArrayList<Constraint> constraints = new ArrayList<>();

    protected ArrayList<Variable> neighbors = new ArrayList<>();

    public Variable (PVariable var) {
        varRef = var;
        name = var.getName();
        initDomain = new Domain(var.getDomain().getName(), var.getDomain().getValues().clone());
        currDomain = new Domain(initDomain.getName(), initDomain.getValues().clone());
    }

    public Variable () {

    }

    @Override
    public int compareTo(Variable var) {
        return this.name.compareTo(var.getName());
    }

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Domain getInitialDomain() {
        return initDomain;
    }

    public Domain getCurrentDomain() {
        return currDomain;
    }

    public ArrayList<Variable> getNeighbors() {
        return neighbors;
    }

    public int initialDomainLength() {
        return initDomain.length();
    }

    public int neighborLength() {
        return this.neighbors.size();
    }

    public int degree() {
        if (this.neighborLength() == 0) {
            return 0;
        }
        return this.initialDomainLength()/this.neighborLength();
    }

    @Override
    public String toString() {
        String cons = "{";
        for (int i = 0; i < constraints.size(); i++) {
            cons += constraints.get(i).getName();
            if (i != (constraints.size()-1)) {
                cons += ", ";
            }
        }
        cons += "}";

        String neigh = "{";
        for (int i = 0; i < neighbors.size(); i++) {
            neigh += neighbors.get(i).getName();
            if (i != (neighbors.size()-1)) {
                neigh += ", ";
            }
        }
        neigh += "}";


        return "Name: " + name + ", initial-domain: " + initDomain.toString() + ", constraints: " + cons + ", neighbors: " + neigh;
    }

    public boolean equals(Variable o) {
        return this.name.equals(o.getName());
    }

    public void addConstraint(Constraint con) {
        this.constraints.add(con);
    }

    public void addNeighbor(Variable var) {
        this.neighbors.add(var);
    }

    public void removeValue(int a) {
        this.currDomain.removeValue(a);
    }

    public boolean isEmpty() {
        return this.currDomain.isEmpty();
    }

    public void resetDomain() {
        this.currDomain = new Domain(initDomain.getName(), initDomain.getValues().clone());
    }


    
}
