package org.elasticsearch.http.jetty.security;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author imotov
 */
public class RestPathMapTest {

    @Test
    public void testRestPathMap() {
        RestPathMap<Integer> restPathMap = new RestPathMap<Integer>();
        restPathMap.put("/test", 1);
        restPathMap.put("/other", 2);
        restPathMap.put("/{index}/{id}/_search", 3);
        restPathMap.put("/{index}/_search", 3);
        restPathMap.put("/_search", 4);

        assertThat(restPathMap.match("/test"), equalTo(1));
        assertThat(restPathMap.match("/other"), equalTo(2));
        assertThat(restPathMap.match("/foo/bar/_search"), equalTo(3));
        assertThat(restPathMap.match("/foo/_search"), equalTo(3));
        assertThat(restPathMap.match("/foo/bar/baz/_search"), nullValue());
        assertThat(restPathMap.match("/_search"), equalTo(4));
        assertThat(restPathMap.match("/cluster/_health"), nullValue());
        assertThat(restPathMap.match("/_searching"), nullValue());

        assertThat(restPathMap.containsMatch("/_searching"), equalTo(false));
        assertThat(restPathMap.containsMatch("/_search"), equalTo(true));

        assertThat(restPathMap.getMatch("/foo/bar/_search").getValue(), equalTo(3));
        assertThat(restPathMap.getMatch("/foo/bar/_search").getKey(), equalTo("/{index}/{id}/_search"));

        restPathMap.clear();
        assertThat(restPathMap.match("/test"), nullValue());
        assertThat(restPathMap.match("/other"), nullValue());
        restPathMap.put("/other", 5);
        assertThat(restPathMap.match("/other"), equalTo(5));

    }

    @Test
    public void testDefaultMap() {
        RestPathMap<Integer> restPathMap = new RestPathMap<Integer>();
        restPathMap.put("/test", 1);
        restPathMap.put("/other", 2);
        restPathMap.put("/{index}/{id}/_search", 3);
        restPathMap.put("/{index}/_search", 3);
        restPathMap.put("/*/state", 5);
        restPathMap.put("/*", 6);
        restPathMap.put("*", 7);

        assertThat(restPathMap.match("/test"), equalTo(1));
        assertThat(restPathMap.match("/foo/bar/_search"), equalTo(3));
        assertThat(restPathMap.match("/foo/_search"), equalTo(3));
        assertThat(restPathMap.match("/notfound"), equalTo(6));
        assertThat(restPathMap.match("/foo/bar/baz/_search"), equalTo(7));

    }

}
