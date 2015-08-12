package no.auke.drone.utils;

import no.auke.drone.annotation.Column;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by huyduong on 8/7/2015.
 */
public class ReflectionUtils {
    private ReflectionUtils() {};

    public static Field[] getAllColumnFields(Class T) {
        List<Field> fields = new ArrayList<Field>();
        fields.addAll(Arrays.asList(T.getDeclaredFields()));
        if (T.getSuperclass() != null) {
            fields.addAll(Arrays.asList(getAllColumnFields(T.getSuperclass())));
        }

        Iterator<Field> iterator = fields.iterator();
        while(iterator.hasNext()) {
            Field field = iterator.next();
            if(!field.isAnnotationPresent(Column.class)) {
                iterator.remove();
            }
        }
        return fields.toArray(new Field[] {});
    }
}
