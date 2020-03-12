import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

public class Main {

    // This function draws a card from deck and that card goes under the deck after operation
    public static String drawCard(ArrayList<String> deck)
    {
        String temp = deck.get(0);
        for(int i = 1; i < deck.size(); i++)
            deck.set(i - 1, deck.get(i));
        deck.set(deck.size()-1 , temp);

        return temp;
    }

    public static void showPlayer(PrintWriter outFile, Player player)
    {
        int i;
        outFile.format("%s\t%d\thave:", player.getName(), player.getMoney());
        if (player.getProperties().size() != 0) outFile.format(" ");
        for(i = 0; i < player.getProperties().size()-1; i++)
        {
            outFile.format("%s,", player.getProperties().get(i).getName());
        }
        if(player.getProperties().size() > 0)
            outFile.format("%s\n", player.getProperties().get(i).getName());
        else    outFile.format("\n");
    }

    // This function shows the state of game
    public static void show(PrintWriter outFile, Player player1, Player player2, Banker banker)
    {
        outFile.format("-----------------------------------------------------------------------------------------------------------\n");
        showPlayer(outFile, player1);
        showPlayer(outFile, player2);
        outFile.format("Banker\t%d\n", banker.getMoney());
        if(player1.getMoney() > player2.getMoney())
            outFile.format("Winner Player 1\n");
        else if(player1.getMoney() < player2.getMoney())
            outFile.format("Winner Player 2\n");
        else    outFile.format("Winner\tscoreless\n");
        outFile.format("-----------------------------------------------------------------------------------------------------------\n");
    }

