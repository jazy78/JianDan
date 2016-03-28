package com.example;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * GreenDao生成器
 */

public class MyGreenDao {

    //辅助文件生成的相对路径
    public static final String DAO_PATH = "app/src/main/java-gen";
    //辅助文件的包名
    public static final String PACKAGE_NAME = "com.source.greendao";
    //数据库的版本号
    public static final int VERSION_CODE = 1;

    public static void main(String[] arg) throws Exception {
      /**  你创建了一个用于添加实体（Entity）的模式（Schema）对象。
        两个参数分别代表：数据库版本号与自动生成代码的包路径。*/
        Schema schema=new Schema(VERSION_CODE,PACKAGE_NAME);
        //用对象添加实体表名,获得实体，
        addCache(schema,"FreshNewsCache");
        addCache(schema, "PictureCache");
        addCache(schema, "SisterCache");
        addCache(schema, "JokeCache");
        addCache(schema, "VideoCache");

        //这一步生成 相应的 Cache ，CacheDao ,DaoSession,DaoMaster 文件
        new DaoGenerator().generateAll(schema,DAO_PATH);


    }

    private static  void addCache(Schema schema,String tablename){

        Entity entity=schema.addEntity(tablename);

        //添加表的列项
        entity.addIdProperty().primaryKey().autoincrement();
        entity.addStringProperty("result");
        entity.addIntProperty("page");
        entity.addLongProperty("time");

    }

}
