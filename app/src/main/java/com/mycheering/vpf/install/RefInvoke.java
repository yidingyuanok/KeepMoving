package com.mycheering.vpf.install;


 

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RefInvoke {

    public static Object invokeDexStaticMethod(Class<?> obj_class, String method_name, Class<?>[] pareTyple, Object[] pareVaules) {
        try {
            Method method = obj_class.getDeclaredMethod(method_name, pareTyple);
            method.setAccessible(true);
            return method.invoke(null, pareVaules);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeStaticMethod(String class_name, String method_name, Class<?>[] pareTyple, Object[] pareVaules) {
        try {
            Class<?> obj_class = Class.forName(class_name);
            Method method = obj_class.getDeclaredMethod(method_name, pareTyple);
            method.setAccessible(true);
            return method.invoke(null, pareVaules);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeStaticMethod(Class<?> obj_class, String method_name, Class<?>[] pareTyple, Object[] pareVaules) {
        try {
            Method method = obj_class.getDeclaredMethod(method_name, pareTyple);
            method.setAccessible(true);
            return method.invoke(null, pareVaules);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeMethod(String class_name, String method_name, Object obj, Class<?>[] pareTyple, Object[] pareVaules) {
        try {
            Class<?> obj_class = Class.forName(class_name);
            Method method = obj_class.getDeclaredMethod(method_name, pareTyple);
            method.setAccessible(true);
            return method.invoke(obj, pareVaules);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeMethod(Class<?> obj_class, String method_name, Object obj, Class<?>[] pareTyple, Object[] pareVaules) {
        try {
            Method method = obj_class.getDeclaredMethod(method_name, pareTyple);
            method.setAccessible(true);
            return method.invoke(obj, pareVaules);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeMethodWithException(Class<?> obj_class, String method_name,
                                                   Object obj, Class<?>[] pareTyple,
                                                   Object[] pareVaules) throws Exception {
        Method method = obj_class.getDeclaredMethod(method_name, pareTyple);
        method.setAccessible(true);
        return method.invoke(obj, pareVaules);
    }

    public static Object getFieldOjbect(String class_name, Object obj, String filedName) {
        try {
            Class<?> obj_class = Class.forName(class_name);
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static Object getFieldOjbect(Class<?> obj_class, Object obj, String filedName) {
        try {
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

//    public static Object getSuperFieldOjbect(Class<?> obj_class, Object obj, String filedName) {
//        Object field = getFieldOjbect(obj_class, obj, filedName);
//        Class<?> cls = obj_class;
//        if (field == null) {
//            while (!cls.equals(BaseInfo.class)) {
//                cls = cls.getSuperclass();
//                field = getFieldOjbect(cls, obj, filedName);
//                if (field != null) return field;
//            }
//        }
//        return field;
//    }

    public static Object getStaticFieldOjbect(String class_name, String filedName) {
        try {
            Class<?> obj_class = Class.forName(class_name);
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(null);
        } catch (SecurityException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static Object getStaticFieldOjbect(Class<?> obj_class, String filedName) {
        try {
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(null);
        } catch (SecurityException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void setFieldOjbect(String classname, String filedName, Object obj, Object filedVaule) {
        try {
            Class<?> obj_class = Class.forName(classname);
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(obj, filedVaule);
        } catch (SecurityException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void setFieldOjbect(Class<?> obj_class, String filedName, Object obj, Object filedVaule) {
        try {
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(obj, filedVaule);
        } catch (SecurityException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void setStaticOjbect(String class_name, String filedName, Object filedVaule) {
        try {
            Class<?> obj_class = Class.forName(class_name);
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(null, filedVaule);
        } catch (SecurityException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void setStaticOjbect(Class<?> obj_class, String filedName, Object filedVaule) {
        try {
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(null, filedVaule);
        } catch (SecurityException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            //     Auto-generated catch block
            e.printStackTrace();
        }
    }
}
