package Room;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

@Database(entities = {FavouriteRecipe.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {
   /* private static RecipeDatabase instance;*/

    public static RecipeDatabase getInstance() {
        return instance;
    }

    public static void setInstance(RecipeDatabase instance) {
        RecipeDatabase.instance  =instance;
    }

    public abstract RecipeDao favouriteDao();
    private static RecipeDatabase  instance = null;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static  synchronized RecipeDatabase getInstance(Context context) {
        if (getInstance() == null) {
            setInstance(Room.databaseBuilder(context.getApplicationContext(), RecipeDatabase.class, "recipe_database").fallbackToDestructiveMigration().build());
        }
        return getInstance();
    }

}
