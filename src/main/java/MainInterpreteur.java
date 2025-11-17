import com.ubo.paco.interpreteur.SimulationVisitor;
import org.antlr.v4.runtime.*;
import simulation.antlr4.SatelliteLangLexer;
import simulation.antlr4.SatelliteLangParser;

import java.util.Scanner;

public class MainInterpreteur {

    public static void main(String[] args) {
        System.out.println("=== Interpréteur SatelliteLang ===");
        System.out.println("Tape une commande (ou 'exit' pour quitter)");
        System.out.println("------------------------------------");

        Scanner scanner = new Scanner(System.in);
        SimulationVisitor visitor = new SimulationVisitor();

        while (true) {
            System.out.print("> "); // prompt

            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                System.out.println("Fin de l'interpréteur.");
                break;
            }

            if (input.isEmpty()) continue;

            try {
                // Crée un flux à partir de la ligne saisie
                CharStream charStream = CharStreams.fromString(input);

                // Lexer et parser
                SatelliteLangLexer lexer = new SatelliteLangLexer(charStream);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                SatelliteLangParser parser = new SatelliteLangParser(tokens);

                // Parser selon la bonne règle (ici on peut supposer "statement" ou "program")
                var tree = parser.statement(); // ou parser.program() selon ta grammaire

                // Exécuter
                Object result = visitor.visit(tree);

                // Afficher résultat ou état
                if (result != null)
                    System.out.println("Résultat: " + result);

            } catch (Exception e) {
                System.err.println("Erreur: " + e.getMessage());
                e.printStackTrace(System.err);
            }
        }

        // Affiche l’état final des variables
        System.out.println("État final des variables:");
        System.out.println(visitor.getVariables());
        scanner.close();
    }
}
