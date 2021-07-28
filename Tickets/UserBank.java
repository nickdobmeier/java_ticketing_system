// Nicholas Dobmeier
// njd170130

package Tickets;

import java.util.ArrayList;
import java.util.LinkedList;

public class UserBank implements Comparable<UserBank>
{
    private String userName;                                                                                            // variables to store username/password
    private String password;
    private LinkedList<CustomerOrder> customerOrders = new LinkedList<CustomerOrder>();                                             // linked list to store all order objects, which each represent a different order
    private boolean isAdmin=false;

    UserBank(String userName, String password)                                                                          // constructor requires a username and password
    {
        setUserName(userName);                                                                                          // save data
        setPassword(password);
        if(this.userName.equals("admin")){                                                                              // determine if is admin based on username
            isAdmin = true;
        }
    }


    public String getUserName() {                                                                                       // necessary getters and setters
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdmin(boolean isTrue){
        isAdmin = true;
    }
    public boolean isAdmin(){
        return isAdmin;
    }



    public boolean addOrder(CustomerOrder order){return customerOrders.add(order);}                                     // add an order to the linked list
    public CustomerOrder removeOrder(int index){                                                                        // remove an order from the linked list
        return customerOrders.remove(index);
    }
    public int getNumOrders(){                                                                                          // get size of linked list, aka num orders
        return customerOrders.size();
    }
    public CustomerOrder getOrder(int index){                                                                           // get a specific order object
        return customerOrders.get(index);
    }


    @Override
    public int hashCode(){
        return getUserName().hashCode();                                                                                // returns the hashcode of the username (calls string hashcode function)
    }

    @Override
    public int compareTo(UserBank otherUser){
        if(this.getPassword().compareTo(otherUser.getPassword()) == 0  &&  this.getUserName().compareTo(otherUser.getUserName()) == 0){     // if a user has same pw and username, they are equal
            return 0;
        }else{
            return -1;
        }
    }

    @Override
    public String toString(){
        String str ="\n";
        if(customerOrders.size() > 0){
            for(int i=0; i<customerOrders.size();i++){                                                                  // loop through each order and append detailed string onto what is already present
                str = str+"Order "+(i+1)+"\n\tAuditorium   "+customerOrders.get(i).getWhichAuditorium()+"\n\t\tSeats:\t"+customerOrders.get(i).toString() +"\n\t\tAdult Tickets:  "+customerOrders.get(i).getNumAdultTickets()+"\n\t\tChild Tickets:  "+customerOrders.get(i).getNumChildTickets()+"\n\t\tSenior Tickets: "+customerOrders.get(i).getNumSeniorTickets()+"\n\n";
            }
        }else{
            str = str+"NO order currently\n";
        }
        return str;                                                                                                     // return the (complete appended) string
    }

    public String printReciept(){
        String str = "---------------------------------------\nReceipt\n";
        double customerTotal=0.0;
        if(customerOrders.size() > 0)                                                                                   // if orders are present
        {
            for(int i=0; i<customerOrders.size();i++)
            {
                double orderTotal=0.0;
                str = str+"---------------------------------------\n";

                ArrayList<String> cols = (ArrayList<String>)customerOrders.get(i).getOrderRowCol().clone();             // get the string data
                str = str+"Order "+(i+1)+
                        "\n\tAuditorium   "+customerOrders.get(i).getWhichAuditorium()+
                        "\n\t\tSeats:";//+customerOrders.get(i).toString();
                for(int j=0;j<cols.size();j++)                                                                          // for each seat, determine if is adult, child, or senior, than save necessary data
                {
                    char thisSeat ='%';
                    double thisColTickCost=-1;

                    String tempStr = cols.get(j);
                    if(tempStr.charAt(tempStr.length()-1)== 'A'){
                        thisColTickCost = 10.00;
                        thisSeat='A';
                    }else if(tempStr.charAt(tempStr.length()-1)== 'C'){
                        thisColTickCost=5.0;
                        thisSeat='C';
                    }else if(tempStr.charAt(tempStr.length()-1)== 'S'){
                        thisColTickCost=7.5;
                        thisSeat='S';
                    }
                    orderTotal += thisColTickCost;                                                                      // add order cost to total cost
                    str = str+String.format("\n\t\t\t%-4s(%1c ticket)%4s$ %6.2f", tempStr.substring(0,tempStr.length()-1),thisSeat," ",thisColTickCost);    // append detailed string to whats already saved
                }

                customerTotal += orderTotal;
                if(customerOrders.get(i).getNumAdultTickets()>0) {                                                      // add number of each ticket type to receipt
                    str = str+"\n\t\tAdult Tickets:  "+customerOrders.get(i).getNumAdultTickets();
                }
                if(customerOrders.get(i).getNumChildTickets()>0) {
                    str = str+"\n\t\tChild Tickets:  "+customerOrders.get(i).getNumChildTickets();
                }
                if(customerOrders.get(i).getNumSeniorTickets()>0){
                    str = str+"\n\t\tSenior Tickets: "+customerOrders.get(i).getNumSeniorTickets();
                }
                str = str+String.format("\n\t\t%-22s$ %6.2f\n", "Order Total:", orderTotal);                            // append to comprehensive string
            }

        }else{
            str = str+"---------------------------------------\n"+"\t\tNO orders\n";
        }
        str = str+"---------------------------------------\n";
        str = str + String.format("%-30s$ %6.2f\n", "Overall Final Total: ", customerTotal);
        str = str+"---------------------------------------\n";

        return str;                                                                                                     // return comprehensive receipt string
    }
}
