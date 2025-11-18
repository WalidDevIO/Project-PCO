package com.ubo.paco.interpreteur;

import java.lang.reflect.*;
import java.util.*;
import simulation.antlr4.*;
import io.github.classgraph.*;


/**
 * Interpréteur dynamique de programmes SatelliteLang basé sur un visiteur ANTLR.
 * <p>
 * Cette classe exécute un programme SatelliteLang en utilisant la réflexion Java :
 * <ul>
 *   <li>Gestion des variables et de leur environnement</li>
 *   <li>Appels dynamiques de méthodes sur des objets instanciés</li>
 *   <li>Recherche automatique des classes dans des packages fournis</li>
 *   <li>Conversion dynamique des types pour les constructeurs et méthodes</li>
 * </ul>
 * Elle permet d'intégrer la logique du langage dans une simulation Java.
 */
public class SimulationVisitor extends SatelliteLangBaseVisitor<Object> {

    /**
     * Liste des packages dans lesquels rechercher des classes instanciables.
     * Remplie par les paramètres du constructeur + le package interne "com.ubo.paco".
     */
    private final List<String> allPackages = new ArrayList<>();

    /**
     * Variables définies dans le programme SatelliteLang.
     * Le nom de la variable est associé à un objet Java.
     */
    private final Map<String, Object> variables = new HashMap<>();

    /**
     * Crée un interpréteur en ajoutant une liste variable de packages externes
     * dans lesquels les classes pourront être recherchées.
     *
     * @param externalPackages liste des packages à analyser.
     */
    public SimulationVisitor(String... externalPackages) {
        this.allPackages.addAll(Arrays.asList(externalPackages));
        this.allPackages.add("com.ubo.paco");
    }

    /**
     * Crée un interpréteur avec une liste de packages externe.
     *
     * @param externalPackages packages supplémentaires pour la résolution des classes.
     */
    public SimulationVisitor(ArrayList<String> externalPackages) {
        this.allPackages.addAll(externalPackages);
        this.allPackages.add("com.ubo.paco");
    }

    /**
     * Recherche une classe accessible dans les packages fournis.
     * La méthode inspecte :
     * <ol>
     *     <li>Les classes détectées via ClassGraph (scan du classpath)</li>
     *     <li>Une tentative directe via {@code Class.forName()}</li>
     * </ol>
     *
     * @param className nom simple de la classe (sans package)
     * @return la classe correspondante si trouvée
     * @throws ClassNotFoundException si aucune classe correspondante n'est trouvée
     */
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

    /**
     * Exécute séquentiellement l’ensemble des statements du programme.
     *
     * @param ctx contexte ANTLR du programme
     * @return résultat du dernier statement exécuté
     */
    @Override
    public Object visitProgram(SatelliteLangParser.ProgramContext ctx) {
        Object result = null;
        for (var stmt : ctx.statement()) {
            result = visit(stmt);
        }
        return result;
    }
    /**
     * Exécute un statement d’affectation.
     *
     * @param ctx contexte ANTLR
     * @return valeur assignée
     */
    @Override
    public Object visitAssignStatement(SatelliteLangParser.AssignStatementContext ctx) {
        return visit(ctx.assignStmt());
    }
    /**
     * Exécute un statement d'appel de méthode.
     *
     * @param ctx contexte ANTLR
     * @return valeur retournée par la méthode appelée
     */
    @Override
    public Object visitMethodCallStatement(SatelliteLangParser.MethodCallStatementContext ctx) {
        return visit(ctx.methodCall());
    }
    /**
     * Ignore les commentaires.
     *
     * @param ctx contexte ANTLR
     * @return toujours {@code null}
     */
    @Override
    public Object visitCommentStatement(SatelliteLangParser.CommentStatementContext ctx) {
        return null;
    }

    /**
     * Affecte une variable à partir d’une expression.
     *
     * @param ctx contexte ANTLR de l'affectation
     * @return valeur assignée
     */
    @Override
    public Object visitAssignStmt(SatelliteLangParser.AssignStmtContext ctx) {
        String varName = ctx.ID().getText();
        SatelliteLangParser.ExprContext exprCtx = ctx.expr();

        Object value = visit(exprCtx);
        variables.put(varName, value);

        return value;
    }

