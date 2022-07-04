package se.mickelus.mutil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public class Perks {
    private static final Logger logger = LogManager.getLogger();
    private static volatile PerkData data;

    public static void init(String uuid) {
        try {
            Gson gson = new GsonBuilder().create();
            HttpRequest request = HttpRequest.newBuilder(new URI("https://mickelus.se/util/perks/" + uuid.replace("-", "")))
                    .header("Accept", "application/json")
                    .build();
            logger.info("Sending perk request...");
            HttpClient.newHttpClient()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(body -> gson.fromJson(body, PerkData.class))
                    .thenAccept(Perks::setData)
                    .get();
        } catch (URISyntaxException | ExecutionException | InterruptedException e) {
            logger.error("Failed to get perk data");
            e.printStackTrace();
            data = new PerkData();
        }
    }

    public static synchronized PerkData getData() {
        return data;
    }

    private static synchronized void setData(PerkData newData) {
        logger.info("Got new perk data " + newData);
        data = newData;
    }

    static class PerkData {
        public int support;
        public int contribute;
        public int community;
        public int moderate;

        @Override
        public String toString() {
            return "PerkData{" +
                    "support=" + support +
                    ", contribute=" + contribute +
                    ", community=" + community +
                    ", moderate=" + moderate +
                    '}';
        }
    }
}
