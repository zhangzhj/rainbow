package net.rainbow.web.converter;

/**
 *
 * @author (sean)zhijun.zhang@gmail.com
 */
public class BooleanConverter implements Converter<Boolean> {

    public Boolean convert(String s) {
        return Boolean.parseBoolean(s);
    }

}
