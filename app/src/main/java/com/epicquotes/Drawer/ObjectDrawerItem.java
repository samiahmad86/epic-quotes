package com.epicquotes.Drawer;

/**
 * Created by SAMI on 02-11-2015.
 */
public class ObjectDrawerItem {

    public String name;
    public boolean header;
    public boolean footer;

    // Constructor.
    public ObjectDrawerItem(String name,boolean header,boolean footer) {


        this.name = name;
        this.header=header;
        this.footer=footer;
    }
}