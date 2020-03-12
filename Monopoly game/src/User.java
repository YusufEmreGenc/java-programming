public abstract class User
{
    private int money;
    private String name;

    public User(int money, String name) {
        this.money = money;
        this.name = name;
    }

    public int getMoney()   {   return money;   }

    public void setMoney(int money) {   this.money = money; }

    public String getName() {   return name;    }

    public void setName(String name)    {   this.name = name;   }

    public void collectMoney(int money)     {   this.money += money;    }

    public boolean payMoney(int money)
    {
        if((this.money - money) < 0 )  return false;
        else
        {
            this.money -= money;
            return true;
        }
    }
}
