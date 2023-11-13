import java.util.Scanner;

/*
"100" + "500"            -> "100500"
"Hi World!" - "World!"   -> "Hi "
"Bye-Bye!" - "World!"    -> "Bye-Bye!"
"Java" * 5               -> "JavaJavaJavaJavaJava"
"Example!!!" / 3         -> "Exa"
 */

public class StringCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите выражение: ");
        String inputStr = scanner.nextLine();
        // Проверяем что первый операнд строка и что после выражения нет пробела
        if (inputStr.charAt(0) != '\"' || inputStr.charAt(inputStr.length()-1) == ' ') {
            throw new RuntimeException("Введено не валидное выражение");
        }

        // Находим индекс математического оператора
        int indexMathSymbol = findIndexMathSymbol(inputStr);

        // Удоляем пробелы перед и после оператора, если такие есть
        String inputStrWithoutSpace = removeSpace(inputStr, indexMathSymbol);

        // Находим новый индекс оператора, так как длина строки меняется после удаления пробелов
        int newIndex = findIndexMathSymbol(inputStrWithoutSpace);

        // Находим переменные для выполнения операции
        char mathSymbol = inputStrWithoutSpace.charAt(newIndex);
        String subStr1 = checkSubstrings(inputStrWithoutSpace.substring(0, newIndex));
        String subStr2 = checkSubstrings(inputStrWithoutSpace.substring(newIndex+1));

        // Выполняем математическую операцию
        String resultStr;
        if (mathSymbol == '+') {
            resultStr = subStr1 + subStr2;
        } else if (mathSymbol == '-') {
            if (subStr1.endsWith(subStr2)) {
                resultStr = subStr1.substring(0, subStr1.length()-subStr2.length());
            } else {
                resultStr = subStr1;
            }
        } else if (mathSymbol == '*') {
            checkNumberFormat(subStr2);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Integer.parseInt(subStr2); i++) {
                sb.append(subStr1);
            }
            if (sb.length() > 40) {
                resultStr = sb.substring(0, 41) + "...";
            } else {
                resultStr = sb.toString();
            }
        } else {
            checkNumberFormat(subStr2);
            int newLength = subStr1.length() / Integer.parseInt(subStr2);
            resultStr = subStr1.substring(0, newLength);
        }

        System.out.println(resultStr);
    }

    private static void checkNumberFormat(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            throw new RuntimeException("При делении и умножении второй операнд должен быть числом");
        }
    }

    private static int findIndexMathSymbol(String inputStr) {
        int resIndex;
        if (inputStr.contains("\" +") || inputStr.contains("\"+")) {
            resIndex = findIndex(inputStr, '+');
        } else if (inputStr.contains("\" -") || inputStr.contains("\"-")) {
            resIndex = findIndex(inputStr, '-');
        } else if (inputStr.contains("\" *") || inputStr.contains("\"*")) {
            resIndex = findIndex(inputStr, '*');
        } else if (inputStr.contains("\" /") || inputStr.contains("\"/")) {
            resIndex = findIndex(inputStr, '/');
        } else {
            throw new RuntimeException("Математический знак не найден");
        }
        return resIndex;
    }

    private static int findIndex(String str, char mathSymbol) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\"' && str.charAt(i+1) == mathSymbol) {
                return i+1;
            }
            if (str.charAt(i) == '\"' && str.charAt(i+1) == ' ' && str.charAt(i+2) == mathSymbol) {
                return i+2;
            }
        }
        return -1;
    }

    private static String removeSpace(String inputStr, int indexMathSymbol) {
        StringBuilder sb = new StringBuilder();
        if (inputStr.charAt(indexMathSymbol-1) == ' ') {
            sb.append(inputStr, 0, indexMathSymbol-1);
        } else {
            sb.append(inputStr, 0, indexMathSymbol);
        }
        if (inputStr.charAt(indexMathSymbol+1) == ' ') {
            sb.append(inputStr.charAt(indexMathSymbol)).append(inputStr.substring(indexMathSymbol+2));
        } else {
            sb.append(inputStr.charAt(indexMathSymbol)).append(inputStr.substring(indexMathSymbol+1));
        }
        return sb.toString();
    }

    private static String checkSubstrings(String subStr) {
        if (subStr.contains("\"")) {
            String resultStr = subStr.replace("\"", "");
            if (resultStr.length() > 10) {
                throw new RuntimeException("Длина операндов больше 10 символов");
            }
            return resultStr;
        } else {
            if (Integer.parseInt(subStr) > 10) {
                throw new RuntimeException("Число больше 10");
            }
            return subStr;
        }
    }
}
