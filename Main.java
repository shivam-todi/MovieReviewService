package com.company;

import javafx.util.Pair;

import java.util.*;

class MovieReviewService {
    public Integer currYear = 2021;
    public List<String> userList = new ArrayList();
    public List<String> movieList = new ArrayList();
    public List<String> topMoviesN = new ArrayList<>();
    public HashMap <String, Integer> releasedYear = new HashMap<>();
    public HashMap <String, List<String>> movieGenres = new HashMap<>();
    public HashMap <Pair<String,String>, Integer> reviews = new HashMap<>();
    public HashMap <String, Integer> reviewsByUser = new HashMap<>();
    public HashMap <String, Integer> userStatus = new HashMap<>();
    public HashMap <String, Pair<Integer,Integer> > movieRating = new HashMap<>();
    public void add_user(String user){
        userList.add(user);
        userStatus.put(user, 0);
    }

    public void add_movie(String movie, Integer year, List<String> genre){
        movieList.add(movie);
        releasedYear.put(movie,year);
        movieGenres.put(movie,genre);
    }

    public void add_review(String user, String movie, Integer rating){
        Pair <String ,String> chk = new Pair<>(user,movie);
            if(reviews.containsKey(chk)){
                System.out.println("Exception multiple reviews not allowed");
            }
            else{
                    if(releasedYear.get(movie) >= currYear){
                        System.out.println("Exception movie yet to be released");
                    }
                    else{
                        reviews.put(chk, rating);
                        if(reviewsByUser.containsKey(user)) {
                            reviewsByUser.put(user, reviewsByUser.get(user) + 1);
                        }
                        if(movieRating.containsKey(movie)) {
                            Pair <Integer ,Integer> rate = movieRating.get(movie);
                            Integer totalRating = rate.getKey();
                            Integer outofRating = rate.getValue();
                            if(userStatus.get(user) == 1) {
                                totalRating = totalRating + 2*rating;
                                outofRating = outofRating + 20;
                            }
                            else {
                                totalRating = totalRating + rating;
                                outofRating = outofRating + 10;
                            }
                            Pair <Integer ,Integer> nwrate = new Pair<>(totalRating, outofRating);
                            movieRating.put(movie,nwrate);
                        }
                        else  {
                            Integer totalRating = 0;
                            Integer outofRating = 0;
                            if(userStatus.get(user) == 1) {
                                totalRating = 2*rating;
                                outofRating = 20;
                            }
                            else {
                                totalRating = rating;
                                outofRating = 10;
                            }
                            Pair <Integer ,Integer> nwrate = new Pair<>(totalRating, outofRating);
                            movieRating.put(movie,nwrate);
                        }
                    }
            }
        if(reviewsByUser.containsKey(user) && reviewsByUser.get(user)>3) {
            userStatus.put(user, 1);
        }
    }

    public List<String> getTopMovies (Integer n) {
        List< Pair <Double, String> > topMovies = new ArrayList<>();
        Integer i = 0;
        for (HashMap.Entry<String, Pair<Integer,Integer> > mp : movieRating.entrySet()) {
            Pair <Double, String> pr = new Pair((1.0 * mp.getValue().getKey())/(1.0 * mp.getValue().getValue()), mp.getKey());
            topMovies.add(i,pr);
            ++i;
        }
        Collections.sort(topMovies, new Comparator<Pair>() {
            @Override public int compare(Pair p1, Pair p2)
            {
                Double a = (Double) p1.getKey();
                Double b = (Double) p2.getKey();
                return b.compareTo(a);
            }
        });
        for (int j=0 ;j<topMovies.size() &&  j<n; j++)
        {
            topMoviesN.add(j, topMovies.get(j).getValue());
        }
        return topMoviesN;
    }

    public Double info (String movie) {
        if(movieRating.containsKey(movie)) {
            Pair <Integer ,Integer> rate = movieRating.get(movie);
            Integer totalRating = rate.getKey();
            Integer outofRating = rate.getValue();
            Double rating = 10.0*(1.0*totalRating)/(1.0*outofRating);
            return rating;
        }
        else {
            return null;
        }
    }

    public Double ratingForYear (int year) {
        Double rating = 0.0;
        Double outOfRating = 0.0;
        for (HashMap.Entry<String, Integer> entry : releasedYear.entrySet()){
            if(entry.getValue()==year) {
                rating = rating + info(entry.getKey());
                outOfRating = outOfRating + 10;
            }
        }
        rating = 10.0*(rating/outOfRating);
        return rating;
    }
}
public class Main {

    public static void main(String[] args) {

        MovieReviewService review = new MovieReviewService();
        review.add_movie("Tiger",2008, Arrays.asList("Drama"));
        review.add_movie("Don", 2006, Arrays.asList("Action", "Comedy"));
        review.add_movie("Padmaavat", 2006, Arrays.asList("Comedy"));
        review.add_movie("Lunchbox", 2021, Arrays.asList("Drama"));
        review.add_movie("Guru",2006,Arrays.asList("Drama"));
        review.add_movie("Metro", 2006, Arrays.asList("Romance"));
        review.add_user(("SRK"));
        review.add_user(("Deepika"));
        review.add_user(("Salman"));
        review.add_review("SRK","Don",2);
        review.add_review("SRK","Padmaavat",8);
        review.add_review("Salman","Don",5);
        review.add_review("Deepika","Don",9);
        review.add_review("Deepika","Guru",6);
        review.add_review("SRK","Don",10);
        review.add_review("Deepika","Lunchbox",5);
        review.add_review("SRK","Tiger",5);
        review.add_review("SRK","Metro",7);
        System.out.println(review.info("Don"));
        System.out.println(review.info("Metro"));
        System.out.println(review.info("Tiger"));
        System.out.println(review.info("Padmaavat"));
        System.out.println(review.ratingForYear(2006));
        System.out.println(review.getTopMovies(2));
    }
}
