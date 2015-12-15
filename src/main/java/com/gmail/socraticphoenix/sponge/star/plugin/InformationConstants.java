/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 socraticphoenix@gmail.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Socratic_Phoenix (socraticphoenix@gmail.com)
 */
package com.gmail.socraticphoenix.sponge.star.plugin;

public class InformationConstants {

    public static final String ARGUMENTS_SPEC = "Syntax Specification for Arguments\n" +
            "\n" +
            "Goals:\n" +
            "    1. Allow the default arguments style to continue to be used (/<command> <arg1> <arg2> <agr3>... (split by spaces)) while also:\n" +
            "    2. Implementing more versatile styles, as well as more advanced parsing\n" +
            "    3. Making it easy for both the developer and the user to interact with the system\n" +
            "\n" +
            "\n" +
            "Specification:\n" +
            "\n" +
            "Arguments can be give in 2 forms, listed and mapped. Listed is a list of values, mapped is a set of mappings. The two types CANNOT be mixed.\n" +
            "\n" +
            "Type 1, ordered:\n" +
            "    - Values appear in a list separated by commas and/or spaces\n" +
            "    - Examples:\n" +
            "        - /test 1 2 3\n" +
            "        - /test 1, 2, 3\n" +
            "        - /test 1, 2 3\n" +
            "    - The API should internally assign default names to the values, as specified by the command specification\n" +
            "\n" +
            "Type 2, unordered:\n" +
            "    - Values appear in a list of key-value mappings, separated by commas and/or spaces. Key-value mappings should be separated with colons.\n" +
            "    - Examples:\n" +
            "        - /test last:3 second:2 first:1\n" +
            "        - /test last:3, first:1, second:2\n" +
            "        - /test last:3, first:1 second:2\n" +
            "\n" +
            "Additional Notes:\n" +
            "    \"/test 1 2 3\" is equivalent to \"/test last:3, second:2, first:1\" because the default order of values\n" +
            "     for the arguments is \"first, second, last\", therefore the parser should assign \"1\" to \"first,\" \"2\" to \"second,\" and\n" +
            "     \"3\" to \"last\" in the first example. In the second example, the default order of values is not used, but rather\n" +
            "     all values are directly mapped.\n" +
            "\n" +
            "     If deeper data is needed, JSON should be used as an argument value, for example:\n" +
            "     /test detail:{\"key\":\"value\"}, last:3, first:1, second:2\n" +
            "\n" +
            "     Additionally, if a list is needed list form should be used:\n" +
            "     /test list:[1, 2, 3, 4, 5] last:3 first:1 second:2\n" +
            "     List form follows the same rules and specifications as listed argument form, except their values' 'default names' are integers that specify the index in the list\n" +
            "\n";

