// To run it:
//  ./gradlew build
//  groovy -cp build/libs/add-version-1.0.jar ExampleLocalAST.groovy

@demo.Version('2.0')
class Foo {
}

println "Version is: ${Foo.VERSION}"
