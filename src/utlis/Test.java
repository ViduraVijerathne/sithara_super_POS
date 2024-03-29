/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utlis;

/**
 *
 * @author vidur
 */
public class Test {

//    public String formatString(String name){
////      format String 
//    if name String length > 5 ?
//    1st Row String count is 5
//    other strings a 2nd row
//            example: name = "HelloWorld";
//            return "HelloW \n orld"
//    }
//    
//    public static String formatString(String name) {
//        // Format String 
//        if (name.length() > 5) {
//            // 1st Row String count is 5, other strings on a 2nd row
//            // example: name = "HelloWorld";
//            // return "HelloW \n orld"
//            return name.substring(0, 5) + " \n" + name.substring(5);
//        } else {
//            // If the length of the string is not greater than 5, return the string itself
//            return name;
//        }
//    }
//    public String formatString(String name) {
//        // Split the string by spaces
//        String[] words = name.split(" ");
//
//        // Process the first word
//        String firstWord = words[0].length() > 5 ? words[0].substring(0, 5) + " \n" : words[0].substring(0, 1).toUpperCase() + words[0].substring(1) + " ";
//
//        // Process subsequent words
//        StringBuilder formattedString = new StringBuilder(firstWord);
//        for (int i = 1; i < words.length; i++) {
//            if (name.length() > 5 && i > 0) {
//                formattedString.append(" \n");
//            }
//            formattedString.append(words[i]);
//            if (i < words.length - 1) {
//                formattedString.append(" ");
//            }
//        }
//        return formattedString.toString();
//    }
    public String formatString(String name) {
        // Split the string by spaces
        String[] words = name.split(" ");
        String returnWord = "";
        if (words[0].length() >= 5) {
            returnWord += words[0].substring(0,5)+" \n "+words[0].substring(5);
            for(int i  = 1 ; i < words.length; i++){
                returnWord += words[i]+" ";
            }
        }else{
            for(int i  = 0 ; i < words.length; i++){
                if(i == 0){
                    returnWord += words[i]+" \n ";
                }else{
                    returnWord += words[i]+" ";
                }
                
            }
        }
        
        return returnWord;
        
        

    }

    public static void main(String[] args) {
//        System.out.println(formatString("How Are You"));
//        
//         name = "How Are you" return "How \n Are you"
//                 name = "hello World" return "Hello world"
//                         name = "helloWorld" return "HelloW orld"
//                 
//        if 1st world have 5>char new line other words,
//                else if name String length > 5 ?
//    1st Row String count is 5
//    other strings a 2nd row
//            example: name = "HelloWorld";
//            return "HelloW \n orld"
//    }
        Test test = new Test();
        String[] names = {"How Are you", "hello World", "helloWorld","hello how you im fine thank you"};
        for (String name : names) {
            System.out.println("name = \"" + name + "\" return \"" + test.formatString(name) + "\"");
        }
    }

}
//
//run:
//name = "How Are you" return "How  
//Are you"
//name = "hello World" return "Hello  
//World"
//name = "helloWorld" return "hello 
//World"
