package hr.fer.zemris.ppj.manipulators.instuctions;

import hr.fer.zemris.ppj.Attribute;
import hr.fer.zemris.ppj.Node;
import hr.fer.zemris.ppj.Production;
import hr.fer.zemris.ppj.SemanticErrorReporter;
import hr.fer.zemris.ppj.code.command.CommandFactory;
import hr.fer.zemris.ppj.code.generator.FRISCGenerator;
import hr.fer.zemris.ppj.interfaces.Manipulator;
import hr.fer.zemris.ppj.types.IntType;
import hr.fer.zemris.ppj.types.Type;

/**
 * <code>BranchInstructionManipulator</code> is a manipulator for branch instruction.
 *
 * @author Filip Gulan
 *
 * @version 1.1
 */
public class BranchInstructionManipulator implements Manipulator {

    private static final CommandFactory ch = new CommandFactory();

    /**
     * Name of the node.
     */
    public static final String NAME = "<BranchInstruction>";

    /**
     * Name of the node in Croatian.
     */
    public static final String HR_NAME = "<naredba_grananja>";

    /**
     * {@inheritDoc} <br>
     *
     * Referring pages: 63, 64.
     *
     * @since 1.0
     */
    @Override
    public boolean check(final Node node) {
        final int count = node.getChildren().size();

        // <naredba_grananja> ::= KR_IF L_ZAGRADA <izraz> D_ZAGRADA <naredba> KR_ELSE <naredba>
        if (count == 7) {
            final Node expression = node.getChild(2);
            final Node firstInstruction = node.getChild(4);
            final Node secondInstruction = node.getChild(6);

            if ("<naredba>".equals(firstInstruction.name()) && "<naredba>".equals(secondInstruction.name())
                    && "<izraz>".equals(expression.name())) {
                if (!expression.check()) {
                    SemanticErrorReporter.report(node);
                    return false;
                }

                final Type type = (Type) expression.getAttribute(Attribute.TYPE);
                final boolean ableToConvert = type.implicitConversion(new IntType());

                if (!ableToConvert) {
                    SemanticErrorReporter.report(node);
                    return false;
                }

                if (!firstInstruction.check()) {
                    SemanticErrorReporter.report(node);
                    return false;
                }
                if (!secondInstruction.check()) {
                    SemanticErrorReporter.report(node);
                    return false;
                }

                return true;
            }
        }

        // <naredba_grananja> ::= KR_IF L_ZAGRADA <izraz> D_ZAGRADA <naredba>
        if (count == 5) {
            final Node expression = node.getChild(2);
            final Node firstInstruction = node.getChild(4);

            if ("<naredba>".equals(firstInstruction.name()) && "<izraz>".equals(expression.name())) {
                if (!expression.check()) {
                    SemanticErrorReporter.report(node);
                    return false;
                }

                final Type type = (Type) expression.getAttribute(Attribute.TYPE);
                final boolean ableToConvert = type.implicitConversion(new IntType());

                if (!ableToConvert) {
                    SemanticErrorReporter.report(node);
                    return false;
                }

                if (!firstInstruction.check()) {
                    SemanticErrorReporter.report(node);
                    return false;
                }

                return true;
            }
        }

        System.err.println("Shold never happen");
        SemanticErrorReporter.report(node);
        return false;
    }

    @Override
    public void generate(Node node) {
        switch (Production.fromNode(node)) {
        case BRANCH_INSTRUCTION_1: {
            // BRANCH_INSTRUCTION_1("<naredba_grananja> ::= KR_IF L_ZAGRADA <izraz> D_ZAGRADA <naredba>"),
            node.getChild(2).generate();
            FRISCGenerator.generateStartIfIntstruction();
            node.getChild(4).generate();
            FRISCGenerator.generateEndIfIntstruction();
            break;
        }

        case BRANCH_INSTRUCTION_2: {
            // BRANCH_INSTRUCTION_2("<naredba_grananja> ::= KR_IF L_ZAGRADA <izraz> D_ZAGRADA <naredba> KR_ELSE <naredba>"),
            node.getChild(2).generate();
            FRISCGenerator.generateStartIfElseIntstruction();
            node.getChild(4).generate();
            FRISCGenerator.generateElseIntstruction();
            node.getChild(6).generate();
            FRISCGenerator.generateEndIfElseIntstruction();
            break;
        }

        default:
            System.err.println("Generation reached undefined production!");
            break;
        }
    }
}
