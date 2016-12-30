package com.mmazzarolo.dev.topgithub.rest;

import android.util.Log;

import com.mmazzarolo.dev.topgithub.event.SearchFailureEvent;
import com.mmazzarolo.dev.topgithub.event.SearchSuccesEvent;
import com.mmazzarolo.dev.topgithub.model.SearchResult;

import java.net.URLEncoder;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Matteo on 31/08/2015.
 */
public class GithubApiClient {

    private static final String BASE_URL = "https://api.github.com/";
    private GithubApiInterface mGithubApiInterface;

    // Creates the Retrofit RestAdapter
    public GithubApiClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .build();

        mGithubApiInterface = restAdapter.create(GithubApiInterface.class);
    }

    public GithubApiInterface getGithubApiInterface() {
        return mGithubApiInterface;
    }
    
    
     /**
      * @desc:查询接口(根据语言选择项目排行榜)
      * @author：Arison on 2016/12/19
      */
    public void startSearch(String language, String created) {
        // Callback for the search results
        Callback callback = new Callback<SearchResult>() {
            @Override
            public void success(SearchResult searchResult, Response response) {
                EventBus.getDefault().post(new SearchSuccesEvent(searchResult));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                EventBus.getDefault().post(new SearchFailureEvent(retrofitError));
            }
        };

        // Generate the "q" parameter for the API call: be carefull on encoding the value but not
        // the "+" symbol that links the parameters
        String query = "stars:>1+created:>" + created;
        if (!"".equals(language)) {
            query = query.concat("+language:" + URLEncoder.encode(language));
        }
        Log.d("GithubApiClient","query:"+query);
        // Start the GitHubApi call
        mGithubApiInterface.getRepositories(
                query,
                "stars",
                "desc",
                "30",
                "1",
                callback);
    }


    /**
     * @desc:查询接口(根据语言选择项目排行榜,分页)
     * @author：Arison on 2016/12/19
     */
    public void startSearch(String page,String language, String created) {
        // Callback for the search results
        Callback callback = new Callback<SearchResult>() {
            @Override
            public void success(SearchResult searchResult, Response response) {
                EventBus.getDefault().post(new SearchSuccesEvent(searchResult));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                EventBus.getDefault().post(new SearchFailureEvent(retrofitError));
            }
        };

        // Generate the "q" parameter for the API call: be carefull on encoding the value but not
        // the "+" symbol that links the parameters
        String query = "stars:>1+created:>" + created;
        if (!"".equals(language)) {
            query = query.concat("+language:" + URLEncoder.encode(language));
        }
        Log.d("GithubApiClient","query:"+query+" page:"+page);
        // Start the GitHubApi call
        mGithubApiInterface.getRepositories(
                query,
                "stars",
                "desc",
                "30",
                 page,
                callback);
    }

    /**
     * @desc:查询接口(根据关键字)
     * @author：Arison on 2016/12/19
     */
    public void startSearch(String page,String q,String language, String created) {
        // Callback for the search results
        Callback callback = new Callback<SearchResult>() {
            @Override
            public void success(SearchResult searchResult, Response response) {
                EventBus.getDefault().post(new SearchSuccesEvent(searchResult));
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                EventBus.getDefault().post(new SearchFailureEvent(retrofitError));
            }
        };

        // Generate the "q" parameter for the API call: be carefull on encoding the value but not
        // the "+" symbol that links the parameters
        String query ="q="+q+"+stars:>1+created:>" + created;
        if (!"".equals(language)) {
            query = query.concat("+language:" + URLEncoder.encode(language));
        }
        Log.d("GithubApiClient","query:"+query+" page:"+page);
        // Start the GitHubApi call
        mGithubApiInterface.getRepositories(
                query,
                "stars",
                "desc",
                "30",
                page,
                callback);
    }

}
