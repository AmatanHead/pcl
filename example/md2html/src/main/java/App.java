import com.github.amatanhead.pcl.combinators.ast.AST;
import com.github.amatanhead.pcl.combinators.ast.NDefer;
import com.github.amatanhead.pcl.errors.ParsingError;
import com.github.amatanhead.pcl.errors.TokenizationError;
import com.github.amatanhead.pcl.lexer.RegexpTextLexer;
import com.github.amatanhead.pcl.lexer.TextLexer;
import com.github.amatanhead.pcl.parser.Parser;
import com.github.amatanhead.pcl.parser.RecursiveDescentParser;
import com.github.amatanhead.pcl.token.Token;
import com.github.amatanhead.pcl.token.TokenKind;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import static com.github.amatanhead.pcl.combinators.Combinators.*;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    private static final TokenKind OP = new TokenKind("OP");
    private static final TokenKind TEXT = new TokenKind("TEXT");

    private static TextLexer makeLexer() {
        RegexpTextLexer lexer = new RegexpTextLexer();

        lexer.addRule(Pattern.compile("~~|//|\\*\\*|__"), OP);
        lexer.addRule(Pattern.compile("."), TEXT);

        return lexer;
    }

    private static AST<Token> op_(String text) {
        return some(token -> token.getTokenKind() == OP && token.getData().equals(text));
    }

    private static Parser<String> makeParser() {
        NDefer<String> inlines = defer();

        AST<String> emphasis = seq(op_("//"), inlines, op_("//")).bind(t -> "<em>" + t.get(1) + "</em>");
        AST<String> double_emphasis = seq(op_("**"), inlines, op_("**")).bind(t -> "<strong>" + t.get(1) + "</strong>");
        AST<String> triple_emphasis = seq(op_("__"), inlines, op_("__")).bind(t -> "<u>" + t.get(1) + "</u>");
        AST<String> strike = seq(op_("~~"), inlines, op_("~~")).bind(t -> "<s>" + t.get(1) + "</s>");

        inlines.setDeferred(
                many(
                        or(
                                triple_emphasis,
                                double_emphasis,
                                emphasis,
                                strike,
                                a(TEXT).bind(Token::getData)
                        )
                ).bind(a -> a.stream().reduce("", String::concat))
        );

        AST<String> md = seq(inlines, a(TokenKind.EOF)).bind(t -> (String) t.get(1));

        return new RecursiveDescentParser<>(md);
    }

    public static void main(String[] args) {
        TextLexer lexer = makeLexer();
        Parser<String> parser = makeParser();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        reader.lines().forEachOrdered(s -> {
            try {
                System.out.println(parser.parse(lexer.tokenize(s)));
            } catch (TokenizationError | ParsingError error) {
                error.printStackTrace();
            }
        });
    }
}
