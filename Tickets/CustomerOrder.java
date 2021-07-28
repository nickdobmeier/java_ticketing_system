// Nicholas Dobmeier
// njd170130

package Tickets;


import java.util.ArrayList;

public class CustomerOrder
{
    private int whichAuditorium=-1;

    private ArrayList<String> orderRowCol = new ArrayList<String>();                                                         // variable to store all the seats (and type) within the order

    private int numAdultTickets=0;                                                                                      // variable to store the number of each type in this order
    private int numChildTickets=0;
    private int numSeniorTickets=0;

                                                                                                                        // constructor requires all the necessary data
    CustomerOrder(int whichAuditorium, int numAdultTickets, int numChildTickets, int numSeniorTickets, ArrayList<String> orderRowCol)
    {
        setWhichAuditorium(whichAuditorium);                                                                            // set each class variable based on what was recieved
        setNumAdultTickets(numAdultTickets);
        setNumChildTickets(numChildTickets);
        setNumSeniorTickets(numSeniorTickets);
        setOrderRowCol(orderRowCol);
    }




    public ArrayList<String> getOrderRowCol() {return orderRowCol;}                                                     // return the seat array variable
    public void setOrderRowCol(ArrayList<String> orderRowCol) {
        if(orderRowCol !=null && orderRowCol.size()!=0) {                                                               // if passed in variable holds values in atleast 1 index
            this.orderRowCol = (ArrayList<String>)orderRowCol.clone();                                                  // copy, THEN store it
        }
    }
    public int isSeatInOrder(String possibleSeat){
        if(possibleSeat.length() == 2 && orderRowCol.size()!=0){                                                        // make sure the input string is of length 2
            for(int i=0;i<orderRowCol.size();i++){                                                                      // loop through each seat
                if(possibleSeat.compareTo(orderRowCol.get(i).substring(0,orderRowCol.get(i).length()-1)) == 0){         // if the first 2 indexes of string at specified index in array are equal to what was passed in
                    return i;                                                                                           // return the index
                }
            }
        }
        return -1;                                                                                                      // else return -1
    }
    public String removeASeatInOrder(int index){
        return orderRowCol.remove(index);                                                                               // remove a specified index
    }
    public int numSeatsInOrder(){
        if(orderRowCol.size() == (numAdultTickets+numChildTickets+numSeniorTickets)){                                   // if total ticket count is equal to size of array (which it will)
            return orderRowCol.size();                                                                                  // return the size of array
        }
        //System.out.println("\n*************NOOOOOO******************");
        return -1;
    }


    public String getIndex(int index){                                                                                  // get a specified index
        return orderRowCol.get(index);
    }
    public boolean addASeatToOrder(String seatToAdd){                                                                   // add a seat to the seat array
        return orderRowCol.add(seatToAdd);
    }



    public int getWhichAuditorium() {return whichAuditorium;}                                                           // necessary getters and setters
    public void setWhichAuditorium(int whichAuditorium) {this.whichAuditorium = whichAuditorium;}
                                                                                                                        // += used below for easier calls
    public int getNumAdultTickets() {return numAdultTickets;}
    public void setNumAdultTickets(int numAdultTickets) {
        this.numAdultTickets += numAdultTickets;
    }

    public int getNumChildTickets() {return numChildTickets;}
    public void setNumChildTickets(int numChildTickets) {
        this.numChildTickets += numChildTickets;
    }

    public int getNumSeniorTickets() {return numSeniorTickets;}
    public void setNumSeniorTickets(int numSeniorTickets) {
        this.numSeniorTickets += numSeniorTickets;
    }




    @Override
    public String toString()                                                                                            // returns the data for each seat in the order (2A, 2B,...)
    {
        String toReturn = "";

        for (int i=0; i<orderRowCol.size(); i++){
            toReturn = toReturn + orderRowCol.get(i).substring(0, orderRowCol.get(i).length()-1)+"   ";
        }
        return toReturn;
    }
}
