package org.example.parser.allegro;

import org.example.parser.PageParser;
import org.example.parser.model.Item;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.Objects.isNull;

public class AllegroPageParser extends AllegroParser implements PageParser {

    private static final Logger LOGGER = Logger.getLogger(AllegroPageParser.class.getName());

    private final String locale;
    private final String resource;
    private final Map<String, String> localeUrlMap = Map.of(
      "pl", "https://allegro.pl/"
    );


    public AllegroPageParser(String locale) {
        this.locale = locale;
        this.resource = localeUrlMap.get(locale);
    }

    @Override
    public Elements extractCategories() {
        if (isNull(resource)) {
            LOGGER.warning("Can not find resource for locale " + locale);
            throw new RuntimeException();
        }
        Document document = AllegroParser.loadPage(resource);
        return document.select("optgroup[label=Kategorie] option");
    }

    @Override
    public Elements extractSubcategories(String groupUrl) {
        Document document = loadPage(groupUrl);
        return document.select("div*.opbox-sheet div[data-box-name*=all_category]>div[class$=js-navigation-links]>ul>li>a._w7z6o");
    }

    @Override
    public Item extractProductPage(String itemUrl, Map externalData) {
        Document document = loadPage(itemUrl);
        // TODO: implement
        String id = document.select("meta[itemProp=sku]").attr("content");
        String name = document.select("div[data-role=app-container] h1").first().text();
        // and so on...
        Item item = Item.builder()
                .id(id)
                .name(name)
                .url(itemUrl)
                .category((String) externalData.get("category"))
                .subCategory((String) externalData.get("subCategory"))
                .build();
        return item;
    }

}
