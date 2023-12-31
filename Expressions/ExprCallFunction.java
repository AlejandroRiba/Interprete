package Expressions;

import java.util.List;

public class ExprCallFunction extends Expression{
    final Expression callee;
    // final Utils.Token paren;
    final List<Expression> arguments;

    public ExprCallFunction(Expression callee, /*Utils.Token paren,*/ List<Expression> arguments) {
        this.callee = callee;
        // this.paren = paren;
        this.arguments = arguments;
    }
}