    /**
     * Appelle une méthode sur un objet instancié et stocké dans l'environnement.
     * <p>
     * La recherche se fait dynamiquement via la réflexion Java.
     *
     * @param ctx contexte ANTLR de l'appel de méthode
     * @return valeur retournée par la méthode, ou {@code null} si void
     */
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
                Object value = visit(argCtx.expr());
                methodArgs.add(value);
            }
        }

        try {
            Object result = invokeMethod(target, methodName, methodArgs);


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

    /**
     * Évalue une expression (nombre, chaîne, identifiant, appel ou instanciation).
     *
     * @param ctx contexte ANTLR de l’expression
     * @return valeur évaluée
     */
    @Override
    public Object visitExpr(SatelliteLangParser.ExprContext ctx) {
        if (ctx.NUMBER() != null) {
            String numText = ctx.NUMBER().getText();
            // CORRECTION : toujours parser en Double pour éviter les problèmes de conversion
            return Double.parseDouble(numText);
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

    /**
     * Instancie dynamiquement une classe avec ou sans arguments nommés.
     *
     * @param ctx contexte ANTLR
     * @return instance construite
     */
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

    /**
     * Crée une instance en cherchant d'abord un constructeur compatible,
     * puis en dernier recours en utilisant les setters.
     *
     * @param clazz classe à instancier
     * @param args  arguments nommés du constructeur
     * @return instance nouvellement créée
     * @throws Exception si aucune instanciation n'est possible
     */
    private Object createInstance(Class<?> clazz, Map<String, Object> args) throws Exception {
        if (args.isEmpty()) {
            return clazz.getDeclaredConstructor().newInstance();
        }

        Object[] argValues = args.values().toArray();

        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (tryConstructor(constructor, argValues)) {
                Object[] convertedArgs = convertArgs(constructor.getParameterTypes(), argValues);
                if (convertedArgs != null) {
                    return constructor.newInstance(convertedArgs);
                }
            }
        }

        return createWithSetters(clazz, args);
    }
    /**
     * Tente de construire l'objet via les setters si aucun constructeur ne correspond.
     *
     * @param clazz classe à instancier
     * @param args  map d'arguments nommés
     * @return instance construite
     * @throws Exception si l'instanciation échoue
     */
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

    /**
     * Recherche et invoque dynamiquement une méthode sur un objet cible.
     * Les arguments sont automatiquement convertis vers les types requis.
     *
     * @param target     instance sur laquelle appeler la méthode
     * @param methodName nom de la méthode
     * @param args       arguments de l'appel
     * @return résultat de la méthode invoquée
     * @throws Exception si aucune méthode compatible n'est trouvée
     */
    private Object invokeMethod(Object target, String methodName, List<Object> args) throws Exception {
        Class<?> clazz = target.getClass();
        Object[] argArray = args.toArray();

        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName)) {
                Class<?>[] paramTypes = method.getParameterTypes();
                if (paramTypes.length == argArray.length) {
                    Object[] convertedArgs = convertArgs(paramTypes, argArray);
                    if (convertedArgs != null) {
                        return method.invoke(target, convertedArgs);
                    }
                }
            }
        }

        throw new NoSuchMethodException("Méthode introuvable : " + methodName +
                " avec " + args.size() + " argument(s) dans " + clazz.getName());
    }

    /**
     * Convertit automatiquement les types d’arguments pour qu’ils soient
     * compatibles avec les paramètres d’une méthode ou d’un constructeur.
     * Gère notamment les conversions numériques (Double → int, etc.).
     *
     * @param paramTypes types attendus
     * @param args       arguments réels
     * @return tableau d’arguments convertis ou {@code null} si incompatible
     */
    private Object[] convertArgs(Class<?>[] paramTypes, Object[] args) {
        Object[] converted = new Object[args.length];

        for (int i = 0; i < paramTypes.length; i++) {
            if (args[i] == null) {
                if (paramTypes[i].isPrimitive()) return null;
                converted[i] = null;
                continue;
            }

            Class<?> expected = paramTypes[i];
            Object arg = args[i];

            // Conversion numérique automatique
            if (arg instanceof Number) {
                Number num = (Number) arg;
                if (expected == double.class || expected == Double.class) {
                    converted[i] = num.doubleValue();
                } else if (expected == int.class || expected == Integer.class) {
                    converted[i] = num.intValue();
                } else if (expected == float.class || expected == Float.class) {
                    converted[i] = num.floatValue();
                } else if (expected == long.class || expected == Long.class) {
                    converted[i] = num.longValue();
                } else if (expected == short.class || expected == Short.class) {
                    converted[i] = num.shortValue();
                } else if (expected == byte.class || expected == Byte.class) {
                    converted[i] = num.byteValue();
                } else {
                    converted[i] = arg;
                }
            } else if (expected.isAssignableFrom(arg.getClass())) {
                converted[i] = arg;
            } else {
                return null; // Type incompatible
            }
        }

        return converted;
    }
    /**
     * Vérifie simplement si un constructeur possède le bon nombre d’arguments.
     *
     * @param constructor constructeur à tester
     * @param args        arguments fournis
     * @return {@code true} si le nombre correspond
     */
    private boolean tryConstructor(Constructor<?> constructor, Object[] args) {
        return constructor.getParameterTypes().length == args.length;
    }
    /**
     * Capitalise la première lettre d’une chaîne (utile pour déduire les setters).
     *
     * @param str chaîne à modifier
     * @return chaîne capitalisée
     */
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Récupère la valeur d’une variable stockée dans l’environnement.
     *
     * @param name nom de la variable
     * @return valeur associée ou {@code null} si absente
     */
    public Object getVariable(String name) {
        return variables.get(name);
    }
    /**
     * Retourne une vue non modifiable de toutes les variables.
     *
     * @return map immuable des variables
     */
    public Map<String, Object> getVariables() {
        return Collections.unmodifiableMap(variables);
    }
}