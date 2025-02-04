package csp;

import abscon.instance.components.PConstraint;
import abscon.instance.components.PExtensionConstraint;
import abscon.instance.components.PIntensionConstraint;
import abscon.instance.tools.InstanceParser;
import java.util.ArrayList;

public class MyParser {

    private String name;
    private ArrayList<Variable> variables;
    private ArrayList<Constraint> constraints;

    public MyParser(String filename) {
        InstanceParser parser = new InstanceParser();
        parser.loadInstance(filename);
        parser.parse(false);

        variables = new ArrayList<Variable>();

        this.name = parser.getName();

        System.out.println("Instance name: " + this.name);

        System.out.println("Variables: ");
        for (int i = 0; i < parser.getVariables().length; i++) {
            Variable newVar = new Variable(parser.getVariables()[i]);
            System.out.println(newVar);
            variables.add(newVar);
        }

        System.out.println("Constraints: ");
        for (String key : parser.getMapOfConstraints().keySet()) {
            PConstraint con = parser.getMapOfConstraints().get(key);
            Constraint newCon = new Constraint(con, variables);
            if (con instanceof PIntensionConstraint inten) {
                ((Intension) newCon).setRelation(inten.getFunction().getName());
            } else if (con instanceof PExtensionConstraint exten) {
                ((Extension) newCon).setDefinition(exten.getRelation().getSemantics());
                ((Extension) newCon).setTuples(exten.getRelation().getTuples().clone());
            }
            constraints.add(newCon);
            System.out.println(newCon);
        }
    }

    public static void main(String[] args) {
        //Hardcoded now... but should read in the file through the arguments, -f <XML-NAME>
        MyParser parser = new MyParser("src/csp/chain4-conflicts.xml"); 
    }

}