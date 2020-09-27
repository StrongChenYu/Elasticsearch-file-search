package domain;


import java.util.HashMap;
import java.util.Map;

public class FileObj {
    String id;
    String name;
    String type;
    String content;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id",id);
        map.put("name",name);
        map.put("type",type);
        map.put("content",content);
        return map;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "FileObj{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
