package net.rainbow.web.converter;

/**
*
* @author (sean)zhijun.zhang@gmail.com
*/
public class LongConverter implements Converter<Long> {

    public Long convert(String s) {
        return Long.parseLong(s);
    }

}
