package com.bashellwang.refactdemo.storage.filemodule.custom;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bashellwang.refactdemo.storage.filemodule.apach.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by liang.wang on 2019/3/13.
 *
 * 自定义的
 */
public class FileHelper {

    /**
     * The number of bytes in a kilobyte.
     */
    public static final long ONE_KB = 1024;

    /**
     * The number of bytes in a megabyte.
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;

    /**
     * The number of bytes in a gigabyte.
     */
    public static final long ONE_GB = ONE_KB * ONE_MB;


    /**
     * 在指定目录创建文件：先判断目录是否存在， 不存在，则创建目录；再判断文件是否存在，不存在则创建文件
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return
     */
    public File newFile(@NonNull String filePath, @NonNull String fileName) {
        if (TextUtils.isEmpty(filePath) || TextUtils.isEmpty(fileName)) {
            return null;
        }

        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            StringBuilder sbFile = new StringBuilder(filePath);
            if (!filePath.endsWith(File.separator)) {
                sbFile.append(File.separator);
            }
            sbFile.append(fileName);
            File file = new File(sbFile.toString());
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;

        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 删除文件或目录：判断 File 类型，是文件直接删除；如果是目录，则递归删除
     *
     * @param filePath 目标路径
     */
    public void deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (file.exists()) {
            deleteFile(file);
        }
    }

