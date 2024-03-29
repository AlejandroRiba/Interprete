package Principal;

import Statements.Statement;
import Utils.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    static boolean existenErrores = false;


    public static void main(String[] args) throws IOException {
        if(args.length > 1) {
            System.out.println("Uso correcto: Interprete [archivo.txt]");

            //Convención definida en el archivo "system.h" de UNIX
            System.exit(64);
        } else if(args.length == 1){
            ejecutarArchivo(args[0]);
        } else{
            ejecutarPrompt();
        }
    } //nos ayuda a ver como va a empezar a ejecutar el programa

    private static void ejecutarArchivo(String path) throws IOException{
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        ejecutar(new String(bytes, Charset.defaultCharset()));

        //Se indica que existe un error
        if(existenErrores) System.exit(65);
    }

    private static void ejecutarPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for(;;){
            System.out.print(">>> ");
            String linea = reader.readLine();
            if(linea == null) break; //Presionar Ctrl + D
            ejecutar(linea);
            existenErrores = false;
        }
    }

    private static void ejecutar(String source) {
        try{
            Scanner scanner = new Scanner(source);
            List<Token> tokens = scanner.scan();
            List<Statement> program;

            /*for(Token token : tokens){
                System.out.println(token);
            }*/
            //comento la impresión de tokens
            if(!existenErrores) {
                Parser parser = new ASDR(tokens);
                parser.parse();
            }

            if(!existenErrores){
                TablaSimbolos tabla = new TablaSimbolos();
                AST ast = new AST(tokens);
                program = ast.program();

                if(program != null){
                    for(Statement stmt : program){
                        stmt.ejecutar(tabla);
                    }
                }
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /* El método error se puede usar desde las distintas clases para
    reportar los errores: Main.error(...);
     */
    public static void error(int linea, String mensaje){
        reportar(linea, "", mensaje);
    }

    private static void reportar(int linea, String posicion, String mensaje){
        /*System.err.println(
                "[linea " + linea + "] Error " + posicion + ": " + mensaje
        );*/
        existenErrores = true;
    }

}
