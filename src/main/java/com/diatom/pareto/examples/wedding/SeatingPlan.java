package com.diatom.pareto.examples.wedding;


import com.diatom.pareto.Solution;

import java.util.*;

/**
 * Created by rachlin on 4/5/16.
 * A seating plan is a 2D array of names.  Each row is a table, defining the guest sequence along the row
 */

public class SeatingPlan extends Solution {



    GuestBook book;
    SeatingConfiguration config;
    int maxTables;
    int seatsPerTable;

    String[][] plan;
    Map<String,Integer> assignedTable; // which table is the person assigned to (fast lookup)
    Map<String,Integer> assignedSeat;  // which seat is the person assigned to (fast lookup)
    static final String EMPTY = "";

    /**
     * Constructor
     * @param book
     * @param config
     */
    public SeatingPlan(GuestBook book, SeatingConfiguration config)
    {
        this.book = book;
        this.config = config;
        this.maxTables = config.getIntProperty("max_tables");
        this.seatsPerTable = config.getIntProperty("seats_per_table");

        plan = new String[maxTables][seatsPerTable];
        for (int i=0; i<maxTables; i++)
            for (int j=0; j<seatsPerTable; j++)
                plan[i][j] = EMPTY;

        assignedTable = new HashMap<String,Integer>();
        assignedSeat = new HashMap<String, Integer>();
    }

    /**
     * Is a given seat occupied
     * @param table
     * @param seat
     * @return
     */
    public boolean isOccupied(int table, int seat)
    {
        return (getOccupant(table,seat).equals(EMPTY));
    }

    /**
     * Get the occupant of a specified seat
     * @param table
     * @param seat
     * @return name of guest or null if seat is empty
     */
    public String getOccupant(int table, int seat)
    {
        return plan[table][seat];
    }

    /**
     * Returns set of all guests at a specific table
     * @param table
     * @return names of guests or empty set if null
     */
    public Set<String> getOccupants(int table)
    {
        Set<String> list = new HashSet<String>();
        for (int s=0; s<seatsPerTable; s++)
        {
            String name = getOccupant(table, s);
            if (name.equals(EMPTY)==false)
                list.add(name);
        }
        return list;
    }

    /**
     * Test whether a guest is seated anywhere
     * @param name Name of guest
     * @return true if seated, false otherwise
     */
    public boolean isSeated(String name)
    {
        Integer table = assignedTable.get(name);
        Integer seat = assignedSeat.get(name);
        return (table!=null && seat!=null && plan[table][seat].equals(name));
    }


    /**
     * Unassign a guest from a seat.
     * NOTE! If guest is part of a multi-person party, the party is now broken!
     * This method should be called only by unassignParty
     * @param name
     */
    private void unassignGuest(String name)
    {
        if (name == null) return; // do nothing

        Integer table = assignedTable.get(name);
        if (table != null) assignedTable.remove(name);

        Integer seat = assignedSeat.get(name);
        if (seat != null) assignedSeat.remove(name);

        if (table != null && seat != null)
            plan[table][seat] = EMPTY;
    }

    /**
     * Assign guest to a particular seat
     * This could break party rules, so this function is only used by assignParty
     * @param name
     * @param table
     * @param seat
     */
    private void assignGuest(String name, int table, int seat)
    {
        // Validate assignment
        if (table<0 || table>=maxTables || seat<0 || seat>=seatsPerTable)
            return;

        // Unassign from current location
        // Don't want to assign a person to two seats!
        unassignGuest(name);

        // Unassign current occupant
        // Don't want to assign two people to the same seat!
        String currentOccupant = getOccupant(table,seat);
        if (currentOccupant.equals(EMPTY)==false)
            unassignGuest(currentOccupant);

        assignedTable.put(name, table);
        assignedSeat.put(name, seat);
        plan[table][seat] = name;
    }


    /**
     * Swap two guests seats
     * They either have to be part of the same party
     * or both be parties of one.
     * @param name1
     * @param name2
     * @return true if swap is successful
     */
    public boolean swapGuests(String name1, String name2, boolean partyOverride)
    {
        if (!isSeated(name1) || !isSeated(name2))
            return false;

        if (name1.equals(name2))
            return false;

        Guest g1 = book.getGuest(name1);
        Guest g2 = book.getGuest(name2);
        if (g1==null || g2==null)
            return false;

        if (partyOverride || g1.getParty().equalsIgnoreCase(g2.getParty())==true ||
                (g1.partyOfOne() && g2.partyOfOne()))
        {

            Integer table1 = assignedTable.get(name1);
            Integer seat1 = assignedSeat.get(name1);
            Integer table2 = assignedTable.get(name2);
            Integer seat2 = assignedSeat.get(name2);
            assignGuest(name1, table2, seat2);
            assignGuest(name2, table1, seat1);
            return true;
        }

        return false;
    }




