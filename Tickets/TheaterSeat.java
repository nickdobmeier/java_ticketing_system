// Nicholas Dobmeier
// njd170130

package Tickets;

public class TheaterSeat extends BaseNode
{
    private TheaterSeat UpNext;                         // default value for java class variables is null
    private TheaterSeat DownNext;
    private TheaterSeat LeftNext;
    private TheaterSeat RightNext;
                                                        // pass the data received to the BaseNode constructor
    TheaterSeat(int seatRow, char seatColumn, char ticketType){
        super(seatRow,seatColumn,ticketType);
    }

                                                        // necessary getters and setters for each data type below
    public TheaterSeat getUp() {return UpNext;}
    public TheaterSeat getDown() {return DownNext;}
    public TheaterSeat getLeft() {return LeftNext;}
    public TheaterSeat getRight() {return RightNext;}


    public void setUp(TheaterSeat up) { UpNext = up; }

    public void setDown(TheaterSeat down) { DownNext = down; }

    public void setLeft(TheaterSeat left) { LeftNext = left; }

    public void setRight(TheaterSeat right) { RightNext = right; }

}
