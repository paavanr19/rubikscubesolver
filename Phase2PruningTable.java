import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


public class Phase2PruningTable {
     public static final ArrayList<String> PHASE2_MOVES = new ArrayList<>(Arrays.asList(
            "U","U'","U2","D","D'","D2","F2","B2","L2","R2"
        )); //moves that do not change orientation


        //helper function for move pruning
        public void reverse_move(Cubie cube, String move){
            if (move==null){
                return;
            }
            switch(move){
                case "R" -> cube.applyMoves("R'");
                case "R'" -> cube.applyMoves("R");
                case "R2" -> cube.applyMoves("R2");
                case "L" -> cube.applyMoves("L'");
                case "L'" -> cube.applyMoves("L");
                case "L2" -> cube.applyMoves("L2");
                case "B" -> cube.applyMoves("B'");
                case "B'" -> cube.applyMoves("B");
                case "B2" -> cube.applyMoves("B2");
                case "U" -> cube.applyMoves("U'");
                case "U'" -> cube.applyMoves("U");
                case "U2" -> cube.applyMoves("U2");
                case "D" -> cube.applyMoves("D'");
                case "D'" -> cube.applyMoves("D");
                case "D2" -> cube.applyMoves("D2");
                case "F" -> cube.applyMoves("F'");
                case "F'" -> cube.applyMoves("F");
                case "F2" -> cube.applyMoves("F2");

            }
        }


    public static HashMap<EP, Integer> EP_Pruning_Table = new HashMap<>(); //stores distance from solved to any edge perm
    public static HashMap<CP, Integer> CP_Pruning_Table = new HashMap<>(); //stores distance from solved to any corner perm
    public static HashMap<Slice_Permutations, Integer> Slice_Permutation_Pruning_Table = new HashMap<>(); //stores distance from solved to any slice perm

    public static int URF = 0;
    public static int UFL = 1;
    public static int ULB = 2;
    public static int UBR = 3;

    public static int DFR = 4;
    public static int DLF = 5;
    public static int DBL = 6;
    public static int DRB = 7;

    public static int UR = 0;
    public static int UF = 1;
    public static int UL = 2;
    public static int UB = 3;
    public static int FR = 4;
    public static int FL = 5;
    public static int BL = 6;
    public static int BR = 7;
    public static int DR = 8;
    public static int DF = 9;
    public static int DL = 10;
    public static int DB = 11;


    //handles edge perm hashing and equals for EP_Pruning_Table
    public static class EP{
        public int[] edge_permutations;
        public int[] edge_permutations_8;


        public EP(int[] edge_permutations) {
            this.edge_permutations = edge_permutations;
            this.edge_permutations_8 = new int[8];
            edge_permutations_8[0] = edge_permutations[UR];
            edge_permutations_8[1] = edge_permutations[UF];
            edge_permutations_8[2] = edge_permutations[UL];
            edge_permutations_8[3] = edge_permutations[UB];
            edge_permutations_8[4] = edge_permutations[DR];
            edge_permutations_8[5] = edge_permutations[DF];
            edge_permutations_8[6] = edge_permutations[DL];
            edge_permutations_8[7] = edge_permutations[DB];
        }


        @Override
        public int hashCode(){
            int hash = Arrays.hashCode(edge_permutations_8);
            return hash;
        }

        @Override
        public boolean equals(Object cubie){
            if (!(cubie instanceof EP)){
                return false;
            }
            EP new_state = (EP) cubie;
            boolean edge_perm = Arrays.equals(new_state.edge_permutations_8, this.edge_permutations_8);
            return edge_perm;
        }

    }


    //handles corner perm hashing and equals for CP_Pruning_Table
    public static class CP{
        public int[] corner_permutation;

        public CP(int[] corner_permutation) {
            this.corner_permutation = corner_permutation;
        }


        @Override
        public int hashCode(){
            int hash = Arrays.hashCode(corner_permutation);
            return hash;
        }

        @Override
        public boolean equals(Object cubie){
            if (!(cubie instanceof CP)){
                return false;
            }
            CP new_state = (CP) cubie;
            boolean corner_perm = Arrays.equals(new_state.corner_permutation, this.corner_permutation);
            return corner_perm;
        }
    }

