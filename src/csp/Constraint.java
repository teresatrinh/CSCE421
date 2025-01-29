package csp;

import java.util.Arrays;

public class Constraint {
    protected String name;
    protected Variable[] scope;
    protected String definition;
    protected int[][] tuples;

    public Constraint(String name, Variable[] scope, String def, int[][] tuples) {
        this.name = name;
        this.scope = scope;
        this.definition = def;
        this.tuples = tuples;
    }

    public String toString() {
        String variables = "{";
        for (int i = 0; i < scope.length; i++) {
            variables += scope[i];
            if (i != (scope.length -1)) {
                variables += ", ";
            }
        }
        variables += "}";

        String list = "{";
        for (int j = 0; j < tuples.length; j++) {
            list += Arrays.toString(tuples[j]);
            if (j != (tuples.length-1)) {
                list += ", ";
            }
        }
        list += "}";
        
        return "Name: " + this.name + ", variables: " + variables + ", definition: " + this.definition + " " + list;
    }

}