    public void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }
            for (File f : file.listFiles()) {
                deleteFile(f);
            }
            file.delete();
        }
    }


    /**
     * 获取文件大小：如果是文件，直接返回文件大小
     *
     * @param filePath
     * @return
     */
    public long getFileSize(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return 0;
        }
        try {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                return file.length();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public long getFileSize(File file) {
        if (file == null) {
            return 0;
        }
        if (file.exists() && file.isFile()) {
            return file.length();
        }
        return 0;
    }

    public long getDirSize(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return 0;
        }
        return getDirSize(new File(dirPath));
    }

    /**
     * 获取目录大小
     *
     * @param dirFile
     * @return
     */
    public long getDirSize(File dirFile) {
        if (dirFile == null) {
            return 0;
        }
        long size = 0;
        try {
            //如果是目录则递归计算其内容的总大小
            if (dirFile.exists() && dirFile.isDirectory()) {
                File[] children = dirFile.listFiles();
                if (children == null || children.length == 0) {
                    // TODO
                    size += dirFile.length(); // todo
                }
                for (File f : children) {
                    if (f.isDirectory()) {
                        size += getDirSize(f);
                    } else if (f.isFile()) {
                        size += f.length();
                    }
                }
                return size;
            } else {
                return 0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return size;
    }

    /**
     * 拷贝文件到指定目录：目录不存在，则创建目录；拷贝文件
     *
     * @param sourceFilePath
     * @param targetDirPath
     */
    public void copyFile(String sourceFilePath, String targetDirPath) {
        if (TextUtils.isEmpty(sourceFilePath) || TextUtils.isEmpty(targetDirPath)) {
            return;
        }

        File sourceFile = new File(sourceFilePath);
        if (!sourceFile.exists()) {
            return;
        }

        File targetDir = new File(targetDirPath);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            File targetFile = new File(targetDirPath, sourceFile.getName());
            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024 * 4];
            int byteCount = 0;
            while ((byteCount = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
        } catch (Exception e) {

        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e1) {
                // ignore
            }
        }
    }

    /**
     * 拷贝目录到指定目录:目录不存在，则递归创建目录；递归拷贝文件
     *
     * @param sourceDirPath
     * @param targetDirPath
     */
    public void copyDir(String sourceDirPath, String targetDirPath) {
        if (TextUtils.isEmpty(sourceDirPath) || TextUtils.isEmpty(targetDirPath)) {
            return;
        }

        File soureDir = new File(sourceDirPath);
        if (!soureDir.exists() || !soureDir.isDirectory()) {
            return;
        }
        File[] childFiles = soureDir.listFiles();
        if (childFiles == null || childFiles.length == 0) {
            return;
        }

        File targetDir = new File(targetDirPath);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        // 递归拷贝
        for (File file : childFiles) {
            if (file.isDirectory()) {
                copyDir(file.getPath(), targetDirPath + File.separator + file.getName());// TODO
            } else {
                copyFile(file.getPath(), targetDirPath);
            }
        }
    }

    /**
     * 剪切文件到指定目录：目录不存在，则递归创建目录；拷贝文件，删除源文件
     *
     * @param sourceFilePath
     * @param targetDirPath
     */
    public void cutFile(String sourceFilePath, String targetDirPath) {
        if (TextUtils.isEmpty(sourceFilePath) || TextUtils.isEmpty(targetDirPath)) {
            return;
        }
        try {
            copyFile(sourceFilePath, targetDirPath);
            deleteFile(sourceFilePath);
        } catch (Exception e) {

        }
    }

    /**
     * 剪切目录到指定目录：如果目录不存在，递归创建目录；递归拷贝文件，删除源目录
     *
     * @param sourceDirPath
     * @param targetDirPath
     */
    public void cutDir(String sourceDirPath, String targetDirPath) {
        if (TextUtils.isEmpty(sourceDirPath) || TextUtils.isEmpty(targetDirPath)) {
            return;
        }
        try {
            copyDir(sourceDirPath, targetDirPath);
            deleteFile(sourceDirPath);
        } catch (Exception e) {

        }
    }

    public boolean renameFile(File file, String newName) {
        if (file == null) {
            return false;
        }
        if (!file.exists()) {
            return false;
        }
        if (TextUtils.isEmpty(newName)) {
            return false;
        }
        if (newName.equals(file.getName())) {
            return true;
        }
        File newFile = new File(file.getParent() + File.separator + newName);
        return !newFile.exists()
                && file.renameTo(newFile);
    }

    public boolean renameFile(String filePath, String newName) {

        if (TextUtils.isEmpty(filePath) || TextUtils.isEmpty(newName)) {
            return false;
        }
        return renameFile(getFileByPath(filePath), newName);
    }

    public File getFileByPath(final String filePath) {
        return TextUtils.isEmpty(filePath) ? null : new File(filePath);
    }

    //-----------------------------------------------------------------------

    /**
     * Opens a {@link FileInputStream} for the specified file, providing better
     * error messages than simply calling <code>new FileInputStream(file)</code>.
     * <p>
     * At the end of the method either the stream will be successfully opened,
     * or an exception will have been thrown.
     * <p>
     * An exception is thrown if the file does not exist.
     * An exception is thrown if the file object exists but is a directory.
     * An exception is thrown if the file exists but cannot be read.
     *
     * @param file the file to open for input, must not be <code>null</code>
     * @return a new {@link FileInputStream} for the specified file
     * @throws FileNotFoundException if the file does not exist
     * @throws IOException           if the file object is a directory
     * @throws IOException           if the file cannot be read
     * @since Commons IO 1.3
     */
    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canRead() == false) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }

    //-----------------------------------------------------------------------

    /**
     * Opens a {@link FileOutputStream} for the specified file, checking and
     * creating the parent directory if it does not exist.
     * <p>
     * At the end of the method either the stream will be successfully opened,
     * or an exception will have been thrown.
     * <p>
     * The parent directory will be created if it does not exist.
     * The file will be created if it does not exist.
     * An exception is thrown if the file object exists but is a directory.
     * An exception is thrown if the file exists but cannot be written to.
     * An exception is thrown if the parent directory cannot be created.
     *
     * @param file the file to open for output, must not be <code>null</code>
     * @return a new {@link FileOutputStream} for the specified file
     * @throws IOException if the file object is a directory
     * @throws IOException if the file cannot be written to
     * @throws IOException if a parent directory needs creating but that fails
     * @since Commons IO 1.3
     */
    public static FileOutputStream openOutputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && parent.exists() == false) {
                if (parent.mkdirs() == false) {
                    throw new IOException("File '" + file + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file);
    }

    /**
     * Returns a human-readable version of the file size, where the input
     * represents a specific number of bytes.
     *
     * @param size the number of bytes
     * @return a human-readable display value (includes units)
     */
    public static String byteCountToDisplaySize(long size) {
        String displaySize;

        if (size / ONE_GB > 0) {
            displaySize = String.valueOf(size / ONE_GB) + " GB";
        } else if (size / ONE_MB > 0) {
            displaySize = String.valueOf(size / ONE_MB) + " MB";
        } else if (size / ONE_KB > 0) {
            displaySize = String.valueOf(size / ONE_KB) + " KB";
        } else {
            displaySize = String.valueOf(size) + " bytes";
        }
        return displaySize;
    }

    //-----------------------------------------------------------------------
    /**
     * Compare the contents of two files to determine if they are equal or not.
     * <p>
     * This method checks to see if the two files are different lengths
     * or if they point to the same file, before resorting to byte-by-byte
     * comparison of the contents.
     * <p>
     * Code origin: Avalon
     *
     * @param file1  the first file
     * @param file2  the second file
     * @return true if the content of the files are equal or they both don't
     * exist, false otherwise
     * @throws IOException in case of an I/O error
     */
    public static boolean contentEquals(File file1, File file2) throws IOException {
        boolean file1Exists = file1.exists();
        if (file1Exists != file2.exists()) {
            return false;
        }

        if (!file1Exists) {
            // two not existing files are equal
            return true;
        }

        if (file1.isDirectory() || file2.isDirectory()) {
            // don't want to compare directory contents
            throw new IOException("Can't compare directories, only files");
        }

        if (file1.length() != file2.length()) {
            // lengths differ, cannot be equal
            return false;
        }

        if (file1.getCanonicalFile().equals(file2.getCanonicalFile())) {
            // same file
            return true;
        }

        InputStream input1 = null;
        InputStream input2 = null;
        try {
            input1 = new FileInputStream(file1);
            input2 = new FileInputStream(file2);
            return IOUtils.contentEquals(input1, input2);

        } finally {
            IOUtils.closeQuietly(input1);
            IOUtils.closeQuietly(input2);
        }
    }
}
