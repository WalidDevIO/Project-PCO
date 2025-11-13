package com.ubo.paco.interpreteur;

import java.lang.reflect.*;
import java.util.*;
import simulation.antlr4.*;
import io.github.classgraph.*;

public class SimulationVisitor extends SatelliteLangBaseVisitor<Object> {

    private final List<String> allPackages = new ArrayList<>();
    private final Map<String, Object> variables = new HashMap<>();

    // --- Constructeurs ---
    public SimulationVisitor(String... externalPackages) {
        this.allPackages.addAll(Arrays.asList(externalPackages));
        this.allPackages.add("com.ubo.paco");
    }

    public SimulationVisitor(ArrayList<String> externalPackages) {
        this.allPackages.addAll(externalPackages);
        this.allPackages.add("com.ubo.paco");
    }

    // --- Recherche de classes ---
    public Class<?> findClass(String className) throws ClassNotFoundException {
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(allPackages.toArray(new String[0]))
                .ignoreClassVisibility()
                .scan()) {

            ClassInfo ci = scanResult.getAllClasses().stream()
                    .filter(c -> c.getSimpleName().equals(className))
                    .findFirst()
                    .orElse(null);

            if (ci != null) return ci.loadClass();
        }

        for (String packageName : allPackages) {
            try {
                String fullClassName = packageName + "." + className;
                return Class.forName(fullClassName);
            } catch (ClassNotFoundException ignored) {}
        }

