package csp;

import abscon.instance.components.PVariable;

public class Variable {
    //Keep reference to original variable, just in case it is needed later
    protected PVariable varRef;

    //Best to create *deep copy* of the data-structures that are needed for the homework
    protected String name;

    protected Domain initDomain;

    protected Domain currDomain;

    protected Constraint[] constraints;

    protected Variable[] neighbors;

    public Variable (PVariable var) {
        varRef = var;
        name = var.getName();
        initDomain = new Domain(var.getDomain().getName(), var.getDomain().getValues().clone());
        currDomain = new Domain(initDomain.getName(), initDomain.getValues().clone());
    }

    public Variable() {
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

    public Domain getDomain() {
        return initDomain;
    }

    public String toString() {
        return "Name: " + name + ", initial-domain: " + initDomain.toString() + ", constraints: x, neighbors: x";
    }


    
}
