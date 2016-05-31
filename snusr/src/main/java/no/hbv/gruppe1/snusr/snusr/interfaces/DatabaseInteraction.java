package no.hbv.gruppe1.snusr.snusr.interfaces;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import no.hbv.gruppe1.snusr.snusr.dataclasses.Filtration;
import no.hbv.gruppe1.snusr.snusr.dataclasses.Sorting;

import java.util.List;

/**
 * Created by Knut Johan Hesten 2016-05-26.
 *
 * Interface for common database tasks
 */
public interface DatabaseInteraction {

    /**
     * Fetches a list of snus
     * @param filtrationList    A list of {@link Filtration} to filter result
     * @param sorting           A {@link Sorting} that defines the sorting method used
     * @return                  Returns a navigable cursor for the result
     */
    Cursor fetchSnus(List<Filtration> filtrationList, Sorting sorting);

    /**
     * Fetches snus that are in "my list"
     * @param restriction       Restrict what content is included.
     * @return                  Returns a navigable cursor for the result
     */
    Cursor fetchMyList(int restriction);

    /**
     * Fetches one specific snus based on ID
     * @param snusId            The snus id
     * @return                  Returns a single snus
     */
    Cursor fetchSpecificSnus(int snusId);

    /**
     * Fetches a list of manufacturers
     * @return                  Returns a navigable cursor containing id and names of the manufacturers
     */
    Cursor fetchManufacturers();

    Cursor fetchLines(int manufacturerId);

    Cursor fetchTastes();

    Cursor fetchLines();

    Cursor fetchTypes();

    int updatePersonalRankingAndUpdateAverage(int snusId, double ranking);

    int setMyListBookmarked(int snusId);


    /**
     * Closes the database connection
     */
    void close();

    void update(String table, ContentValues values, String whereClause, String where[]);

    long insert(String table, ContentValues values);
}
