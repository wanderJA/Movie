package com.wander.baseframe.utils;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Zip operator tools
 */
public class ZipCRCUtils {

    private static final int _BUFFER_SIZE_CRC = 1024;
    private static final int _BUFFER_SIZE = 8192;

    private static final String FILE_SPLIT = "/";

    public static boolean checksumByCRC(File f, String crcValue) {
        if (TextUtils.isEmpty(crcValue))
            return false;
        byte[] buffer = getBytesBySize(f, _BUFFER_SIZE_CRC);
        if (null == buffer)
            return false;
        CRC32 crc32 = new CRC32();
        crc32.update(buffer);
        String value = toStr(Long.toHexString(crc32.getValue()).toUpperCase(), "");
        return value.equals(crcValue);
    }

    private static byte[] getBytesBySize(File f, int size) {
        if (null == f || !f.exists() || !f.canRead() || size > _BUFFER_SIZE_CRC) {
            return null;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            byte[] buffer = new byte[size];
            int readCount = fis.read(buffer);
            if (readCount > 0)
                return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static String getFromLastIndexToEnd(String string, String split, boolean containSplit) {
        return cutStringOfLastIndex(string, split, containSplit, false);
    }

    /**
     * Cut stirng base on last index.
     *
     * @param string
     * @param split
     * @param containSplit
     * @param fromStartToLastIndex
     * @return
     */
    private static String cutStringOfLastIndex(String string, String split, boolean containSplit, boolean fromStartToLastIndex) {
        if (TextUtils.isEmpty(string))
            return null;
        int index = string.lastIndexOf(split);
        if (index < 1)
            return string;
        return fromStartToLastIndex ? string.substring(0, containSplit ? index + 1 : index)
                : string.substring(containSplit ? index : index + 1);
    }

    public static boolean unzipToSelfPath(String zipPath) {
        if (TextUtils.isEmpty(zipPath))
            return false;
        return unzip(zipPath, zipPath2ZipDir(zipPath));
    }

    public static String zipPath2ZipDir(String zipPath) {
        return zipPath2ZipDir(zipPath, true);
    }

    public static String zipPath2ZipDir(String zipPath, boolean containsFileSeparator) {
        return toStr(zipPath.substring(0, zipPath.length() - ".zip".length()), "")
                + (containsFileSeparator ? File.separator : "");
    }

    public static boolean unzip(String zipPath, String unzipToPath) {
        if (TextUtils.isEmpty(zipPath) || TextUtils.isEmpty(unzipToPath))
            return false;
        File zF = new File(zipPath);
        if (!zF.exists())
            return false;
        File f = new File(unzipToPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        boolean r = true;
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(zF);
            Enumeration<? extends ZipEntry> emu = zipFile.entries();
            while (emu.hasMoreElements()) {
                ZipEntry entry = emu.nextElement();
                if (unzipAndWriteOneFile(zipFile, entry, unzipToPath) < -1) {
                    r = false;
                }
            }
            return r;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } finally {
            if (null != zipFile) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private static int unzipAndWriteOneFile(ZipFile zipFile, ZipEntry entry, String unzipPath) {
        if (null == entry)
            return -2;
        String name = toStr(getFromLastIndexToEnd(entry.getName(), FILE_SPLIT, false), "");
        if (TextUtils.isEmpty(name)) {
            return -1;
        }

        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(zipFile.getInputStream(entry));
            File file = new File(unzipPath + name);
            DebugLog.d("customMapPath", file.getAbsolutePath());
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos, _BUFFER_SIZE);
            int count;
            byte[] data = new byte[_BUFFER_SIZE];
            while ((count = bis.read(data, 0, _BUFFER_SIZE)) != -1) {
                bos.write(data, 0, count);
            }
            bos.flush();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        } finally {
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != bis) {
                try {
                    bis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static String toStr(Object _obj, String _defaultValue) {
        return null != _obj && !TextUtils.isEmpty(String.valueOf(_obj)) ? String.valueOf(_obj) : _defaultValue;
    }
}
