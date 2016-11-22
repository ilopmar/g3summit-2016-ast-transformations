package demo

import static groovy.test.GroovyAssert.assertScript

import org.codehaus.groovy.control.MultipleCompilationErrorsException
import spock.lang.Specification

class VersionASTTransformationSpec extends Specification {

    void 'Can not annotate properties'() {
        when:
            assertScript """
                class Foo {
                    @demo.Version
                    String bar
                }
            """

        then:
            def e = thrown RuntimeException
            e.message.contains('We can only use @Version to annotate classes')
    }

    void 'Can not annotate methods'() {
        when:
            assertScript """
                class Foo {
                    @demo.Version
                    void bar() { }
                }
            """

        then:
            def e = thrown RuntimeException
            e.message.contains('We can only use @Version to annotate classes')
    }

    void 'Do not add VERSION if it already exists'() {
        expect:
            assertScript """
                @demo.Version
                class Foo {
                    static final String VERSION = '0.1'
                }

                assert Foo.VERSION == '0.1'
            """
    }

    void 'Add VERSION because it does not exist'() {
        expect:
            assertScript """
                @demo.Version('2.0')
                class Foo { }

                assert Foo.VERSION == '2.0'
            """
    }

    void 'Compilation error because the value of the annotation is not a constant'() {
        when:
            assertScript """
                @demo.Version(a)
                class Foo { }
            """

        then:
            def e = thrown MultipleCompilationErrorsException
            e.message.contains('Value not valid for the annotation')
    }

}
