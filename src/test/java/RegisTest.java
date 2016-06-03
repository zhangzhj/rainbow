
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * Copyright (c) 2014 Qunar.com. All Rights Reserved.
 */

public class RegisTest {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("cmd stat|ERROR|Exception|WARN");
        Matcher matcher = pattern.matcher("ERROR");
        boolean b= matcher.matches();
        System.out.println("======="+b);
    }
}
