package hr.fer.zemris.ppj;

/**
 * <code>Attribute</code> attributes for grammar symbols.
 *
 * @author Jan Kelemen
 *
 * @version 1.0
 */
public enum Attribute {
    /**
     * tip
     */
    TYPE, // VariableType

    /**
     * tipovi
     */
    TYPES, // List<VariableType>

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
     * pov
     */
    RETURN_VALUE, // VariableType

    /**
     * inherited type
     */
    ITYPE, // VariableType

    /**
     * Unutar petlje
     */
    INSIDE_LOOP, // boolean

    /**
     * number of chars in char string
     */
    CELEM_COUNT, // int

    /**
     * parameter names
     */
    VALUES, // List<String>

    /**
     * function name
     */
    FUNCTION_NAME, // String

    // IDK ovo treba popuniti po potrebi

}
