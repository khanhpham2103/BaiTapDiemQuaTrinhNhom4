package com.nguyenhuutrong.rssreader;

public class Food {
    private String title;
    private String link;
    private String dec;
    private String image;
    public  Food(String title, String link, String dec, String image){
        this.title = title;
        this.image = image;
        this.dec = dec;
        this.link = link;

    }


    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDec() {
        return dec;
    }

    public String getImage() {
        return image;
    }
}
