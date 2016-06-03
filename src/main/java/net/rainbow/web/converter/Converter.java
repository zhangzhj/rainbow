package net.rainbow.web.converter;

/**
*
* @author (sean)zhijun.zhang@gmail.com
*/
public interface Converter<T> {

    /**
     * Convert a not-null String to specified object.
     */
    T convert(String s);

}
