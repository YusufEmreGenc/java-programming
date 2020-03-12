import java.io.PrintWriter;
import java.util.ArrayList;

public abstract class Square implements Comparable<Square>
{
    private int id;
    private String name;

    public Square(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Square o)
    {
        int id = o.getId();
        return this.id - id;
    }

    public void comingToPropertySquare(PrintWriter outFile, Player player1, Player player2, Player player, Player notPlaying, Banker banker, int dice)
    {
        Property property = (Property) this;
        if(property.isHasOwner())     // Paying for Rent
        {
            if(((Property) this).getOwner().getName().equals(player.getName())) return;
            player.payForRent(outFile, property, player1, player2, property.getOwner(), banker, dice);
        }
        else    // Buy or Bankrupt
        {
            // If this is true, calling function already bought the property
            if( !(player.tryToBuyProperty(outFile, property, banker, notPlaying)) ) //Bankrupt
            {
                outFile.format("%d\t%d\t%d\t%s goes bankrupt\n", player.getLocation(), player1.getMoney(), player2.getMoney(), player.getName());
                Main.show(outFile, player1, player2, banker);
                outFile.close();
                System.exit(0);
            }
        }
    }

    public void comingToActionSquare(PrintWriter outFile, String card, Player player1, Player player2, Player player, Player otherPlayer, ArrayList<Square> squares, Banker banker, int dice)
    {
        if(this.getName().equals("Chance"))
        {
            // Card options
            if(card.equals("Advance to Go (Collect $200)"))
            {
                player.setLocation(1);
                player.collectMoney(200);
                banker.payMoney(200);
            }
            else if(card.equals("Advance to Leicester Square"))     // Check here again because idk it will pass from Go or not
            {
                if(player.getLocation() > 27)
                {
                    player.collectMoney(200);
                    banker.payMoney(200);
                }
                player.setLocation(27);
                squares.get(27-1).comingToPropertySquare(outFile, player1, player2, player, otherPlayer, banker, dice);
            }
            else if(card.equals("Go back 3 spaces"))
            {
                player.setLocation(player.getLocation() - 3);
                Square square = squares.get(player.getLocation()-1);
                if(square instanceof Property)
                    square.comingToPropertySquare(outFile, player1, player2, player, otherPlayer, banker, dice);
                else if (square instanceof ActionSquare)
                    ;
                else if (square instanceof TaxSquare)
                    square.comingToTaxSquare(outFile, player1, player2, player, otherPlayer, banker);
            }
            else if(card.equals("Pay poor tax of $15"))
            {
                if( !(player.payMoney(15)) )
                {
                    outFile.format("%d\t%d\t%d\t%s goes bankrupt\n", player.getLocation(), player1.getMoney(), player2.getMoney(), player.getName());
                    Main.show(outFile, player1, player2, banker);
                    outFile.close();
                    System.exit(0);
                }
                banker.collectMoney(15);
            }
            else if (card.equals("Your building loan matures - collect $150"))      // Be sure what if she has not property
                player.collectMoney(150);
            else if(card.equals("You have won a crossword competition - collect $100 "))
                player.collectMoney(100);
        }
        else if(this.getName().equals("Community Chest"))
        {
            // Card options
            if(card.equals("Advance to Go (Collect $200)"))
            {
                player.setLocation(1);
                player.collectMoney(200);
                banker.payMoney(200);
            }
            else if (card.equals("Bank error in your favor - collect $75"))
            {
                player.collectMoney(75);
                banker.payMoney(75);
            }
            else if (card.equals("Doctor's fees - Pay $50"))
            {
                if( !(player.payMoney(50)) )
                {
                    outFile.format("%d\t%d\t%d\t%s goes bankrupt\n", player.getLocation(), player1.getMoney(), player2.getMoney(), player.getName());
                    Main.show(outFile, player1, player2, banker);
                    outFile.close();
                    System.exit(0);
                }
            }
            else if (card.equals("It is your birthday Collect $10 from each player"))
            {
                if( !(otherPlayer.payMoney(10)) )
                {
                    outFile.format("%d\t%d\t%d\t%s goes bankrupt\n", player.getLocation(), player1.getMoney(), player2.getMoney(), player.getName());
                    Main.show(outFile, player1, player2, banker);
                    outFile.close();
                    System.exit(0);
                }
                player.collectMoney(10);
            }
            else if (card.equals("Grand Opera Night - collect $50 from every player for opening night seats"))
            {
                if( !(otherPlayer.payMoney(50)) )
                {
                    outFile.format("%d\t%d\t%d\t%s goes bankrupt\n", player.getLocation(), player1.getMoney(), player2.getMoney(), player.getName());
                    Main.show(outFile, player1, player2, banker);
                    outFile.close();
                    System.exit(0);
                }
                player.collectMoney(50);
            }
            else if (card.equals("Income Tax refund - collect $20"))
                {
                    player.collectMoney(20);
                    banker.payMoney(20);
                }
            else if (card.equals("Life Insurance Matures - collect $100"))
            {
                player.collectMoney(100);
            }
            else if (card.equals("Pay Hospital Fees of $100"))
            {
                if( !(player.payMoney(100)) )
                {
                    outFile.format("%d\t%d\t%d\t%s goes bankrupt\n", player.getLocation(), player1.getMoney(), player2.getMoney(), player.getName());
                    Main.show(outFile, player1, player2, banker);
                    outFile.close();
                    System.exit(0);
                }
            }
            else if (card.equals("Pay School Fees of $50"))
            {
                if( !(player.payMoney(50)) )
                {
                    outFile.format("%d\t%d\t%d\t%s goes bankrupt\n", player.getLocation(), player1.getMoney(), player2.getMoney(), player.getName());
                    Main.show(outFile, player1, player2, banker);
                    outFile.close();
                    System.exit(0);
                }
            }
            else if (card.equals("You inherit $100"))
            {
                player.collectMoney(100);
            }
            else if (card.equals("From sale of stock you get $50"))
            {
                player.collectMoney(50);
            }
        }
    }

    public void comingToOtherSquare(PrintWriter outFile, Player player)
    {
        if(this.getName().equals("Jail"))
            player.setNumOfTourBanned(3);
        else if (this.getName().equals("GO"))
            ;
        else if (this.getName().equals("Free Parking"))
            ;
        else if (this.getName().equals("Go to Jail"))
        {
            player.setLocation(11);
            player.setNumOfTourBanned(3);
        }
    }

    public void comingToTaxSquare(PrintWriter outFile, Player player1, Player player2, Player player, Player otherPlayer, Banker banker)
    {
        if( !(player.payMoney(100)) )
        {
            outFile.format("%d\t%d\t%d\t%s goes bankrupt\n", player.getLocation(), player1.getMoney(), player2.getMoney(), player.getName());
            Main.show(outFile, player1, player2, banker);
            outFile.close();
            System.exit(0);
        }
        banker.collectMoney(100);
    }
}
