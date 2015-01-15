package com.m2dl.biodiversity.biodiversity;

import java.util.List;

public class BioType {

    public String id;
    public List<String> soustypes;

    public BioType(String id, List<String> soustypes) {
        this.id = id;
        this.soustypes = soustypes;
    }

}
