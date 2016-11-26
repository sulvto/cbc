package type;

import ast.Slot;
import utils.ErrorHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sulvto on 16-11-19.
 */
public class TypeTable {
    private int intSize;
    private int longSize;
    private int pointerSize;
    private Map<TypeRef, Type> table;

    public TypeTable(int intSize, int longSize, int pointerSize) {
        this.intSize = intSize;
        this.longSize = longSize;
        this.pointerSize = pointerSize;
        this.table = new HashMap<>();
    }

    public boolean isDefined(TypeRef ref) {
        return table.containsKey(ref);
    }

    public void put(TypeRef ref, Type type) {
        if (table.containsKey(ref)) {
            throw new Error("duplicated type definition: " + ref);
        }
        table.put(ref, type);
    }

    public Type get(TypeRef ref) {
        Type type = table.get(ref);
        if (type == null) {
            if (ref instanceof UserTypeRef) {
                UserTypeRef userTypeRef = (UserTypeRef) ref;
                throw new Error("undefined type: " + userTypeRef.getName());
            } else if (ref instanceof PointerTypeRef) {
                PointerTypeRef pointerTypeRef = (PointerTypeRef) ref;
                Type t = new PointerType(pointerSize, get(pointerTypeRef.getBaseType()));
                table.put(pointerTypeRef, t);
                return t;
            } else if (ref instanceof ArrayTypeRef) {
                ArrayTypeRef arrayTypeRef = (ArrayTypeRef) ref;
                Type t = new ArrayType(get(arrayTypeRef.getBaseType()), arrayTypeRef.getLength(), pointerSize);
                table.put(arrayTypeRef, t);
                return t;
            } else if (ref instanceof FunctionTypeRef) {
                FunctionTypeRef functionTypeRef = (FunctionTypeRef) ref;
                Type t = new FunctionType(get(functionTypeRef.getReturnType()), functionTypeRef.getParams().internTypes(this));
                table.put(functionTypeRef, t);
                return t;

            }
            throw new Error("unregistered type: " + ref.toString());
        }
        return type;
    }


    public Type getParamType(TypeRef ref) {
        Type type = get(ref);
        return type.isArray() ? pointerTo(type.getBaseType()) : type;
    }

    public PointerType pointerTo(Type baseType) {
        return new PointerType(pointerSize, baseType);
    }

    public int getIntSize() {
        return intSize;
    }

    public int getLongSize() {
        return longSize;
    }

    public int getPointerSize() {
        return pointerSize;
    }

    public int maxIntSize() {
        return pointerSize;
    }

    protected void checkRecursiveDefinition(Type type, ErrorHandler errorHandler) {
        doCheckRecursiveDefinition(type, new HashMap<Type, Object>(), errorHandler);
    }

    protected static final Object checking = new Object();
    protected static final Object checked = new Object();

    private void doCheckRecursiveDefinition(Type type, HashMap<Type, Object> marks, ErrorHandler errorHandler) {
        if (marks.get(type) == checking) {
            // checking
            errorHandler.error(((NamedType) type).location(), "recursive type definition： " + type);
        } else if (marks.get(type) == checked) {
            // checked
        } else {
            marks.put(type, checking);
            if (type instanceof CompositeType) {
                CompositeType ct = (CompositeType) type;
                for (Slot slot : ct.getMembers()) {
                    doCheckRecursiveDefinition(slot.getType(), marks, errorHandler);
                }
            } else if (type instanceof ArrayType) {
                ArrayType at = (ArrayType) type;
                doCheckRecursiveDefinition(at.getBaseType(), marks, errorHandler);
            } else if (type instanceof UserType) {
                UserType ut = (UserType) type;
                doCheckRecursiveDefinition(ut.realType(), marks, errorHandler);
            }
            marks.put(type, checked);
        }
    }

    public IntegerType signedInt() {
        return (IntegerType) table.get(IntegerTypeRef.intRef());
    }

    public IntegerType unsignedInt() {
        return (IntegerType) table.get(IntegerTypeRef.uintRef());
    }

    public IntegerType signedLong() {
        return (IntegerType) table.get(IntegerTypeRef.longRef());
    }

    public IntegerType unsignedLong() {
        return (IntegerType) table.get(IntegerTypeRef.ulongRef());
    }

    public void semanticCheck(ErrorHandler errorHandler) {
        for (Type t : types()) {
            if (t instanceof CompositeType) {
                checkVoidMembers((CompositeType) t, errorHandler);
                checkDuplicatedMembers((CompositeType) t, errorHandler);
            } else if (t instanceof ArrayType) {
                checkVoidMembers((ArrayType) t, errorHandler);
            }
            checkRecursiveDefinition(t, errorHandler);
        }
    }

    private void checkDuplicatedMembers(CompositeType t, ErrorHandler errorHandler) {
        Map<String, Slot> slotMap = new HashMap<>();

        for (Slot s : t.getMembers()) {
            if (slotMap.containsKey(s.getName())) {
                errorHandler.error(t.location(), t.toString() + " has duplicated member: " + s.getName());
            } else {
                slotMap.put(s.getName(), s);
            }
        }
    }


    private void checkVoidMembers(ArrayType t, ErrorHandler errorHandler) {
        if (t.getBaseType().isVoid()) {
            errorHandler.error("array cannot contain void");
        }
    }

    private void checkVoidMembers(CompositeType t, ErrorHandler errorHandler) {
        for (Slot s : t.getMembers()) {
            if (s.getType().isVoid()) {
                errorHandler.error("array cannot contain void");
            }
        }
    }

    private Collection<Type> types() {
        return table.values();
    }
}
