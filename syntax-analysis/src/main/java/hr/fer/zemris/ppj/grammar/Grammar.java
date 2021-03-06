package hr.fer.zemris.ppj.grammar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hr.fer.zemris.ppj.grammar.interfaces.Symbol;

/**
 * <code>Grammar</code> represents a context free grammar
 *
 * @author Jan Kelemen
 *
 * @version 1.0
 */
public class Grammar {

    private final Map<String, Symbol> nonterminalSymbols = new HashMap<>();
    private final Map<String, Symbol> terminalSymbols = new HashMap<>();
    private final Map<Symbol, List<Production>> productions = new HashMap<>();
    private final Symbol startSymbol;

    private final Set<Symbol> emptySymbols = new HashSet<>();
    private final Map<Symbol, Set<Symbol>> startsWith = new HashMap<>();

    private final Set<Production> productionSet = new HashSet<>();

    /**
     * Class constructor, specifies the formal definition of the grammar.
     *
     * @param nonterminalSymbols
     *            the nonterminal symbols.
     * @param terminalSymbols
     *            the terminal symbols.
     * @param productions
     *            the productions.
     * @param startSymbol
     *            the start symbol.
     * @since alpha
     */
    public Grammar(final Set<Symbol> nonterminalSymbols, final Set<Symbol> terminalSymbols,
            final Set<Production> productions, final Symbol startSymbol) {

        for (final Symbol symbol : nonterminalSymbols) {
            this.nonterminalSymbols.put(symbol.toString(), symbol);
        }

        for (final Symbol symbol : terminalSymbols) {
            this.terminalSymbols.put(symbol.toString(), symbol);
        }

        for (final Production production : productions) {
            final Symbol leftSide = production.leftSide();

            final List<Production> productionList = this.productions.containsKey(leftSide)
                    ? this.productions.get(leftSide) : new ArrayList<Production>();

            productionList.add(production);

            productionSet.add(production);

            this.productions.put(leftSide, productionList);
        }

        this.startSymbol = startSymbol;

        calculateEmptySymbols();

        calculateStartsWith();
    }

    private void calculateEmptySymbols() {
        boolean change = false;
        do {
            change = false;
            for (final Symbol symbol : nonterminalSymbols.values()) {
                if (!emptySymbols.contains(symbol)) {
                    for (final Production production : productions.get(symbol)) {
                        boolean allEmpty = true;
                        if (!production.isEpsilonProduction()) {
                            for (final Symbol productionSymbol : production.rightSide()) {
                                if (!productionSymbol.isTerminal() || !emptySymbols.contains(productionSymbol)) {
                                    allEmpty = false;
                                    break;
                                }
                            }
                        }
                        if (allEmpty) {
                            change = true;
                            emptySymbols.add(symbol);
                            break;
                        }
                    }
                }
            }
        } while (change);

        emptySymbols.add(ProductionParser.parseSymbol("$"));
    }

    private void calculateStartsWith() {
        final List<Symbol> orderedSymbols = new ArrayList<>();
        orderedSymbols.addAll(nonterminalSymbols.values());
        orderedSymbols.addAll(terminalSymbols.values());
        Collections.sort(orderedSymbols);

        final boolean[][] table = new boolean[orderedSymbols.size()][];
        for (int i = 0; i < table.length; i++) {
            table[i] = new boolean[orderedSymbols.size()];
        }

        // ZapocinjeIzravnoZnakom
        for (int i = 0; i < table.length; i++) {
            final Symbol symbol = orderedSymbols.get(i);
            if (productions.containsKey(symbol)) {
                for (final Production production : productions.get(symbol)) {
                    if (!production.isEpsilonProduction()) {
                        for (final Symbol productionSymbol : production.rightSide()) {
                            table[i][orderedSymbols.indexOf(productionSymbol)] = true;
                            if (!emptySymbols.contains(productionSymbol)) {
                                break;
                            }
                        }
                    }

                }
            }
        }

        // ZapocinjeZnakom
        for (int i = 0; i < table.length; i++) {
            table[i][i] = true;
        }

        for (int n = 0; n < table.length; n++) {
            for (int i = 0; i < table.length; i++) {
                for (int j = 0; j < table[i].length; j++) {
                    if (table[i][j]) {
                        for (int k = 0; k < table[j].length; k++) {
                            if (table[j][k]) {
                                table[i][k] = true;
                            }
                        }
                    }

                }
            }
        }

        // Read from table
        for (int i = 0; i < table.length; i++) {
            final Set<Symbol> starts = new HashSet<>();
            for (int j = 0; j < table[i].length; j++) {
                if (table[i][j] && orderedSymbols.get(j).isTerminal()) {
                    starts.add(orderedSymbols.get(j));
                }
            }
            startsWith.put(orderedSymbols.get(i), starts);
        }

    }

