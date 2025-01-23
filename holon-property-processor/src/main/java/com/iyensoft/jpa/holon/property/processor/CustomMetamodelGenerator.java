package com.iyensoft.jpa.holon.property.processor;


import com.holonplatform.core.internal.utils.TypeUtils;
import jakarta.persistence.Entity;
import org.hibernate.processor.Context;
import org.hibernate.processor.HibernateProcessor;
import org.hibernate.processor.model.Metamodel;

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
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes("jakarta.persistence.Entity")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
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

    private static void printClassDeclaration( Metamodel entity,  PrintWriter pw) {
        pw.print("public ");
        if (!entity.isImplementation() && !entity.isJakartaDataStyle()) {
            pw.print("abstract ");
        }

        pw.print(entity.isJakartaDataStyle() ? "interface " : "class ");
        pw.print(getGeneratedClassName(entity));
        String superClassName = entity.getSupertypeName();
        if (superClassName != null) {
            String var10001 = getGeneratedSuperclassName(entity, superClassName);
            pw.print(" extends " + var10001);
        }

        if (entity.isImplementation()) {
            pw.print(entity.getElement().getKind() == ElementKind.CLASS ? " extends " : " implements ");
            pw.print(entity.getSimpleName());
        }

        pw.println(" {");
    }

    private static  String getGeneratedClassName( Metamodel entity) {
        String className = entity.getSimpleName();
        return entity.isJakartaDataStyle() ? "_" + className : className + "_";
    }

    private static  String getGeneratedSuperclassName( Metamodel entity,  String superClassName) {
        if (entity.isJakartaDataStyle()) {
            int lastDot = superClassName.lastIndexOf(46);
            if (lastDot < 0) {
                return "_" + superClassName;
            } else {
                String var10000 = superClassName.substring(0, lastDot + 1);
                return var10000 + "_" + superClassName.substring(lastDot + 1);
            }
        } else {
            return superClassName + "_";
        }
    }

    private String toSnakeCase(String input) {
        return input.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    private String toPascalCase(String input) {
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }
}