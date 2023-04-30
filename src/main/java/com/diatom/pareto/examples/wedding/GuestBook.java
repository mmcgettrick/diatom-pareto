package com.diatom.pareto.examples.wedding;

import com.diatom.pareto.Resource;
import org.apache.commons.collections.CollectionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * Created by rachlin on 4/5/16.
 */
public class GuestBook extends Resource {

    Map<String, Guest> book;
    Set<String> categories;
    Map<String,Set<Guest>> party2guest;  // party name -> guests
    Map<Guest,String> guest2party;       // guest -> party name

    Set<String> compatible; // name1|name2 pairs that are compatible based on categories
    Set<String> violations; // name1|name2 pairs that create table violations

    public GuestBook(Map<String, Object> options)
    {
        super(options);
        book = new HashMap<String, Guest>();
        categories = new HashSet<String>();
        party2guest = new HashMap<String,Set<Guest>>();
        guest2party = new HashMap<Guest,String>();
        compatible = new HashSet<String>();
        violations = new HashSet<String>();

        String filename = (String) options.get("filename");
        readFromFile(filename);

        for (Guest g1 : book.values())
            for (Guest g2 : book.values()) {
                if (CollectionUtils.containsAny(g1.getCategories(), g2.getCategories())) {
                    compatible.add(g1.getName() + "|" + g2.getName());
                    compatible.add(g2.getName() + "|" + g1.getName());
                }
                if (g1.getExclusions().contains(g2.getName()))
                    violations.add(g1.getName()+"|"+g2.getName());
            }

    }


    public boolean areCompatible(String name1, String name2)
    {
        return compatible.contains(name1+"|"+name2);
    }

    public boolean isViolation(String name1, String name2) { return violations.contains(name1+"|"+name2); }


    public void addGuest(Guest g)
    {
        book.put(g.getName(), g);
        categories.addAll(g.getCategories());
        guest2party.put(g, g.getParty());
        Set<Guest> members = party2guest.get(g.getParty());
        if (members == null)
        {
            members = new HashSet<Guest>();
            party2guest.put(g.getParty(), members);
        }
        members.add(g);
    }


    public Guest getGuest(String guestName)
    {
        return book.get(guestName);
    }

    /**
     * Get guests that belong to specified party
     * @param partyName
     * @return
     */


    public Set<Guest> getParty(String partyName)
    {
        return party2guest.get(partyName);
    }

    public int getPartySize(String partyName)
    {
        Set<Guest> guests = getParty(partyName);
        if (guests==null)
            return 0;
        else return guests.size();
    }

    public List<String> getParties()
    {
        return new ArrayList(party2guest.keySet());
    }

    public List<String> getGuestList()
    {
        return new ArrayList(book.keySet());
    }


    private List<String> parseTags(String s)
    {
        List<String> tags = new ArrayList<String>();
        String[] tokens = s.split(",");
        if ((tokens == null) && (tokens.length==0))
            return tags;

        for (int i=0; i<tokens.length; i++)
            tags.add(tokens[i].trim().toUpperCase());

        return tags;

    }



    private int readFromFile(String filename)
    {
        int guests = 0;
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
            in.readLine(); // skip header
            String line = in.readLine();
            while (line != null)
            {
                String[] tokens = line.split("\t");
                if ((tokens != null) && (tokens.length>0)) {
                    //System.out.println(tokens.length + "\t" + line);
                    guests++;
                    String name = tokens[0].trim();
                    String party = tokens[1];
                    if ((party == null) || (party.equals("")))
                        party = name;
                    else party = party.trim();
                    String sex = tokens[2].trim();
                    int age = new Integer(tokens[3]).intValue();

                    Guest g = new Guest(name, party, sex, age);

                    // parse categories
                    if (tokens.length >=5)
                        g.addCategories(parseTags(tokens[4]));

                    if (tokens.length >=6)
                        g.addExclusions(parseTags(tokens[5]));

                    if (tokens.length >=7)
                        g.addInclusions(parseTags(tokens[6]));


                    addGuest(g);

                }
                line = in.readLine();
            }
        } catch (Exception e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return guests;
    }

    public static void main(String[] args)
    {
        GuestBook gb = new GuestBook(null);
        gb.readFromFile("/home/rachlin/diatom/wedding/guestbook.csv");
        System.out.println(gb.getGuestList().size());
        System.out.println(gb.getGuest("G100").getInclusions().toString());
        System.out.println(gb.getGuest("G96").getCategories().toString());
        System.out.println(gb.getGuest("G1").getExclusions().toString());
        System.out.println(gb.getGuest("G2").getExclusions().toString());
    }
}
