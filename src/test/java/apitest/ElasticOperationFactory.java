package apitest;

import api.ElasticOperation;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElasticOperationFactory {

    private List<ElasticOperation> eloList = new ArrayList<>();

    public ElasticOperation generate() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(Contant.HOSTNAME, Contant.PORT, Contant.SCHEME)
                )
        );

        ElasticOperation elo = new ElasticOperation();
        elo.setClient(client);

        eloList.add(elo);

        return elo;
    }

    public void releaseAll() {
        for (ElasticOperation elo : eloList) {
            release(elo);
        }
    }

    public void release(ElasticOperation elo) {
        try {
            elo.getClient().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
