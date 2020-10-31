package org.example.parser.allegro;

import org.example.parser.ParserLauncher;
import org.example.parser.model.Item;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class AllegroParser extends ParserLauncher {

    private static final Logger LOGGER = Logger.getLogger(AllegroParser.class.getName());

    @Override
    public List<Item> parse(String locale) {
        LOGGER.info("Parser processing is started!");
        List<Item> items = new ArrayList<>();
        AllegroPageParser allegroPageParser = new AllegroPageParser("pl");
        Elements groups = allegroPageParser.extractCategories();
        for (Element group: groups) {
            Elements subcategories = allegroPageParser.extractSubcategories(group.absUrl("value"));
//            // here should be implementation for all categories extraction if you need it
//            for (Element subcategory: subcategories) {
//            }

            int[] randomindexes = {
                    (int)(Math.random()*subcategories.size()),
                    (int)(Math.random()*subcategories.size()),
                    (int)(Math.random()*subcategories.size())
            };
            for (Integer index: randomindexes) {
                Element subCategory = subcategories.get(index);
                String nextUrl = subCategory.absUrl("href");
                int extractedProducts = 0;
                while (nonNull(nextUrl)) {
                    Document listingPage = loadPage(nextUrl);
                    // selector for all item elements
                    Elements itemPageElements = listingPage.select("article[data-role=offer] div>div>h2>a[title]");
                    //filtered items according to task
                    List<Element> itemFilteredPegeElements =
                            itemPageElements.stream()
                            .filter(it -> nonNull(it.parent().parent().parent().select("div>span*._9c44d_KrRuv")))
                            .collect(Collectors.toList());

                    for (Element itemElement : itemFilteredPegeElements) {
                        // interrupt extraction with max amount
                        if (nonNull(getMaxProducts()) && extractedProducts >= getMaxProducts()) {
                            LOGGER.info(items.size() + " items were extracted");
                            LOGGER.info("Parser processing was interrupted!");
                            return items;
                        }
                        Map<String, String> externalData = Map.of(
                                "category", group.text(),
                                "subCategory", subCategory.text()
                        );
                        Item item = allegroPageParser.extractProductPage(itemElement.absUrl("href"), externalData);
                        if (nonNull(item.getId())) {
                            items.add(item);
                            extractedProducts++;
                        }
                    }
                    // pagination
                    Elements nextPage = listingPage.select("a[rel=next]");
                    if (nonNull(nextPage) && !nextPage.isEmpty()) {
                        nextUrl = listingPage.select("a[rel=next]").first().absUrl("href");
                    }
                }
            }
        }
        LOGGER.info(items.size() + " items were extracted");
        LOGGER.info("Parser processing is completed!");
        return items;
    }
}
