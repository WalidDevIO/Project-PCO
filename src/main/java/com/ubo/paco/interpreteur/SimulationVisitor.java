package com.ubo.paco.interpreteur;

import java.lang.reflect.*;
import java.util.*;
import simulation.antlr4.*;

public class SimulationVisitor extends SatelliteLangBaseVisitor<Object> {

    private final List<String> basePackages;
    private final Map<String, Object> variables = new HashMap<>();

    /**
     * @param basePackages Liste des packages de base où chercher les classes
     */
    public SimulationVisitor(String... basePackages) {
        this.basePackages = Arrays.asList(basePackages);
    }

    /**
     * @param basePackages Liste des packages de base où chercher les classes
     */
    public SimulationVisitor(List<String> basePackages) {
        this.basePackages = new ArrayList<>(basePackages);
    }

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

    @Override
    public Object visitAssignStmt(SatelliteLangParser.AssignStmtContext ctx) {
        String varName = ctx.ID(0).getText();
        String className = ctx.ID(1).getText();

        // Collecter les arguments
        Map<String, Object> args = new LinkedHashMap<>();
        if (ctx.argList() != null) {
            var argListCtx = ctx.argList();
            for (var argCtx : argListCtx.arg()) {
                String argName = argCtx.ID().getText();
                Object argValue = visit(argCtx.expr());
                args.put(argName, argValue);
            }
        }

        try {
            Class<?> clazz = findClass(className);
            Object instance = createInstance(clazz, args);
            variables.put(varName, instance);
            return instance;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Classe introuvable : " + className +
                    " dans les packages : " + basePackages, e);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de " + className, e);
        }
    }

    @Override
    public Object visitMethodCall(SatelliteLangParser.MethodCallContext ctx) {
        String varName = ctx.ID(0).getText();
        String methodName = ctx.ID(1).getText();

        Object target = variables.get(varName);
        if (target == null) {
            throw new RuntimeException("Variable non trouvée : " + varName);
        }

        // Collecter les arguments de la méthode
        List<Object> methodArgs = new ArrayList<>();
        if (ctx.argList() != null) {
            for (var argCtx : ctx.argList().arg()) {
                Object argValue = visit(argCtx.expr());
                methodArgs.add(argValue);
            }
        }

        try {
            return invokeMethod(target, methodName, methodArgs);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'appel de " + methodName + " sur " + varName, e);
        }
    }

    @Override
    public Object visitArgList(SatelliteLangParser.ArgListContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Object visitArg(SatelliteLangParser.ArgContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Object visitExpr(SatelliteLangParser.ExprContext ctx) {
        if (ctx.NUMBER() != null) {
            String num = ctx.NUMBER().getText();
            if (num.contains(".")) {
                return Double.parseDouble(num);
            }
            return Integer.parseInt(num);
        }

        if (ctx.STRING() != null) {
            String str = ctx.STRING().getText();
            return str.substring(1, str.length() - 1);
        }

        if (ctx.HASHWORD() != null) {
            return ctx.HASHWORD().getText();
        }

        if (ctx.ID() != null) {
            String varName = ctx.ID().getText();
            Object value = variables.get(varName);
            if (value == null) {
                throw new RuntimeException("Variable non trouvée : " + varName);
            }
            return value;
        }

        return visitChildren(ctx);
    }

    /**
     * Cherche une classe dans tous les packages de base
     */
    private Class<?> findClass(String className) throws ClassNotFoundException {
        // D'abord essayer le nom de classe directement (peut contenir le package complet)
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            // Ignorer et continuer
        }

        // Essayer dans chaque package de base
        ClassNotFoundException lastException = null;
        for (String basePackage : basePackages) {
            try {
                return Class.forName(basePackage + "." + className);
            } catch (ClassNotFoundException e) {
                lastException = e;
            }
        }

        throw new ClassNotFoundException("Classe " + className +
                " introuvable dans les packages : " + basePackages, lastException);
    }

    /**
     * Crée une instance en trouvant le constructeur approprié
     */
    private Object createInstance(Class<?> clazz, Map<String, Object> args) throws Exception {
        if (args.isEmpty()) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("La classe " + clazz.getSimpleName() +
                        " n'a pas de constructeur sans paramètres et aucun argument n'a été fourni", e);
            }
        }

        // Essayer avec les valeurs dans l'ordre fourni
        Object[] argValues = args.values().toArray();

        // Chercher un constructeur avec le bon nombre de paramètres et types compatibles
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (tryConstructor(constructor, argValues)) {
                return constructor.newInstance(argValues);
            }
        }

        // Essayer avec un constructeur sans paramètres + setters
        return createWithSetters(clazz, args);
    }


    /**
     * Essaie de créer l'instance avec constructeur sans paramètres + setters
     */
    private Object createWithSetters(Class<?> clazz, Map<String, Object> args) throws Exception {
        Object instance;
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(buildConstructorErrorMessage(clazz, args), e);
        }

        // Appliquer les setters
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            boolean success = false;

            // Essayer setXxx
            String setterName = "set" + capitalize(entry.getKey());
            try {
                invokeMethod(instance, setterName, Collections.singletonList(entry.getValue()));
                success = true;
            } catch (Exception e) {
                // Essayer avec le nom tel quel
                try {
                    invokeMethod(instance, entry.getKey(), Collections.singletonList(entry.getValue()));
                    success = true;
                } catch (Exception e2) {
                    // Ignorer
                }
            }

            if (!success) {
                System.err.println("Avertissement : impossible de définir " + entry.getKey() +
                        " pour " + clazz.getSimpleName());
            }
        }

        return instance;
    }

    /**
     * Vérifie si un constructeur peut être utilisé avec les arguments donnés
     */
    private boolean tryConstructor(Constructor<?> constructor, Object[] args) {
        Class<?>[] paramTypes = constructor.getParameterTypes();
        return isCompatible(paramTypes, args);
    }

    /**
     * Construit un message d'erreur détaillé sur les constructeurs disponibles
     */
    private String buildConstructorErrorMessage(Class<?> clazz, Map<String, Object> args) {
        StringBuilder sb = new StringBuilder();
        sb.append("Impossible de créer une instance de ").append(clazz.getSimpleName());
        sb.append("\nArguments fournis (").append(args.size()).append(") : ");

        int i = 0;
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            sb.append(entry.getKey()).append("=");
            if (entry.getValue() != null) {
                sb.append(entry.getValue().getClass().getSimpleName());
            } else {
                sb.append("null");
            }
            if (i < args.size() - 1) sb.append(", ");
            i++;
        }

        sb.append("\n\nConstructeurs disponibles :");
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 0) {
            sb.append("\n  Aucun constructeur public");
        } else {
            for (Constructor<?> constructor : constructors) {
                sb.append("\n  - ").append(clazz.getSimpleName()).append("(");
                Class<?>[] paramTypes = constructor.getParameterTypes();
                for (int j = 0; j < paramTypes.length; j++) {
                    sb.append(paramTypes[j].getSimpleName());
                    if (j < paramTypes.length - 1) sb.append(", ");
                }
                sb.append(")");
            }
        }

        return sb.toString();
    }
    /**
     * Invoque une méthode de manière dynamique
     */
    private Object invokeMethod(Object target, String methodName, List<Object> args) throws Exception {
        Class<?> clazz = target.getClass();
        Object[] argArray = args.toArray();

        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName) &&
                    isCompatible(method.getParameterTypes(), argArray)) {
                return method.invoke(target, argArray);
            }
        }

        throw new NoSuchMethodException("Méthode introuvable : " + methodName +
                " avec " + args.size() + " argument(s) dans " + clazz.getName());
    }

    /**
     * Vérifie si les types de paramètres sont compatibles avec les arguments
     */
    private boolean isCompatible(Class<?>[] paramTypes, Object[] args) {
        if (paramTypes.length != args.length) {
            return false;
        }

        for (int i = 0; i < paramTypes.length; i++) {
            if (args[i] == null) {
                if (paramTypes[i].isPrimitive()) {
                    return false;
                }
                continue;
            }

            Class<?> paramType = paramTypes[i];
            Class<?> argType = args[i].getClass();

            if (paramType.isPrimitive()) {
                paramType = wrapperClass(paramType);
            }

            if (!paramType.isAssignableFrom(argType)) {
                if (!isNumericConversion(paramType, argType)) {
                    return false;
                }
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
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public Object getVariable(String name) {
        return variables.get(name);
    }

    public Map<String, Object> getVariables() {
        return Collections.unmodifiableMap(variables);
    }

    public List<String> getBasePackages() {
        return Collections.unmodifiableList(basePackages);
    }
}