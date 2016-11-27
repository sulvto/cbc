package type;

import ast.Location;
import ast.Slot;
import exception.SemanticError;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sulvto on 16-11-15.
 */
public abstract class CompositeType extends NamedType {
    protected List<Slot> members;
    protected long cacheSize;
    protected long cacheAlign;
    private boolean isRecursiveChecked;

    public CompositeType(String name, List<Slot> members, Location location) {
        super(name, location);
        this.members = members;
        this.cacheSize = Type.sizeUnknown;
        this.cacheAlign = Type.sizeUnknown;
        this.isRecursiveChecked = false;
    }

    @Override
    public boolean isCompositeType() {
        return true;
    }

    @Override
    public boolean isSameType(Type other) {
        return compareMemberTypes(other, "isSameType");
    }

    @Override
    public boolean isCompatible(Type other) {
        return compareMemberTypes(other, "isCompatible");
    }

    @Override
    public boolean isCastableTo(Type target) {
        return compareMemberTypes(target, "isCastableTo");
    }

    private boolean compareMemberTypes(Type other, String cmpMethod) {
        if (isStruct() && !other.isStruct()) return false;
        if (isUnion() && !other.isUnion()) return false;
        CompositeType otherType = other.getCompositeType();
        if (members.size() != other.size()) return false;
        Iterator<Type> otherTypes = otherType.memberTypes().iterator();
        for (Type t : memberTypes()) {
            if (!compareTypesBy(cmpMethod, t, otherTypes.next())) {
                return false;
            }
        }
        return true;
    }

    private boolean compareTypesBy(String cmpMethod, Type t, Type tt) {
        try {
            Method cmp = Type.class.getMethod(cmpMethod, Type.class);
            return (Boolean) cmp.invoke(t, tt);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            throw new Error(e.getMessage());
        }
    }

    public List<Slot> getMembers() {
        return members;
    }

    public List<Type> memberTypes() {
        List<Type> result = new ArrayList<>();
        for (Slot s : members) {
            result.add(s.getType());
        }
        return result;
    }

    @Override
    public long size() {
        if (cacheSize == Type.sizeUnknown) {
            computeOffsets();
        }
        return cacheSize;
    }

    @Override
    public long alignment() {
        if (cacheAlign == Type.sizeUnknown) {
            computeOffsets();
        }
        return cacheAlign;
    }

    public boolean hasMember(String name) {
        return getMember(name) != null;
    }

    public Type memberType(String name) {
        return fetch(name).getType();
    }

    protected Slot fetch(String name) {
        Slot member = getMember(name);
        if (member == null) {
            throw new SemanticError("no such member in " + toString() + ": " + name);
        }
        return member;
    }

    protected abstract void computeOffsets();

    public long memberOffset(String name) {
        Slot fetch = fetch(name);
        if (fetch.getOffset() == Type.sizeUnknown) {
            computeOffsets();
        }
        return fetch.getOffset();
    }

    public Slot getMember(String name) {
        for (Slot s : members) {
            if (name.equals(s.getName())) {
                return s;
            }
        }
        return null;
    }
}
