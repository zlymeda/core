package com.dotcms.vanityurl.cache;

import java.util.List;

import com.dotcms.vanityurl.model.CachedVanityUrl;
import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.business.DotCacheAdministrator;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.languagesmanager.model.Language;
import com.dotmarketing.util.Logger;
import com.liferay.util.StringPool;

import io.vavr.control.Try;

/**
 * This class implements {@link VanityUrlCache} the cache for Vanity URLs.
 * Is used to map the Vanity URLs path to the Vanity URL content
 *
 * @author oswaldogallango
 * @version 4.2.0
 * @since June 24, 2017
 */
public class VanityUrlCacheImpl extends VanityUrlCache {

    private DotCacheAdministrator cache;

    private static final String PRIMARY_GROUP = "VanityURLCache";
    private static final String VANITY_URL_404_GROUP = "VanityURL404Cache";


    public VanityUrlCacheImpl() {
        cache = CacheLocator.getCacheAdministrator();
    }

    @Override
    public String getPrimaryGroup() {

      return PRIMARY_GROUP;
    }

    @Override
    public String[] getGroups() {

      return new String[] {PRIMARY_GROUP,VANITY_URL_404_GROUP};
    }

    @Override
    public void clearCache() {
      cache.flushGroup(PRIMARY_GROUP);
      cache.flushGroup(VANITY_URL_404_GROUP);

    }

    @Override
    public void remove(final Contentlet vanityURL) {
      if(vanityURL==null || !vanityURL.isVanityUrl()) {
        return;
      }
      Host host = Try.of(() -> APILocator.getHostAPI().find(vanityURL.getHost(), APILocator.systemUser(), false)).getOrNull();
      Language lang = Try.of(() -> APILocator.getLanguageAPI().getLanguage(vanityURL.getLanguageId())).getOrNull();
      if(host==null || lang==null ) {
        Logger.warn(this.getClass(), "vanity url not invalidating cache, missing host or language attr: " + vanityURL);
        return;
      }

      cache.remove(key(host,lang), PRIMARY_GROUP);
      cache.flushGroup(VANITY_URL_404_GROUP);
    }

    @Override
    public void put(final Host host, Language lang, List<CachedVanityUrl> vanityURLs) {
      if(host==null || lang==null || vanityURLs==null) {
        Logger.warn(this.getClass(), "invalid put into vanityurl cache. host:" + host +" lang:" + lang + " vanityURLs:" + vanityURLs);
        return;
      }
    
      cache.put(key(host,lang), vanityURLs, PRIMARY_GROUP);

      cache.flushGroup(VANITY_URL_404_GROUP);
    }

    @Override
    public List<CachedVanityUrl> getCachedVanityUrls(final Host host, final Language lang) {
      if(host==null || lang==null ) {
        Logger.warn(this.getClass(), "vanity url  missing host or language attr: " + host + "  " + lang);
      }
      final String key=key(host,lang);

      return  (List<CachedVanityUrl>) cache.getNoThrow(key, PRIMARY_GROUP); 

       
    }
    
    

    
    @Override
    public boolean is404(final Host host, final Language lang, final String url) {

      return cache.getNoThrow(key(host,lang, url), VANITY_URL_404_GROUP)!=null;
    }

    @Override
    public void put404(final Host host, final Language lang, final String url) {

      cache.put(key(host,lang, url), Boolean.TRUE, VANITY_URL_404_GROUP);

    }



    String key(final Host host, final Language lang) {
      return key(host, lang, null);
    }



    String key(final Host host, final Language lang, final String url) {

      return (host!=null ? host.getIdentifier() : StringPool.BLANK)
          + StringPool.UNDERLINE 
          + (lang!=null ? String.valueOf(lang.getId()) : StringPool.BLANK) 
          + StringPool.UNDERLINE  
          + (url != null ?  url : StringPool.BLANK );
    }
  

    


}