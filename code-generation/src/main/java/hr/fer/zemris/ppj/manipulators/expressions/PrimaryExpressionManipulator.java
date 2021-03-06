package hr.fer.zemris.ppj.manipulators.expressions;

import hr.fer.zemris.ppj.Attribute;
import hr.fer.zemris.ppj.Node;
import hr.fer.zemris.ppj.Production;
import hr.fer.zemris.ppj.SemanticErrorReporter;
import hr.fer.zemris.ppj.code.command.CommandFactory;
import hr.fer.zemris.ppj.code.generator.FRISCGenerator;
import hr.fer.zemris.ppj.interfaces.Manipulator;
import hr.fer.zemris.ppj.types.CharType;
import hr.fer.zemris.ppj.types.IntType;
import hr.fer.zemris.ppj.types.Type;
import hr.fer.zemris.ppj.types.arrays.ConstCharArrayType;
import hr.fer.zemris.ppj.types.functions.FunctionType;

/**
 * <code>PrimaryExpressionManipulator</code> is a manipulator for primary expression.
 *
 * @author Jan Kelemen
 *
 * @version 1.1
 */
public class PrimaryExpressionManipulator implements Manipulator {

    private static final CommandFactory ch = new CommandFactory();

    /**
     * Name of the node.
     */
    public static final String NAME = "<PrimaryExpression>";

    /**
     * Name of the node in Croatian.
     */
    public static final String HR_NAME = "<primarni_izraz>";

    /**
     * {@inheritDoc} <br>
     *
     * Referring pages: 51, 52.
     *
     * @since 1.0
     */
    @Override
    public boolean check(final Node node) {
        final Node firstChild = node.getChild(0);
        final String firstSymbol = firstChild.name();

        // <primarni_izraz> ::= IDN
        if ("IDN".equals(firstSymbol)) {

            // 1. IDN.ime je deklarirano
            if (!firstChild.check()) {
                SemanticErrorReporter.report(node);
                return false;
            }

            final String name = (String) firstChild.getAttribute(Attribute.VALUE);
            if (!node.identifierTable().isDeclared(name)) {
                SemanticErrorReporter.report(node);
                return false;
            }

            final Type type = node.identifierTable().identifierType(name);

            node.addAttribute(Attribute.TYPE, type);
            node.addAttribute(Attribute.L_EXPRESSION, type.isLExpression());
            return true;
        }

        // <primarni_izraz> ::= BROJ
        if ("BROJ".equals(firstSymbol)) {

            // 1. vrijednost je u rasponu tipa int
            if (!firstChild.check()) {
                SemanticErrorReporter.report(node);
                return false;
            }

            node.addAttribute(Attribute.TYPE, new IntType());
            node.addAttribute(Attribute.L_EXPRESSION, false);
            return true;
        }

        // <primarni_izraz> ::= ZNAK
        if ("ZNAK".equals(firstSymbol)) {

            // 1. znak je ispravan po 4.3.2
            if (!firstChild.check()) {
                SemanticErrorReporter.report(node);
                return false;
            }

            node.addAttribute(Attribute.TYPE, new CharType());
            node.addAttribute(Attribute.L_EXPRESSION, false);
            return true;
        }

        // <primarni_izraz> ::= NIZ_ZNAKOVA
        if ("NIZ_ZNAKOVA".equals(firstSymbol)) {

            // 1. konstantni niz znakova je ispravan po 4.3.2
            if (!firstChild.check()) {
                SemanticErrorReporter.report(node);
                return false;
            }

            node.addAttribute(Attribute.TYPE, new ConstCharArrayType());
            node.addAttribute(Attribute.L_EXPRESSION, false);
            return true;
        }

        // <primarni_izraz> ::= L_ZAGRADA <izraz> D_ZAGRADA
        if ("L_ZAGRADA".equals(firstSymbol)) {
            final Node expression = node.getChild(1);

            // 1. provjeri(<izraz>)
            if (!expression.check()) {
                SemanticErrorReporter.report(node);
                return false;
            }

            node.addAttribute(Attribute.TYPE, expression.getAttribute(Attribute.TYPE));
            node.addAttribute(Attribute.L_EXPRESSION, expression.getAttribute(Attribute.L_EXPRESSION));
            return true;
        }

        System.err.println("Shold never happen");
        SemanticErrorReporter.report(node);
        return false;
    }

    @Override
    public void generate(Node node) {
        switch (Production.fromNode(node)) {
            case PRIMARY_EXPRESSION_1: {
                // PRIMARY_EXPRESSION_1("<primarni_izraz> ::= IDN"),
                String name = (String) node.getChild(0).getAttribute(Attribute.VALUE);
                Type type = node.identifierTable().identifierType(name);

                if (type.isFunction()) {
                    FunctionType functionType = (FunctionType) type;
                    FRISCGenerator.generateFunctionCall(name, functionType);
                }
                else {
                    FRISCGenerator.generateIdentifier(name);
                }
                break;
            }

            case PRIMARY_EXPRESSION_2: {
                // PRIMARY_EXPRESSION_2("<primarni_izraz> ::= BROJ"),
                Integer value = (Integer) node.getChild(0).getAttribute(Attribute.VALUE);
                FRISCGenerator.generateNumber(value);
                break;
            }

            case PRIMARY_EXPRESSION_3: {
                // PRIMARY_EXPRESSION_3("<primarni_izraz> ::= ZNAK"),
                char value = (Character) node.getChild(0).getAttribute(Attribute.VALUE);
                FRISCGenerator.generateNumber(value);
                break;
            }

            case PRIMARY_EXPRESSION_4: {
                // PRIMARY_EXPRESSION_4("<primarni_izraz> ::= NIZ_ZNAKOVA"),
                String value = (String) node.getChild(0).getAttribute(Attribute.VALUE);
                for (int i = 0; i < value.length(); i++) {
                    FRISCGenerator.generateNumber(value.charAt(i));
                }
                FRISCGenerator.generateNumber(0);
                break;
            }

            case PRIMARY_EXPRESSION_5: {
                // <primarni_izraz> ::= L_ZAGRADA <izraz> D_ZAGRADA
                node.getChild(1).generate();
                break;
            }

            default:
                System.err.println("Generation reached undefined production!");
                break;
        }
    }
}
