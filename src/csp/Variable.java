package csp;

import abscon.instance.components.PVariable;

public class Variable {
    //Keep reference to original variable, just in case it is needed later
    protected PVariable varRef;

    //Best to create *deep copy* of the data-structures that are needed for the homework
    protected String name;

    protected Domain domain;

    public Variable (PVariable var) {
        varRef = var;
        name = var.getName();
        domain = new Domain(var.getDomain().getName(), var.getDomain().getValues().clone());
    }

    public String getName() {
        return name;
    }

    public Domain getDomain() {
        return domain;
    }

    public String toString() {
        return "Name: " + name + ", initial-domain: " + domain.toString() + ", constraints: x, neighbors: x";
    }
    
}
