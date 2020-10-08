package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LoxClass extends LoxInstance implements LoxCallable  {
    final String name;
    final LoxClass superclass;
    private final Map<String, LoxFunction> methods;

    LoxClass(String name) {
        super(null);
        this.name = name;
        this.superclass = null;
        this.methods = new HashMap<>();
    }

    LoxClass(String name, LoxClass superclass, Map<String, LoxFunction> methods) {
        super();
        this.name = name;
        this.methods = methods;
        this.superclass = superclass;
    }

    @Override
    Object get(Token name) {
        LoxFunction method = findMethod(name.lexeme);
        if (method.isStatic()) {
            return method.bind(this);
        }
        throw new RuntimeError(name, "Method is not static!");
    }


    LoxFunction findMethod(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }

        if (superclass != null) {
            return superclass.findMethod(name);
        }

        return null;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        LoxInstance instance = new LoxInstance(this);
        LoxFunction initializer = findMethod("init");
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }

    @Override
    public int arity() {
        LoxFunction initializer = findMethod("init");
        if (initializer == null) return 0;
        return initializer.arity();
    }

    @Override
    public String toString() {
        return name;
    }
}
