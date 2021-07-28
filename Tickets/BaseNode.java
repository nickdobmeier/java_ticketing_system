// Nicholas Dobmeier
// njd170130

package Tickets;

public abstract class BaseNode {
    private int seatRow;
    private char seatColumn;
    private boolean isReserved=false;   // default false
    private char ticketType;


    BaseNode(int seatRow, char seatColumn, char ticketType){            // constructor to build the seat data
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.ticketType = ticketType;
        if(ticketType != '.'){                                          // depending on ticket type passed, set isReserved
            isReserved= true;
        }
    }

                                                                        // necessary getters and setters
    public int getSeatRow() { return seatRow;}
    public char getSeatColumn() { return seatColumn;}
    public boolean isReserved() { return isReserved;}
    public char getTicketType() { return ticketType;}


    public void setSeatRow(int seatRow) {
        this.seatRow = seatRow;
    }

    public void setSeatColumn(char seatColumn) {
        this.seatColumn = seatColumn;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public void setTicketType(char ticketType)
    {
        this.ticketType = ticketType;                                   // upon setting ticket type, check is need to change isReserved value too
        if(ticketType != '.'){
            setReserved(true);
        }
    }



}