        throw new ClassNotFoundException("Classe " + className + " introuvable dans les packages : " + allPackages);
    }

    // --- Programme principal ---
    @Override
    public Object visitProgram(SatelliteLangParser.ProgramContext ctx) {
        Object result = null;
        for (var stmt : ctx.statement()) {
            result = visit(stmt);
        }
        return result;
    }

    @Override
    public Object visitAssignStatement(SatelliteLangParser.AssignStatementContext ctx) {
        return visit(ctx.assignStmt());
    }

    @Override
    public Object visitMethodCallStatement(SatelliteLangParser.MethodCallStatementContext ctx) {
        return visit(ctx.methodCall());
    }

    @Override
    public Object visitCommentStatement(SatelliteLangParser.CommentStatementContext ctx) {
        return null;
    }

    // --- Affectation : var := expr ---
    @Override
    public Object visitAssignStmt(SatelliteLangParser.AssignStmtContext ctx) {
        String varName = ctx.ID().getText();
        SatelliteLangParser.ExprContext exprCtx = ctx.expr();

        Object value = visit(exprCtx);
        variables.put(varName, value);

        System.out.println("[DEBUG] " + varName + " := " + value);
        return value;
    }

    // --- Appel de méthode : obj.method(a=1, b=2) ---
    @Override
    public Object visitMethodCall(SatelliteLangParser.MethodCallContext ctx) {
        String varName = ctx.ID(0).getText();
        String methodName = ctx.ID(1).getText();

        Object target = variables.get(varName);
        if (target == null) {
            throw new RuntimeException("Variable non trouvée : " + varName);
        }

        // Collecte et évaluation correcte des arguments
        List<Object> methodArgs = new ArrayList<>();
        if (ctx.argList() != null) {
            for (var argCtx : ctx.argList().arg()) {
                Object value = visit(argCtx.expr()); // <--- garde le typage dynamique
                methodArgs.add(value);
            }
        }

        try {
            Object result = invokeMethod(target, methodName, methodArgs);

            // (Optionnel) : log de debug
            System.out.printf("[DEBUG] %s.%s(%s) => %s%n",
                    varName, methodName,
                    methodArgs.stream().map(Object::toString).toList(),
                    result);

            return result;
        } catch (IllegalArgumentException e) {
            System.err.println("[ERREUR TYPE] lors de " + methodName +
                    " sur " + target.getClass().getSimpleName());
            System.err.println("Arguments passés : " + methodArgs);
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'appel de " + methodName +
                    " sur " + varName + " (" + target.getClass().getSimpleName() + ")", e);
        }
    }


    // --- Expressions ---
    @Override
    public Object visitExpr(SatelliteLangParser.ExprContext ctx) {
        if (ctx.NUMBER() != null) {
            String numText = ctx.NUMBER().getText();
            return numText.contains(".") ? Double.parseDouble(numText) : Integer.parseInt(numText);
        }
        if (ctx.STRING() != null) {
            String str = ctx.STRING().getText();
            return str.substring(1, str.length() - 1); // retirer guillemets
        }
        if (ctx.HASHWORD() != null) {
            return ctx.HASHWORD().getText();
        }
        if (ctx.ID() != null) {
            String name = ctx.ID().getText();
            if (!variables.containsKey(name))
                throw new RuntimeException("Variable non définie : " + name);
            return variables.get(name);
        }
        if (ctx.instantiation() != null) {
            return visit(ctx.instantiation());
        }
        if (ctx.methodCall() != null) {
            return visit(ctx.methodCall());
        }
        throw new RuntimeException("Expression inconnue : " + ctx.getText());
    }

    // --- Instanciation : new ClassName(a=1, b=2) ---
    @Override
    public Object visitInstantiation(SatelliteLangParser.InstantiationContext ctx) {
        String className = ctx.ID().getText();

        Map<String, Object> args = new LinkedHashMap<>();
        if (ctx.argList() != null) {
            for (var argCtx : ctx.argList().arg()) {
                String argName = argCtx.ID().getText();
                Object argValue = visit(argCtx.expr());
                args.put(argName, argValue);
            }
        }

        try {
            Class<?> clazz = findClass(className);
            return createInstance(clazz, args);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Classe introuvable : " + className +
                    " dans les packages : " + allPackages, e);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de " + className, e);
        }
    }

    // --- Création d'instances ---
    private Object createInstance(Class<?> clazz, Map<String, Object> args) throws Exception {
        if (args.isEmpty()) {
            return clazz.getDeclaredConstructor().newInstance();
        }

        Object[] argValues = args.values().toArray();

        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (tryConstructor(constructor, argValues)) {
                return constructor.newInstance(argValues);
            }
        }

        return createWithSetters(clazz, args);
    }

    private Object createWithSetters(Class<?> clazz, Map<String, Object> args) throws Exception {
        Object instance = clazz.getDeclaredConstructor().newInstance();

        for (Map.Entry<String, Object> entry : args.entrySet()) {
            String setterName = "set" + capitalize(entry.getKey());
            try {
                invokeMethod(instance, setterName, Collections.singletonList(entry.getValue()));
            } catch (Exception e) {
                try {
                    invokeMethod(instance, entry.getKey(), Collections.singletonList(entry.getValue()));
                } catch (Exception ignored) {
                    System.err.println("[WARN] Impossible d'appliquer " + entry.getKey() + " sur " + clazz.getSimpleName());
                }
            }
        }
        return instance;
    }

    // --- Invocation dynamique ---
    private Object invokeMethod(Object target, String methodName, List<Object> args) throws Exception {
        Class<?> clazz = target.getClass();
        Object[] argArray = args.toArray();

        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName) && isCompatible(method.getParameterTypes(), argArray)) {
                return method.invoke(target, argArray);
            }
        }

        throw new NoSuchMethodException("Méthode introuvable : " + methodName +
                " avec " + args.size() + " argument(s) dans " + clazz.getName());
    }

    private boolean tryConstructor(Constructor<?> constructor, Object[] args) {
        return isCompatible(constructor.getParameterTypes(), args);
    }

    private boolean isCompatible(Class<?>[] paramTypes, Object[] args) {
        if (paramTypes.length != args.length) return false;

        for (int i = 0; i < paramTypes.length; i++) {
            if (args[i] == null) {
                if (paramTypes[i].isPrimitive()) return false;
                continue;
            }

            Class<?> expected = paramTypes[i].isPrimitive() ? wrapperClass(paramTypes[i]) : paramTypes[i];
            Class<?> actual = args[i].getClass();

            if (!expected.isAssignableFrom(actual) && !isNumericConversion(expected, actual)) {
                return false;
            }
        }
        return true;
    }

    private boolean isNumericConversion(Class<?> target, Class<?> source) {
        return Number.class.isAssignableFrom(target) && Number.class.isAssignableFrom(source);
    }

    private Class<?> wrapperClass(Class<?> primitive) {
        if (primitive == int.class) return Integer.class;
        if (primitive == double.class) return Double.class;
        if (primitive == boolean.class) return Boolean.class;
        if (primitive == long.class) return Long.class;
        if (primitive == float.class) return Float.class;
        if (primitive == byte.class) return Byte.class;
        if (primitive == short.class) return Short.class;
        if (primitive == char.class) return Character.class;
        return primitive;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // --- Accès variables ---
    public Object getVariable(String name) {
        return variables.get(name);
    }

    public Map<String, Object> getVariables() {
        return Collections.unmodifiableMap(variables);
    }
}
