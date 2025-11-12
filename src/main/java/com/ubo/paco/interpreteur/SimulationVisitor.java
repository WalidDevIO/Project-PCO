package com.ubo.paco.interpreteur;

import com.ubo.paco.model.Balise;
import com.ubo.paco.model.Satellite;
import simulation.antlr4.SatelliteLangBaseVisitor;
import simulation.antlr4.SatelliteLangParser;

import java.util.HashMap;
import java.util.Map;

public class SimulationVisitor extends SatelliteLangBaseVisitor<Object> {
    // Stocke les variables créées
    private final Map<String, Object> variables = new HashMap<>();

    public Map<String, Object> getVariables() {
        return variables;
    }

    @Override
    public Object visitAssignStatement(SatelliteLangParser.AssignStatementContext ctx) {
        var assign = ctx.assignStmt();

        // Nom de la variable
        String varName = assign.ID(0).getText();

        // Nom de la classe à instancier
        String className = assign.ID(1).getText();

        // Arguments de l'instanciation
        Map<String, Object> args = new HashMap<>();
        if (assign.argList() != null) {
            for (var argCtx : assign.argList().arg()) {
                String key = argCtx.ID().getText();
                Object value = visit(argCtx.expr()); // on visite l'expression
                args.put(key, value);
            }
        }

        // Crée l'objet (simplifié ici avec des switch, tu peux remplacer par réflexion)
        Object obj;
        switch (className) {
            case "Satellite":
                obj = new Satellite(null, null, null);
                break;
            case "Balise":
                obj = new Balise(null, null, null);
                break;
            default:
                throw new RuntimeException("Classe inconnue: " + className);
        }

        // Stocke la variable
        variables.put(varName, obj);

        return obj;
    }

    @Override
    public Object visitExpr(SatelliteLangParser.ExprContext ctx) {
        if (ctx.NUMBER() != null) {
            return Integer.parseInt(ctx.NUMBER().getText());
        } else if (ctx.STRING() != null) {
            return ctx.STRING().getText().replace("\"", "");
        } else if (ctx.HASHWORD() != null) {
            return ctx.HASHWORD().getText();
        } else if (ctx.ID() != null) {
            return variables.get(ctx.ID().getText());
        }
        return null;
    }
}
