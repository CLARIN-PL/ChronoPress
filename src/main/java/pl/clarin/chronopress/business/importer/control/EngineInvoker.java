package pl.clarin.chronopress.business.importer.control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.clarin.chronopress.presentation.page.admin.datamanagment.ProcessSamplesStatusEvent;

@Singleton
public class EngineInvoker {

    private static final String nlprestURL = "http://ws.clarin-pl.eu/nlprest2/base/";

    @Inject
    javax.enterprise.event.Event<ProcessSamplesStatusEvent> statusEvent;

    private String nlpFileUpload(String fileName) throws IOException {
        return ClientBuilder.newClient().target(nlprestURL + "upload").request().
                post(Entity.entity(new File(fileName), MediaType.APPLICATION_OCTET_STREAM)).
                readEntity(String.class);
    }

    private void nlpFileDownload(String id, String path) throws IOException {
        URL url = new URL(nlprestURL + "download" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream is = conn.getInputStream();
            Files.copy(is, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
        } else {
            throw new IOException("Error downloading file");
        }
    }

    private String getRes(Response res) throws IOException {
        if (res.getStatus() != 200) {
            throw new IOException("Error in nlprest processing");
        }
        return res.readEntity(String.class);
    }

    private JSONArray nlpProcess(JSONObject request) throws IOException, InterruptedException {
        Client client = ClientBuilder.newClient();
        String taskid = client.target(nlprestURL + "startTask").request().
                post(Entity.entity(request.toString(), MediaType.APPLICATION_JSON)).readEntity(String.class);

        String status = "";
        JSONObject jsonres = new JSONObject();
        System.out.println(nlprestURL + "getStatus/" + taskid);
        while (!status.equals("DONE")) {

            String res = getRes(client.target(nlprestURL + "getStatus/" + taskid).request().get());
            //String res = getRes(client.target("http://ws.clarin-pl.eu/nlprest2/base/getStatus/2333c8d9-bdae-457c-96e6-265d875d95a9").request().get());
            jsonres = new JSONObject(res);

            status = jsonres.getString("status");
            if (status.equals("ERROR")) {
                throw new IOException("Error in processing :" + jsonres.getString("value"));
            }
            if (status.equals("PROCESSING")) {
                statusEvent.fire(new ProcessSamplesStatusEvent(String.format("Procesowanie NLP %.2f", jsonres.getDouble("value") * 100) + "%"));
            }

            if (status.equals("DONE")) {
                statusEvent.fire(new ProcessSamplesStatusEvent("Procesowanie NLP Zakończone!"));
            }
            Thread.sleep(500);
        }

        return jsonres.getJSONArray("value");

    }

    public void liner2(String name, String filepath) throws IOException, InterruptedException {

        String id = nlpFileUpload(name);
        JSONObject request = new JSONObject();
        request.put("user", "chronopress");

        request.put("lpmn", "filezip(" + id + ")|any2txt|wcrft2({\"morfeusz2\":false})|liner2({\"model\":\"all\"})|converter({\"type\":\"ccl2chronopress\"})");
        JSONArray results = nlpProcess(request);

        statusEvent.fire(new ProcessSamplesStatusEvent("Pobieranie przetworzonych plików ..."));
        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            nlpFileDownload(result.getString("fileID"), filepath + result.getString("name"));
        }
        statusEvent.fire(new ProcessSamplesStatusEvent("Pobieranie przetworzonych plików zakończone!"));
    }
}
