package edu.cmu.cs.mvelezce.tool.instrumentation.java.Bytecode;

/**
 * Created by mvelezce on 6/26/17.
 */
public class BytecodeUtils {

    public static String toJavaName(String descriptor) {
        int arrayDim = 0;
        int i = 0;
        char c = descriptor.charAt(0);
        while (c == '[') {
            ++arrayDim;
            c = descriptor.charAt(++i);
        }

        String name;
        if(c == 'L') {
            int i2 = descriptor.indexOf(';', i++);
            name = descriptor.substring(i, i2).replace('/', '.');
            i = i2;
        }
        else if(c == 'V') {
            name = "void";
        }
        else if(c == 'I') {
            name = "int";
        }
        else if(c == 'B') {
            name = "byte";
        }
        else if(c == 'J') {
            name = "long";
        }
        else if(c == 'D') {
            name = "double";
        }
        else if(c == 'F') {
            name = "float";
        }
        else if(c == 'C') {
            name = "char";
        }
        else if(c == 'S') {
            name = "short";
        }
        else if(c == 'Z') {
            name = "boolean";
        }
        else {
            throw new RuntimeException("bad descriptor: " + descriptor);
        }

        if(i + 1 != descriptor.length()) {
            throw new RuntimeException("multiple descriptors?: " + descriptor);
        }

        if(arrayDim == 0) {
            return name;
        }
        else {
            StringBuffer sbuf = new StringBuffer(name);
            do {
                sbuf.append("[]");
            } while (--arrayDim > 0);

            return sbuf.toString();
        }
    }

    /**
     * Converts to a descriptor from a Java class name
     */
    public static String toBytecodeDescriptor(String classname) {
        if(classname.isEmpty()) {
            return "";
        }

        boolean isArray = false;

        if(classname.endsWith("[]")) {
            isArray = true;
            classname = classname.replace("[]", "");
        }
        else if(classname.endsWith("...")) {
            isArray = true;
            classname = classname.replace("...", "");
        }

        String desc;

        if(classname.equals("void")) {
            desc = "V";
        }
        else if(classname.equals("int")) {
            desc = "I";
        }
        else if(classname.equals("byte")) {
            desc = "B";
        }
        else if(classname.equals("long")) {
            desc = "J";
        }
        else if(classname.equals("double")) {
            desc = "D";
        }
        else if(classname.equals("float")) {
            desc = "F";
        }
        else if(classname.equals("char")) {
            desc = "C";
        }
        else if(classname.equals("short")) {
            desc = "S";
        }
        else if(classname.equals("boolean")) {
            desc = "Z";
        }
        else {
            desc = "L" + toJvmName(classname) + ";";
        }

        if(isArray) {
            desc = "[" + desc;
        }

        if(desc.contains("<") && desc.contains(">")) {
            desc = desc.replaceAll("<.*>", "");
        }

        return desc;
    }

    public static String toJvmName(String classname) {
        return classname.replace('.', '/');
    }

}
