package Main;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class main {
    private static String toString(String url) throws MalformedURLException, IOException {
        Scanner in = new Scanner(new URL(url).openStream(), "UTF-8");
        String out = in.useDelimiter("\\A").next();
        in.close();
        return out;
    }

    private static File toFile(String url, String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                FileUtils.copyURLToFile(new URL(url), file);
            } catch (IOException e) {
                System.out.println("Invalid URL " + url);
            }
        }
        return file;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String username = in.nextLine();
        in.close();

        JsonParser parser = new JsonParser();
        JsonArray favourites = null;
        for (int page = 0; ; page += 50) {
            try {
                favourites = (JsonArray) parser.parse(toString("https://api.soundcloud.com/users/" + username + "/favorites?client_id=e7pCwTm0KONbBx2NoD5a0k3kP474wQnC&offset=" + page));
            } catch (IOException e) {
                System.out.println("bad user id");
            }
            for (int i = 0; i < favourites.size(); i++) {
                JsonObject favourite = (JsonObject) favourites.get(i);
                String title = favourite.get("title").toString().replace("\"", "");
                String url = favourite.get("stream_url").toString().replace("\"", "");
                toFile(url + "?client_id=e7pCwTm0KONbBx2NoD5a0k3kP474wQnC", "songs/" + title + ".mp3");
                System.out.println(title);
            }
            if (favourites.size() < 50) break;
        }
    }
}
