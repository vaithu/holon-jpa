package com.iyensoft.jpa.holon.property.processor;


import com.holonplatform.core.internal.utils.TypeUtils;
import jakarta.persistence.Entity;
import org.hibernate.processor.Context;
import org.hibernate.processor.HibernateProcessor;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes("jakarta.persistence.Entity")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class CustomMetamodelGenerator extends HibernateProcessor {
    private Context context;
    

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing annotations");
        for (Element element : roundEnv.getElementsAnnotatedWith(Entity.class)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing entity: " + element.getSimpleName());
            generateMetamodelClass((TypeElement) element);
        }
        return true;
    }

    private void generateMetamodelClass(TypeElement entity) {
        String className = entity.getSimpleName() + "_";
        String packageName = processingEnv.getElementUtils().getPackageOf(entity).getQualifiedName().toString();

        try {
            context = new Context(processingEnv);
            context.logMessage(Diagnostic.Kind.ERROR,"Reached Holon Processor");
            JavaFileObject file = processingEnv.getFiler().createSourceFile(packageName + "." + className);
            try (Writer writer = file.openWriter()) {
                writer.write("package " + packageName + ";\n");
                writer.write("import jakarta.persistence.metamodel.SingularAttribute;\n");
                writer.write("import jakarta.persistence.metamodel.StaticMetamodel;\n");
                writer.write("import com.example.StringProperty;\n"); // Assuming StringProperty is in com.example package
                writer.write("@StaticMetamodel(" + entity.getSimpleName() + ".class)\n");
//                writer.write("public abstract class " + className + " {\n");
                writer.write("public interface " + className + "Model {\n");

                for (Element enclosed : entity.getEnclosedElements()) {
                    if (enclosed.getKind() == ElementKind.FIELD) {
                        VariableElement field = (VariableElement) enclosed;
                        if (TypeUtils.isString(field.getClass())) {
                            String fieldName = field.getSimpleName().toString();
                            String snakeCaseName = toSnakeCase(fieldName).toUpperCase();
                            writer.write("    public static final StringProperty " + snakeCaseName + " = StringProperty.create(" + className + "." + fieldName + ").message(\"" + toPascalCase(fieldName) + "\");\n");
                        }
                    }
                }

                writer.write("}\n");
            }
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.toString());
        }
    }


    private String toSnakeCase(String input) {
        return input.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    private String toPascalCase(String input) {
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }
}