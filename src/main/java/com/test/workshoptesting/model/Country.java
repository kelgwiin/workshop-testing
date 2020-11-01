package com.test.workshoptesting.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    private String name;
    private Continent continent;
    private long population;
    private String label;

    public void classifyCountry() {
        if (continent == Continent.EUROPE) {
            label = "Old Continent";
        } else {
            label = "N/A";
        }
    }
}
