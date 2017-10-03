package com.k23b.team13.project3.ac.structures;

import com.k23b.team13.project3.ac.classes.Job;

import java.util.ArrayList;

/**
 *  <code>PeriodicJobList</code> is a singleton class consisted of an arraylist that contains <code>Job</code>s.
 * 	@author C. Patsouras I. Venieris
 *	@version 1
 */
public class PeriodicJobList {
    /**
     * 	The instance of this class.
     */
    private static PeriodicJobList instance = null;
    /**
     * 	An arraylist containing periodic jobs.
     */
    private ArrayList<Job> list = new ArrayList<Job>();
    /**
     * @return instance of this class.
     */
    public static PeriodicJobList getInstance(){
        if (instance == null){
            instance = new PeriodicJobList();
        }
        return instance;
    }
    /**
     * Adds a new periodic job to the list.
     * @param a The <code>Job</code> to be pushed.
     */
    public void push(Job a){
        synchronized (list) {
            list.add(a);
        }
    }
    /**
     * Removes a periodic job from the list.
     * @param a The <code>Job</code> to be removed.
     */
    public void pop(Job a){
        synchronized (list) {
            list.remove(a);
        }
    }
    /**
     * @return this class's arraylist containing periodic jobs.
     */
    public ArrayList<Job> getAlist() {
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
     * @param al new periodic job list.
     */
    public void setAlist(ArrayList<Job> al){
       synchronized (list) {
           list.clear();
           list.addAll(al);
       }
    }
    /**
     * Remove job that exist in given position of the arraylist.
     * @param pos position of the arraylist that I want to remove.
     */
    public void remove(int pos){
        synchronized(list){
            list.remove(pos);
        }
    }
    /**
     * @param jobId Identifier of the job I want to find.
     * @return searched job or null if job wasn't found.
     */
    public Job findJobById(int jobId){
        synchronized(list){
            for (Job j : list){
                if (j.getId() == jobId) return j;
            }
        }
        return null;
    }
}