    public static void main(String[] args) {
        ArrayList<Square> squares = new ArrayList<Square>();
        ArrayList<String> chanceList = new ArrayList<String>();
        ArrayList<String> communityChestList = new ArrayList<String>();
        JSONParser parser = new JSONParser();
        Scanner commandScanner = null;
        PrintWriter outFile = null;
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        Banker banker = new Banker("Banker");

        try
        {
            commandScanner = new Scanner(new File(args[0]));
            outFile = new PrintWriter("output.txt");

            //Defining action squares
            squares.add(new ActionSquare(3, "Community Chest"));
            squares.add(new ActionSquare(8, "Chance"));
            squares.add(new ActionSquare(18, "Community Chest"));
            squares.add(new ActionSquare(23, "Chance"));
            squares.add(new ActionSquare(34, "Community Chest"));
            squares.add(new ActionSquare(37, "Chance"));

            //Defining tax squares
            squares.add(new TaxSquare(5, "Income Tax"));
            squares.add(new TaxSquare(39, "Super Tax"));

            //Defining other undefined type of squares
            squares.add(new OtherSquare(1, "GO"));
            squares.add(new OtherSquare(11, "Jail"));
            squares.add(new OtherSquare(21, "Free Parking"));
            squares.add(new OtherSquare(31, "Go to Jail"));


            // Reading json file - properties
            Object obj = parser.parse(new FileReader("property.json"));
            JSONObject jsonObject = (JSONObject) obj;

            // Reading Lands
            JSONArray landArray = (JSONArray) jsonObject.get("1");
            Iterator<JSONObject> landIterator = landArray.iterator();
            while (landIterator.hasNext())
            {
                JSONObject jObj = landIterator.next();

                String id = (String) jObj.get("id");
                String name = (String) jObj.get("name");
                String cost = (String) jObj.get("cost");

                squares.add(new Land(Integer.parseInt(id), name, Integer.parseInt(cost)));
            }

            // Reading Rail Roads
            JSONArray railRoadArray = (JSONArray) jsonObject.get("2");
            Iterator<JSONObject> railRoadIterator = railRoadArray.iterator();
            while ((railRoadIterator.hasNext()))
            {
                JSONObject jObj = railRoadIterator.next();

                String id = (String) jObj.get("id");
                String name = (String) jObj.get("name");
                String cost = (String) jObj.get("cost");

                squares.add(new RailRoad(Integer.parseInt(id), name, Integer.parseInt(cost)));
            }

            // Reading Companies
            JSONArray companyArray = (JSONArray) jsonObject.get("3");
            Iterator<JSONObject> companyIterator = companyArray.iterator();
            while ((companyIterator.hasNext()))
            {
                JSONObject jObj = companyIterator.next();

                String id = (String) jObj.get("id");
                String name = (String) jObj.get("name");
                String cost = (String) jObj.get("cost");

                squares.add(new Company(Integer.parseInt(id), name, Integer.parseInt(cost)));
            }

            // Sorting squares of table in comparison with IDs
            Collections.sort(squares);


            // Reading json file - list
            obj = parser.parse(new FileReader("list.json"));
            jsonObject = (JSONObject) obj;

            // Reading chance deck
            JSONArray chanceListArray = (JSONArray) jsonObject.get("chanceList");
            Iterator<JSONObject> chanceListIterator = chanceListArray.iterator();
            while (chanceListIterator.hasNext())
            {
                String item = (String)chanceListIterator.next().get("item");
                chanceList.add(item);
            }

            // Reading community chest deck
            JSONArray communityChestListArray = (JSONArray) jsonObject.get("communityChestList");
            Iterator<JSONObject> communityChestListIterator = communityChestListArray.iterator();
            while (communityChestListIterator.hasNext())
            {
                String item = (String)communityChestListIterator.next().get("item");
                communityChestList.add(item);
            }

            // The commands are operated in this loop
            while (commandScanner.hasNext())
            {
                String[] line = commandScanner.nextLine().split(" ");
                if(line[0].equals("show()"))
                {
                    Main.show(outFile, player1, player2, banker);
                }
                else
                {
                    String[] command = line[1].split(";");
                    int dice = Integer.parseInt(command[1]);
                    Player playing;
                    Player notPlaying;
                    if(command[0].equals("1"))
                    {
                        outFile.format("Player 1\t");
                        playing = player1;
                        notPlaying = player2;
                    }
                    else if (command[0].equals("2"))
                    {
                        outFile.format("Player 2\t");
                        playing = player2;
                        notPlaying = player1;
                    }
                    else {
                        System.out.println("Unknown Player");
                        continue;
                    }

                    int oldLocation = playing.getLocation();    // to keep track of whether passes by go or not
                    outFile.format("%d\t", dice);

                    if(playing.getNumOfTourBanned() > 0)
                    {
                        if(playing.getNumOfTourBanned() == 3)   outFile.format("%d\t%d\t%d\t%s in jail (count=1)\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName());
                        else if(playing.getNumOfTourBanned() == 2)  outFile.format("%d\t%d\t%d\t%s in jail (count=2)\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName());
                        else if (playing.getNumOfTourBanned() == 1) outFile.format("%d\t%d\t%d\t%s in jail (count=3)\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName());
                        playing.decrementNumOfTourBanned();
                        continue;
                    }
                    playing.setLocation(playing.getLocation() + dice);      // Move player

                    if(playing.getLocation() > 40)  playing.setLocation(playing.getLocation() - 40);
                    if(oldLocation > playing.getLocation())
                    {
                        playing.collectMoney(200);
                        banker.payMoney(200);
                        if(playing.getLocation() == 11)     // If player comes the jail by passing by go, she refund the money taken
                        {
                            if( !(playing.payMoney(200)))
                            {
                                outFile.format("%d\t%d\t%d\t%s goes bankrupt\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName());
                                Main.show(outFile, player1, player2, banker);
                                outFile.close();
                                System.exit(0);
                            }
                        }
                    }
                    Square square = squares.get(playing.getLocation()-1);   // Current square

                    if(square instanceof Property)
                    {
                        if(((Property) square).getOwner() == null)  // Property has no owner
                        {
                            square.comingToPropertySquare(outFile, player1, player2, playing, notPlaying, banker, dice);
                            outFile.format("%d\t%d\t%d\t%s bought %s\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName(), square.getName());
                        }
                        else if(((Property) square).getOwner().getName().equals(playing.getName()))     // Player has the Property
                            outFile.format("%d\t%d\t%d\t%s has %s\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName(), square.getName());
                        else if (((Property) square).getOwner().getName().equals(notPlaying.getName())) // The other player has Property
                        {
                            square.comingToPropertySquare(outFile, player1, player2, playing, notPlaying, banker, dice);
                            outFile.format("%d\t%d\t%d\t%s paid rent for %s\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName(), square.getName());
                        }
                    }
                    else if(square instanceof ActionSquare)
                    {
                        String card = null;
                        if(square.getName().equals("Chance"))
                            card = Main.drawCard(chanceList);
                        else if (square.getName().equals("Community Chest"))
                            card = Main.drawCard(communityChestList);

                        if(square.getName().equals("Chance"))
                        {
                            if(card.equals("Advance to Go (Collect $200)") || card.equals("Pay poor tax of $15") || card.equals("Your building loan matures - collect $150") || card.equals("You have won a crossword competition - collect $100 "))
                            {
                                square.comingToActionSquare(outFile, card, player1, player2, playing, notPlaying, squares, banker, dice);
                                outFile.format("%d\t%d\t%d\t%s draw %s\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName(), card);
                            }
                            else if (card.equals("Advance to Leicester Square"))
                            {
                                if(((Property)(squares.get(27-1))).getOwner() == null)
                                {
                                    square.comingToActionSquare(outFile, card, player1, player2, playing, notPlaying, squares, banker, dice);
                                    outFile.format("%d\t%d\t%d\t%s draw %s %s bought %s\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName(), card, playing.getName(), squares.get(27-1).getName());
                                }
                                else if (((Property)(squares.get(27-1))).getOwner().getName().equals(playing.getName()))
                                {
                                    square.comingToActionSquare(outFile, card, player1, player2, playing, notPlaying, squares, banker, dice);
                                    outFile.format("%d\t%d\t%d\t%s draw %s %s has %s\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName(), card, playing.getName(), squares.get(27-1).getName());
                                }
                                else if (((Property)(squares.get(27-1))).getOwner().getName().equals(notPlaying.getName()))
                                {
                                    square.comingToActionSquare(outFile, card, player1, player2, playing, notPlaying, squares, banker, dice);
                                    outFile.format("%d\t%d\t%d\t%s draw %s %s paid rent for %s\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName(), card, playing.getName(), squares.get(27-1).getName());
                                }
                            }
                            else if (card.equals("Go back 3 spaces"))   // It evaluates all square types and operates
                            {
                                Square nextSquare = squares.get(playing.getLocation() -3 -1);
                                if(nextSquare instanceof Property)
                                {
                                    if(((Property) nextSquare).getOwner() == null)
                                    {
                                        square.comingToActionSquare(outFile, card, player1, player2, playing, notPlaying, squares, banker, dice);
                                        outFile.format("%d\t%d\t%d\t%s draw %s, %s bought %s\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName(), card, playing.getName(), nextSquare.getName());
                                    }
                                    else if( ((Property) nextSquare).getOwner().getName().equals(playing.getName()) )
                                    {
                                        square.comingToActionSquare(outFile, card, player1, player2, playing, notPlaying, squares, banker, dice);
                                        outFile.format("%d\t%d\t%d\t%s draw %s, %s has %s\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName(), card, playing.getName(), nextSquare.getName());
                                    }
                                    else if ( ((Property) nextSquare).getOwner().getName().equals(notPlaying.getName()) )
                                    {
                                        square.comingToActionSquare(outFile, card, player1, player2, playing, notPlaying, squares, banker, dice);
                                        outFile.format("%d\t%d\t%d\t%s draw %s, %s paid rent for %s\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName(), card, playing.getName(), nextSquare.getName());
                                    }
                                }
                                else if (nextSquare instanceof ActionSquare)
                                {
                                    String nextCard = null;
                                    if(nextSquare.getName().equals("Chance"))
                                        nextCard = Main.drawCard(communityChestList);
                                    else if (nextSquare.getName().equals("Community Chest"))
                                        nextCard = Main.drawCard(communityChestList);
                                    nextSquare.comingToActionSquare(outFile, nextCard, player1, player2, playing, notPlaying, squares, banker, dice);
                                    outFile.format("%d\t%d\t%d\t%s draw %s, %s draw %s\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName(), card, playing.getName(), nextCard);
                                }
                                else if (nextSquare instanceof TaxSquare)
                                {
                                    nextSquare.comingToActionSquare(outFile, card, player1, player2, playing, notPlaying, squares, banker, dice);
                                    outFile.format("%d\t%d\t%d\t%s draw %s, paid Tax\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName(), card);
                                }
                            }
                        }
                        else if(square.getName().equals("Community Chest"))
                        {
                            if( card.equals("Advance to Go (Collect $200)") || card.equals("Bank error in your favor - collect $75") || card.equals("Doctor's fees - Pay $50") || card.equals("It is your birthday Collect $10 from each player") || card.equals("Grand Opera Night - collect $50 from every player for opening night seats") || card.equals("Income Tax refund - collect $20") || card.equals("Life Insurance Matures - collect $100") || card.equals("Pay Hospital Fees of $100") || card.equals("Pay School Fees of $50") || card.equals("You inherit $100") || card.equals("From sale of stock you get $50") )
                            {
                                square.comingToActionSquare(outFile, card, player1, player2, playing, notPlaying, squares, banker, dice);
                                outFile.format("%d\t%d\t%d\t%s draw %s\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName(), card);
                            }
                        }
                    }
                    else if(square instanceof OtherSquare)
                    {
                        if(square.getName().equals("Jail"))
                        {
                            square.comingToOtherSquare(outFile, playing);
                            outFile.format("%d\t%d\t%d\t%s went to jail\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName());
                        }
                        else if (square.getName().equals("GO"))
                        {
                            square.comingToOtherSquare(outFile, playing);
                            outFile.format("%d\t%d\t%d\t%s is in GO square\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName());
                        }
                        else if (square.getName().equals("Go to Jail"))
                        {
                            square.comingToOtherSquare(outFile, playing);
                            outFile.format("%d\t%d\t%d\t%s went to jail\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName());
                        }
                        else if (square.getName().equals("Free Parking"))
                        {
                            square.comingToOtherSquare(outFile, playing);
                            outFile.format("%d\t%d\t%d\t%s is in Free Parking\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName());
                        }
                    }
                    else if(square instanceof TaxSquare)
                    {
                        square.comingToTaxSquare(outFile, player1, player2, playing, notPlaying, banker);
                        outFile.format("%d\t%d\t%d\t%s paid Tax\n", playing.getLocation(), player1.getMoney(), player2.getMoney(), playing.getName());
                    }
                }
            }
            Main.show(outFile, player1, player2, banker);
            outFile.close();
        }
        catch (FileNotFoundException e)   {   e.printStackTrace();    }
        catch (IOException e)   {   e.printStackTrace();    }
        catch (ParseException e)    {   e.printStackTrace();    }
        catch (Exception e)    {   e.printStackTrace();    }
        finally {   if (outFile != null) outFile.close();   }
    }
}
