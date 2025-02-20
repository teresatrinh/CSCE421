package csp;

import java.util.ArrayList;
import abscon.instance.components.PConstraint;
import abscon.instance.components.PVariable;

public class Constraint {
    protected String name;
    protected ArrayList<Variable> scope;

    public Constraint(String name, ArrayList<Variable> scope) {
        this.name = name;
        this.scope = scope;
    }

    public Constraint(PConstraint constraint, ArrayList<Variable> variables) {
        this.name = constraint.getName();
        this.scope = new ArrayList<Variable>();
        for (PVariable var : constraint.getScope()) {
            Variable key = new Variable();
            for (Variable curr : variables) {
                if (curr.getName().equals(var.getName())) {
                    key = curr;
                    break;
                } else {
                    key = new Variable(var);
                }
            }
            this.scope.add(key);
            
        }
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
        
        return "Name: " + this.name + ", variables: " + variables + ", definition: ";
    }

    public ArrayList<Variable> getScope() {
        return scope;
    }

    public void setScope(ArrayList<Variable> scope) {
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean check(Variable v1, int a, Variable v2, int b) {
        return false;
    }

    public boolean check(Variable var, int a) {
        return false;
    }

    public boolean supported(Variable v1, int a, Variable v2) {
        boolean support = false;

        if (!this.inScope(v1) || !this.inScope(v2)) {
            return support;
        }

        for (int x : v2.getCurrentDomain().getValues()) {
            if (check(v1, a, v2, x)) {
                support = true; 
                return support;
            }
        }

        return support;
    }

    public boolean inScope(Variable var) {
        return this.scope.contains(var);
    }

    
}
