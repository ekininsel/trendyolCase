//Ekin INSEL Trendyol Test Otomasyon Mühendisi API Testi Ödevi
package trendyolCase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;

public class trendyolAPITestCase {
    Response resultBySearch;
    String linkBySearch = "http://www.omdbapi.com/?apikey=db908955&s=Harry Potter";
    String imdbID;
    String linkByID;
    String movieName = "Harry Potter and the Sorcerer's Stone";
    String expectedYear = "2001";
    String expectedReleased = "16 Nov 2001";
    int expectedHttp = 200;
    int movieIndex = 0;

    @Before
    public void startBySearch(){
        resultBySearch = RestAssured.get (linkBySearch).andReturn ();
        //all movie names
        String movies = resultBySearch.jsonPath ().getString ("Search.Title");
        //from all movie names listed find the index of the wanted movie
        for(int i=0; i<movies.split (",").length; i++){
            if(resultBySearch.getBody ().path ("Search[" + i + "].Title").equals (movieName))
                movieIndex = i;
        }
        //find the ID of the wanted movie according to the index
        imdbID = resultBySearch.getBody ().path ("Search[" + movieIndex + "].imdbID");
        linkByID = "http://www.omdbapi.com/?apikey=db908955&i="  + imdbID;
    }
    @Test
    public void test_TitleFromID(){
        RestAssured.when ().get (linkByID).then ().assertThat ().body ("Title", equalTo (movieName));
    }

    @Test
    public void test_YearFromID(){
        RestAssured.when ().get (linkByID).then ().assertThat ().body ("Year", equalTo (expectedYear));
    }

    @Test
    public void test_ReleasedFromID(){
        RestAssured.when ().get (linkByID).then ().assertThat ().body ("Released", equalTo (expectedReleased));
    }

    @Test
    public void test_HTTPFromID(){
        RestAssured.when ().get (linkByID).then ().assertThat ().statusCode (expectedHttp);
    }
}
