package com.zx.uitls;

import com.zx.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import io.reactivex.Observable;

import static com.zx.config.MyApp.context;

/**
 * Created by 八神火焰 on 2016/12/12.
 */

public class ZipUtils
{
    private static final String TAG = ZipUtils.class.getSimpleName();

    /**
     * 解压文件到指定路径
     *
     * @param assetsName    资源文件名称
     * @param directoryPath 解压后的文件夹路径
     * @param isCover       是否覆盖
     */
    public static Observable<Integer> unZipFolder(String assetsName, String directoryPath, boolean isCover) {
        return Observable.create(subscriber -> {
            int zipCount = getZipSize(directoryPath + context.getString(R.string.zip_extension)) - 1; // PS:-1是因为存在文件夹
            if (zipCount == FileUtils.getFileSize(directoryPath)) {
                subscriber.onComplete();
                return;
            }

            int            count     = 0;
            String         unZipPath = new File(directoryPath).getParent() + File.separator;
            String         szName;
            ZipEntry       zipEntry;
            ZipInputStream inZip     = null;
            try {
                subscriber.onNext(0);
                inZip = new ZipInputStream(context.getResources().getAssets().open(assetsName));
                while ((zipEntry = inZip.getNextEntry()) != null) {
                    szName = zipEntry.getName();
                    if (zipEntry.isDirectory()) {
                        szName = szName.substring(0, szName.length() - 1);
                        FileUtils.createDirectory(unZipPath + szName);
                    } else {
                        unZipFile(unZipPath + szName, inZip, isCover);
                    }
                    if (++count % 20 == 0) {
                        subscriber.onNext(count * 100 / zipCount);
                    }
                }
                subscriber.onComplete();
                inZip.close();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    /**
     * 解压单个文件
     *
     * @param filePath    解压路径
     * @param inputStream 文件流
     */
    private static void unZipFile(String filePath, ZipInputStream inputStream, boolean isCover) {
        try {
            int  len;
            File file = new File(filePath);
            if (file.exists() && isCover) {
                file.delete();
            }
            if (!file.exists()) {
                file.createNewFile();
                FileOutputStream out    = new FileOutputStream(file);
                byte[]           buffer = new byte[2048];
                while ((len = inputStream.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.close();
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.getMessage());
        }
    }

    /**
     * 获取Zip大小
     *
     * @param path Zip路径
     * @return size
     */
    private static int getZipSize(String path) {
        int zipSize = 0;
        try {
            ZipFile zipFile = new ZipFile(new File(path));
            zipSize = zipFile.size();
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipSize;
    }
}