    //handles slice perm hashing and equals for Slice_Permutations_Pruning_Table
    public static class Slice_Permutations{
        public int[] edge_permutations;
        public int[] slice_array = new int[4]; 

        /*for reference:
        FR: 4
        FL: 5
        BR: 7
        BL: 6
         */
        

        public Slice_Permutations(int[] edge_permutations) {
            this.edge_permutations = edge_permutations;
            slice_array[0] = edge_permutations[FR];
            slice_array[1] = edge_permutations[FL];
            slice_array[2] = edge_permutations[BL];
            slice_array[3] = edge_permutations[BR];
        }


        @Override
        public int hashCode(){
            int hash = Arrays.hashCode(slice_array);
            return hash;
        }

        @Override
        public boolean equals(Object cubie){
            if (!(cubie instanceof Slice_Permutations)){
                return false;
            }
            Slice_Permutations new_state = (Slice_Permutations) cubie;
            boolean slice_pos = Arrays.equals(new_state.slice_array, this.slice_array);
            return slice_pos;
        }

        public boolean correctSlicePositions(){
        for (int i=0;i<slice_array.length;i++){
            if (slice_array[i]!=FR && slice_array[i]!=FL  && slice_array[i]!=BR && slice_array[i]!=BL){
                return false;
            }
        }
        return true;
        }
    }

    //fills EP_Pruning_Table
    public void EP_BFS(){

        Queue<Cubie> queue = new LinkedList<>();
        Cubie solvedCubeState = new Cubie();
        EP solved = new EP(solvedCubeState.edge_permutations);
        queue.add(solvedCubeState);
        EP_Pruning_Table.put(solved, 0);

        while(!(queue.isEmpty())){
            Cubie current_cube = queue.poll();             
            EP current = new EP(current_cube.edge_permutations);  
            int distance = EP_Pruning_Table.get(current);

            for (String move: PHASE2_MOVES){
                Cubie new_state = new Cubie(current_cube);
                new_state.applyMoves(move);
                EP next = new EP(new_state.edge_permutations);
                if (!(EP_Pruning_Table.containsKey(next))){
                    EP_Pruning_Table.put(next,distance+1);
                    queue.add(new_state);
                }
                
                

            }
        }

        
        }

    //fills CP_Pruning_Table
    public void CP_BFS(){

        Queue<CP> queue = new LinkedList<>();
        Cubie solvedCubeState = new Cubie();
        CP solved = new CP(solvedCubeState.corner_permutations);
        queue.add(solved);
        CP_Pruning_Table.put(solved, 0);

        while(!(queue.isEmpty())){
            CP current = queue.poll();

            for (String move: PHASE2_MOVES){
                Cubie new_state = new Cubie(); // copy all cube state
                new_state.corner_permutations = Arrays.copyOf(current.corner_permutation, 8);
                new_state.applyMoves(move);
                CP next = new CP(new_state.corner_permutations);
                if (!(CP_Pruning_Table.containsKey(next))){
                    CP_Pruning_Table.put(next,CP_Pruning_Table.get(current)+1);
                    queue.add(next);
                }
                
            }
        }

        
        }

        //fills Slice_Permutations_Pruning_Table
        public void Slices_BFS(){

        Queue<Slice_Permutations> queue = new LinkedList<>();
        Cubie solvedCubeState = new Cubie();
        Slice_Permutations solved = new Slice_Permutations(solvedCubeState.edge_permutations);
        queue.add(solved);
        Slice_Permutation_Pruning_Table.put(solved, 0);

        while(!(queue.isEmpty())){
            Slice_Permutations current = queue.poll();

            for (String move: PHASE2_MOVES){
                Cubie new_state = new Cubie(); 
                new_state.edge_permutations = Arrays.copyOf(current.edge_permutations, 12);
                new_state.applyMoves(move);
                Slice_Permutations next = new Slice_Permutations(new_state.edge_permutations);
                if (!(Slice_Permutation_Pruning_Table.containsKey(next))){
                    Slice_Permutation_Pruning_Table.put(next,Slice_Permutation_Pruning_Table.get(current)+1);
                    queue.add(next);
                }
                
            }
        }

        
        }
    

}