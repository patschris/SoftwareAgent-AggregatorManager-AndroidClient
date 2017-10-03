package com.k23b.team13.project3.ac.structures;


import com.k23b.team13.project3.ac.classes.Result;

import java.util.ArrayList;

/**
 *  <code>ResultList</code> is a singleton class consisted of an arraylist that contains <code>Result</code>s.
 * 	@author C. Patsouras I. Venieris
 *	@version 1
 */
public class ResultList {
    /**
     * 	The instance of this class.
     */
    private static ResultList instance = null;
    /**
     * 	An arraylist containing results.
     */
    private ArrayList<Result> list = new ArrayList<Result>();
    /**
     * Number of results.
     */
    private String num = null;
    /**
     * Hashkey of agent whose results are in the list.
     */
    private String hashkey = null;
    /**
     * @return instance of this class.
     */
    public static ResultList getInstance(){
        if (instance == null){
            instance = new ResultList();
        }
        return instance;
    }
    /**
     * Adds a new result to the list.
     * @param a The <code>Result</code> to be pushed.
     */
    public void push(Result a){
        synchronized (list) {
            list.add(a);
        }
    }
    /**
     * Removes a result from the list.
     * @param a The <code>Result</code> to be removed.
     */
    public void pop(Result a){
        synchronized (list) {
            list.remove(a);
        }
    }

    /**
     * @return number of results.
     */
    public String getNum() {
        return num;
    }

    /**
     * Sets number of results.
     * @param num number of results.
     */
    public void setNum(String num) {
        this.num = num;
    }

    /**
     * @return agent's hashkey whose results are in the list.
     */
    public String getHashkey() {
        return hashkey;
    }
    /**
     * Sets this hashkey.
     * @param hashkey agent's hashkey whose results are in the list.
     */
    public void setHashkey(String hashkey) {
        this.hashkey = hashkey;
    }
    /**
     * @return this class's arraylist containing results.
     */
    public ArrayList<Result> getAlist() {
        synchronized(list){
            return list;
        }
    }
    /**
     * Empties list.
     */
    public void clear(){
        synchronized(list){
            list.clear();
        }
    }
    /**
     * Clear the list and fill it with another list.
     * @param al new result list.
     */
    public void setAlist(ArrayList<Result> al){
        synchronized(list) {
            list.clear();
            list.addAll(al);
        }
    }
}
