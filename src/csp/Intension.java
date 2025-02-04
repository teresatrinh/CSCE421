package csp;

import java.util.ArrayList;

public class Intension extends Constraint{

    protected String relation;

    public Intension(String name, ArrayList<Variable> scope, String relation) {
        super(name, scope);
        this.relation = relation;
    }

    public Intension(Constraint con) {
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

        return "Name: " + this.name + ", variables: " + variables + ", definition: " + this.relation;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
    
    
}
