package edu.cmu.cs.mvelezce.java.programs.evaluation.berkeleydb;

import com.sleepycat.je.*;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * Created by miguelvelez on 5/16/17.
 */
public class BerkeleyDB {
    public static void main(String[] args) throws DatabaseException, UnsupportedEncodingException {
        Environment myDbEnvironment = null;
        Database myDatabase = null;

        // Open the environment, creating one if it does not exist
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        File file = new File(".");
        myDbEnvironment = new Environment(new File("src/main/java/edu/cmu/cs/mvelezce/java/programs/evaluation/berkeleydb/tmp/dbEnv"), envConfig);

        // Open the database, creating one if it does not exist
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        myDatabase = myDbEnvironment.openDatabase(null, "TestDatabase", dbConfig);


        String key = "myKey";
        String data = "myData";

        DatabaseEntry theKey = new DatabaseEntry(key.getBytes("UTF-8"));
        DatabaseEntry theData = new DatabaseEntry(data.getBytes("UTF-8"));
        myDatabase.put(null, theKey, theData);


        if (myDatabase != null) {
            myDatabase.close();
        }

        if (myDbEnvironment != null) {
            myDbEnvironment.close();
        }
    }
}
