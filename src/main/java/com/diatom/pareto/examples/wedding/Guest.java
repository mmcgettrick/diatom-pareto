/**
 * Created by rachlin on 4/5/16.
 */
package com.diatom.pareto.examples.wedding;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Guest {
    String name;
    int age;
    String party;
    String sex;
    Set<String> categories;
    Set<String> exclusions;
    Set<String> inclusions;


    /**
     * Constructor for guests that have no party affiliation
     * @param name
     * @param age
     */
    public Guest(String name, String sex, int age)
    {
        this(name, null, sex, age);
    }

    /**
     * Constructor
     * @param name
     * @param age
     * @param party
     */

    public Guest(String name, String party, String sex, int age)
    {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.party = party;

        if (party==null || party=="")
            this.party = name;

        categories = new HashSet<String>();
        exclusions = new HashSet<String>();
        inclusions = new HashSet<String>();
    }

    public String getName() {
        return name;
    }

    public String getSex() { return sex; }

    public int getAge() {
        return age;
    }

    public String getParty() {
        return party;
    }

    public boolean partyOfOne() { return name.equalsIgnoreCase(party); }

    public Set<String> getCategories() {
        return categories;
    }

    public Set<String> getExclusions() {
        return exclusions;
    }

    public Set<String> getInclusions() {
        return inclusions;
    }

    public void addInclusion(String name) { inclusions.add(name); }
    public void addInclusions(Collection<String> nlist) { inclusions.addAll(nlist); }

    public void addExclusion(String name) { exclusions.add(name); }
    public void addExclusions(Collection<String> nlist) { exclusions.addAll(nlist); }


    public void addCategory(String category) { categories.add(category); }
    public void addCategories(Collection<String> clist) { categories.addAll(clist); }


}