    /**
     * Swap two parties
     * Parties must be distinct and of the same size
     * @param party1
     * @param party2
     * @return true if swap is successful
     */
    public boolean swapParties(String party1, String party2)
    {

        if (party1.equals(party2))
            return false;

        Set<Guest> p1 = book.getParty(party1);
        Set<Guest> p2 = book.getParty(party2);

        if (p1==null || p2==null)
            return false;

        if (p1.size() != p2.size())
            return false;

        Iterator it1 = p1.iterator();
        Iterator it2 = p2.iterator();

        for (int i=0; i<p1.size(); i++)
        {
            String name1 = ((Guest) it1.next()).getName();
            String name2 = ((Guest) it2.next()).getName();
            swapGuests(name1, name2, true);
        }

        return true;
    }

    /**
     * Which parties are represented at the given table
     * @param table
     * @return # of distinct parties
     */
    public Set<String> representedParties(int table)
    {
        return representedParties(table, 0, seatsPerTable);
    }



    /**
     * Which parties are represented by the N seats at specified table starting at specified seat.
     * @param table
     * @param seat
     * @param size
     * @return
     */
    public Set<String> representedParties(int table, int seat, int size)
    {
        Set<String> parties = new HashSet<String>();

        for (int i=0; i<size; i++)
        {
            int currentSeat = (seat+i) % seatsPerTable;
            String name = getOccupant(table, currentSeat);
            if (name.equals(EMPTY)==false) // seat is occupied
                parties.add(book.getGuest(name).getParty()); // add party of occupant to list
        }
        return parties;
    }




    /**
     * Assign a party to a table starting at specified position
     * @param party
     * @param table
     * @param seat
     */
    public void assignParty(String party, int table, int seat)
    {
        // Validate assignment
        if (table<0 || table>=maxTables || seat<0 || seat>=seatsPerTable)
            return;

        // Unassignable party
        Set<Guest> guests = book.getParty(party);
        if (guests.size() > seatsPerTable)
            return;

        // Which parties will be displaced by this assignment?
        Set<String> affectedParties = representedParties(table, seat, guests.size());

        // Unassign all guests in all affected parties
        for (String ap : affectedParties)
            unassignParty(ap);

        // Now assign the new party to the table
        int currentSeat = seat;

        for (Guest g : guests) {
            assignGuest(g.getName(), table, currentSeat);
            currentSeat++;
            if (currentSeat == seatsPerTable)
                currentSeat = 0;
        }
    }

    /**
     * Compressess the table and returns
     * @param table
     * @return position of first empty seat or 0 if table is full
     */
    public int compressTable(int table)
    {
        if (isTableFull(table) || isTableEmpty(table))
            return 0;

        int next = 0;
        for (int seat=0; seat<seatsPerTable; seat++)
        {
            String occupant = getOccupant(table, seat);
            if (occupant.equals(EMPTY)==false) // unoccupied, do nothing, else resassign to next available seat
            {
                // Don't need to re-assign a guest to the same seat
                if (seat>next)
                    assignGuest(occupant, table, next);
                next++;
            }
        }
        return next;
    }


    /**
     * How many people at the table are with someone they don't want to be with
     * i.e., the person is on their exclusion list?
     * Note: if A excludes B, it doesn't mean that B excludes A.
     * If B also lists A as an exlcusion, it would count as two violations
     * @param table
     * @return
     */
    public int tableViolations(int table)
    {
        Map<String,Set<String>> map = tableViolationsMap(table);
        if (map==null)
            return 0;
        else return (map.keySet().size());
    }


    /**
     * Get the list of table violations
     * @param table
     * @return
     */

    public Map<String,Set<String>> tableViolationsMap(int table)
    {
        Set<String> guestsAtTable = getOccupants(table);
        Map<String,Set<String>> map = new HashMap<String,Set<String>>();

        for (String name : guestsAtTable)
        {

            Guest g = book.getGuest(name);
            Set<String> ex = g.getExclusions();
            for (String other : ex)
                if (guestsAtTable.contains(other))  // violation detected
                {
                    Set<String> violations = map.get(name);
                    if (violations == null)
                    {
                        violations = new HashSet<String>();
                        map.put(name, violations);
                    }
                    violations.add(other);
                }
        }

        return map;
    }



