package net.rainbow.web.converter;

/**
*
* @author (sean)zhijun.zhang@gmail.com
*/
public class IntegerConverter implements Converter<Integer> {

    public Integer convert(String s) {
        return Integer.parseInt(s);
    }

}
