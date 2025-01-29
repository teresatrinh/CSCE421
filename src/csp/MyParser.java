package csp;

import abscon.instance.components.PConstraint;
import abscon.instance.tools.InstanceParser;
import java.util.ArrayList;
import java.util.List;

public class MyParser {

    private List<Variable> variables;
    private List<Constraint> constraints;

    public MyParser(String filename) {
        InstanceParser parser = new InstanceParser();
        parser.loadInstance(filename);
        parser.parse(false);

        variables = new ArrayList<Variable>();

        System.out.println("Instance name: " + parser.getName());

        System.out.println("Variables: ");
        for (int i = 0; i < parser.getVariables().length; i++) {
            Variable newVar = new Variable(parser.getVariables()[i]);
            System.out.println(newVar);
            variables.add(newVar);
        }

        System.out.println("Constraints: ");
        for (String key : parser.getMapOfConstraints().keySet()) {
            PConstraint con = parser.getMapOfConstraints().get(key);
            System.out.println(con.getName());
        }
    }

    public static void main(String[] args) {
        //Hardcoded now... but should read in the file through the arguments, -f <XML-NAME>
        MyParser parser = new MyParser("src/csp/chain4-conflicts.xml"); 
    }

}