    /**
     * Get table violation map
     * @param table
     * @return
     */
    public Map<String,Set<String>> tableViolationsMap2(int table)
    {
        Set<String> guestsAtTable = getOccupants(table);
        Map<String,Set<String>> map = new HashMap<String,Set<String>>();

        for (String name1 : guestsAtTable)
        {
            for (String name2 : guestsAtTable) {
                if (book.isViolation(name1, name2))  // violation detected
                {
                    Set<String> violations = map.get(name1);
                    if (violations == null) {
                        violations = new HashSet<String>();
                        map.put(name1, violations);
                    }
                    violations.add(name2);
                }
            }
        }

        return map;
    }


    /**
     * Assign party to specified table. Current guests are "squeezed" together. The party
     * is then assigned starting at the first empty seat.  If there aren't enough empty seats, the party
     * is assigned so as to use as many of the free seats as possible.
     * @param party
     * @param table
     */
    public void assignParty(String party, int table)
    {
        int sz = book.getPartySize(party);
        int seat = compressTable(table);
        assignParty(party, table, seat);
    }


    public void unassignParty(String party) {
        Set<Guest> guests = book.getParty(party);
        for (Guest g : guests)
            unassignGuest(g.getName());
    }



    public int emptySeats(int table)
    {
        int count = 0;
        for (int j=0; j<seatsPerTable; j++)
            if (plan[table][j].equals(EMPTY))
                count++;
        return count;
    }



    public int filledSeats(int table)
    {
        return seatsPerTable - emptySeats(table);
    }

    public boolean isTableFull(int table) { return filledSeats(table)==seatsPerTable; }

    public boolean isTableEmpty(int table)
    {
        return filledSeats(table)==0;
    }

    /**
     * Find the first table that fits a party of specified size.
     * If no table exists, return -1
     * @param partySize
     * @return -1 if no table exists, otherwise table id (0...maxTables-1)
     */
    public int firstAvailableTable(int partySize)
    {
        for (int t=0; t<maxTables; t++)
            if (emptySeats(t)>=partySize)
                return t;
        return -1;
    }


    /**
     * Get list of unassigned guests
     * @return
     */
    public Set<String> unassignedGuests()
    {
        Set<String> unassigned = new HashSet<String>();
        for (String name : book.getGuestList())
            if (isSeated(name)==false)
                unassigned.add(name);
        return unassigned;
    }

    /**
     * Get list of unassigned parties
     * @return
     */
    public Set<String> unassignedParties()
    {
        Set<String> unassigned = new HashSet<String>();
        Set<String> unassignedGuests = unassignedGuests();
        for (String name : unassignedGuests)
            unassigned.add(book.getGuest(name).getParty());
        return unassigned;
    }




    private void printTable(int table)
    {
        Map<String,Set<String>> violations = tableViolationsMap(table);
        System.out.println("\nTABLE "+(table+1)+" has "+filledSeats(table)+" guests. ");

        int numViolations = violations.keySet().size();
        if (numViolations==1) System.out.println("WARNING: "+numViolations+" seating violation!");
        else if (numViolations>1) System.out.println("WARNING: "+numViolations+" seating violations!");


        for (int i=0; i<seatsPerTable; i++)
        {
            String name = getOccupant(table, i);
            if (name.equals(EMPTY)==false)
            {
                Guest g = book.getGuest(name);
                String party = g.getParty();
                String sex = g.getSex();
                int age = g.getAge();
                String cats = g.getCategories().toString();
                Set<String> viol = violations.get(name);
                String vList = "";
                if (viol!=null) vList = viol.toString();
                System.out.println(String.format("\t%10s%10s%3s%5d%50s%30s",name,party,sex,age,cats,vList));
            }

        }
    }


    protected void print()
    {
        for (int t=0; t<maxTables; t++)
            if (isTableEmpty(t)==false)
                printTable(t);
    }


    protected void doClone(Solution solution)
    {
        // shallow copy of key objects and values
        SeatingPlan sp = (SeatingPlan) solution;
        sp.book = book;
        sp.config = config;
        sp.maxTables = maxTables;
        sp.seatsPerTable = seatsPerTable;

        // deep copy of the rest
        sp.plan = new String[maxTables][seatsPerTable];
        for (int i=0; i<maxTables; i++)
            for (int j=0; j<seatsPerTable; j++)
                sp.plan[i][j] = plan[i][j];

        sp.assignedTable = new HashMap<String,Integer>();
        for (String key : assignedTable.keySet())
            sp.assignedTable.put(key, assignedTable.get(key));

        sp.assignedSeat = new HashMap<String, Integer>();
        for (String key : assignedSeat.keySet())
            sp.assignedSeat.put(key, assignedSeat.get(key));

    }

}
