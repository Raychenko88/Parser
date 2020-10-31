package org.example.parser.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Item {

    private String id;
    private String name;
    private String priceRow;
    private Double price;
    private Double initPrice;
    private String currency;
    private String category;
    private String subCategory;
    private String url;

    @Override
    public String toString() {
        // TODO: implement
        return
                id + "," +
                name + "," +
                priceRow + "," +
                price + "," +
                initPrice + "," +
                currency + "," +
                url + "," +
                category + "," +
                subCategory + "\n";
    }
}
