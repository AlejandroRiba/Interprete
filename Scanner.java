import java.util.*;

public class Scanner {

    private static final Map<String, TipoToken> palabrasReservadas;
    public static final Map<String, TipoToken> simbolos;

    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and",    TipoToken.AND);
        palabrasReservadas.put("else",   TipoToken.ELSE);
        palabrasReservadas.put("false",  TipoToken.FALSE);
        palabrasReservadas.put("for",    TipoToken.FOR);
        palabrasReservadas.put("fun",    TipoToken.FUN);
        palabrasReservadas.put("if",     TipoToken.IF);
        palabrasReservadas.put("null",   TipoToken.NULL);
        palabrasReservadas.put("or",     TipoToken.OR);
        palabrasReservadas.put("print",  TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true",   TipoToken.TRUE);
        palabrasReservadas.put("var",    TipoToken.VAR);
        palabrasReservadas.put("while",  TipoToken.WHILE);

        simbolos = new HashMap<>();
        simbolos.put("+",   TipoToken.PLUS);
        simbolos.put("-",   TipoToken.MINUS);
        simbolos.put("*",   TipoToken.STAR);
        simbolos.put("{",   TipoToken.LEFT_BRACE);
        simbolos.put("}",   TipoToken.RIGHT_BRACE);
        simbolos.put("(",   TipoToken.LEFT_PAREN);
        simbolos.put(")",   TipoToken.RIGHT_PAREN);
        simbolos.put(",",   TipoToken.COMMA);
        simbolos.put(".",   TipoToken.DOT);
        simbolos.put(";",   TipoToken.SEMICOLON);
    }

    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private final List<String> caracteres = Arrays.asList("+", "-", "*", "{", "}", "(", ")", ",", ".",";");

    public Scanner(String source){
        this.source = source + " ";
    }

    public List<Token> scan() throws Exception {
        String lexema = "";
        int estado = 0;
        char c;

        for(int i=0; i<source.length(); i++){
            c = source.charAt(i);

            switch (estado){
                case 0:
                    if(Character.isLetter(c)){
                        estado = 13;
                        lexema += c;
                    }
                    else if(Character.isDigit(c)){
                        estado = 15;
                        lexema += c;
                    }
                    else if(c == '>'){
                        estado = 1;
                        lexema += c;
                    }
                    else if(c == '<'){
                        estado = 4;
                        lexema += c;
                    }
                    else if(c == '='){
                        estado = 7;
                        lexema += c;
                    }
                    else if(c == '!'){
                        estado = 10;
                        lexema += c;
                    }
                    else if(caracteres.contains(c+"")){
                        estado = 33;
                        lexema += c;
                    }

                    break;
                case 1:
                    if(c == '='){
                        lexema += c;
                        Token t = new Token(TipoToken.GREATER_EQUAL, lexema);
                        tokens.add(t);
                    }
                    else{
                        Token t = new Token(TipoToken.GREATER, lexema);
                        tokens.add(t);
                        i--;
                    }

                    estado = 0;
                    lexema = "";
                    break;
                case 4:
                    if(c == '='){
                        lexema += c;
                        Token t = new Token(TipoToken.LESS_EQUAL, lexema);
                        tokens.add(t);
                    }
                    else{
                        Token t = new Token(TipoToken.LESS, lexema);
                        tokens.add(t);
                        i--;
                    }

                    estado = 0;
                    lexema = "";
                    break;
                case 7:
                    if(c == '='){
                        lexema += c;
                        Token t = new Token(TipoToken.EQUAL_EQUAL, lexema);
                        tokens.add(t);
                    }
                    else{
                        Token t = new Token(TipoToken.EQUAL, lexema);
                        tokens.add(t);
                        i--;
                    }

                    estado = 0;
                    lexema = "";
                    break;
                case 10:
                    if(c == '='){
                        lexema += c;
                        Token t = new Token(TipoToken.BANG_EQUAL, lexema);
                        tokens.add(t);
                    }
                    else{
                        Token t = new Token(TipoToken.BANG, lexema);
                        tokens.add(t);
                        i--;
                    }

                    estado = 0;
                    lexema = "";
                    break;
                case 13:
                    if(Character.isLetter(c) || Character.isDigit(c)){
                        //estado = 13;
                        lexema += c;
                    }
                    else{
                        // Vamos a crear el Token de identificador o palabra reservada
                        TipoToken tt = palabrasReservadas.get(lexema);

                        if(tt == null){
                            Token t = new Token(TipoToken.IDENTIFIER, lexema);
                            tokens.add(t);
                        }
                        else{
                            Token t = new Token(tt, lexema);
                            tokens.add(t);
                        }

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 15:
                    if(Character.isDigit(c)){
                        //estado = 15;
                        lexema += c;
                    }
                    else if(c == '.'){
                        estado = 16;
                        lexema += c;
                    }
                    else if(c == 'E'){
                        estado = 18;
                        lexema += c;
                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        tokens.add(t); //Aquí se mandaría al estado 22 siguiendo el afd

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 16:
                    if(Character.isDigit(c)){
                        estado = 17;
                        lexema += c;
                    }
                    else{
                        //tenemos que ver si hay algún error aquí): no sé cómo aún
                        estado = 0;
                        lexema = ""; //No generamos token y lo mandamos de regreso
                    }
                    break;
                case 17:
                    if(Character.isDigit(c)){
                        //estado = 17;
                        lexema += c;
                    }
                    else if(c == 'E'){
                        estado = 18;
                        lexema += c;
                    }
                    else {
                        Token t = new Token(TipoToken.NUMBER, lexema);
                        tokens.add(t); //Aquí se mandaría al estado 23 siguiendo el afd

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 18:
                    if(c == '+' || c == '-'){
                       estado = 19;
                       lexema += c;
                    }
                    else if(Character.isDigit(c)){
                        estado = 20;
                        lexema += c;
                    }
                    else{
                        //tenemos que ver si hay algún error aquí): no sé cómo aún
                        estado = 0;
                        lexema = ""; //No generamos token y lo mandamos de regreso
                    }
                    break;
                case 19:
                    if(Character.isDigit(c)){
                        estado = 20;
                        lexema += c;
                    }
                    else{
                        //tenemos que ver si hay algún error aquí): no sé cómo aún
                        estado = 0;
                        lexema = ""; //No generamos token y lo mandamos de regreso
                    }
                    break;
                case 20:
                    if(Character.isDigit(c)){
                        //estado = 20;
                        lexema += c;
                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema);
                        tokens.add(t); //Aquí se mandaría al estado 21 siguiendo el afd

                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 33:
                    TipoToken tt = simbolos.get(lexema);
                    Token t = new Token(tt, lexema);
                    tokens.add(t);

                    estado = 0;
                    lexema = "";
                    i--;
                    break;
            }
        }


        return tokens;
    }
}

