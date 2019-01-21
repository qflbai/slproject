package com.sl.shenmian.module.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.sl.shenmian.module.db.entity.SealInfoEntity;

import java.util.List;

import io.reactivex.Single;

/**
 * @author WenXian Bai
 * @Date: 2018/9/21.
 * @Description:
 */
@Dao
public interface DBDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(SealInfoEntity... disassemblesEntities);

    @Query("SELECT * FROM SealInfoEntity WHERE userAccount  = :userAccount AND sealType = :sealType")
    Single<List<SealInfoEntity>> quserOverdueAcency(String userAccount, int sealType);

    @Query("UPDATE SealInfoEntity SET uploadingState = :uploadingState WHERE id =:id")
    void upDateUpLoadingState(long id, int uploadingState);
}
