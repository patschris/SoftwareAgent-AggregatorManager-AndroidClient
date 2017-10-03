package com.k23b.team13.project3.ac.structures;

import com.k23b.team13.project3.ac.classes.DistinctJob;

import java.util.ArrayList;

/**
 *  <code>DistinctJobList</code> is a singleton class consisted of an arraylist that contains <code>Distinct job</code>.
 *
 * 	@author C. Patsouras I. Venieris
 *	@version 1
 */
public class DistinctJobList {
    /**
     * 	The instance of this class.
     */
    private static DistinctJobList instance = null;
    /**
     * 	An arraylist containing distinct jobs.
     */
    private ArrayList<DistinctJob> list = new ArrayList<DistinctJob>();
    /**
     * @return instance of this class.
     */
    public static DistinctJobList getInstance(){
        if (instance == null){
            instance = new DistinctJobList();
        }
        return instance;
    }
    /**
     * Adds a new distinct job to the list.
     * @param a The <code>DistinctJob</code> to be pushed.
     */
    public void push(DistinctJob a){
        synchronized (list) {
            list.add(a);
        }
    }
    /**
     * Removes a distinct job from the list.
     * @param a The <code>DistinctJob</code> to be removed.
     */
    public void pop(DistinctJob a){
        synchronized (list) {
            list.remove(a);
        }
    }
    /**
     * @return this class's arraylist containing distinct jobs.
     */
    public ArrayList<DistinctJob> getAlist() {
        synchronized(list){
            return list;
        }
    }
    /**
     * Empties the list.
     */
    public void clear(){
        synchronized(list){
            list.clear();
        }
    }
    /**
     * Clear the list and fill it with another list.
     * @param al new distinct job list.
     */
    public void setAlist(ArrayList<DistinctJob> al){
        synchronized (list) {
            list.clear();
            list.addAll(al);
        }
    }
}
