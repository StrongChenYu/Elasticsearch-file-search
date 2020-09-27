package apitest;

import api.ElasticOperation;
import api.FileOperation;
import com.alibaba.fastjson.JSON;
import domain.FileObj;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.recycler.Recycler;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class FileTest {

    private ElasticOperationFactory eloFactory;
    private String path;
    private FileOperation fileOperation;
    private String dirPath;

    @Before
    public void initClient() {
        eloFactory = new ElasticOperationFactory();
        path = Contant.PATH;
        dirPath = Contant.DIRPATH;
        fileOperation = new FileOperation();
    }

    @After
    public void releaseSource() {
        eloFactory.releaseAll();
    }

    @Test
    public void fileReadTest() throws IOException {
        FileObj fileObj = fileOperation.readFile(path);
        //System.out.println(fileObj);
        System.out.println(fileObj.getName());
        System.out.println(JSON.toJSONString(fileObj));
        Assert.assertFalse("byte 不为空", fileObj.getContent().isEmpty());
    }

    /**
     * 运行前请先设置Constant中的PATH变量，这个函数测试的是单个文件，所以path代表的测试的那个具体的文件
     * 先将读入的文件转化为base64编码，然后上传到elasticsearch服务器
     * @throws IOException
     */
    @Test
    public void fileUploadTest() throws IOException {
        ElasticOperation elo = eloFactory.generate();

        //先将文件读入内存之后，再上传
        FileObj fileObj = fileOperation.readFile(path);
        elo.upload(fileObj);
    }

    /**
     * 运行前请先设置Constant中的DIRPATH变量，这会将这个目录下的所有文件全部上传到elasticsearch
     * 但是由于没有多线程，可能会上传慢
     * @throws IOException
     */
    @Test
    public void dirUploadTest() throws IOException {
        List<FileObj> files = fileOperation.readFileByDir(dirPath);
        ElasticOperation elo = eloFactory.generate();

        for (FileObj fileObj : files) {
            elo.upload(fileObj);
        }
    }

    /**
     * 这部分会根据输入的关键字去查询数据库中的信息，然后返回对应的结果
     * @throws IOException
     */
    @Test
    public void fileSearchTest() throws IOException {
        ElasticOperation elo = eloFactory.generate();

        elo.search("数据库国务院计算机网络");
    }
//    @Test
//    public void file
}
