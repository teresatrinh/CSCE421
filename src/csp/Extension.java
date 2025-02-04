package csp;

import java.util.ArrayList;
import java.util.Arrays;

public class Extension extends Constraint{

    protected String def;
    
    protected int[][] tuples;

    public Extension(String name, ArrayList<Variable> scope, String def, int[][] tuples) {
        super(name, scope);
        this.def = def;
        this.tuples = tuples;
    }

    public Extension(Constraint con) {
        super(con.name, con.scope);
    }

    public String toString() {
        String variables = "{";
        for (int i = 0; i < scope.size(); i++) {
            variables += scope.get(i).getName();
            if (i != (scope.size() -1)) {
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
        
        return "Name: " + this.name + ", variables: " + variables + ", definition: " + this.def + " " + list;
    }

    public String getDefinition() {
        return def;
    }

    public void setDefinition(String def) {
        this.def = def;
    }

    public int[][] getTuples() {
        return tuples;
    }

    public void setTuples(int[][] tuples) {
        this.tuples = tuples;
    }

    
    
}
