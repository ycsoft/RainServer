package com.yangmuyao.utils;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fengyuyangchen on 16/6/14.
 */
public class FilePool  implements Serializable{

    private     static Map<String ,CacheFile>    filepool = new ConcurrentHashMap<String, CacheFile>();
    public      static Map<String ,CacheFile>    getAllFiles(){
        return filepool;
    }

    /**
     *
     * @param thread_id
     * @return
     * @throws Exception
     */
    public      static CacheFile getFile(long thread_id) throws Exception{

        String key = String.valueOf(thread_id);

        if ( !filepool.containsKey(key) || filepool.isEmpty() ){

            //filename
            String uuid                         = "tmp" + File.separator + UUID.randomUUID().toString();
            CacheFile               cacheFile   = new CacheFile(uuid);

            filepool.put(key,cacheFile);

            return cacheFile;
        }else {
            return filepool.get(key);
        }
    }


}
