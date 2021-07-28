// Nicholas Dobmeier
// njd170130

package Tickets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Auditorium
{
    private TheaterSeat headFirstSeat = null;
    private int rowLength = 0;
    private int lineCounter =0;
    private String fileName;


    Auditorium(String fileName) throws FileNotFoundException
    {
        this.fileName = fileName;
        File theFile = new File(fileName);
        Scanner inputFromFile = new Scanner(theFile);                                                           // THIS is where FNF exception is thrown if file DNE
        if( theFile.length() == 0 ){throw new FileNotFoundException("File \""+fileName+"\' is EMPTY");}

        while (inputFromFile.hasNext())                                                                         // while the file has a next line
        {
            lineCounter++;                                                                                      // increment line counter, starting at 0
            String tempStr = inputFromFile.nextLine();                                                          // read in the entire line from the file

            for(int i=0; i<tempStr.length();i++)                                                                // for each char in the string
            {                                                                                                   // BASE NODE ONLY, headFirstSeat is only null for very first seat
                if(headFirstSeat == null) {
                    headFirstSeat = new TheaterSeat(lineCounter, (char)(i+65), tempStr.charAt(i));
                }

                else{                                                                                           // for every OTHER seat
                    TheaterSeat currentSeat = new TheaterSeat(lineCounter, (char)(i+65), tempStr.charAt(i));    // create a seat object

                    if(i==0){
                                                                                                                // for the first column of each newline
                        TheaterSeat upNeighbor = headFirstSeat;                                                 // i ==0 in the first column
                        for(int j=1; j<lineCounter-1;j++){                                                      // loop down from the head to the first column in given row
                            upNeighbor = upNeighbor.getDown();
                        }
                        upNeighbor.setDown(currentSeat);                                                        // set corresponding pointer values
                        currentSeat.setUp(upNeighbor);
                    }
                    else{                                                                                       // for every other column
                                                                                                                // currentSeat will connect to node to its LEFT. Then set that left nodes right neighbor.
                        TheaterSeat leftNeighbor = getLeftNeighbor(currentSeat);                                // call getLeftNeighbor to obtain the left neighbor of the current seat
                        currentSeat.setLeft(leftNeighbor);                                                      // set corresponding pointer values
                        leftNeighbor.setRight(currentSeat);

                        if (lineCounter > 1){                                                                   // if on at least second line
                                                                                                                // currentSeat will connect to node ABOVE IT
                            TheaterSeat upNeighbor = getLeftNeighbor(currentSeat);
                            upNeighbor = upNeighbor.getUp().getRight();                                         // have to go up THEN right, as the upNeighbor.getRight is currentSeat, which does NOT have an up yet

                            currentSeat.setUp(upNeighbor);
                            upNeighbor.setDown(currentSeat);                                                    // if an object has an up neighbor, that up neighbor therefore has a down neighbor***
                        }
                    }
                }
                if(lineCounter == 1){                                                                           // increment row counter only on the first line of the file
                    rowLength++;
                }
            }

        }

    }
    public TheaterSeat getHeadFirstSeat() {return headFirstSeat;}                                               // necessary getters and setters
    public void setHeadFirstSeat(TheaterSeat headFirstSeat) {
        this.headFirstSeat = headFirstSeat;
    }
    public int getRowLength() {
        return rowLength;
    }
    public int getLineCounter(){ return lineCounter; }



    private TheaterSeat getLeftNeighbor(TheaterSeat currentSeat)
    {
        int row = currentSeat.getSeatRow();                                                                     // get the data from the seat passed
        int column = currentSeat.getSeatColumn()-64;                                                            // -64 **** since 'A'-64 = 1, thats what need (not dealing w/ indexes)

        TheaterSeat stored = headFirstSeat;                                                                     // loop until on correct row
        for(int i =1; i<row; i++){
            stored = stored.getDown();
        }
        for(int i=1; i<column-1; i++){                                                                          // loop until at the left neighbor
            stored = stored.getRight();
        }
        return stored;                                                                                          // return the left neighbor
    }



    public TheaterSeat getSeat(int row, int column)
    {
        TheaterSeat theSeatWanted = headFirstSeat;
        for(int i =1; i<row; i++){                                                                              // loop until at the right row
            theSeatWanted = theSeatWanted.getDown();
        }
        for(int i=1; i<column; i++){                                                                            // loop until at the correct column the user wants
            theSeatWanted = theSeatWanted.getRight();
        }
        return theSeatWanted;                                                                                   // return the seat the user wants
    }

    public boolean checkAvailability(int row, int firstCol, int numTickets)
    {
        TheaterSeat theSeatWanted = headFirstSeat;                                                              // loop until at the seat wated
        for(int i =1; i<row; i++){
            theSeatWanted = theSeatWanted.getDown();
        }
        for(int i=1; i<firstCol+numTickets; i++){                                                               // loop until at the number of tickets wanted + the first column wanted
            if(theSeatWanted == null)
                return false;
            if(i >=firstCol){                                                                                   // once looping through the seat the user wants, check that they all contain '.'
                if (theSeatWanted.getTicketType() != '.')
                    return false;                                                                               // if any are unavailable, return false
            }
            theSeatWanted = theSeatWanted.getRight();
        }
        return true;                                                                                            // if made it through all the checks, return true
    }


    public void reserveSeats(CustomerOrder theOrderToChange, int row, int firstCol, int adultTicketsWanted, int childTicketsWanted, int seniorTicketsWanted, UserBank theCurrentUser) {
        int totalTicketsWanted = adultTicketsWanted+childTicketsWanted+seniorTicketsWanted;                     // calculate total tickets
        TheaterSeat theSeatWanted = getSeat(row, firstCol);                                                     // get the first seat of the block of seats that is to be reserved

        System.out.print("\t-Seats Reserved: ");

        ArrayList<String> orderArray = new ArrayList<String>(totalTicketsWanted);
        CustomerOrder thisOrder = new CustomerOrder((fileName.charAt(1)-48),adultTicketsWanted, childTicketsWanted, seniorTicketsWanted, null);


        for(int i =1; i<=totalTicketsWanted; i++)
        {                                                                                                       // for each seat in the block
            char ticketTypeToSet='$';
            if(adultTicketsWanted-- > 0){                                                                       // if more adult tickets left to enter
                theSeatWanted.setTicketType('A');
                ticketTypeToSet='A';
            }else if(childTicketsWanted-- > 0){                                                                 // if more child tickets left to enter
                theSeatWanted.setTicketType('C');
                ticketTypeToSet='C';
            }else if(seniorTicketsWanted-- > 0){                                                                // if more senior tickets left to enter
                theSeatWanted.setTicketType('S');
                ticketTypeToSet='S';
            }
            char temp = theSeatWanted.getSeatColumn();
            System.out.print(theSeatWanted.getSeatRow());
            System.out.print((temp)+"  ");

            orderArray.add((i-1), row +""+temp+ticketTypeToSet);

            theSeatWanted = theSeatWanted.getRight();                                                           // move to neighbor
        }
        System.out.println("\n");

        thisOrder.setOrderRowCol(orderArray);                                                                   // set the seat array for this order
        if(theOrderToChange == null) {
            theCurrentUser.addOrder(thisOrder);                                                                 // if NOT modifying an order, add it to the customer
        }else{
            theOrderToChange.setNumAdultTickets(thisOrder.getNumAdultTickets());                                // otherwise have to modify each value of the already given order
            theOrderToChange.setNumChildTickets(thisOrder.getNumChildTickets());
            theOrderToChange.setNumSeniorTickets(thisOrder.getNumSeniorTickets());
            for(int i=0; i<thisOrder.numSeatsInOrder(); i++){
                theOrderToChange.addASeatToOrder(thisOrder.getIndex(i));
            }
        }
    }

    public char unReserveSeat(int row, int column){
        TheaterSeat theSeat = this.getSeat(row, column);                                    // get seat based on passed parameters
        if(theSeat.isReserved()){                                                           // if the seat IS reserved
            char tempChar = theSeat.getTicketType();
            theSeat.setTicketType('.');                                                     // remove reserve data
            theSeat.setReserved(false);
            return tempChar;                                                                // return ticket type
        }else{
            return '$';
        }
    }



    public void displayAuditorium(boolean isHidden)
    {
        TheaterSeat theSeatWanted = headFirstSeat;                                                              // to display entire auditorium, must first start at the head
        int lineCounter =1;
        while(theSeatWanted != null)
        {
            if(lineCounter==1){                                                                                 // if first line has not been output, output the char signs that correspond to each column
                System.out.print("  ");
                for(int i=0;i<rowLength;i++)
                    System.out.print((char)(65+i));
                System.out.println();
            }

            int k=0;                                                                                            // will keep track of the column currently on
            while (theSeatWanted != null)
            {
                if(k++==0){                                                                                     // if first seat for given column has not been printed, print line number
                    System.out.printf("%-2d", lineCounter);
                }
                if(isHidden == true  && theSeatWanted.getTicketType() != '.' ){                                 // if wanted to be hidden and is reserved, print #
                    System.out.print('#');}
                else{
                    System.out.print(theSeatWanted.getTicketType());                                            // otherwise print the actual char there
                }
                theSeatWanted = theSeatWanted.getRight();
            }
            lineCounter++;
            theSeatWanted = headFirstSeat;                                                                      // move down to next row
            for(int j=1; j<lineCounter;j++){
                theSeatWanted = theSeatWanted.getDown();
            }
            System.out.println();
        }
    }


    public Object[] bestAvailable(int adultTicketsWanted, int childTicketsWanted, int seniorTicketsWanted)
    {
        int totalTicketsWanted = adultTicketsWanted+childTicketsWanted+seniorTicketsWanted;                     // calculate total tickets wanted
        Object [] bestAvailableRowCol = {-1,-1, Math.pow((rowLength*lineCounter),2)};                           // create default obj array to hold -1
        double [] exactMidpoint = {  ((lineCounter+1.0)/2.0) , ((rowLength+1.0)/2.0)    };                      // [row, col]

        TheaterSeat theSeatWanted = headFirstSeat;
        for(int i=1;i<=lineCounter; i++)                                                                        // will loop through each seat in the theatre
        {
            int validConsecutiveSeats = 0;
            for(int j=1;j<=rowLength; j++)                                                                      // loop through each column
            {
                    if (theSeatWanted.getTicketType() == '.') {                                                 // if seat is NOT reserved

                        validConsecutiveSeats++;                                                                // increment the variable counting num consecutive valid seats
                        if (validConsecutiveSeats >= totalTicketsWanted) {                                      // if number of valid consecutive seats is now equal or greater than tickets wanted
                            double thisIterationDistanceFromMiddle = Math.sqrt(Math.pow(Math.abs(i - exactMidpoint[0]), 2) + Math.pow(Math.abs(((j + (j-totalTicketsWanted+1.0)) / 2.0) - exactMidpoint[1]), 2));   // use distance formula to calculate distance from theatre midpoint

                            if (thisIterationDistanceFromMiddle < (Double)bestAvailableRowCol[2]) {             // if the distance just found is less than the currently saved best available
                                bestAvailableRowCol = new Object[]{theSeatWanted.getSeatRow(), (char)(theSeatWanted.getSeatColumn()-totalTicketsWanted+1), thisIterationDistanceFromMiddle}; // pass the FIRST seat of the block of new best available seats
                            }


                            else if(new Double(thisIterationDistanceFromMiddle).equals(bestAvailableRowCol[2]))     // if distances are the same
                            {
                                if(  Math.abs(new Double(i)-(exactMidpoint[0]) ) <    Math.abs(new Double((Integer)bestAvailableRowCol[0])-(exactMidpoint[0]) )   ){                         // row closet to middle
                                    bestAvailableRowCol = new Object[]{theSeatWanted.getSeatRow(), (char)(theSeatWanted.getSeatColumn()-totalTicketsWanted+1), thisIterationDistanceFromMiddle};
                                }
                                else{
                                    if(theSeatWanted.getSeatRow() < ((Integer) bestAvailableRowCol[0])) {                                                                                   // if tied for closest row, pick smaller row #
                                        bestAvailableRowCol = new Object[]{theSeatWanted.getSeatRow(), (char)(theSeatWanted.getSeatColumn()-totalTicketsWanted+1), thisIterationDistanceFromMiddle};
                                    }
                                }

                            }


                        }
                    }else{                                                                                      // if not open, reset valid consecutive seats to 0
                        validConsecutiveSeats =0;
                    }
                    theSeatWanted = theSeatWanted.getRight();                                                   // move to next seat
            }
            theSeatWanted = headFirstSeat;                                                                      // at end of each row, move down to next row
            for(int g =1; g<=i; g++){
                theSeatWanted = theSeatWanted.getDown();
            }
        }
        return bestAvailableRowCol;                                                                             // return the obj array storing the data
    }
        //  double distanceFromRowMiddle = Math.abs(((j + (j-totalTicketsWanted+1.0)) / 2.0) - exactMidpoint[1]); //**** was doing integer division...
        //  double thisIterationDistanceFromMiddle = Math.sqrt(Math.pow(Math.abs(i - exactMidpoint[0]), 2) + Math.pow(Math.abs((j - (totalTicketsWanted / 2)) - exactMidpoint[1]), 2));



    public Object[] displayReport()
    {
        final double adultPrice = 10.0;                                                                         // constant double values that won't be changed
        final double childPrice = 5.0;
        final double seniorPrice = 7.5;
        int numAdultTickets=0;                                                                                  // will count number of each ticket type in the auditorium
        int numChildTickets=0;
        int numSeniorTickets=0;

        TheaterSeat theSeatWanted = headFirstSeat;                                                              // will loop through every seat in the grid, starting with head
        int lineCounter =1;
        while(theSeatWanted != null)
        {
            while (theSeatWanted != null)
            {
                if(theSeatWanted.getTicketType() == 'A'){                                                       // depending on the character held in current array spot, increment corresponding counter variable
                    numAdultTickets++;
                }
                if(theSeatWanted.getTicketType() == 'C'){                                                       // increment child var
                    numChildTickets++;
                }
                if(theSeatWanted.getTicketType() == 'S'){                                                       // increment senior var
                    numSeniorTickets++;
                }

                theSeatWanted = theSeatWanted.getRight();                                                       // move to next seat for next iteration
            }
            lineCounter++;
            theSeatWanted = headFirstSeat;                                                                      // at end of each row, move down to next row
            for(int j=1; j<lineCounter;j++){
                theSeatWanted = theSeatWanted.getDown();
            }
        }
        int openSeats = this.lineCounter*rowLength -(numAdultTickets+numChildTickets+numSeniorTickets);
        int reservedCount = numAdultTickets+numChildTickets+numSeniorTickets;
        double totalSales = (numAdultTickets*adultPrice  +  numChildTickets*childPrice  +  numSeniorTickets*seniorPrice);
        String auditoriumIs = "Auditorium "+fileName.charAt(1);

        return new Object[]{auditoriumIs, openSeats, reservedCount, numAdultTickets, numChildTickets, numSeniorTickets," ",totalSales};     // return array containing all the data about the auditorium
        //return String.format("%-15s%9d%9d%10d%10d%9d%6s$%5.2f", auditoriumIs,openSeats, reservedCount, numAdultTickets, numChildTickets, numSeniorTickets,"",totalSales);
    }

    public void exitProgram()
    {
        File theFile = new File(fileName);
        try {
            PrintWriter outputToFile = new PrintWriter(theFile);                                                // create printWriter object to update the auditorium file
            int lineCounter = 1;

            TheaterSeat theSeatWanted = headFirstSeat;
            while(theSeatWanted != null)                                                                        // loop through each seat in the grid, and output its seat type
            {
                while (theSeatWanted != null)                                                                   // print each seat, then move right till null
                {
                    outputToFile.print(theSeatWanted.getTicketType());
                    theSeatWanted = theSeatWanted.getRight();
                }
                lineCounter++;
                theSeatWanted = headFirstSeat;
                for(int j=1; j<lineCounter;j++){
                    theSeatWanted = theSeatWanted.getDown();                                                    // if hit null, move down
                }
                if(lineCounter <= this.lineCounter)
                    outputToFile.println();
            }

            outputToFile.close();                                                                               // close file to save data
        }catch (FileNotFoundException ex){                                                                      // if file writing to somehow does not exist, display error message
            System.out.println("Could not output to file");
        }
    }


}
