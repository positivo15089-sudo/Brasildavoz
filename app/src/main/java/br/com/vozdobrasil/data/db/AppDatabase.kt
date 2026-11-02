package br.com.vozdobrasil.data.db
import androidx.room.*
import br.com.vozdobrasil.data.model.Project
import kotlinx.coroutines.flow.Flow

@Dao interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY createdAt DESC") fun all(): Flow<List<Project>>
    @Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun insert(p: Project)
    @Update suspend fun update(p: Project)
    @Delete suspend fun delete(p: Project)
    @Query("SELECT * FROM projects WHERE id=:id") suspend fun byId(id:String): Project?
}
@Database(entities=[Project::class], version=1, exportSchema=false)
abstract class AppDatabase : RoomDatabase() { abstract fun projectDao(): ProjectDao }
