package csp;

import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.max;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class Solver {

    public MyParser problem;
    private float cpuTime;
    private int cc;
    private int nv;
    private int bt;
    private String varOrderingHeuristic = "LX";
    private String varStaticDynamic = "static";
    private String valueOrderingHeuristic = "LX";
    private String valueStaticDynamic = "static";
    private ArrayList<InstantiatedVariable> currentPath = new ArrayList<>();
    private ArrayList<Domain> currentDomain = new ArrayList<>();
    private ArrayList<Domain> initialDomain = new ArrayList<>();
    private ArrayList<HashSet<Integer>> conflictSet = new ArrayList<>();
    private boolean consistent = true;
    private String status = null;
    private ArrayList<Stack<Integer>> futureFC = new ArrayList<>();
    private ArrayList<Stack<Integer>> pastFC = new ArrayList<>();
    private ArrayList<HashMap<Integer, ArrayList<Integer>>> reductions = new ArrayList<>();
    
        public Solver(MyParser problem) {
            this.problem = problem;
            this.setProblem(problem);  
        }
    
        public Solver(){}
    
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
                case "dDD" -> this.varStaticDynamic = "dynamic";
                case "dLD" -> this.varStaticDynamic = "dynamic";
                case "dDEG" -> this.varStaticDynamic = "dynamic";
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
                HashSet<Integer> set = new HashSet<>();
                HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
                for (int i = 0; i < problem.getVariables().size(); i++) {
                    map.put(i, new ArrayList<>());
                }
                Stack<Integer> stack = new Stack();
                Stack<Integer> stack2 = new Stack();
                set.add(0);
                this.conflictSet.add(set);
                this.reductions.add(map);
                futureFC.add(stack);
                pastFC.add(stack2);

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

        String solution = "First Solution: [";

        for (InstantiatedVariable v : currentPath) {
            solution += v.getValue() + ", ";
        }

        solution = solution.substring(0, solution.length()-2);
        solution += "]";

        System.out.println(solution);
    }

    // print statistics of finding the first solution
    private void printStats(String search) {
        System.out.println("Intance name: " + this.problem.getName());
        System.out.println("Search: " + search);
        System.out.println("variable-order-heuristic: " + this.varOrderingHeuristic);
        System.out.println("var-static-dynamic: " + this.varStaticDynamic);
        System.out.println("value-order-heuristic: " + this.valueOrderingHeuristic);
        System.out.println("val-static-dynamic: " + this.valueStaticDynamic);
        System.out.println("cc: " + this.cc);
        System.out.println("nv: " + this.nv);
        System.out.println("bt: " + this.bt);
        System.out.println("cpu (ms): " + this.cpuTime);
        
    }

    // print statistics for finding all solutions
    private void printFinalStats() {
        System.out.println("all-sol cc: " + this.cc);
        System.out.println("all-sol nv: " + this.nv);
        System.out.println("all-sol bt: " + this.bt);
        System.out.println("all-sol cpu (ms): " + this.cpuTime);
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

        int i = 0;
        int solution = 0;

        while (status == null) {
            if (consistent) {
                i = btLabel(i);
            } else {
                i = btUnlabel(i);
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
        //this.printStats("BT");
        
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
                    i = btLabel(i);
                } else {
                    i = btUnlabel(i);
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
    private int btLabel(int i) {
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
                        this.cc++;
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
    private int btUnlabel(int i) {
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

    // Implemented CBJ as defined in Prossers paper
    public void cbj() {
        long start = System.currentTimeMillis();

        this.NC();

        consistent = true;

        int i = 0;
        int solution = 0;

        while (status == null) {
            if (consistent) {
                i = cbjLabel(i);
            } else {
                i = cbjUnlabel(i);
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
        //this.printStats("CBJ");
        
        if (status.equals("solution")) {
            //printSolution();
            currentDomain.get(currentDomain.size()-1).removeValue(currentPath.get(currentDomain.size()-1).getValue());
            i--;
            for (int j = 0; j < i; j++) {
                conflictSet.get(i).add(j);
            }
            while (status.equals("solution")) {
                end = System.currentTimeMillis();
                if ((end - start) > 3600000) {
                    status = "timeout";
                    break;
                }
                if (consistent) {
                    i = cbjLabel(i);
                } else {
                    i = cbjUnlabel(i);
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

    // Implements CBJ label as in Prosser's paper
    private int cbjLabel(int i) {
        consistent = false;
        for (int x : currentDomain.get(i).getValues()) {
            currentPath.get(i).setValue(x);
            this.nv++;
            while (!consistent){
                consistent = true;
                int h = 0;
                while (h < i) {
                    if (consistent) {
                        consistent = check(i, h);
                        this.cc++;
                    } else {
                        break;
                    }
                    h++;

                }

                if (!consistent) {
                    conflictSet.get(i).add(h-1);
                    currentDomain.get(i).removeValue(x);
                    break;
                } else {
                    return i + 1;
                }
            }
        }
        return i;
    }

    // Implements CBJ unlabel as in Prosser's paper 
    private int cbjUnlabel(int i) {
        if (conflictSet.get(i).isEmpty()) {
            status = "impossible";
            return -1;
        }
        int h = max(conflictSet.get(i));
        conflictSet.get(i).remove(h);
        conflictSet.get(h).addAll(conflictSet.get(i));

        this.bt++;

        for (int j = h+1; j < i + 1; j++) {
            conflictSet.get(j).clear();
            conflictSet.get(j).add(0);
            currentDomain.get(j).setValues(initialDomain.get(j).getValues().clone());
        }

        if (h >= 0) {
            if (!currentDomain.get(h).isEmpty()) {
                currentDomain.get(h).removeValue(currentPath.get(h).getValue());
            }
            consistent = !currentDomain.get(h).isEmpty();
        } else {
            consistent = false;
        }

        return h;
    }

    //implement check forward from Prosser's paper
    private Boolean checkForward(int i, int j) {

        ArrayList<Integer> reduction = new ArrayList<>();

        for (int x : currentDomain.get(j).getValues()) {
            currentPath.get(j).setValue(x);

            if (!check(i, j)) {
                reduction.add(x);
            }

            this.cc++;
        }
        if (!reduction.isEmpty()) {
            currentDomain.get(j).removeAll(reduction);
            if (reductions.get(j).containsKey(i)) {
                reductions.get(j).get(i).addAll(reduction);
            } else {
                reductions.get(j).put(i, reduction);
            }
            futureFC.get(i).push(j);
            pastFC.get(j).push(i);
        }

        return !currentDomain.get(j).isEmpty();
    }

    // undo-reductions as detailed in Prosser's paper
    private void undoReduction(int i) {

        while(!futureFC.get(i).isEmpty()) {
            int j = futureFC.get(i).pop();
            
            if (reductions.get(j).containsKey(i)) {
                ArrayList<Integer> reduction = reductions.get(j).remove(i);
                currentDomain.get(j).addAll(reduction);
            }
            if (!pastFC.get(j).empty()) {
                pastFC.get(j).pop();
            }
        }
    } 

    // updated-current-domain as detailed in Prosser's paper
    private void updatedCurrentDomain(int i) {
        currentDomain.get(i).setValues(initialDomain.get(i).getValues().clone());
        for (ArrayList<Integer> reduction: reductions.get(i).values()) {
            currentDomain.get(i).removeAll(reduction);
        }
    }

    // fc-label as detailed in Prosser's paper
    private int fcLabel(int i) {
        this.consistent = false;
        int problemSize = problem.getVariables().size();
        for (int x : currentDomain.get(i).getValues()) {
            currentPath.get(i).setValue(x);
            this.nv++;

            consistent = true;
            for (int j = i + 1; j < problemSize && consistent; j++) {
                consistent = checkForward(i, j);
            }

            if (!consistent) {
                currentDomain.get(i).removeValue(x);
                undoReduction(i);
            } else {
                return selectNextVariable(i);
            }

        }
        
        return i;
    }

    // fc-unlabel as detailed in Prosser's paper
    private int fcUnlabel(int i) {
        if (i == 0) {
            status = "impossible";
            return -1;
        }
        int h = i-1;
        undoReduction(h);
        updatedCurrentDomain(i);
        currentDomain.get(h).removeValue(currentPath.get(h).getValue());
        consistent = !currentDomain.get(h).isEmpty();
        this.bt++;
        return h;
    }

    public void fc() {
        long start = System.currentTimeMillis();

        this.NC();

        this.consistent = true;

        int i = 0;
        int solution = 0;
        this.status = null;

        while (status == null) {
            if (consistent) {
                i = fcLabel(i);
            } else {
                i = fcUnlabel(i);
            }

            if (i == this.problem.getVariables().size()) {
                status = "solution";
                solution++;
            } else if (i == -1) {
                status = "impossible";
            }

            // timeout if algorithm runs longer than 1 hour
            long end = System.currentTimeMillis();
            if((end - start > 3600000)) {
                status = "timeout";
                break;
            }
        }

        long end = System.currentTimeMillis();
        this.cpuTime = end - start;

        //String s = printCSV1();
        this.printStats("BT");
        
        if (status.equals("solution")) {
            printSolution();
            currentDomain.get(currentDomain.size()-1).removeValue(currentPath.get(currentDomain.size()-1).getValue());
            i--;

            while (status.equals("solution") & i >= 0) {
                end = System.currentTimeMillis();
                if ((end - start) > 3600000) {
                    status = "timeout";
                    break;
                }
                if (consistent) {
                    i = fcLabel(i);
                } else {
                    i = fcUnlabel(i);
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
    
            printFinalStats();
            System.out.println("Number of solutions: " + solution);
        } else {
            System.out.println("First solution: No solutions");
        }

        //printCSV2(s, solution);
    }

    // this method will help select the next variable for dynamic ordering
    private int selectNextVariable(int i) {
        
        // if this is the variable, returns i + 1
        if ((i >= problem.getVariables().size() - 1) || !varStaticDynamic.equals("dynamic")) {
            return i + 1;
        }

        ArrayList<VariableScore> scores = new ArrayList<>();
        int next = i + 1;

        for (int j = next; j < problem.getVariables().size(); j++) {
            VariableScore score = scoreVariable(j);
            score.setIndex(j);
            scores.add(score);
        }

        switch (varOrderingHeuristic) {
            case "dLD" -> scores.sort(Comparator.comparingInt(VariableScore::getDomainSize));
            case "dDEG" -> scores.sort(Comparator.comparingInt(VariableScore::getDegree));
            case "dDD" -> scores.sort(Comparator.comparingDouble(VariableScore::getRatio));
            default -> scores.sort(Comparator.comparingInt(VariableScore::getIndex));
        }

        int best = scores.get(0).getIndex();

        if (best != next) {
            swapVariables(next, best);
        }

        return next;
    }

    // calculates current values of variable for ordering
    private VariableScore scoreVariable(int i) {
        Variable var = currentPath.get(i).getVar();

        int domainSize = currentDomain.get(i).length();

        int degree = 0;
        for (Constraint constraint : problem.getConstraints()) {
            ArrayList<Variable> scope = constraint.getScope();
            if (scope.contains(var)) {
                for (Variable v : scope) {
                    int j = getVariableIndex(v);
                    if (j > i) {
                        degree++;
                    }
                }
            }
        }

        return new VariableScore(domainSize, degree);

    }

    // gets index of current variable in current path
    private int getVariableIndex(Variable var) {
        for (int i = 0; i < currentPath.size(); i++) {
            if(currentPath.get(i).getVar().equals(var)) {
                return i;
            }
        }
        return -1;
    }

    private void swapVariables(int i, int j) {
        InstantiatedVariable temp = currentPath.get(i);
        currentPath.set(i, currentPath.get(j));
        currentPath.set(j, temp);

        Domain tempDomain = currentDomain.get(i);
        currentDomain.set(i, currentDomain.get(j));
        currentDomain.set(j, tempDomain);

        Domain tempInitialDomain = initialDomain.get(i);
        initialDomain.set(i, initialDomain.get(j));
        initialDomain.set(j, tempInitialDomain);
        
        Stack<Integer> tempPast = pastFC.get(i);
        pastFC.set(i, pastFC.get(j));
        pastFC.set(j, tempPast);

        Stack<Integer> tempFuture = futureFC.get(i);
        futureFC.set(i, futureFC.get(j));
        futureFC.set(j, tempFuture);

        HashMap<Integer, ArrayList<Integer>> tempReduction = reductions.get(i);
        reductions.set(i, reductions.get(j));
        reductions.set(j, tempReduction);

        for (int x = 0;x < i;x++) {
            int index = futureFC.get(x).indexOf(i);
            if (index >= 0) {
                futureFC.remove(index);
                Stack<Integer> stack = new Stack<>();
                stack.add(j);
                futureFC.add(index, stack);
            }
            
        }
    }

}
