package com.dab.zx.uitls;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import io.reactivex.Observable;

import static br.com.zbra.androidlinq.Linq.stream;
import static com.dab.zx.config.MyApp.context;

/**
 * Created by 八神火焰 on 2016/12/12.
 */

public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * 创建文件夹
     *
     * @param outputPath 输出路径
     *                   return 是否成功创建文件夹
     */
    public static boolean createDirectory(String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()) {
            boolean isCreated = new File(outputPath).mkdirs();
            LogUtils.i(TAG, "createDirectory->" + outputPath + ":" + isCreated);
            return isCreated;
        }
        return true;
    }

    /**
     * 获取文件下的文件总数
     *
     * @param path 路径
     */
    public static int getDirectorySize(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return 0;
        }
        return file.listFiles().length;
    }

    /**
     * 拷贝资源文件到指定目录
     *
     * @param resId      资源文件Id
     * @param parentPath 父级路径
     * @param childPath  子级路径
     * @param isCover    是否覆写
     */
    public static void copyRaw(int resId, String parentPath, String childPath, boolean isCover) {
        createDirectory(parentPath);
        File file = new File(childPath);

        try {
            InputStream in = context.getResources().openRawResource(resId);
            // 条件同时满足 压缩文件存在、压缩文件大小未变、不进行覆盖操作
            if (file.exists() && SpUtils.getInt(childPath) == SystemUtils.getSystemVersionCode() && !isCover) {
                in.close();
                return;
            }
            // 压缩文件存在执行删除操作
            if (file.exists()) {
                file.delete();
            }
            // 重新创建压缩文件
            file.createNewFile();
            FileOutputStream out    = new FileOutputStream(file);
            byte[]           buffer = new byte[2048];
            int              bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            in.close();
            out.close();
            SpUtils.putInt(childPath, SystemUtils.getSystemVersionCode());
            LogUtils.e(TAG, "copyRaw->Succeed");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "copyRaw->Failed:" + childPath);
        }
    }

    /**
     * 拷贝资源文件到指定目录
     *
     * @param fileName 资源文件名称
     * @param outPath  输出路径
     * @param isCover  是否覆写
     */
    public static Observable<Integer> copyAssets(String fileName, String outPath, boolean isCover) {
        return Observable.create(subscriber -> {
            createDirectory(new File(outPath).getParent());
            File file = new File(outPath);
            // 文件存在、版本正确、非覆写操作->跳过拷贝过程
            if (file.exists() && SpUtils.getInt(outPath) == SystemUtils.getSystemVersionCode() && !isCover) {
                subscriber.onComplete();
                return;
            }
            // 压缩文件存在执行删除操作
            if (file.exists()) {
                boolean isDelete = file.delete();
                LogUtils.d(TAG, "resourcesDelete->" + fileName + ":" + isDelete);
            }
            subscriber.onNext(0);
            try {
                InputStream in = context.getResources().getAssets().open(fileName);
                // 重新创建压缩文件
                long    available = in.available();
                boolean isCreate  = file.createNewFile();
                LogUtils.d(TAG, "resourcesCreate->" + fileName + ":" + isCreate);

                // 拷贝文件
                int              copyBytes   = 0; // 已拷贝长度
                float            copyPercent = 0; // 已拷贝十分比,取整范围(0-10)
                FileOutputStream out         = new FileOutputStream(file);
                byte[]           buffer      = new byte[2048];
                int              bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                    copyBytes += bytesRead;
                    float tempCopyPercent = (int)((float)copyBytes * 10 / (float)available); // 将拷贝百分比扩大10倍,取整范围(0-10)
                    if (tempCopyPercent > copyPercent) {
                        copyPercent = tempCopyPercent;
                        subscriber.onNext((int)copyPercent * 10); // 将拷贝十分比扩大10倍,,取整范围(0-100)
                    }
                }
                out.close();
                in.close();
                LogUtils.i(TAG, "copyAssets->" + fileName + ":" + true);
            } catch (Exception e) {
                subscriber.onError(e);
                LogUtils.e(TAG, "copyAssets->" + fileName + ":" + false);
            }
            subscriber.onComplete();
            // 版本信息存入Sp
            SpUtils.putInt(outPath, SystemUtils.getSystemVersionCode());
        });
    }

    /**
     * 获取资源文件内容
     *
     * @param fileName 资源文件名称
     */
    public static String getAssetsContent(String fileName) {
        String content = "";
        try {
            InputStream       inputStream       = context.getResources().getAssets().open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            char              input[]           = new char[inputStream.available()];
            inputStreamReader.read(input);
            content = new String(input);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.getMessage());
        }
        return content;
    }


    /**
     * 获取文件名称（不包含扩展名）
     *
     * @param path 文件路径
     * @return 文件名称
     */
    public static String getFileName(String path) {
        File file = new File(path);
        if (file.exists()) {
            String fileName = file.getName();
            return fileName.substring(0, fileName.lastIndexOf("."));
        }
        return "";
    }

    /**
     * 获取文件夹下所有顶级文件路径
     *
     * @param path 文件路径
     * @return 文件路径集合
     */
    public static List<String> getFilePathList(String path) {
        createDirectory(path);

        return stream(new File(path).listFiles()).select(File::getAbsolutePath).toList();
    }

    /**
     * 获取文件夹下所有顶级文件名称（包含扩展名）
     *
     * @param path 文件路径
     * @return 文件名称集合
     */
    public static List<String> getFileNameExList(String path) {
        createDirectory(path);

        return stream(new File(path).listFiles()).select(File::getName).toList();
    }

    /**
     * 获取文本内容
     *
     * @param path 文件路径
     * @return 文本内容
     */
    public static String getFileContent(String path) {
        File   file    = new File(path);
        String content = "";
        if (!file.exists()) {
            return content;
        }
        try {
            FileInputStream   fileInputStream   = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
            char              input[]           = new char[fileInputStream.available()];
            inputStreamReader.read(input);
            content = new String(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 拷贝文件
     *
     * @param fileNamePath 旧文件路径
     * @param newFilePath  新文件路径
     * @return true|false
     */
    public static boolean copyFile(String fileNamePath, String newFilePath) {
        try {
            FileInputStream  fileInputStream  = new FileInputStream(fileNamePath);
            FileOutputStream fileOutputStream = new FileOutputStream(newFilePath);
            byte             bt[]             = new byte[2048];
            int              c;
            while ((c = fileInputStream.read(bt)) > 0) {
                fileOutputStream.write(bt, 0, c); //将内容写到新文件当中
            }
            fileInputStream.close();
            fileOutputStream.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 重命名文件
     *
     * @param fileNamePath   原始文件路径
     * @param renameFilePath 熊文件路径
     * @return 操作结果
     */
    public static boolean renameFile(String fileNamePath, String renameFilePath) {
        File file       = new File(fileNamePath);
        File renameFile = new File(renameFilePath);
        return !renameFile.exists() && file.renameTo(renameFile);
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 操作结果
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.delete();
    }

    /**
     * 删除文件夹以及目录下的文件
     *
     * @param file 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static void deleteDirectory(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (File childFile : childFiles) {
                deleteDirectory(childFile);
            }
            file.delete();
        }
    }

    /**
     * 将内容写进文件
     *
     * @param content  文本
     * @param filePath 文件路径
     */
    public static boolean writeFile(String content, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[]           bytes            = content.getBytes();
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "writeFile->" + e.getMessage());
            return false;
        }
    }
}
