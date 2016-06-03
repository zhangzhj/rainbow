package net.rainbow.web.converter;
/**
*
* @author (sean)zhijun.zhang@gmail.com
*/
public class ByteConverter implements Converter<Byte> {

    public Byte convert(String s) {
        return Byte.parseByte(s);
    }

}
