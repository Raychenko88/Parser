package org.example.parser;

import lombok.Getter;
import lombok.Setter;
import org.example.parser.model.Item;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Setter
@Getter
public abstract class ParserLauncher {

    private static final Logger LOGGER = Logger.getLogger(ParserLauncher.class.getName());
    private Integer maxProducts;

    public abstract List<Item> parse(String locale);

    public static Document loadPage(String url){
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.severe("Page was not loaded.");
        throw new RuntimeException();
    }

    public static Document loadPageWithHeaders(String url, Map headers){
        try {
            Connection connection = Jsoup.connect(url);
            connection.headers(headers);
            return connection.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.severe("Page was not loaded.");
        throw new RuntimeException();
    }

    public static Document loadPageWithHeadersAndCookies(String url, Map headers, Map cookies){
        try {
            Connection connection = Jsoup.connect(url);
            connection.headers(headers);
            connection.cookies(cookies);
            return connection.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.severe("Page was not loaded.");
        throw new RuntimeException();
    }

}
