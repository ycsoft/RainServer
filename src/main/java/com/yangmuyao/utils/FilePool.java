package com.yangmuyao.utils;

import sun.misc.Queue;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by fengyuyangchen on 16/6/14.
 */
public class FilePool  implements Serializable{

    private     static Map<String ,CacheFile>    filepool = new ConcurrentHashMap<String, CacheFile>();
    public      static Map<String ,CacheFile>    getAllFiles(){
        return filepool;
    }

    public      static ConcurrentLinkedQueue<String>  fileHistory = new ConcurrentLinkedQueue<String>();

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
            fileHistory.add(uuid);
            return cacheFile;
        }else {
            return filepool.get(key);
        }
    }


}
