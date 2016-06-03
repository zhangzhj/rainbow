package net.rainbow.web.converter;

/**
*
* @author (sean)zhijun.zhang@gmail.com
*/
public class DoubleConverter implements Converter<Double> {

    public Double convert(String s) {
        return Double.parseDouble(s);
    }

}
