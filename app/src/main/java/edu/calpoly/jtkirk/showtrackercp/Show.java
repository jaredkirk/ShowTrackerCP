package edu.calpoly.jtkirk.showtrackercp;

/**
 * Created by jaredkirk on 4/27/16.
 */
public class Show {

    private int id, tvdbID, episodesSeen;
    private String language;
    private String name;
    private String banner;
    private String overview;
    private String firstAired;
    private String network;
    private String status;
    private String imdbID;

    public Show(int id, int tvdbID, String language, String name, String banner, String overview,
                String firstAired, String network, String imdbID, String status, int episodesSeen) {
        this.id = id;
        this.tvdbID = tvdbID;
        this.language = language;
        this.name = name;
        this.banner = banner;
        this.overview = overview;
        this.firstAired = firstAired;
        this.network = network;
        this.imdbID = imdbID;
        this.status = status;
        this.episodesSeen = episodesSeen;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(String firstAired) {
        this.firstAired = firstAired;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTvdbID() {
        return tvdbID;
    }

    public void setTvdbID(int tvdbID) {
        this.tvdbID = tvdbID;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public int getEpisodesSeen() {
        return episodesSeen;
    }

    public void setEpisodesSeen(int episodesSeen) {
        this.episodesSeen = episodesSeen;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
