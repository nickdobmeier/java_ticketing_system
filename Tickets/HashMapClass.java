// Nicholas Dobmeier
// njd170130

package Tickets;


import java.math.BigInteger;
import java.util.LinkedList;

public class HashMapClass
{
    private LinkedList<UserBank> [] hashArray;                                                                  // the underneath hash array
    private double MAX_LOAD_FACTOR = .7;                                                                        // pre-set max LF


    HashMapClass(int initialSize){                                                                              // constructor takes in siz var and creates array of that size
        hashArray = new LinkedList[initialSize];
    }
    public int getHashTotalSize(){                                                                              // returns size of the hash array
        return hashArray.length;
    }


    public double getActualLoadFactor()
    {
        double isOpen =0.0;
        for(int i =0; i<hashArray.length; i++){                                                                 // loops through entire array
            if(hashArray[i] != null){                                                                           // determines what indexs' are empty
                isOpen +=hashArray[i].size();                                                                   // if NOT empty, add to isOpen
            }
        }
        return isOpen/getHashTotalSize();                                                                       // return the ratio of filled seats to total size
    }



    public boolean insert(UserBank keyUser)
    {
        if(getActualLoadFactor() > MAX_LOAD_FACTOR){                                                            // if the array is already over, the max LF, rehash before inserting
            reHash();
        }
        int indexToBePut = theHashFunction(keyUser);                                                            // get index from hash function
        if(indexToBePut == -1){
            return false;
        }else{
            if(hashArray[indexToBePut] == null){                                                                // if the index is currently empty, create a new linked list at that index
                hashArray[indexToBePut] = new LinkedList<UserBank>();
            }
            return hashArray[indexToBePut].add(keyUser);                                                        // add the User to the linked list at the specified index in the hash array
        }
    }
    private int theHashFunction(UserBank keyUser) {
        int index = Math.abs(keyUser.hashCode());                                                               // calls hash function of User
        index = index % getHashTotalSize();                                                                     // compress index to size & return
        return index;
    }

    private void reHash()
    {
                                                                                                                // 1st find the NEXT smallest prime number- starting at size*2 - using method in Big Integer class
        int sizeOfNewArray = getHashTotalSize() * 2;
        BigInteger bigInteger = new BigInteger(sizeOfNewArray+"");
        bigInteger = bigInteger.nextProbablePrime();
        sizeOfNewArray = Integer.parseInt(bigInteger.toString());

        LinkedList<UserBank>[] storeOldHashArray;                                                               // create copy of original hash array
        storeOldHashArray = hashArray.clone();
        hashArray = new LinkedList[sizeOfNewArray];

        for(int i=0;i<storeOldHashArray.length;i++){                                                            // re-insert each object into new hash array using updated hash function (since class variable is now new size)
            if(storeOldHashArray[i] != null){
                for(int j=0;j<storeOldHashArray[i].size();j++){
                    this.insert(storeOldHashArray[i].get(j));}
            }
        }
        //System.out.println("\n\nREHASHING***\n");
    }




    public UserBank doesExist(UserBank keyUser)
    {
        int index = theHashFunction(keyUser);                                                                   // get where User should be from hash function
        if(hashArray[index] == null){                                                                           // if no linked list at index, User is NOT there
            return null;
        }
        for(int j=0; j<hashArray[index].size();j++){
            if(keyUser.compareTo(hashArray[index].get(j)) == 0){
                return hashArray[index].get(j);                                                                 // search each element in the linked list for the User, if found, return it
            }
        }
        return null;                                                                                            // if reach here, User was never found
    }

}
