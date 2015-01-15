package com.m2dl.biodiversity.biodiversity;

import java.util.List;

/**
 * Created by Hugo on 15/01/2015.
 */
public class BioType {

    public String id;
    public List<String> soustypes;

    public BioType(String id, List<String> soustypes) {
        this.id = id;
        this.soustypes = soustypes;
    }

}
