package csp;

import abscon.instance.components.PFunction;
import java.util.ArrayList;

public class Intension extends Constraint{

    protected PFunction relation;

    public Intension(String name, ArrayList<Variable> scope, PFunction relation) {
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

        return "Name: " + this.name + ", variables: " + variables + ", definition: " + this.relation.getName();
    }

    public String getRelation() {
        return this.relation.getName();
    }

    public void setRelation(PFunction relation) {
        this.relation = relation;
    }

    @Override
    public boolean check(Variable v1, int a, Variable v2, int b) {
        //TODO: implement;
        return false;
    }

    @Override
    public boolean check(Variable var, int a) {
        //TODO: implement
        return false;
    }
    
    
}
