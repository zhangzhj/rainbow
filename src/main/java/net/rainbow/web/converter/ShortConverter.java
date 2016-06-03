package net.rainbow.web.converter;

/**
*
* @author (sean)zhijun.zhang@gmail.com
*/
public class ShortConverter implements Converter<Short> {

    public Short convert(String s) {
        return Short.parseShort(s);
    }

}
