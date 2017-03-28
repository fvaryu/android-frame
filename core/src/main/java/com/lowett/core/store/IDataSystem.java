package com.lowett.core.store;


/**
 * Created by Hyu on 2016/11/16.
 * Email: fvaryu@qq.com
 */

public interface IDataSystem {

    IDataSystem file(String name);

    void write(String key, Object value);

    int readInt(String key);

    String readString(String key);

    float readFloat(String key);

    long readLong(String key);

    boolean readBoolean(String key);

    boolean readBoolean(String key, boolean d);

    void clean();

    void remove(String key);


}
