package com.wander.baseframe.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.wander.baseframe.BaseApp;

import java.util.Date;

public class PreferenceTool {
    private static SharedPreferences pref;

    private static SharedPreferences.Editor editor;

    private static final String PREF_NAME = "qiyireader";

    static {
        initialize();
        if (editor == null) {
            editor = pref.edit();
        }
    }

    private static void initialize() {
        pref = BaseApp.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean contains(String key) {
        return pref.contains(key);
    }


    public static boolean get(String key, boolean defValue) {
        return loadPrefBoolean(key, defValue);
    }

    public static int get(String key, int defValue) {
        return loadPrefInt(key, defValue);
    }

    public static long get(String key, long defValue) {
        return loadPrefLong(key, defValue);
    }

    public static String get(String key, String defValue) {
        return loadPrefString(key, defValue);
    }

    public static String get(String key) {
        return loadPrefString(key, null);
    }

    public static Object getObject(String key, Object defValue) {
        Gson gson = new Gson();
        Object result;
        String json = pref.getString(key, "");
        if (!"".equals(json) && json.length() > 0) {
            result = gson.fromJson(json, defValue.getClass());
        } else {
            result = defValue;
        }
        return result;
    }

    public static void put(String key, boolean value) {
        savePrefBoolean(key, value);
    }

    public static void put(String key, float value) {
        savePrefFloat(key, value);
    }

    public static void put(String key, int value) {
        savePrefInt(key, value);
    }

    public static void put(String key, long value) {
        savePrefLong(key, value);
    }

    public static void put(String key, String value) {
        savePrefString(key, value);
    }

    public static void putObject(String key, Object value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        commit();
    }

    public static void commit() {
        try {
            editor.apply();
        } catch (Throwable e) {
            e.printStackTrace();
            editor.commit();
        }
    }

    public static void remove(String key) {
        if (pref == null) {
            initialize();
        }
        if (editor == null) {
            editor = pref.edit();
        }
        editor.remove(key);
        commit();
    }

    public static String loadPrefString(String key, String defaultValue) {
        if (pref == null) {
            initialize();
        }
        String strRet = defaultValue;
        try {
            strRet = pref.getString(key, defaultValue);
        } catch (ClassCastException e) {
            // 一定是另一种类型
            strRet = defaultValue;

            boolean bLong = true;
            boolean bInt = true;
            boolean bBool = true;
            boolean bFloat = true;

            long lVal = 0;
            int iVal = 0;
            boolean bVal = false;
            float fVal = 0f;

            try {
                lVal = pref.getLong(key, 0);
            } catch (ClassCastException e2) {
                bLong = false;
                try {
                    iVal = pref.getInt(key, 0);
                } catch (ClassCastException e3) {
                    bInt = false;
                    try {
                        bVal = pref.getBoolean(key, false);
                    } catch (ClassCastException e4) {
                        bBool = false;
                        try {
                            fVal = pref.getFloat(key, 0f);
                        } catch (ClassCastException e5) {
                            bFloat = false;
                        }
                    }
                }
            }
            if (bLong) {
                strRet = Long.toString(lVal);
            } else if (bInt) {
                strRet = Integer.toString(iVal);
            } else if (bBool) {
                strRet = bVal ? "1" : "0";
            } else if (bFloat) {
                strRet = Float.toString(fVal);
            }
        }
        return strRet;
    }

    public static void savePrefString(String key, String value) {
        if (pref == null) {
            initialize();
        }
        if (editor == null) {
            editor = pref.edit();
        }
        editor.putString(key, value);
        commit();
    }

    public static int loadPrefInt(String key, int defaultValue) {
        if (pref == null) {
            initialize();
        }

        int iRet = defaultValue;
        try {
            iRet = pref.getInt(key, defaultValue);
        } catch (ClassCastException e) {
            // 一定是另一种类型
            iRet = defaultValue;

            boolean bStr = true;
            boolean bLong = true;
            boolean bBool = true;
            boolean bFloat = true;

            String strVal = "";
            long lVal = 0;
            boolean bVal = false;
            float fVal = 0f;

            try {
                strVal = pref.getString(key, "");
            } catch (ClassCastException e3) {
                bStr = false;
                try {
                    lVal = pref.getLong(key, 0);
                } catch (ClassCastException e2) {
                    bLong = false;
                    try {
                        bVal = pref.getBoolean(key, false);
                    } catch (ClassCastException e4) {
                        bBool = false;
                        try {
                            fVal = pref.getFloat(key, 0f);
                        } catch (ClassCastException e5) {
                            bFloat = false;
                        }
                    }
                }
            }

            if (bStr) {
                try {
                    iRet = Integer.parseInt(strVal);
                } catch (NumberFormatException e2) {
                    iRet = defaultValue;
                }
            } else if (bLong) {
                iRet = (int) (lVal);
            } else if (bBool) {
                iRet = bVal ? 1 : 0;
            } else if (bFloat) {
                iRet = (int) (fVal);
            }
        }

        return iRet;
    }

    public static void savePrefInt(String key, int value) {
        if (pref == null) {
            initialize();
        }
        if (editor == null) {
            editor = pref.edit();
        }
        editor.putInt(key, value);
        commit();
    }

    public static long loadPrefLong(String key, long defaultValue) {
        if (pref == null) {
            initialize();
        }

        long lRet = defaultValue;
        try {
            lRet = pref.getLong(key, defaultValue);
        } catch (ClassCastException e) {
            // 一定是另一种类型
            lRet = defaultValue;

            boolean bStr = true;
            boolean bInt = true;
            boolean bBool = true;
            boolean bFloat = true;

            String strVal = "";
            int iVal = 0;
            boolean bVal = false;
            float fVal = 0f;

            try {
                strVal = pref.getString(key, "");
            } catch (ClassCastException e2) {
                bStr = false;
                try {
                    iVal = pref.getInt(key, 0);
                } catch (ClassCastException e3) {
                    bInt = false;
                    try {
                        bVal = pref.getBoolean(key, false);
                    } catch (ClassCastException e4) {
                        bBool = false;
                        try {
                            fVal = pref.getFloat(key, 0f);
                        } catch (ClassCastException e5) {
                            bFloat = false;
                        }
                    }
                }
            }
            if (bStr) {
                try {
                    lRet = Long.parseLong(strVal);
                } catch (NumberFormatException e2) {
                    lRet = defaultValue;
                }

            } else if (bInt) {
                lRet = (long) (iVal);
            } else if (bBool) {
                lRet = bVal ? 1 : 0;
            } else if (bFloat) {
                lRet = (long) (fVal);
            }
        }

        return lRet;
    }

    public static void savePrefLong(String key, long value) {
        if (pref == null) {
            initialize();
        }
        if (editor == null) {
            editor = pref.edit();
        }
        editor.putLong(key, value);
        commit();
    }

    public static float loadPrefFloat(String key, float defaultValue) {
        if (pref == null) {
            initialize();
        }

        float fRet = defaultValue;
        try {
            fRet = pref.getFloat(key, defaultValue);
        } catch (ClassCastException e) {
            // 一定是另一种类型
            fRet = defaultValue;

            boolean bStr = true;
            boolean bLong = true;
            boolean bInt = true;
            boolean bBool = true;

            String strVal = "";
            long lVal = 0;
            int iVal = 0;
            boolean bVal = false;

            try {
                strVal = pref.getString(key, "");
            } catch (ClassCastException e1) {
                bStr = false;
                try {
                    lVal = pref.getLong(key, 0);
                } catch (ClassCastException e2) {
                    bLong = false;
                    try {
                        iVal = pref.getInt(key, 0);
                    } catch (ClassCastException e3) {
                        bInt = false;
                        try {
                            bVal = pref.getBoolean(key, false);
                        } catch (ClassCastException e4) {
                            bBool = false;
                        }
                    }
                }
            }

            if (bStr) {
                try {
                    fRet = Float.parseFloat(strVal);
                } catch (NumberFormatException e2) {
                    fRet = defaultValue;
                }

            } else if (bLong) {
                fRet = (float) lVal;
            } else if (bInt) {
                fRet = (float) iVal;
            } else if (bBool) {
                fRet = bVal ? 1.0f : 0.0f;
            }
        }

        return fRet;
    }

    public static void savePrefFloat(String key, float value) {
        if (pref == null) {
            initialize();
        }
        if (editor == null) {
            editor = pref.edit();
        }
        editor.putFloat(key, value);
        commit();
    }

    public static boolean loadPrefBoolean(String key, boolean defaultValue) {
        if (pref == null) {
            initialize();
        }

        boolean bRet = defaultValue;
        try {
            bRet = pref.getBoolean(key, defaultValue);
        } catch (ClassCastException e) {
            // 一定是另一种类型
            bRet = defaultValue;

            boolean bStr = true;
            boolean bLong = true;
            boolean bInt = true;
            boolean bFloat = true;

            String strVal = "";
            long lVal = 0;
            int iVal = 0;
            float fVal = 0f;

            try {
                strVal = pref.getString(key, "");
            } catch (ClassCastException e1) {
                bStr = false;
                try {
                    lVal = pref.getLong(key, 0);
                } catch (ClassCastException e2) {
                    bLong = false;
                    try {
                        iVal = pref.getInt(key, 0);
                    } catch (ClassCastException e3) {
                        bInt = false;
                        try {
                            fVal = pref.getFloat(key, 0f);
                        } catch (ClassCastException e4) {
                            bFloat = false;
                        }
                    }
                }
            }

            if (bStr) {
                strVal = strVal.toLowerCase();
                bRet = (strVal.equals("true") || strVal.equals("1"));
            } else if (bLong) {
                bRet = (lVal == 1);
            } else if (bInt) {
                bRet = (iVal == 1);
            } else if (bFloat) {
                bRet = !(fVal < 1e-14);
            }
        }
        return bRet;
    }

    public static void savePrefBoolean(String key, boolean value) {
        if (pref == null) {
            initialize();
        }
        if (editor == null) {
            editor = pref.edit();
        }
        editor.putBoolean(key, value);
        commit();
    }

    private static String generateDateKey(String prefsKey) {
        return prefsKey + "@data";
    }


    public static void putValueAndCurrentTime(String key, String value) {
        String dateKey = generateDateKey(key);
        int currentTime = (int) (System.currentTimeMillis() / 1000L);
        put(key, value);
        put(dateKey, currentTime);
    }

    public static String getValueIfValid(String key, String defValue, int validPeriod) {
        Date date = new Date();
        String value = getValueAndDate(key, defValue, date);
        return withinValidatePeriod(date, validPeriod) ? value : defValue;
    }

    private static boolean withinValidatePeriod(Date startDate, int validPeriod) {
        return startDate != null && startDate.getTime() + validPeriod * 1000L >= System.currentTimeMillis();
    }

    public static String getValueAndDate(String key, String defValue, Date outDate) {
        String dateKey = generateDateKey(key);
        int time = get(dateKey, -1);
        if (outDate != null) {
            outDate.setTime(time * 1000L);
        }
        String value = get(key, defValue);
        return value;
    }

}
