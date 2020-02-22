package com.bbo.gmall.manage.util;

import cn.hutool.core.collection.CollectionUtil;
import com.bbo.gmall.manage.bean.pms.PmsBaseAttrValue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompareListBeanUtil {


    /**
     *
     * 判断是否有效 第一判断字段是id,如果id无法判断则用第二判断字段进行判断
     * @param t
     * @param secondJudgeName
     * @param <T>
     * @return
     */
    private static <T> String judge(T t, String secondJudgeName){
        try{
            Class<? extends Object> tClass = t.getClass();
            //得到所有属性
            Field[] fields = tClass.getDeclaredFields();
            String id = null;
            String second = null;
            for (Field field : fields) {
                field.setAccessible(true);//设置可以访问私有变量
                String name = field.getName();//获取属性的名字

                if(name.equals("id")){
                    //将属性名字的首字母大写
                    name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
                    //整合出 getId() 属性这个方法
                    Method m = tClass.getMethod("get"+name);
                    //调用这个整合出来的get方法，强转成自己需要的类型
                    id = (String) m.invoke(t);
                    if(id != null){
                        break;
                    }
                }else if(name.equals(secondJudgeName)){
                    name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
                    //整合出 getId() 属性这个方法
                    Method m = tClass.getMethod("get"+name);
                    second = (String) m.invoke(t);
                }


            }
            //成功通过 T 泛型对象取到具体对象的 id ！
            return id == null ? second : id;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 高效比较法 基本上可以达到10000条数据 在30毫秒以内(亲测)
     * @param dbList 数据库中的List
     * @param newList 要更新的List
     * @return <T> Map<String,<T>> => [("insert",<T>),("update",<T>),("delete",<T>)]
     */
    public static <T> Map<String,List<T>> compare(List<T> dbList, List<T> newList, String secondJudgeName) {

        //        long start = System.currentTimeMillis();

        if(CollectionUtil.isEmpty(dbList)) dbList = new ArrayList<>();
        if(CollectionUtil.isEmpty(newList)) newList = new ArrayList<>();

        Map<String,T>  totalMap = new HashMap<>();

        List<T> update = new ArrayList<>();
        List<T> insert = new ArrayList<>();
        List<T> delete = new ArrayList<>();
        Map<String,Integer> insertMap = new HashMap<>();


        Map<String, Integer> map = new HashMap<>(dbList.size() + newList.size());

        List<T> maxList = dbList;
        List<T> minList = newList;
        boolean reverse = false;

        for (T t : dbList) {
            totalMap.put(judge(t,secondJudgeName),t);
        }

        for (T t : newList) {
            String key = judge(t,secondJudgeName);
            T p = totalMap.get(key);
            if(reverse && p != null) {
                totalMap.put(key, p); //
            }else{
                totalMap.put(key,t);
            }
        }

        if (newList.size() > dbList.size()) {
            maxList = newList;
            minList = dbList;
            reverse = true;
        }

        for (T t : maxList) {
            map.put(judge(t,secondJudgeName), 1);
        }

        for (T t : minList) {
            String key = judge(t,secondJudgeName);
            Integer count = map.get(key);
            if (count != null) {
                map.put(key, ++count);
                continue;
            }else{
                insertMap.put(key,1);
            }
            map.put(key, 1);
        }

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if(entry.getValue() > 1){
                update.add(totalMap.get(entry.getKey()));
            }else{ // == 1
                if(insertMap.get(entry.getKey()) == null){
                    delete.add(totalMap.get(entry.getKey()));
                }else{
                    insert.add(totalMap.get(entry.getKey()));
                }
            }
        }


        Map<String,List<T>> returnMap = new HashMap<>(3);
        returnMap.put("insert", reverse ? delete : insert);
        returnMap.put("delete", reverse ? insert : delete);
        returnMap.put("update", update);

        //        System.out.println("compare 耗时：" + (System.currentTimeMillis() - start) + " 毫秒");

        return returnMap;

    }

        public static void main(String[] args) {

            List<PmsBaseAttrValue> dbList = new ArrayList();

            List<PmsBaseAttrValue> newList = new ArrayList<>();


            PmsBaseAttrValue db1 = new PmsBaseAttrValue();
            PmsBaseAttrValue db2 = new PmsBaseAttrValue();
            PmsBaseAttrValue db3 = new PmsBaseAttrValue();
            PmsBaseAttrValue db4 = new PmsBaseAttrValue();
            PmsBaseAttrValue db5 = new PmsBaseAttrValue();
            PmsBaseAttrValue db6 = new PmsBaseAttrValue();

            db1.setId("1");
            db1.setValueName("dbName1");

            db2.setId("2");
            db2.setValueName("dbName2");

            db3.setId("3");
            db3.setValueName("dbName3");

            db4.setId("4");
            db4.setValueName("dbName4");

            db5.setId("5");
            db5.setValueName("dbName5");

            db6.setId("6");
            db6.setValueName("dbName6");

            dbList.add(db1);
            dbList.add(db2);
            dbList.add(db3);
            dbList.add(db4);
            dbList.add(db5);
            dbList.add(db6);

            PmsBaseAttrValue new1 = new PmsBaseAttrValue();
            PmsBaseAttrValue new2 = new PmsBaseAttrValue();
            PmsBaseAttrValue new3 = new PmsBaseAttrValue();
            PmsBaseAttrValue new4 = new PmsBaseAttrValue();

            new1.setId("1");
            new1.setValueName("newName1");
            new2.setValueName("newName2");
            new3.setValueName("dbName3");
            new4.setId("4");
            new4.setValueName("newName4");

            newList.add(new1);
            newList.add(new2);
            newList.add(new3);
            newList.add(new4);

            Map map = compare(dbList , newList,"valueName");
//            Map map1 = compare(newList ,dbList ,"valueName");


            System.out.println(map);
//            System.out.println(map1);
    }





}
