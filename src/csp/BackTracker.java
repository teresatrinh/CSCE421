package csp;

import java.util.ArrayList;
import java.util.Collections;

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
    private ArrayList<InstantiatedVariable> currentPath = new ArrayList<>();
    private ArrayList<Domain> currentDomain = new ArrayList<>();
    private ArrayList<Domain> initialDomain = new ArrayList<>();
    private boolean consistent = true;

    public BackTracker(MyParser problem) {
        this.problem = problem;
        this.setProblem(problem);  
    }

    public BackTracker(){}

    private Variable getVariable(Variable var) {
        int i = this.problem.getVariables().indexOf(var);
        return this.problem.getVariables().get(i);
    }

    public void setVariableHeuristic(String s) {
        this.varOrderingHeuristic = s;

        switch (varOrderingHeuristic) {
            case "DD" -> Collections.sort(currentPath, InstantiatedVariable.ByDegree);
            case "LD" -> Collections.sort(currentPath, InstantiatedVariable.ByDomain);
            case "DEG" -> Collections.sort(currentPath, InstantiatedVariable.ByNeighbor);
            default -> Collections.sort(currentPath, InstantiatedVariable.ByName);
        }
    }

    public void setProblem(MyParser problem) {
        this.problem = problem;

        ArrayList<Variable> variables = problem.getVariables();

        for (Variable v : variables) {
            // creating deep copies of each domain so that initial Domain is not changed when current domain is
            Domain domain1 = new Domain(v.getCurrentDomain().getName(), v.getCurrentDomain().getValues().clone());
            Domain domain2 = new Domain(v.getCurrentDomain().getName(), v.getCurrentDomain().getValues().clone());
            if (valueOrderingHeuristic.equals("LX")) {
                domain1.sort();
                domain2.sort();
            }
            this.currentDomain.add(domain1);
            this.initialDomain.add(domain2);

            InstantiatedVariable var = new InstantiatedVariable(v);
            this.currentPath.add(var);
        }
    }

    //Node consistency implementation 
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

    // check to see if tuple is allowed or not for all constraints in the problem 
    private boolean check(int i, int j) {
        boolean check = true;

        Variable v1 = currentPath.get(i).getVar();
        int a = currentPath.get(i).getValue();
        Variable v2 = currentPath.get(j).getVar();
        int b = currentPath.get(j).getValue();

        for (Constraint con : problem.getConstraints()) {
            ArrayList<Variable> scope = con.getScope();
            if (scope.contains(v1) && scope.contains(v2)) {
                this.cc++;
                check = con.check(v1, a, v2, b);
                //System.out.println("Constraint " + con.getName() + "is checked with values (" + a + ", " + b + ") and supported: " + check);
                if (check) {
                    return check;
                }
            } 
        }

        return check;
    }

    // prints currentPath/solution as indicated in homework 3 documentation
    private void printSolution() {

        String solution = "First Solution: ";

        for (InstantiatedVariable v : currentPath) {
            solution += v.getValue();
        }

        System.out.println(solution);
    }

    // print statistics of finding the first solution
    private void printStats() {
        System.out.println("Intance name: " + this.problem.getName());
        System.out.println("variable-order-heuristic: " + this.varOrderingHeuristic);
        System.out.println("var-static-dynamic: " + this.varStaticDynamic);
        System.out.println("value-order-heuristic: " + this.valueOrderingHeuristic);
        System.out.println("val-static-dynamic: " + this.valueStaticDynamic);
        System.out.println("cc: " + this.cc);
        System.out.println("nv: " + this.nv);
        System.out.println("bt: " + this.bt);
        System.out.println("cpu: " + this.cpuTime);
        
    }

    // print statistics for finding all solutions
    private void printFinalStats() {
        System.out.println("all-sol cc: " + this.cc);
        System.out.println("all-sol nv: " + this.nv);
        System.out.println("all-sol bt: " + this.bt);
        System.out.println("all-sol cpu: " + this.cpuTime);
    }

    // print statistics for EXCEL file for one solution
    private String printCSV1() {
        String s = this.problem.getName() + "," + this.varOrderingHeuristic + "," + this.cc + "," + this.nv + "," + this.bt + "," + this.cpuTime;
        return s;
    }

    // print statistic for EXCEL file for all solutions and ordering of variables
    private void printCSV2(String s, int solution) {
        s += "," + this.cc + "," + this.nv + "," + this.bt + "," + this.cpuTime + "," + solution + ",";

        for (InstantiatedVariable v : currentPath) {
            s += v.getVar().getName() + ",";
        }

        System.out.println(s);
    }

 
    //backtrack as described in Prosser's paper with node consistency check
    public void backtrack() {
        long start = System.currentTimeMillis();

        this.NC();

        consistent = true;
        String status = null; 

        int i = 0;
        int solution = 0;

        while (status == null) {
            if (consistent) {
                i = label(i);
            } else {
                i = unlabel(i);
            }
            if (i == this.problem.getVariables().size()) {
                status = "solution";
                solution++;
            } else if (i == -1) {
                status = "impossible";
            }
        }

        long end = System.currentTimeMillis();
        this.cpuTime = end - start;

        String s = printCSV1();
        
        if (status.equals("solution")) {
            //printSolution();
            currentDomain.get(currentDomain.size()-1).removeValue(currentPath.get(currentDomain.size()-1).getValue());
            i--;
            while (status.equals("solution")) {
                end = System.currentTimeMillis();
                if ((end - start) > 3600000) {
                    status = "timeout";
                    break;
                }
                if (consistent) {
                    i = label(i);
                } else {
                    i = unlabel(i);
                }
                if (i == this.problem.getVariables().size()) {
                    solution++;
                    currentDomain.get(currentDomain.size()-1).removeValue(currentPath.get(currentDomain.size()-1).getValue());
                    i--;
                } else if (i == -1) {
                    status = "impossible";
                }
            }
    
            end = System.currentTimeMillis();
            this.cpuTime = end - start;
    
            //printFinalStats();
            //System.out.println("Number of solutions: " + solution);
        } else {
            //System.out.println("First solution: No solutions");
        }

        printCSV2(s, solution);
    }

    //BT-label as described in Prosser's paper
    private int label(int i) {
        consistent = false;

        long start = System.currentTimeMillis();

        for (int x : currentDomain.get(i).getValues()) {
            currentPath.get(i).setValue(x);
            this.nv++;
            while (!consistent) {
                long end = System.currentTimeMillis();
                if (end-start > 300000) {
                    currentDomain.get(i).removeValue(x);
                    break;
                }
                consistent = true;
                for (int h = 0; h < i; h++) {
                    if (consistent) {
                        consistent = check(i, h);
                    } else {
                        break;
                    }
                }
                if (!consistent) {
                    currentDomain.get(i).removeValue(x);
                    break;
                } else {
                    return i + 1;
                }
            }
        }
        
        return i;
    }

    //BT-unlabel as described in Prosser's paper
    @SuppressWarnings("unlikely-arg-type")
    private int unlabel(int i) {
        int h = i-1;

        this.bt++;
        currentDomain.get(i).setValues(initialDomain.get(i).getValues().clone());
        if (h >= 0) {
            currentDomain.get(h).removeValue(currentPath.get(h).getValue());
            consistent = !currentDomain.get(h).isEmpty();
        } else {
            consistent = false;
        }
        

        return h;
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
