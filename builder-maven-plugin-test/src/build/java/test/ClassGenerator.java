package test;

import java.io.File;
import java.io.IOException;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * A builder that generates a java class.
 */
@Builder
public class ClassGenerator {

	@Execute(phase = Phase.GENERATE_SOURCES)
	public void generateHelloWorldClass() throws IOException {

		MethodSpec main = MethodSpec
				.methodBuilder("main")
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.returns(void.class)
				.addParameter(String[].class, "args")
				.addStatement("$T.out.println($S)", System.class,
						"Hello world!").build();

		TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
				.addModifiers(Modifier.PUBLIC, Modifier.FINAL).addMethod(main)
				.build();

		JavaFile javaFile = JavaFile.builder("com.example.helloworld",
				helloWorld).build();

		javaFile.writeTo(new File(BuilderFolders.GENERATED_SOURCES));
	}

}