    public static final String JLSC_SPEC = "LSC Specification\n" +
            "\n" +
            "Introduction:\n" +
            "\tJLSC stands for JSON-Like Structured Configuration, it is inspired by JSON's structure, HOCON's syntax, Java's\n" +
            "annotations, and CIF's value format.\n" +
            "\n" +
            "General Syntax:\n" +
            "\tJLSC, like JSON, is made up of three parts, Arrays, Maps, and Values. An array and a map are both special kinds\n" +
            "of values, arrays contain other values, and maps contain key-value mappings of values. The base of a JLSC config is\n" +
            "always a map.\n" +
            "\n" +
            "\tSpecial Characters:\n" +
            "\t\t- { and } enclose a map\n" +
            "\t\t- [ and ] enclose an array\n" +
            "\t\t- new lines mark the end of a map value\n" +
            "\t\t- commas mark the end of an array value\n" +
            "\n" +
            "\tValues:\n" +
            "\t\tA value is any string that can be intepreted into a data or object value. By default, numbers should be\n" +
            "\tinterpreted as the highest storage value they can, for example, in Java numbers are interpreted as Longs or Doubles,\n" +
            "\tunless they have a type specifier, more about type specifiers in the 'properties' section. An example string value could\n" +
            "\tbe \"hello\"\n" +
            "\n" +
            "\tKey-Value pairs:\n" +
            "\t\tKey-value pairs are in the form <key>= <value>, where the key does not contain spaces,\n" +
            "\tthe equals sign, or any character that needs to be escaped in a Java string (quotes, new line, line feed, etc.)\n" +
            "\n" +
            "\n" +
            "\tMaps:\n" +
            "\t\tAll maps, except for the top-level map, are enclosed in curly braces. An example map is as follows:\n" +
            "\t\t{\n" +
            "\t\t\tstring= \"value\"\n" +
            "\t\t\tstring2= \"value2\"\n" +
            "\t\t}\n" +
            "\n" +
            "\tArrays:\n" +
            "\t\tAll arrays are eclosed in square brackets. An example array is as follows:\n" +
            "\t\t[\n" +
            "\t\t\t\"value\",\n" +
            "\t\t\t\"value2\"\n" +
            "\t\t]\n" +
            "\n" +
            "\tNote:\n" +
            "\t\tSome arrays, and/or maps, contain arrays and/or maps:\n" +
            "\t\t[\n" +
            "\t\t\t\"value\",\n" +
            "\t\t\t{\n" +
            "\t\t\t\tstring= \"value2\"\n" +
            "\t\t\t\tarray= [\n" +
            "\t\t\t\t\t2,\n" +
            "\t\t\t\t\t\"value\"\n" +
            "\t\t\t\t]\n" +
            "\t\t\t}\n" +
            "\t\t]\n" +
            "\t\tAccording to the special characters section, a new line marks the end of a map value,\n" +
            "\t\tmeaning that 'array= [' would be the entire value, however, in addition to a new line\n" +
            "\t\tbeing the ending character, brackets must also be balanced at that ending character,\n" +
            "\t\tso 'array= [' has a bracket that is not balanced out until the ']' closing bracket is reached.\n" +
            "\t\tThe same logic is applied to sub maps, maps in arrays, and arrays in arrays.\n" +
            "\t\t\t\n" +
            "\t\t\t\n" +
            "Comments and Properties:\n" +
            "\tIn addition to actual values, JLSC supports two forms of meta-data, key-value comments, and value properties.\n" +
            "Key-value comments are attached to a key-value pair, value properties are attached to a single value.\n" +
            "\n" +
            "\tComments:\n" +
            "\t\tComments are in the form #<comment>, and a new line character specifies their end. All comments should\n" +
            "\tbe applied to the next key-value pair parsed. An example of comments is as follows:\n" +
            "\t\t#Comment for 'string'\n" +
            "\t\t#Another comment for 'string'\n" +
            "\t\tstring= \"value\"\n" +
            "\t\t\n" +
            "\t\t#Comment for 'string2'\n" +
            "\t\tstring2= \"val\"\n" +
            "\n" +
            "\tValue Properties:\n" +
            "\t\tValue properties are applied as a prefix to a value, and are of the form @<property>:\n" +
            "\tAn example key-value pair with value properties is as follows:\n" +
            "\t\ttest=@special:@tagged:@important:\"String\"\n" +
            "\tnotice that value properties can be \"stacked,\" or continuosly applied. Value properties should continue to\n" +
            "\tbe read by the parser until ':@' does not appear. Another example of a key-value pair follows:\n" +
            "\t\tinteger=@integer:1243\n" +
            "\tin addition to being a source of meta data, there are 8 special value properties used to explicitly specify\n" +
            "\tthe type of a value. They are listed below. Also note that all Value Properties are case-sensitive, @integer:\n" +
            "\tis not the same as @INTEGER:\n" +
            "\n" +
            "\t\tSpecial Value Properties:\n" +
            "\t\t\t- double\n" +
            "\t\t\t\t- explicitly defines the value as a double\n" +
            "\t\t\t- float\n" +
            "\t\t\t\t- explicitly defines the value as a float\n" +
            "\t\t\t- long\n" +
            "\t\t\t\t- explicitly defines the value as a long\n" +
            "\t\t\t- integer\n" +
            "\t\t\t\t- explicitly defines the value as a integer\n" +
            "\t\t\t- short\n" +
            "\t\t\t\t- explicitly defines the value as a short\n" +
            "\t\t\t- byte\n" +
            "\t\t\t\t- explicitly defines the value as a byte\n" +
            "\t\t\t- boolean\n" +
            "\t\t\t\t- explicitly defines the value as a boolean\n" +
            "\t\t\t- string\n" +
            "\t\t\t\t- explicitly defines the value as a string\n" +
            "\t\t\t- null\n" +
            "\t\t\t\t- explicitly defines the value as null\n" +
            "\n" +
            "Extensibility:\n" +
            "\tSerialization:\n" +
            "\t\tJLSC requires that serialization of objects be supported. A serialiazed object is simply a JLSC map\n" +
            "\tcontaining appropriate values and data to reconstruct the object. It is up to the implementation exactly how \n" +
            "\tto handle serialization, but the JLSC Map should look something like this:\n" +
            "\tserializedObject= {\n" +
            "\t\t#The type of the data\n" +
            "\t\ttype= \"objectType\"\n" +
            "\n" +
            "\t\t#A pointer to the thing that serialized the data\n" +
            "\t\tserializer= \"serializer\"\n" +
            "\t\t\n" +
            "\t\t#Data required to reconstruct the object/data\n" +
            "\t\tdata= {\n" +
            "\t\t\n" +
            "\t\t}\n" +
            "\t}\n" +
            "\n" +
            "\tValue Conversion: \n" +
            "\t\tJLSC requires extensible value conversion, i.e. the ability to convert a single string to data of\n" +
            "\tsome kind. JLSC requires value conversion support for doubles, floats, longs, integers, shorts, bytes, booleans\n" +
            "\tstrings, and null values, however the JLSC implementation should provide an optional way to register\n" +
            "\tvalue conveters tht can create different values from a single string.\n" +
            "\t\t\t\n" +
            "\n" +
            "Examples:\n" +
            "\tThe following is an example JLSC config:\n" +
            "#Value properties, such as '@integer:' specify a values type, and other possible data about it.\n" +
            "int= @integer:314\n" +
            "#This is a sub section\n" +
            "sub= {\n" +
            "    double= @double:431.41\n" +
            "    Array= [\n" +
            "        \"Hello world\",\n" +
            "        @double:42143.432,\n" +
            "        @long:51451,\n" +
            "        {\n" +
            "            #This is a null value\n" +
            "            #Really, it is\n" +
            "            NullValue= null\n" +
            "            Boolean= true\n" +
            "        },\n" +
            "        false\n" +
            "    ]\n" +
            "}\n" +
            "\n" +
            "it was created by the Plasma JLSC library, in java, with the following code:\n" +
            "\tJLSCCompound compound = new JLSCCompound();\n" +
            "        compound.put(\"int\", 314);\n" +
            "        compound.get(\"int\").get().getComments().add(new JLSCPairComment(\"Value properties, such as '@integer:' specify a values type, and other possible data about it.\"));\n" +
            "        JLSCCompound sub = new JLSCCompound();\n" +
            "        sub.put(\"double\", 431.41);\n" +
            "        compound.put(\"sub\", sub);\n" +
            "        compound.get(\"sub\").get().getComments().add(new JLSCPairComment(\"This is a sub section\"));\n" +
            "        JLSCCompound arraySub = new JLSCCompound();\n" +
            "        arraySub.put(\"NullValue\", JLSCValue.nullValue());\n" +
            "        arraySub.get(\"NullValue\").get().getComments().add(new JLSCPairComment(\"This is a null value\"));\n" +
            "        arraySub.get(\"NullValue\").get().getComments().add(new JLSCPairComment(\"Really, it is\"));\n" +
            "        arraySub.put(\"Boolean\", true);\n" +
            "\n" +
            "        JLSCArray subArray = JLSCArray.of(\"Hello world\", 42143.432, 51451L, arraySub, false);\n" +
            "        sub.put(\"Array\", subArray);";
}
