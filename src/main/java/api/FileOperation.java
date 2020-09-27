package api;

import domain.FileObj;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class FileOperation {

    public FileObj readFile(String path) throws IOException {
        //读文件
        File file = new File(path);
        FileObj fileObj = new FileObj();

        fileObj.setName(file.getName());
        fileObj.setType(file.getName().substring(file.getName().lastIndexOf(".") + 1));

        byte[] bytes = getContent(file);

        //将文件内容转化为base64编码
        String base64 = Base64.getEncoder().encodeToString(bytes);
        fileObj.setContent(base64);

        return fileObj;
    }


    /**
     * 这个方法将一个目录下所有的文件读入然后全部上传
     * @return
     */
    public List<FileObj> readFileByDir(String path) throws IOException {
        List<FileObj> fileObjs = new ArrayList<>();
        File file = new File(path);

        if (!file.isDirectory()) return fileObjs;

        File[] files = file.listFiles();
        for (File f: files) {
            //System.out.println(f.getAbsoluteFile());
            FileObj fileObj = readFile(f.getAbsolutePath());
            fileObjs.add(fileObj);
        }

        return fileObjs;

    }

    private byte[] getContent(File file) throws IOException {

        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        fi.close();
        return buffer;
    }
}
