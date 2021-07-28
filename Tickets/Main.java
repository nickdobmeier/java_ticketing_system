// Nicholas Dobmeier
// njd170130 ~

package Tickets;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        String fileName = "userdb.dat";                                                                                 // create the user data file Scanner
        File theFile = new File(fileName);
        Scanner inputFromFile = null;
        HashMapClass HashMap = new HashMapClass(19);                                                          // create Hash map w/ default size of 19 indexes
        try{
            readUserData(inputFromFile, theFile, HashMap);                                                              // call readUserData function
        }catch (FileNotFoundException ex){
            System.out.println(ex.getMessage());                                                                        // if exception was thrown, END program
            System.exit(0);
        }
        Auditorium auditorium1 =null;                                                                                   // create all auditorium ref variables
        Auditorium auditorium2 =null;
        Auditorium auditorium3 =null;
        try {
            auditorium1 = new Auditorium("A1base.txt");                                                            // create each auditorium
            auditorium2 = new Auditorium("A2base.txt");
            auditorium3 = new Auditorium("A3base.txt");
        }catch (FileNotFoundException ex){                                                                              // if any exceptions were thrown during creation, END program
            System.out.println(ex.getMessage());
            System.exit(0);
        }


        boolean toEXIT = false;
        do {                                                                                                            // will loop while toEXIT is false, which only an admin can change
            Scanner inputFromConsole = new Scanner(System.in);
            UserBank theCurrentUser = null;

            while (theCurrentUser == null) {
                System.out.println("Please Login");                                                                     // ask for username
                System.out.print("\tEnter Username:  -> ");
                String tempUser = inputFromConsole.nextLine();
                int invalidAttemptCount =0;                                                                             // count number of attempts
                do {
                    if(invalidAttemptCount>0){
                        System.out.println("\t\ttry again");
                    }
                    System.out.print("\tEnter Password:  -> ");                                                         // ask for password
                    String tempPW = inputFromConsole.nextLine();
                    theCurrentUser = HashMap.doesExist(new UserBank(tempUser, tempPW));                                 // if user DOES exist, contine program
                }while(theCurrentUser == null && ++invalidAttemptCount < 3);                                            // ask for PW while entry was wrong and has not entered 3 invalid attempts
                System.out.println();
            }


            int choice = -1;
            if(theCurrentUser.isAdmin()){                                                                               // if the user that logged in is an admin
                boolean toLogOut = false;
                do {                                                                                                    // loop until toLogOut is true
                    System.out.println("1. Print Report\n2. Logout\n3. Exit\n");
                    String inputStr;
                    do {
                        try {
                            System.out.print("\tChoice:  ->   ");
                            inputStr = inputFromConsole.nextLine();                                                     // read in user choice, validate
                            choice = Integer.parseInt(inputStr);
                        } catch (NumberFormatException ex) {
                            System.out.println("\t\t" + ex.getMessage());
                        }
                    } while (choice < 1 || choice > 3);                                                                 // if choice is between 1 & 3

                    switch (choice) {
                        case 1:
                            adminPrintReport(auditorium1, auditorium2, auditorium3);                                    // call print report function, passing all 3 auditoriums
                            break;

                        case 2:
                            System.out.println("\t\t*LOGGING OUT*\n\n");                                                // set toLogOut to true, which ends loop
                            toLogOut = true;
                            break;
                        default:
                            System.out.println("\n\t\t*Powering Down System*");                                         // otherwise option 3 was selected
                            auditorium1.exitProgram();                                                                  // print each auditorium to its respective file
                            auditorium2.exitProgram();
                            auditorium3.exitProgram();
                            System.out.print("\t\tGOOD-BYE!");
                            toLogOut =true;                                                                             // set both exit loop variable to true
                            toEXIT = true;
                            break;
                    }
                }while (!toLogOut);                                                                                     // while log out is false
            }

            //////////////////////////
            else {                                                                                                      // if not admin, user is normal user

                boolean toLogOut = false;
                do {
                    System.out.println("1. Reserve Seats\n2. View Orders\n3. Update Order\n4. Display Receipt\n5. Log Out\n");
                    String inputStr;
                    do {
                        try {                                                                                           // validate user choice input, should be int 1-5
                            System.out.print("\tChoice:  ->   ");
                            inputStr = inputFromConsole.nextLine();
                            choice = Integer.parseInt(inputStr);
                        } catch (NumberFormatException ex) {
                            System.out.println("\t\t" + ex.getMessage());
                        }
                    } while (choice < 1 || choice > 5);

                    switch (choice) {                                                                                   // case depending on choice selected
                        case 1:
                            System.out.println("Pick an Auditorium: \n");
                            System.out.println("1.)\tAuditorium 1");
                            System.out.println("2.)\tAuditorium 2");
                            System.out.println("3.)\tAuditorium 3\n");
                            do {                                                                                        // allow user to pick an auditorium, validate until valid input
                                try {
                                    System.out.print("\tChoice:  ->   ");
                                    inputStr = inputFromConsole.nextLine();
                                    choice = Integer.parseInt(inputStr);
                                } catch (NumberFormatException ex) {
                                    System.out.println("\t\t" + ex.getMessage());
                                }
                            } while (choice < 1 || choice > 3);
                            Auditorium audToPass = null;
                            if (choice == 1)                                                                            // store desired auditorium in a temp variables
                                audToPass = auditorium1;
                            else if (choice == 2)
                                audToPass = auditorium2;
                            else
                                audToPass = auditorium3;

                            ReserveSeats(true,null, audToPass, inputFromConsole, theCurrentUser);   // call reserveSeat function, passing the auditorium the user selected

                            break;
                        case 2:
                            System.out.println(theCurrentUser.toString());                                              // if option 2 selected prints out each order for the user
                            break;
                        case 3:

                            updateOrder(inputFromConsole, theCurrentUser, auditorium1, auditorium2, auditorium3);       // if option 3 selected, call update function
                            break;
                        case 4:
                            System.out.println("\n\n"+theCurrentUser.printReciept()+"\n");                              // if option 4 selected, print receipt, received from User method
                            break;
                        default:
                            System.out.println("\t\t*LOGGING OUT*\n\n");                                                // else, initiate log out, set toLogOut to true to end loop
                            toLogOut = true;
                            break;
                    }
                }while (!toLogOut);                                                                                     // loop while toLogOut if false
            }

        }while (!toEXIT);                                                                                               // loop while toExit is false
    }


    public static void readUserData(Scanner inputFromFile, File theFile, HashMapClass HashMap) throws FileNotFoundException
    {
        inputFromFile=new Scanner(theFile);                                                                             // create scanner object, if not found exception is thrown
        if( theFile.length() == 0 ){throw new FileNotFoundException("File \'userdb.dat\' is EMPTY");}                   // if empty, throw exception

        while (inputFromFile.hasNextLine()){                                                                            // while file has lines to read
            String inputStr = inputFromFile.nextLine();
            String[] strParts = inputStr.split("\\s+");                                                          // split function based on space
            if(strParts.length ==2)                                                                                     // two sub strings should now be present
            {
                UserBank newUser = new UserBank(strParts[0], strParts[1]);                                              // create the user, sending username & pw to constructor
                if(HashMap.doesExist(newUser)==null){                                                                   // if this user is NOT alrdy present, add them
                    HashMap.insert(newUser);}
            }
        }
        inputFromFile.close();                                                                                          // close file
    }

    public static void adminPrintReport(Auditorium auditorium1, Auditorium auditorium2, Auditorium auditorium3)
    {
        System.out.println("\n\tPrinting Report:");
        System.out.println("Auditorium #     |  Open  | Reserved |  Adult  | Child |  Senior  |   Sales");
        int allOpenSeats=0;                                                                                             // create a variable for each data piece
        int allReservedSeats=0;
        int allAdultSeats=0;
        int allChildSeats=0;
        int allSeniorSeats=0;
        double allSales =0.0;


        Object[] audData = auditorium1.displayReport();                                                                 // the return of displayReport returns all the data in an object array
        String printStr = String.format("%-15s%9d%9d%10d%10d%9d%6s$%7.2f", ((String)audData[0]),((Integer)audData[1]), ((Integer)audData[2]), ((Integer)audData[3]), ((Integer)audData[4]), ((Integer)audData[5]),((String)audData[6]),((Double)audData[7]));
        for(int i=0;i<printStr.length();i++){                                                                           // create comprehensive string piece for auditorium 1
            System.out.print('-');}
        System.out.println("\n"+printStr);
        for(int i=0;i<printStr.length();i++){
            System.out.print('-');}
        allOpenSeats += ((Integer)audData[1]);                                                                          // increment the total variable for each data column
        allReservedSeats += ((Integer)audData[2]);
        allAdultSeats += ((Integer)audData[3]);
        allChildSeats += ((Integer)audData[4]);
        allSeniorSeats += ((Integer)audData[5]);
        allSales += ((Double) audData[7]);


        audData = auditorium2.displayReport();                                                                          // the return of displayReport returns all the data in an object array
        printStr=String.format("%-15s%9d%9d%10d%10d%9d%6s$%7.2f", ((String)audData[0]),((Integer)audData[1]), ((Integer)audData[2]), ((Integer)audData[3]), ((Integer)audData[4]), ((Integer)audData[5]),((String)audData[6]),((Double)audData[7]));;
        System.out.println("\n"+printStr);                                                                              // create comprehensive string piece for auditorium 2
        for(int i=0;i<printStr.length();i++){
            System.out.print('-');}
        allOpenSeats += ((Integer)audData[1]);                                                                          // increment the total variable for each data column
        allReservedSeats += ((Integer)audData[2]);
        allAdultSeats += ((Integer)audData[3]);
        allChildSeats += ((Integer)audData[4]);
        allSeniorSeats += ((Integer)audData[5]);
        allSales += ((Double) audData[7]);


        audData = auditorium3.displayReport();                                                                          // the return of displayReport returns all the data in an object array
        printStr=String.format("%-15s%9d%9d%10d%10d%9d%6s$%7.2f", ((String)audData[0]),((Integer)audData[1]), ((Integer)audData[2]), ((Integer)audData[3]), ((Integer)audData[4]), ((Integer)audData[5]),((String)audData[6]),((Double)audData[7]));;
        System.out.println("\n"+printStr);                                                                              // create comprehensive string piece for auditorium 3
        allOpenSeats += ((Integer)audData[1]);                                                                          // increment the total variable for each data column
        allReservedSeats += ((Integer)audData[2]);
        allAdultSeats += ((Integer)audData[3]);
        allChildSeats += ((Integer)audData[4]);
        allSeniorSeats += ((Integer)audData[5]);
        allSales += ((Double) audData[7]);


        for(int i=0;i<printStr.length();i++){
            System.out.print('-');}                                                                                     // create final comprehensive formatted string piece for the totals of each colmn
        printStr=String.format("%-15s%9d%9d%10d%10d%9d%6s$%7.2f", "Total",allOpenSeats, allReservedSeats, allAdultSeats, allChildSeats, allSeniorSeats," ",allSales);
        System.out.println("\n"+printStr+"\n\n");                                                                       // print the entire report string to console
    }



    public static boolean ReserveSeats(boolean toOfferBestAvailable, CustomerOrder theOrderToChange, Auditorium theAuditorium, Scanner inputFromUser, UserBank theCurrentUser)
    {
        String tempInputStr;
        System.out.println("Here is the current theatre:\n");
        theAuditorium.displayAuditorium(true);                                                                  // display auditorium, and HIDE the seat characters
        System.out.println("Ticket Prices are as follows:\n\tAdult  - $10.00\n\tChild  - $ 5.00\n\tSenior - $ 7.50\n\n");

        int userChoiceRow=-1;
        char userChoiceColChar=' ';

        do{
            System.out.print("Select the seat ROW first (ie. 2):   ");
            try {
                tempInputStr = inputFromUser.nextLine();                                                                // read in entire string
                if (tempInputStr.length() == 0)
                    throw new InputMismatchException();
                for(int i=0;i<tempInputStr.length();i++){                                                               // if NOTHING was inputed, 3 chars were entered, or is not a number, throw exception
                    if (tempInputStr.charAt(i) > '9'   ||  tempInputStr.charAt(i) < '0' ) {
                        throw new InputMismatchException();}
                }
                userChoiceRow = Integer.parseInt(tempInputStr);
            }catch (InputMismatchException ex){}
        }while((userChoiceRow < 1) || (userChoiceRow > theAuditorium.getLineCounter()));                                // loop until a valid number is inputed

        do{
            System.out.print("Now select left-most seat COLUMN (ie. A):   ");
            try {
                String tempStr = inputFromUser.nextLine();                                                              // read in entire string
                if(tempStr.length() != 1   ||   tempStr.charAt(0) < 'A'  ||   tempStr.charAt(0) > (char)theAuditorium.getRowLength()+64){
                    throw new InputMismatchException();}                                                                // if nothing was inputed, or the is not a alphabetical char, then throw exception
                userChoiceColChar = tempStr.charAt(0);
            }catch (InputMismatchException ex){}
        }while((userChoiceColChar < 'A') || (userChoiceColChar > (char)(theAuditorium.getRowLength()+64)));


        System.out.println("Now pick how many Adult, Child, and Senior tickets you wish to purchase, in that order");
        int adultTicketsWanted=-1;
        int childTicketsWanted =-1;
        int seniorTicketsWanted = -1;
         do{                                                                                                            // NOW read in number values for each type of ticket wanted
             System.out.print("\tAdult:    ");
             try {
                 tempInputStr = inputFromUser.nextLine();                                                               // read in entire string
                 if (tempInputStr.length() == 0)
                     throw new InputMismatchException();
                 for(int i=0;i<tempInputStr.length();i++){
                     if (tempInputStr.charAt(i) > '9'   ||  tempInputStr.charAt(i) < '0' ) {                            // if the input is not a numeric number, throw exception
                         throw new InputMismatchException();}
                 }
                 adultTicketsWanted = Integer.parseInt(tempInputStr);
             }catch (InputMismatchException ex){}
         }while(adultTicketsWanted < 0);                                                                                // loop until in number input is greater than zero
        do{
            System.out.print("\tChild:    ");
            try {
                tempInputStr = inputFromUser.nextLine();                                                                // read in the entire string
                if (tempInputStr.length() == 0)
                    throw new InputMismatchException();
                for(int i=0;i<tempInputStr.length();i++){
                    if (tempInputStr.charAt(i) > '9'   ||  tempInputStr.charAt(i) < '0' ) {                             // if the input is not a numeric number, throw exception
                        throw new InputMismatchException();}
                }
                childTicketsWanted = Integer.parseInt(tempInputStr);
            }catch (InputMismatchException ex){}
        }while(childTicketsWanted < 0);                                                                                 // loop until in number input is greater than zero
        do{
            System.out.print("\tSenior:   ");
            try {
                tempInputStr = inputFromUser.nextLine();
                if (tempInputStr.length() == 0)
                    throw new InputMismatchException();
                for(int i=0;i<tempInputStr.length();i++){
                    if (tempInputStr.charAt(i) > '9'   ||  tempInputStr.charAt(i) < '0' ) {                             // if the input is not a numeric number, throw exception
                        throw new InputMismatchException();}
                }
                seniorTicketsWanted = Integer.parseInt(tempInputStr);
            }catch (InputMismatchException ex){}
        }while(seniorTicketsWanted < 0);                                                                                // loop until in number input is greater than zero
        if ( (adultTicketsWanted+childTicketsWanted+seniorTicketsWanted)==0 )                                           // if a grand total of zero tickets are wanted, loop back to the main menu
        {
            System.out.println("Zero were tickets ordered\n");
            return false;
        }
                                                                                                                        // call the isAvailable function in Auditorium, store return in local boolean varaible
        boolean isAvailable = theAuditorium.checkAvailability(userChoiceRow,userChoiceColChar-64, adultTicketsWanted+childTicketsWanted+seniorTicketsWanted);
        if(isAvailable == true){                                                                                        // if that return was true, call the void reserveSeats function, and go back to main menu
            theAuditorium.reserveSeats(theOrderToChange, userChoiceRow, userChoiceColChar-64,adultTicketsWanted,childTicketsWanted,seniorTicketsWanted, theCurrentUser);
            return true;
        }else{                                                                                                          // otherwise suggest a best available
            System.out.println("\nThose seats are NOT available");
            if(toOfferBestAvailable == true)
            {
                Object[] storage = theAuditorium.bestAvailable(adultTicketsWanted, childTicketsWanted, seniorTicketsWanted);     // best available will return an object array in form of [int, char, int]
                if ((Integer) storage[0] == -1)                                                                                  // this executes when no best available was found. default -1
                {
                    System.out.println("\t\tNO BEST AVAILABLE anywhere for " + (adultTicketsWanted + childTicketsWanted + seniorTicketsWanted) + " consecutive seats");
                } else {                                                                                                           // if a best available exists
                    System.out.print("Best Available Seats:\t");
                    char temp = (Character) storage[1];
                    for (int i = 0; i < (adultTicketsWanted + childTicketsWanted + seniorTicketsWanted); i++) {                     // output each best available to screen
                        System.out.print(storage[0]);
                        System.out.print((char) (temp + i));
                        System.out.print("  ");
                    }
                    do {                                                                                                            // read in response for if that want best available
                        System.out.print("\n\tWould you like to reserve the Best Available Seats? (Y/N)  -> ");
                        tempInputStr = inputFromUser.nextLine();                                                                    // read in the user response
                        try {
                            if (tempInputStr.length() != 1) {                                                                       // if they did not input a singular char
                                throw new InputMismatchException();
                            }
                        } catch (InputMismatchException ex) {
                            tempInputStr = "@";                                                                                     // this so that the check forces another loop
                        }
                    } while (!(tempInputStr.charAt(0) == 'N' || tempInputStr.charAt(0) == 'Y'));
                     if (tempInputStr.charAt(0) == 'Y') {                                                                           // if user inputed a Y, then reserve their seats by calling reserve function in Auditorium
                         int num = (Integer) storage[0];
                         theAuditorium.reserveSeats(theOrderToChange, num, temp - 64, adultTicketsWanted, childTicketsWanted, seniorTicketsWanted, theCurrentUser);
                         return true;
                     }
                }
            }
        }
        return false;
    }



    public static void updateOrder(Scanner inputFromUser, UserBank theCurrentUser, Auditorium auditorium1, Auditorium auditorium2, Auditorium auditorium3){
        String testing123123 = theCurrentUser.toString();                                                               // get return of toString, (orders string data)
        System.out.println(testing123123);
        boolean didUpdate = false;
                                                                                                                        // will be '\n' if order(s) have been saved******
        if(testing123123.charAt(testing123123.length()-2) == '\n' && theCurrentUser.getNumOrders()>0) {
            int orderBeingChanged =-1;
            String inputStr;
            do {
                try {
                    System.out.print("Select an order to modify:   -> ");                                               // allow user the option to select an order, input validate
                    inputStr = inputFromUser.nextLine();
                    orderBeingChanged = Integer.parseInt(inputStr);
                } catch (NumberFormatException ex) {
                    System.out.println("\t\t" + ex.getMessage());
                }
            } while (orderBeingChanged < 1 || orderBeingChanged > theCurrentUser.getNumOrders());                       // loop as input should be a valid integer between 1 & their order count


            CustomerOrder theOrderToChange = theCurrentUser.getOrder(orderBeingChanged-1);                        // retrieve the order the user wishes to modify
            Auditorium theAuditorium =null;                                                                             // store the auditorium that the order is based on in a temp auditorium ref variable
            if(theOrderToChange.getWhichAuditorium() == 1){
                theAuditorium = auditorium1;
            }else if(theOrderToChange.getWhichAuditorium() == 2){
                theAuditorium = auditorium2;
            }else{
                theAuditorium = auditorium3;
            }


            do {                                                                                                        // loop until an update was made to the specified order
                System.out.println("\t1. Add tickets to order\n\t2. Delete tickets from order\n\t3. Cancel Order");

                int choice = -1;
                do {
                    try {
                        System.out.print("\t\tChoice:  ->   ");                                                         // let user select an option, validate
                        inputStr = inputFromUser.nextLine();
                        choice = Integer.parseInt(inputStr);
                    } catch (NumberFormatException ex) {
                        System.out.println("\t\t" + ex.getMessage());
                    }
                } while (choice < 1 || choice > 3);                                                                     // loop until choice is 1-3


                switch (choice) {
                    case 1:
                        //////////////////                                                                              if user choice 1, call did reserve function, but pass false to best available, indstructing a BA to NOT be offered
                        didUpdate = ReserveSeats(false, theOrderToChange,theAuditorium, inputFromUser, theCurrentUser);
                        //////////////////
                        break;


                    case 2:
                        String rowColToCheck = "";
                        choice = -1;
                        do {
                            try {
                                System.out.print("Choose ROW to delete  ->  ");                                         // ask for row by user, validate that it exists in auditorium
                                inputStr = inputFromUser.nextLine();                                                    // read in entire string
                                choice = Integer.parseInt(inputStr);
                            } catch (NumberFormatException ex) {
                                System.out.println("\t\t" + ex.getMessage());
                            }
                        } while (choice < 1 || choice > theAuditorium.getLineCounter());

                        char userChoiceColChar = '$';
                        do {
                            System.out.print("Choose COLUMN to delete  ->  ");                                          // ask user for col, validate that it exists in aud
                            try {
                                String tempStr = inputFromUser.nextLine();                                              // read in entire string
                                if (tempStr.length() != 1 || tempStr.charAt(0) < 'A' || tempStr.charAt(0) > (char) theAuditorium.getRowLength() + 64) {
                                    throw new InputMismatchException();
                                }                                                                                       // if nothing was inputed, or the is not a alphabetical char, then throw exception
                                userChoiceColChar = tempStr.charAt(0);
                            } catch (InputMismatchException ex) {
                            }
                        } while ((userChoiceColChar < 'A') || (userChoiceColChar > (char) (theAuditorium.getRowLength() + 64)));
                        rowColToCheck = choice + "" + userChoiceColChar;
                        choice = -1;

                        int doesExist = theOrderToChange.isSeatInOrder(rowColToCheck);                                  // check if seat IS or is NOT in order
                        if (doesExist >= 0)                                                                             // if DOES exist
                        {
                            String whatRemoved = theOrderToChange.removeASeatInOrder(doesExist);                        // remove the seat from order
                            int rowR = Integer.parseInt(whatRemoved.charAt(0) + "");                                 // first index is row
                            char colR = whatRemoved.charAt(1);                                                          // second index is column
                            colR = theAuditorium.unReserveSeat(rowR, colR - 64);                                // now un-Reserve the seat, save returned ticket type

                            if (colR == 'A') {                                                                          // decrement necessary variables depending on ticket type
                                theOrderToChange.setNumAdultTickets(-1);
                            } else if (colR == 'C') {
                                theOrderToChange.setNumChildTickets(-1);
                            } else if (colR == 'S') {
                                theOrderToChange.setNumSeniorTickets(-1);
                            }

                            System.out.println("\n\tDeleted that seat from your order");
                            if (theOrderToChange.numSeatsInOrder() <= 0) {                                              // if no more seats left, delete the order
                                System.out.println("\t\t-No more seats in this order, order is being deleted");
                                theCurrentUser.removeOrder(orderBeingChanged - 1);
                            }
                            didUpdate =true; //*******                                                                  // if get here, a seat was properly deleted from order
                        } else {
                            System.out.println("\n\tDid NOT delete, that seat is NOT saved in this order");
                        }
                        break;


                    default:                                                                                            // otherwise, the user wished to delete the entire order
                        theCurrentUser.removeOrder(orderBeingChanged - 1);                                        // remove the order from the User linked list of orders
                        for (int i = 0; i < theOrderToChange.numSeatsInOrder(); i++) {                                  // for each seat in the order that was removed
                            String str = theOrderToChange.getIndex(i);                                                  // get the row & col of the seat and call un-reserve function in the specified auditorium
                            theAuditorium.unReserveSeat(str.charAt(0) - 48, str.charAt(1) - 64);
                        }
                        didUpdate = true; //********                                                                    // if get here an update was successfully made
                        break;
                }
            }while (didUpdate == false);                                                                                // loop while no updates have been made
        }else{
            System.out.println("No orders made to modify yet!");                                                        // if get here, no orders have been made by this user
        }
    }

}