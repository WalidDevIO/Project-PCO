package com.ubo.paco.interpreteur;

import com.ubo.paco.model.Balise;
import com.ubo.paco.model.Satellite;
import simulation.antlr4.SatelliteLangBaseVisitor;
import simulation.antlr4.SatelliteLangParser;

import java.util.HashMap;
import java.util.Map;

public class SimulationVisitor extends SatelliteLangBaseVisitor<Object> {
    private final Map<String, Object> context = new HashMap<>();

    @Override
    public Object visitAssignStatement(SatelliteLangParser.AssignStatementContext ctx) {
        String varName = ctx.assignStmt().ID().toString();
        String className = ctx.assignStmt().ID(1).getText(); // ID après 'new'

        // Récupération des arguments
        Map<String, Object> args = new HashMap<>();
        if (ctx.assignStmt().argList() != null) {
            for (SatelliteLangParser.ArgContext argCtx : ctx.assignStmt().argList().arg()) {
                String key = argCtx.ID().getText();
                Object value = visit(argCtx.expr()); // valeur évaluée
                args.put(key, value);
            }
        }

        // Création des objets selon le className
        Object instance = switch (className) {
            case "Satellite" -> new Satellite(null, null, null);
            case "Balise" -> new Balise(null, null, null);
            default -> throw new RuntimeException("Classe inconnue : " + className);
        };

        context.put(varName, instance);
        return instance;
    }

    @Override
    public Object visitMethodCallStatement(SatelliteLangParser.MethodCallStatementContext ctx) {
        String varName = ctx.methodCall().ID(0).getText();
        String method = ctx.methodCall().ID(1).getText();
        Object obj = context.get(varName);
        try {
            obj.getClass().getMethod(method).invoke(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Object visitExpr(SatelliteLangParser.ExprContext ctx) {
        if (ctx.NUMBER() != null) return Integer.parseInt(ctx.NUMBER().getText());
        if (ctx.STRING() != null) return ctx.STRING().getText().replace("\"", "");
        if (ctx.HASHWORD() != null) return ctx.HASHWORD().getText().substring(1);
        if (ctx.ID() != null) return ctx.ID().getText();
        return null;
    }
}
