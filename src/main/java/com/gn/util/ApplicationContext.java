package com.gn.util;

import com.gn.anotation.Autowired;
import com.gn.anotation.Component;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
    //存储实例化的容器
    private Map<String, Object> beanMap = new ConcurrentHashMap<>();

    //存储需要实例化的对象class全路径名
    private Set<String> classNameSet = new HashSet<>();

    public ApplicationContext() {
        try {
            init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化ioc容器
     */
    private void init() throws Exception {
        //1.扫描指定包下的所有类
        String beanScanPath = getBeanScanPath("ioc.bean.scan");
        //2.加载需要实例化的class
        loadBeanClass(beanScanPath);
        //3.实例化bean
        registerBean();
        //4.注入属性
        dependenceInjection();
        //5.注入属性后执行初始化方法 todo
    }

    /**
     * 依赖注入，简单来说就是将有Autowired注解的属性注入到实例化的bean中
     */
    private void dependenceInjection() throws Exception {
        if (beanMap.isEmpty()) {
            return;
        }
        for (Object o : beanMap.values()) {
            doInjection(o);
        }
    }

    private void doInjection(Object o) throws Exception {
        //获取所有的属性字段
        Field[] fields = o.getClass().getDeclaredFields();
        if (fields.length == 0) {
            return;
        }
        for (Field field : fields) {
            //判断是否有Autowired注解
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (autowired == null) {
                continue;
            }
            //得到beanName
            String beanName = autowired.value() == null || autowired.value().equals("") ?
                    IocUtil.toLowercaseIndex(field.getType().getSimpleName()) : autowired.value();
            //判断是否实例化，没有则实例化创建对象
            Class clazz = field.getType();
            //线程安全的写法
            Object object = beanMap.putIfAbsent(beanName, clazz.newInstance());
            field.setAccessible(true);
            field.set(o, object);
            //递归注入
            doInjection(beanMap.get(beanName));
        }
    }

    private void registerBean() throws Exception {
        if (classNameSet.isEmpty()) {
            return;
        }
        for (String className : classNameSet) {
            Class clazz = Class.forName(className);
            //判断是否有Component注解
            Component component = (Component) clazz.getAnnotation(Component.class);
            //如果没有注解，不需要实例化
            if (component == null) {
                continue;
            }
            //定义bean key的名称,如果没有value则使用类名首字母小写，有则使用value
            String beanName = component.value() == null || component.value().equals("") ?
                    IocUtil.toLowercaseIndex(clazz.getSimpleName()) : component.value();
            beanMap.put(beanName, clazz.newInstance());
        }
    }

    private void loadBeanClass(String packageName) {
        String filePath = packageName.replaceAll("\\.", "/");
        URL url = this.getClass().getClassLoader().getResource(filePath);
        File rootFile = new File(url.getFile());
        for (File file : rootFile.listFiles()) {
            //递归获取所有的class文件
            if (file.isDirectory()) {
                loadBeanClass(packageName + "." + file.getName());
            } else {
                classNameSet.add(packageName + "." + file.getName().replace(".class", ""));
            }
        }
    }

    private String getBeanScanPath(String path) {
        Properties properties = IocUtil.getPropertyByName("application.properties");
        return properties.getProperty(path);
    }

    public Object getBean(String beanName) {
        return beanMap.get(beanName);
    }
}
