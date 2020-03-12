public class Property extends Square
{
    private int cost;
    private Player owner;
    private boolean hasOwner;

    public Property(int id, String name, int cost) {
        super(id, name);
        this.cost = cost;
        owner = null;
        hasOwner = false;
    }

    public int getCost() {
        return cost;
    }

    public boolean isHasOwner() {   return hasOwner;    }

    public void setHasOwner(boolean hasOwner)   {   this.hasOwner = hasOwner;   }

    public void setOwner(Player owner)  {   this.owner = owner; }

    public Player getOwner()    {   return owner;   }
}
