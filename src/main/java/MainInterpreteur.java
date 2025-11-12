import com.ubo.paco.interpreteur.SimulationVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import simulation.antlr4.SatelliteLangLexer;
import simulation.antlr4.SatelliteLangParser;

import java.io.File;
import java.io.IOException;

public class MainInterpreteur {

    public static void main(String[] args) {
        try {

            // Chemin réel vers ton fichier
            String filePath = "src/main/txt/test/test.txt";

            // Crée un CharStream depuis le fichier
            var charStream = CharStreams.fromFileName(filePath);

            // Crée le lexer et le parser
            var lexer = new SatelliteLangLexer(charStream);
            var parser = new SatelliteLangParser(new CommonTokenStream(lexer));

            // Parse le programme
            var tree = parser.program();

            // Visiteur pour interpréter
            var visitor = new SimulationVisitor();
            visitor.visit(tree);

            System.out.println(visitor.getVariables());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
