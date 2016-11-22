package demo

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.syntax.SyntaxException
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class VersionASTTransformation extends AbstractASTTransformation {

    private static final String VERSION = 'VERSION'

    @Override
    public void visit(final ASTNode[] nodes, final SourceUnit source) {

        // 1. Verify the type of the arguments
        if (!(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof ClassNode)) {
            throw new RuntimeException('We can only use @Version to annotate classes')
        }

        // 2. Verify that the work it's not done yet
        ClassNode classNode = (ClassNode) nodes[1]
        if (classNode.getField(VERSION)) {
            println 'Field VERSION already exists'
            return
        }

        // 3. Add the field
        AnnotationNode annotation = (AnnotationNode)nodes[0]
        def version = annotation.getMember('value')
        if (version instanceof ConstantExpression) {
            classNode.addField(VERSION, ACC_PUBLIC | ACC_STATIC | ACC_FINAL, ClassHelper.STRING_TYPE, version)
        } else {
            source.addError(new SyntaxException('Value not valid for the annotation', annotation.lineNumber, annotation.columnNumber))
        }
    }
}