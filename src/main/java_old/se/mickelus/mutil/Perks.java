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
    private static volatile Data data;

    public static void init(String uuid) {
        if (!ConfigHandler.client.queryPerks.get()) {
            logger.info("Perks query disabled, skipping fetch!");
            data = new Data();
            return;
        }
        try {
            Gson gson = new GsonBuilder().create();
            HttpRequest request = HttpRequest.newBuilder(new URI("https://mickelus.se/util/perks/" + uuid.replace("-", "")))
                    .header("Accept", "application/json")
                    .build();
            HttpClient.newHttpClient()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(body -> gson.fromJson(body, Data.class))
                    .thenAccept(Perks::setData)
                    .get();
        } catch (URISyntaxException | ExecutionException | InterruptedException e) {
            logger.warn("Failed to get perk data: " + e.getMessage());
            data = new Data();
        }
    }

    public static synchronized Data getData() {
        return data;
    }

    private static synchronized void setData(Data newData) {
        data = newData;
    }

    public static class Data {
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