    /**
     * Checks if a symbol can generate a empty sequence.
     *
     * @param symbol
     *            the symbol.
     * @return <code>true</code> if a symbol can generatea empty
     * @since alpha
     */
    public boolean isEmptySymbol(final Symbol symbol) {
        if (symbol == null) {
            return true;
        }

        return emptySymbols.contains(symbol);
    }

    /**
     * Checks if a given sequence of symbols can generate a empty sequence.
     *
     * @param sequence
     *            the sequence.
     * @return <code>true</code> if the empty sequence can be generated, <code>false</code> otherwise.
     * @since alpha
     */
    public boolean isEmptySequence(final List<Symbol> sequence) {
        for (final Symbol symbol : sequence) {
            if (!emptySymbols.contains(symbol)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if a given sequence of symbols can generate a empty sequence.
     *
     * @param sequence
     *            the sequence.
     * @return <code>true</code> if the empty sequence can be generated, <code>false</code> otherwise.
     * @since alpha
     */
    public boolean isEmptySequence(final String sequence) {
        final List<Symbol> newSequence = new ArrayList<>();

        if (sequence.equals("$")) {
            return true;
        }

        for (final String name : sequence.split(" ")) {
            newSequence.add(ProductionParser.parseSymbol(name));
        }

        return isEmptySequence(newSequence);
    }

    /**
     * Calculates the set of terminal symbols that can appear instead of the specified symbol.
     *
     * @param symbol
     *            the symbol.
     * @return symbols that are possible instead of the specified symbol.
     * @since alpha
     */
    public Set<Symbol> startsWith(final Symbol symbol) {
        final Set<Symbol> result = new HashSet<>();

        if (symbol == null) {
            return result;
        }

        result.addAll(startsWith.get(symbol));

        return result;
    }

    /**
     * Calculates the set of symbols that can appear instead of the specified sequence of symbols.
     *
     * @param sequence
     *            the sequence.
     * @return symbols that are possible instead of the specified sequence.
     * @since alpha
     */
    public Set<Symbol> startsWith(final List<Symbol> sequence) {
        final Set<Symbol> result = new HashSet<>();

        for (final Symbol symbol : sequence) {
            if (symbol.isTerminal()) {
                result.add(symbol);
                break;
            }

            if (!isEmptySymbol(symbol)) {
                result.addAll(startsWith.get(symbol));
                break;
            }

            result.addAll(startsWith.get(symbol));
        }

        return result;
    }

    /**
     * Calculates the set of symbols that can appear instead of the specified sequence of symbols.
     *
     * @param sequence
     *            the sequence.
     * @return symbols that are possible instead of the specified sequence.
     * @since alpha
     */
    public Set<Symbol> startsWith(final String sequence) {
        final List<Symbol> newSequence = new ArrayList<>();

        if (!sequence.equals("$")) {
            for (final String name : sequence.split(" ")) {
                newSequence.add(ProductionParser.parseSymbol(name));
            }
        }

        return startsWith(newSequence);
    }

    /**
     * Returns all productions of the grammar.
     *
     * @return -
     * @since alpha
     */
    public Set<Production> productions() {
        return new HashSet<>(productionSet);
    }

    /**
     * Returns terminal symbols of the grammar.
     *
     * @return -
     * @since alpha
     */
    public Set<Symbol> terminalSymbols() {
        final Set<Symbol> result = new HashSet<>();
        result.addAll(terminalSymbols.values());
        return result;
    }

    /**
     * Returns nonterminal symbols of the grammar.
     *
     * @return -
     * @since alpha
     */
    public Set<Symbol> nonterminalSymbols() {
        final Set<Symbol> result = new HashSet<>();
        result.addAll(nonterminalSymbols.values());
        return result;
    }

    /**
     * Returns start symbol of the grammar.
     *
     * @return -
     * @since alpha
     */
    public Symbol startSymbol() {
        return startSymbol;
    }

    /**
     * Returns the starting production of the grammar.
     *
     * @return -
     * @since alpha
     */
    public Production getStartProduction() {
        return productions.get(startSymbol).get(0);
    }
}
