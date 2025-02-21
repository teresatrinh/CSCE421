package csp;

import java.util.ArrayList;

public class Solver {

    public MyParser problem;
    private float cpuTime;
    private int cc;
    private int fval;
    private int iSize;
    private int fSize;
    protected int fEffect;
    private boolean ac; 

    public Solver(MyParser problem) {
        this.problem = new MyParser(problem);
    }

    private void calculateStats() {
        this.iSize = 1;
        this.fSize = 1;
        for (Variable var : this.problem.getVariables()) {
            int initDomainLength = var.getInitialDomain().getValues().length;
            int currDomainLength = var.getCurrentDomain().getValues().length;
            this.fval += initDomainLength - currDomainLength;
            this.iSize *= initDomainLength;
            this.fSize *= currDomainLength;
            this.fEffect += Math.log(initDomainLength) - Math.log(currDomainLength);
        }
    }

    private boolean check(Variable v1, int a, Variable v2, int b) {
        boolean check = false;

        for (Constraint con : problem.getConstraints()) {
            ArrayList<Variable> scope = con.getScope();
            if (scope.contains(v1) && scope.contains(v2)) {
                this.cc++;
                check = con.check(v1, a, v2, b);
            }
        }

        return check;
    }

    private boolean supported(Variable v1, int a, Variable v2) {
        boolean support = false;

        for (int value : getVariable(v2).getCurrentDomain().getValues()) {
            if (this.check(v1, a, v2, value)) {
                support = true;
                return support;
            }
        }

        return support;
    }

    private boolean revise(Variable v1, Variable v2) {
        boolean revise = false;

        Variable problemV1 = this.getVariable(v1);

        for (int value : problemV1.getCurrentDomain().getValues()) {
            if (!supported(v1, value, v2)) {
                problemV1.removeValue(value);
                revise = true;
            }
        }

        return revise;
    }

    private Variable getVariable(Variable var) {
        int i = this.problem.getVariables().indexOf(var);
        return this.problem.getVariables().get(i);
    }

    private ArrayList<Variable[]> createQueue() {
        ArrayList<Variable[]> queue = new ArrayList<>();

        for (Variable v1 : this.problem.getVariables()) {
            for (Variable v2 : this.problem.getVariables()) {
                if (!v1.equals(v2)) {
                    Variable[] tuple = {v1, v2};
                    queue.add(tuple);
                }
            }
        }

        return queue;
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

    private void printStats() {
        this.calculateStats();
        System.out.println("Intance name: " + this.problem.getName());
        System.out.println("cc: " + this.cc);
        System.out.println("cpu: " + this.cpuTime);
        System.out.println("fval: " + this.fval);
        System.out.println("iSize: " + this.iSize);
        if (!ac) {
            System.out.println("fSize: " + this.ac);
            System.out.println("fEffect: " + this.ac);
        } else {
            System.out.println("fSize: " + this.fSize);
            System.out.println("fEffect: " + this.fEffect);
        }
        
    }

    public void AC1() {
        
        this.NC();
        ArrayList<Variable[]> queue = this.createQueue();

        boolean change = true;
        while (change) {
            for (Variable[] tuple : queue) {
                boolean updated = revise(tuple[0], tuple[1]);
                if (tuple[0].isEmpty()) {
                    System.out.println("Domain wipe-out of variable "+ tuple[0].getName() + ". No solution to CSP.");
                    this.ac = false;
                    change = false;
                    break;
                } else {
                    change = updated || change;
                }
            }
        }

        this.printStats();
    }

}
