package hr.fer.zemris.ppj.lexical.analysis.analyzer.actions;

import hr.fer.zemris.ppj.lexical.analysis.analyzer.LexicalAnalyzer;

public class ReturnAction implements LexerAction {

    private final int offset;

    public ReturnAction(final int offset) {
        this.offset = offset;
    }

    @Override
    public void execute(final LexicalAnalyzer lexer) {
        final int startOffset = (lexer.getStartIndex() + offset) - 1;
        lexer.setFinishIndex(startOffset);
        lexer.setLastIndex(startOffset);
    }

    @Override
    public String toString() {
        return "VRATI_SE " + offset;
    }
}
