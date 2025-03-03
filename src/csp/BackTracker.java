package csp;

import java.util.ArrayList;

public class BackTracker {

    public MyParser problem;
    private float cpuTime;
    private int cc;
    private int nv;
    private int bt;
    private String varOrderingHeuristic = "LX";
    private String varStaticDynamic = "dynamic";
    private String valueOrderingHeuristic = "LX";
    private String valueStaticDynamic = "dynamic";
    private ArrayList<InstantiatedVariable> currentPath;
    private ArrayList<Domain> currentDomain;

    public BackTracker(MyParser problem) {
        this.problem = problem;

        ArrayList<Variable> variables = problem.getVariables();
        this.currentDomain = new ArrayList<>();

        for (Variable v : variables) {
            Domain domain = v.getCurrentDomain();
            domain.sort();
            currentDomain.add(domain);
        }
    }

    private Variable getVariable(Variable var) {
        int i = this.problem.getVariables().indexOf(var);
        return this.problem.getVariables().get(i);
    }

    public void NC() {
        for (Constraint con : this.problem.getConstraints()) {
            if (con.getScope().size() == 1) {
                Variable var = this.getVariable(con.getScope().get(0));
                this.cc++;
                for (int value : var.getCurrentDomain().getValues()) {
                    if (!con.check(var, value)) {
                        var.removeValue(value);
                    }
                }
            }
        }
    }

    

    public void backtrack() {
        long start = System.currentTimeMillis();

        this.NC();

        long end = System.currentTimeMillis();
        this.cpuTime = end - start;
    }

    // Returns list of variables that have been assigned a value
    public ArrayList<Variable> instantiatedVariables() {
        ArrayList<Variable> var = new ArrayList<>();

        for (InstantiatedVariable vvp : currentPath){
            var.add(vvp.getVar());
        }

        return var;
    }

    // Rerturns list of variables that have not been assigned a value
    public ArrayList<Variable> unassignedVariables() {
        ArrayList<Variable> variables = this.problem.getVariables();
        ArrayList<Variable> assigned = instantiatedVariables();

        variables.removeAll(assigned);

        return variables;
    }

    // Given a constraint, returns variables that have been instantiated
    public ArrayList<Variable> instantiatedVar(Constraint con) {
        ArrayList<Variable> scope = con.getScope();
        ArrayList<Variable> instantiated = instantiatedVariables();

        ArrayList<Variable> intersection = new ArrayList<>();

        for (Variable v : scope) {
            if (instantiated.contains(v)) {
                intersection.add(v);
            }
        }
        
        return intersection;
    }

    // Given a constraint, returns variables that have not been instantiated
    public ArrayList<Variable> unassignedVar(Constraint con) {
        ArrayList<Variable> scope = con.getScope();
        ArrayList<Variable> instantiated = instantiatedVariables();

        ArrayList<Variable> subtract = new ArrayList<>();

        for (Variable v : scope) {
            if (!instantiated.contains(v)) {
                subtract.add(v);
            }
        }

        return subtract;
    }
    

}
