package entity;

import java.util.LinkedList;

/**
 * Created by sulvto on 16-11-18.
 */
public class LocalResolver {
    private final LinkedList<Scope> scopesStack;
    private final ConstantTable constantTable;
    private final ErrorHandler errorHandler;

    public LocalResolver(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        this.constantTable = new ConstantTable();
        this.scopesStack = new LinkedList<>();
    }
}
