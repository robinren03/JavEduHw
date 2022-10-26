package com.example.renyanyu;
import android.os.AsyncTask;

import java.nio.channels.AsynchronousChannelGroup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class KEntityRepository {

    private final KEntityDao kEntityDao;
    private final AppDB appDB;

    public KEntityRepository(AppDB appDB){
        this.appDB = appDB;
        this.kEntityDao = this.appDB.kEntityDao();
    }

    public void insertkEntity(KEntity... kEntities){
        InsertKEntityTask insertKEntityTask = new InsertKEntityTask();
        insertKEntityTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, kEntities);
    }

    private class InsertKEntityTask extends AsyncTask<KEntity, Void, Void>{
        @Override
        protected Void doInBackground(KEntity... kEntities){
            kEntityDao.insert(kEntities);
            return null;
        }
    }

    public List<KEntity> getAllKEntities(){
        try {
            GetAllKEntitesTask getAllKEntitesTask = new GetAllKEntitesTask();
            return new ArrayList<KEntity>(Arrays.asList(getAllKEntitesTask.executeOnExecutor(
                    AsyncTask.THREAD_POOL_EXECUTOR,0).get()));
        }catch(ExecutionException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    private class GetAllKEntitesTask extends AsyncTask<Integer, Void, KEntity[]>{
        @Override
        protected KEntity[] doInBackground(Integer... params){
            return kEntityDao.getAllKEntity();
        }
    }

    public KEntity getKEntityByKEntityUri(String...kEntityUri){
        try{
            GetKEntityByKEntityUriTask getKEntityByKEntityUri = new GetKEntityByKEntityUriTask();
            KEntity[] _kEntity = getKEntityByKEntityUri.executeOnExecutor(
                    AsyncTask.THREAD_POOL_EXECUTOR, kEntityUri).get();
            if(_kEntity == null || _kEntity.length == 0) return null;
            return _kEntity[0];
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    private class GetKEntityByKEntityUriTask extends AsyncTask<String, Void, KEntity[]>{
       @Override
       protected KEntity[] doInBackground(String... kEntityUri){
           return kEntityDao.getKEntityByKEntityUri(kEntityUri);
       }
    }

    public void deleteKEntity(KEntity... kEntities){
        DeleteKEntityTask deleteKEntityTask = new DeleteKEntityTask();
        deleteKEntityTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, kEntities);
    }
    private class DeleteKEntityTask extends AsyncTask<KEntity, Void, Void>{
        @Override
        protected Void doInBackground(KEntity... kEntities){
            kEntityDao.delete(kEntities);
            return null;
        }
    }

    public void deleteKEntityByKEntityUri(String... kEntityUri){
        DeleteKEntityByKEntityUriTask deleteKEntityTask = new DeleteKEntityByKEntityUriTask();
        deleteKEntityTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, kEntityUri);
    }
    private class DeleteKEntityByKEntityUriTask extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... kEntityUri){
            kEntityDao.deleteKEntityByKEntityUri(kEntityUri);
            return null;
        }
    }

    public void clearNews(){
       ClearNewsTask clearNewsTask = new ClearNewsTask();
       clearNewsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
    }

    private class ClearNewsTask extends AsyncTask<Integer, Void, Void>{
        @Override
        protected Void doInBackground(Integer... params){
            kEntityDao.clear();
            return null;
        }
    }
}
