import java.io.PrintWriter;
import java.util.ArrayList;

public class Player extends User
{
    private int location;
    private ArrayList<Property> properties = new ArrayList<Property>();
    private int numOfRailRoadOwned;     // It is for rent calculation to be payed by other players
    private int numOfTourBanned;

    public Player(String name) {
        super(15000, name);
        location = 1;
        numOfRailRoadOwned = 0;
        numOfTourBanned = 0;
    }

    public int getLocation()    {   return location;    }

    public void setLocation(int location)   {   this.location = location;   }

    public int getNumOfRailRoadOwned()  {   return numOfRailRoadOwned;  }

    public void incrementNumOfRailRoadOwned()   {   this.numOfRailRoadOwned++;   }

    public int getNumOfTourBanned() {   return numOfTourBanned; }

    public void decrementNumOfTourBanned() {   this.numOfTourBanned -= 1; }

    public void setNumOfTourBanned(int numOfTourBanned) {   this.numOfTourBanned = numOfTourBanned; }

    public ArrayList<Property> getProperties()  { return properties;    }

    // If player can buy the property, the function sells and retruns true. If not returns false
    public boolean tryToBuyProperty(PrintWriter outFile, Property property, Banker banker, Player notPlaying)
    {
        int cost = property.getCost();
        if(this.getMoney() >= cost)     //buy property
        {
            this.payMoney(cost);
            banker.collectMoney(cost);

            properties.add(property);
            property.setHasOwner(true);
            property.setOwner(this);

            if(property instanceof RailRoad)
                incrementNumOfRailRoadOwned();

            return true;
        }
        else    return false;

    }

    public void payForRent(PrintWriter outFile, Property property, Player player1, Player player2, Player owner, Banker banker, int dice)
    {
        int cost = property.getCost();
        int rent = 0;
        if(property instanceof Land)
        {
            if(cost >= 0 && cost <= 2000)
                rent = (int) (cost * 0.4);
            else if(cost >= 2001 && cost <= 3000)
                rent = (int) (cost * 0.3);
            else if(cost >= 3001 && cost <= 4000)
                rent = (int) (cost * 0.35);
        }
        else if(property instanceof Company)
            rent = dice * 4;
        else if(property instanceof RailRoad)
            rent = 25 * owner.getNumOfRailRoadOwned();

        if( !(this.payMoney(rent)) )
        {
            outFile.format("%d\t%d\t%d\t%s goes bankrupt\n", this.getLocation(), player1.getMoney(), player2.getMoney(), this.getName());
            Main.show(outFile, player1, player2, banker);
            outFile.close();
            System.exit(0);
        }
        else
            owner.collectMoney(rent);
    }
}
