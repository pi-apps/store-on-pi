package com.api.service.db.mysql.util.beanToMap;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;


public class BeanMapUtil {

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param type 要转化的类型
     * @param map  包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InstantiationException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Object convertMap(Class type, Map map) {
        Object obj = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
            obj = type.newInstance(); // 创建 JavaBean 对象
            // 给 JavaBean 对象的属性赋值
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();

                if (map.containsKey(propertyName)) {
                    try {
                        Object value = map.get(propertyName);
                        Object[] args = new Object[1];
                        args[0] = value;
                        descriptor.getWriteMethod().invoke(obj, args);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {

        }
        return obj;
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean（map中包含多个实体）
     *
     * @param type      要转化的类型
     * @param map       包含属性值的 map
     * @param className 实体名
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InstantiationException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Object convertMap(Class type, Map map, String className)
            throws IntrospectionException, IllegalAccessException,
            InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        Object obj = type.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = className + "." + descriptor.getName();

            if (map.containsKey(propertyName)) {
                try {
                    Class<?>[] parameterTypes = descriptor.getWriteMethod().getParameterTypes();
                    if (null != parameterTypes && parameterTypes.length > 0) {
                        //获取方法的参数类型
                        String paraType = parameterTypes[0].getName();
                        //取得值
                        Object value = map.get(propertyName);
                        //值类型转换
                        Object[] args = getParaTypeConvert(paraType, value);
                        if (null != args) {
                            descriptor.getWriteMethod().invoke(obj, args);
                        }
                        //取值后，删除该key，减少下次匹配数
//            			map.remove(propertyName);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

    /**
     * 将一个 Map 对象转化为List<Object>（map中包含多个实体）
     *
     * @param type      要转化的类型
     * @param map       包含属性值的 map
     * @param className 实体名
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InstantiationException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static List convertMapToList(Class type, Map map, String className, int count)
            throws IntrospectionException, IllegalAccessException,
            InstantiationException, InvocationTargetException {
        List list = new ArrayList();
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int j = 0; j < count; j++) {
            Object obj = type.newInstance(); // 创建 JavaBean 对象
            //是否保存对象（如果取得的值都为空，则不保存）
            boolean isSave = false;
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = className + "." + descriptor.getName() + j;
                if (map.containsKey(propertyName)) {
                    try {
                        Class<?>[] parameterTypes = descriptor.getWriteMethod().getParameterTypes();
                        if (null != parameterTypes && parameterTypes.length > 0) {
                            //获取方法的参数类型
                            String paraType = parameterTypes[0].getName();
                            //取得值
                            Object value = map.get(propertyName);
                            //值类型转换
                            Object[] args = getParaTypeConvert(paraType, value);
                            if (!isSave && null != args && args.length > 0 && null != args[0] && !"".equals(args[0])) {
                                isSave = true;
                                descriptor.getWriteMethod().invoke(obj, args);
                            }
                            if (null != args && args.length > 0 && null != args[0] && !"".equals(args[0])) {
                                descriptor.getWriteMethod().invoke(obj, args);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (isSave) {
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * 将一个 JavaBean 对象转化为一个  Map
     *
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Map convertBean(Object bean) {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    returnMap.put(propertyName, result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnMap;
    }

    /**
     * 升序map,基本用于签名
     * @param bean
     * @return
     */
    public static TreeMap convertBeanAsc(Object bean) {
        Class type = bean.getClass();
        TreeMap returnMap = new TreeMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    returnMap.put(propertyName, result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnMap;
    }

    /**
     * 参数类型转换
     *
     * @param paraType
     * @param value
     * @return
     */
    public static Object[] getParaTypeConvert(String paraType, Object value) {
        Object[] args = new Object[1];
        if (null == value) {
            return null;
        }
        Object[] or = (Object[]) value;
        value = or[0];
        if (value.equals("")) {
            return null;
        }
        if (paraType.equals("java.util.Date")) {
            value = new Date(((Date) value).getTime());
        } else if (paraType.equals("java.lang.String")) {
            value = String.valueOf(value);
        } else if (paraType.equals("java.lang.Integer") || paraType.equals("int")) {
            value = Integer.valueOf(value.toString());
        } else if (paraType.equals("java.lang.Long")) {
            value = new Long(value.toString());
        } else if (paraType.equals("java.lang.Double")) {
            value = Double.valueOf(value.toString());
        } else if (paraType.equals("java.lang.Float")) {
            value = Float.valueOf(value.toString());
        } else if (paraType.equals("java.lang.Short")) {
            value = Short.valueOf(value.toString());
        } else if (paraType.equals("java.lang.Byte")) {
            value = Byte.valueOf(value.toString());
        } else if (paraType.equals("java.lang.Boolean")) {
            value = Boolean.valueOf(value.toString());
        } else if (paraType.equals("java.math.BigDecimal")) {
            value = new BigDecimal(value.toString());
        } else {
            value = value.toString();
        }
        args[0] = value;
        return args;
    }

    /**
     * 将Bean对象中的page.limit转成map作为查询对象
     */
    public static Map toPageQuery(Object bean) {
        Map returnMap = null;
        try {
            Class type = bean.getClass();
            returnMap = new HashMap();
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                //  String propertyType = descriptor.getPropertyType() + "";//类型
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if (result != null) {
                        if (propertyName.equals("page") || propertyName.equals("limit") || propertyName.equals("sidx") || propertyName.equals("order")) {
                            returnMap.put(propertyName, String.valueOf(result));
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        return returnMap;
    }

    /**
     * 实体转换
     *
     * @param bean 实体对象
     * @param type 需要转换的实体类
     * @return 转换完毕后的实体对象
     */
    public static Object convertEntity(Object bean, Class type) {
        Class classType = bean.getClass();
        Map returnMap = new HashMap();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(classType);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    returnMap.put(propertyName, result);
                }
            }
            return convertMap(type, returnMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}