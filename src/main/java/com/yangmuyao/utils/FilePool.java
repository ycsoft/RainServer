package com.yangmuyao.utils;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by fengyuyangchen on 16/6/14.
 */
public class FilePool  implements Serializable{

    private     static Map<String ,CacheFile>    filepool = new ConcurrentHashMap<String, CacheFile>();
    public      static Map<String ,CacheFile>    getAllFiles(){
        return filepool;
    }

    public      static ConcurrentLinkedQueue<String>  fileHistory = new ConcurrentLinkedQueue<String>();
    public      static ExecutorService  executorService = Executors.newFixedThreadPool(2);

    /**
     * 根据每个线程的ID,或得该线程对应的缓存文件
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
