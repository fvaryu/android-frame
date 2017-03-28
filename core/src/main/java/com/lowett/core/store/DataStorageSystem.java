package com.lowett.core.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import com.lowett.core.Frame;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by Hyu on 2016/11/16.
 * Email: fvaryu@qq.com
 */

public abstract class DataStorageSystem implements IDataSystem {
    private static final String DEFAULT_FILE = "shared_prefs_storage_default";

    public static IDataSystem get() {
        return get(DEFAULT_FILE);
    }

    public static IDataSystem get(String fileName) {
        return DEFAULT.file(fileName);
    }

    private static IDataSystem DEFAULT = new DataStorageSystem() {
        private String fileName;

        @Override
        public IDataSystem file(String fileName) {
            this.fileName = fileName;
            return this;
        }

        @Override
        public int readInt(String key) {
            return (int) read(key, 0);
        }

        @Override
        public String readString(String key) {
            return (String) read(key, "");
        }


        @Override
        public float readFloat(String key) {
            return (float) read(key, 0f);
        }

        @Override
        public long readLong(String key) {
            return (long) read(key, 0L);
        }

        @Override
        public boolean readBoolean(String key) {
            return (boolean) read(key, false);
        }

        public boolean readBoolean(String key, boolean defaultValue) {
            return (boolean) read(key, defaultValue);
        }

        @Override
        public void clean() {
            sharedPreferences(fileName).edit().clear().apply();
        }

        @Override
        public void remove(String key) {
            sharedPreferences(fileName).edit().remove(key).apply();
        }


        Object read(String key, Object defaultValue) {
            if (defaultValue instanceof String) {
                return sharedPreferences(fileName).getString(key, (String) defaultValue);
            } else if (defaultValue instanceof Integer) {
                return sharedPreferences(fileName).getInt(key, (Integer) defaultValue);
            } else if (defaultValue instanceof Float) {
                return sharedPreferences(fileName).getFloat(key, (Float) defaultValue);
            } else if (defaultValue instanceof Boolean) {
                return sharedPreferences(fileName).getBoolean(key, (Boolean) defaultValue);
            } else if (defaultValue instanceof Long) {
                return sharedPreferences(fileName).getLong(key, (Long) defaultValue);
            }
            throw new RuntimeException("not support" + defaultValue.getClass());
        }

        /**
         * 1、将一个对象的所有字段以键值对的形式保存
         * 2、仅支持SharedPreferences支持的类型，double将装换为String
         * 3、以final修饰字段、对象的Object字段、枚举不会保存
         * @param value 对象
         */
        @Override
        public void write(String key, Object value) {
            if (key == null) {
                throw new RuntimeException("the key must be not null");
            }
            if (value == null) {
                throw new RuntimeException("the value must be not null,if you want to " +
                        "update or reset value to null, please call IDataSystem.remove(String)");
            }
            SharedPreferences.Editor editor = sharedPreferences(fileName).edit();
            if (value instanceof String) {
                editor.putString(key, (String) value).apply();
            } else if (value instanceof Integer) {
                editor.putInt(key, (Integer) value).apply();
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value).apply();
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value).apply();
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value).apply();
            } else if ("@object".equalsIgnoreCase(key)) {
                Field[] fields = value.getClass().getDeclaredFields();
                for (Field f : fields) {
                    if ((f.getModifiers() & Modifier.FINAL) == Modifier.FINAL ||
                            f.isEnumConstant()) {
                        continue;
                    }
                    f.setAccessible(true);
                    Object o = null;
                    try {
                        switch (f.getType().getName()) {
                            case "java.lang.String": {
                                o = f.get(value);
                            }
                            break;
                            case "java.lang.Long":
                            case "long": {
                                o = f.getLong(value);
                            }
                            break;
                            case "java.lang.Boolean":
                            case "boolean": {
                                o = f.getBoolean(value);
                            }
                            break;
                            case "java.lang.Float":
                            case "float": {
                                o = f.getFloat(value);
                            }
                            break;
                            case "java.lang.Integer":
                            case "int": {
                                o = f.getInt(value);
                            }
                            break;
                            case "java.lang.Double":
                            case "double": {
                                o = String.valueOf(f.getDouble(value));
                            }
                            break;
                        }
                    } catch (IllegalAccessException e) {
                        continue;
                    }
                    if (o == null) {
                        continue;
                    }
                    write(f.getName(), o);
                }
            } else {
                throw new RuntimeException("not support" + value.getClass());
            }
        }

        SharedPreferences sharedPreferences(String fileName) {
            if (TextUtils.isEmpty(fileName)) {
                throw new IllegalArgumentException("fileName name must be not null, " +
                        "please call DataStorageSystem.file(String) first!");
            }
            return Frame.getInstance().getAppContext()
                    .getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }

    };


}
