package net.rainbow.web.converter;

/**
*
* @author (sean)zhijun.zhang@gmail.com
*/
public class FloatConverter implements Converter<Float> {

    public Float convert(String s) {
        return Float.parseFloat(s);
    }

}
