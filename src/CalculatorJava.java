import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.script.ScriptException;



public class CalculatorJava {
        public static void main(String[] args) throws IOException, ScriptException {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Калькулятор умеет выполнять операции сложения, вычитания, \n" +
                    "умножения и деления с двумя целыми арабскими или с римскими числами, \nот 1 до 10 включительно a + b," +
                    " a - b, a * b, a / b");

            while (true) {
                System.out.println("Введите выражение: ");
                String st = br.readLine().trim();
                if (st.isEmpty() == true) {
                    System.out.println("Строка пустая");
                } else {
                    System.out.println("Вы ввели выражение: " + st);
                    try {
                        ParsingExpression parExpr = new ParsingExpression(st);
                        if (parExpr.arabic) {
                            System.out.println("Результат : " + calc(parExpr.value_1, parExpr.value_2, parExpr.operator));
                        } else {
                            System.out.println("Результат : " + arabToRoman(calc(parExpr.value_1, parExpr.value_2, parExpr.operator)));
                        }
                    } catch (RomanAndArabic e) {
                        System.err.println(e.getMessage());
                        break;
                    }
                }
            }
        }

        private static String arabToRoman(int num) {
            String m[] = {"", "M", "MM", "MMM"};
            String c[] = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
            String x[] = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
            String i[] = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
            String thousands = m[num / 1000];
            String hundereds = c[(num % 1000) / 100];
            String tens = x[(num % 100) / 10];
            String ones = i[num % 10];
            return thousands + hundereds + tens + ones;
        }

    public static int calc(int num1, int num2, char operation) {
        int result = 0;
        switch (operation) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                result = num1 / num2;
                break;
            default:
                result = 0;
        }
        return result;
    }
}

class ParsingExpression {
    ParsingExpression(String text) throws RomanAndArabic {
        equation = text;
        romanian();
    }

    String equation;
    int value_1, value_2;
    boolean arabic = true;
    boolean dataError = false;
    char operator;

    void romanian() throws RomanAndArabic {
        String[] nums = equation.toUpperCase().split("[+-/*]");
        String romPat = "IVXLCDM";
        if (nums.length != 2) {
            throw new RomanAndArabic("Вы ввели некорректное выражение");
        }
        if (nums[0].trim().charAt(0) == '0' || nums[1].trim().charAt(0) == '0') {
            throw new RomanAndArabic("Числа на вход от 1 до 10 включительно," +
                    " вы ввели : " + 0);
        }
        if ((romPat.indexOf(nums[0].trim().charAt(0)) != -1) && (romPat.indexOf(nums[1].trim().charAt(0)) != -1)) {
            value_1 = romanToArabic(nums[0].trim());
            value_2 = romanToArabic(nums[1].trim());
            arabic = false;

        } else if ((romPat.indexOf(nums[0].trim().charAt(0)) != -1) || (romPat.indexOf(nums[1].trim().charAt(0)) != -1)) {
//            try {
            throw new RomanAndArabic("Ошибка ввода  одна цифра римская другая арабская");
            //            } catch (Exception e) {
//                System.err.println(e.getMessage());
//                dataError = true;
//            }


        } else {
            value_1 = Integer.valueOf(nums[0].trim());
            value_2 = Integer.valueOf(nums[1].trim());
        }

        if (!(0 < value_1 && value_1 <= 10) || !(0 < value_2 && value_2 <= 10)) {
            throw new RomanAndArabic("Числа на вход числа от 1 до 10 включительно," +
                    " вы ввели : " + value_1 + " и " + value_2);
        }
        operator = equation.charAt(nums[0].length());
    }

    private static int value(char r) {
        if (r == 'I')
            return 1;
        if (r == 'V')
            return 5;
        if (r == 'X')
            return 10;
        if (r == 'L')
            return 50;
        if (r == 'C')
            return 100;
        if (r == 'D')
            return 500;
        if (r == 'M')
            return 1000;
        return -1;
    }

    private static int romanToArabic(String str) {
        int res = 0;
        for (int i = 0; i < str.length(); i++) {
            int s1 = value(str.charAt(i));
            if (i + 1 < str.length()) {
                int s2 = value(str.charAt(i + 1));
                if (s1 >= s2) {
                    res = res + s1;
                } else {
                    res = res + s2 - s1;
                    i++;
                }
            } else {
                res = res + s1;
                i++;
            }
        }
        return res;

    }
}
class RomanAndArabic extends Exception {
    RomanAndArabic(String  name){super(name);}
    }
