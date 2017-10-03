package com.k23b.team13.project3.ac.structures;


import com.k23b.team13.project3.ac.classes.Agents;

import java.util.ArrayList;
/**
 *  <code>AgentsList</code> is a singleton class consisted of an arraylist that contains <code>Agents</code>
 * 	(SoftwareAgents/clients).
 *
 * 	@author C. Patsouras I. Venieris
 *	@version 1
 */
public class AgentsList {
    /**
     * 	The instance of this class.
     */
    private static AgentsList instance = null;
    /**
     * 	An arraylist containing agents.
     */
    private static ArrayList<Agents> alist = new ArrayList<Agents>();
    /**
     * @return instance of this class.
     */
    public static AgentsList getInstance(){
        if (instance == null){
            instance = new AgentsList();
        }
        return instance;
    }
    /**
     * Adds a new agent to the list.
     * @param a The <code>Agent</code> to be pushed.
     */
    public void push(Agents a){
        synchronized (alist) {
            alist.add(a);
        }
    }
    /**
     * Removes an agent from the list.
     * @param a The <code>Agent</code> to be removed.
     */
    public void pop(Agents a){
        synchronized (alist) {
            alist.remove(a);
        }
    }

    /**
     * @return this class's arraylist containing agents.
     */
    public ArrayList<Agents> getAlist() {
        synchronized(alist){
            return alist;
        }
    }
    /**
     * Empties the list.
     */
    public void clear(){
        synchronized(alist){
            alist.clear();
        }
    }
    /**
     * Clear the list and fill it with another list.
     * @param al new agents list.
     */
    public void updateList(ArrayList<Agents> al){
        synchronized (alist) {
            alist.clear();
            alist.addAll(al);
        }
    }
    /**
     * @param hashkey Identifier of the agent i want to find.
     * @return searched agent or null if agent wasn't found.
     */
    public Agents getAgentbyHashkey(int hashkey){
        synchronized(alist){
            for (Agents a : alist){
                if (a.getHashKey() == hashkey){
                    return a;
                }
            }
        }
        return null;
    }
}
