package com.mmga.cloudcover.model;

import java.lang.String;import java.util.List;

public class Songs {

    private String name;

    private List<Artist> artists;

    private Album album;




    public static class Artist {
        private String name;

        public String getName() {
            return name;
        }
    }

    public static class Album{
        private String name;

        private String picUrl;

        public String getName() {
            return name;
        }

        public String getPicUrl() {
            return picUrl;
        }
    }


    public Album getAlbum() {
        return album;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
