package hr.fer.zemris.ppj;

/**
 * <code>Attribute</code> attributes for grammar symbols.
 *
 * @author Jan Kelemen
 *
 * @version 1.1
 */
public enum Attribute {
    /**
     * tip
     */
    TYPE, // Type

    /**
     * tipovi
     */
    TYPES, // List<Type>

    /**
     * l-izraz
     */
    L_EXPRESSION, // Boolean

    /**
     * br-elem
     */
    ELEMENT_COUNT, // Integer

    /**
     * uniformnog simbola
     */
    UNIFORM_SYMBOL, // String

    /**
     * vrijednosti intova, charova & stuff
     */
    VALUE, // String

    /**
     * redak leksicke jedinke
     */
    LINE_NUMBER, // Integer

    /**
     * inherited type
     */
    ITYPE, // Type

    /**
     * Unutar petlje
     */
    INSIDE_LOOP, // Boolean

    /**
     * parameter names
     */
    VALUES, // List<String>

    /**
     * function name
     */
    FUNCTION_NAME, // String

}
