# 使用Elasticsearch实现的一个对word，pdf，txt文件的全文内容检索的简单Demo

依赖版本

Elasticsearch-7.9.1

博客：[https://www.cnblogs.com/strongchenyu/p/13777596.html](https://www.cnblogs.com/strongchenyu/p/13777596.html)

打开`Test`目录，请运行前修改变量为自己的变量

```java
public void dirUploadTest() throws IOException {
    List<FileObj> files = fileOperation.readFileByDir(dirPath);
    ElasticOperation elo = eloFactory.generate();

    for (FileObj fileObj : files) {
        elo.upload(fileObj);
    }
}
```
这段代码可以将对应目录下的文件上传至`Elasticsearch`服务器。

```java
@Test
public void fileSearchTest() throws IOException {
    ElasticOperation elo = eloFactory.generate();

    elo.search("数据库国务院计算机网络");
}
```
这段代码可以根据上面上传的文件搜索到对应的结果。
