package no.hbv.gruppe1.snusr.snusr.interfaces;

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
public interface DatabaseGrabber {

    /**
     * Fetches a list of snus
     * @param context           Application context
     * @param filtrationList    A list of {@link Filtration} to filter result
     * @param sorting           A {@link Sorting} that defines the sorting method used
     * @return                  Returns a navigable cursor for the result
     */
    Cursor fetchSnus(Context context, List<Filtration> filtrationList, Sorting sorting);

    /**
     * Fetches snus that are in "my list"
     * @param context           Application context
     * @param restriction       Restrict what content is included.
     * @return                  Returns a navigable cursor for the result
     */
    Cursor fetchMyList(Context context, int restriction);

    /**
     * Fetches one specific snus based on ID
     * @param context           Application context
     * @param snusId            The snus id
     * @return                  Returns a single snus
     */
    Cursor fetchSpecificSnus(Context context, int snusId);

    /**
     * Fetches a list of manufacturers
     * @param context           Application context
     * @return                  Returns a navigable cursor containing id and names of the manufacturers
     */
    Cursor fetchManufacturers(Context context);
}
