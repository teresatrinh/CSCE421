package csp;

import abscon.instance.components.PIntensionConstraint;
import java.util.ArrayList;

public class Intension extends Constraint{

    protected PIntensionConstraint relation;

    public Intension(String name, ArrayList<Variable> scope, PIntensionConstraint relation) {
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

        return "Name: " + this.name + ", variables: " + variables + ", definition: " + this.relation.getFunction().getFunctionalExpression();
    }

    public String getRelation() {
        return this.relation.getName();
    }

    public void setRelation(PIntensionConstraint relation) {
        this.relation = relation;
    }

    @Override
    public boolean check(Variable v1, int a, Variable v2, int b) {
        long check = this.relation.computeCostOf(new int[]{a, b});
        return check == 0;
    }

    @Override
    public boolean check(Variable var, int a) {
        long check = this.relation.computeCostOf(new int[]{a});
        return check == 0;
    }
    
    
}
