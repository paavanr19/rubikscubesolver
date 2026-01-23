import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Phase1PruningTable {
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

        public static final ArrayList<String> MOVES = new ArrayList<>(Arrays.asList( //all moves are allowed for phase1
            "R","R'","R2","L","L'","L2","B","B'","B2",
            "F","F'","F2","D","D'","D2","U","U'","U2"
        ));


    public static HashMap<EO, Integer> EO_Pruning_Table = new HashMap<>(); //stores distance from solved to any edge ori
    public static HashMap<CO, Integer> CO_Pruning_Table = new HashMap<>(); //stores distance from solved to any corner ori
    public static HashMap<Slices, Integer> Slice_Pruning_Table = new HashMap<>(); //stores distance from solved to any slice perm (fr,fl,br,bl)



    //handles hashing and equals for EO_Pruning_Table
    public static class EO{
        public int[] edge_orientation;

        public EO(int[] edge_orientation) {
            this.edge_orientation = edge_orientation;
        }


        @Override
        public int hashCode(){
            int hash = Arrays.hashCode(edge_orientation);
            return hash;
        }

        @Override
        public boolean equals(Object cubie){
            if (!(cubie instanceof EO)){
                return false;
            }
            EO new_state = (EO) cubie;
            boolean edge_ori = Arrays.equals(new_state.edge_orientation, this.edge_orientation);
            return edge_ori;
        }


    }


    //handles hashing and equals for CO_Pruning_Table
    public static class CO{
        public int[] corner_orientation;

        public CO(int[] corner_orientation) {
            this.corner_orientation = corner_orientation;
        }


        @Override
        public int hashCode(){
            int hash = Arrays.hashCode(corner_orientation);
            return hash;
        }

        @Override
        public boolean equals(Object cubie){
            if (!(cubie instanceof CO)){
                return false;
            }
            CO new_state = (CO) cubie;
            boolean corner_ori = Arrays.equals(new_state.corner_orientation, this.corner_orientation);
            return corner_ori;
        }
    }


    //handles hashing and equals for Slices_Pruning_Table
    public static class Slices{
        public int[] edge_permutations;
        public int[] slice_array = new int[4];

        /*for reference:
        FR: 4
        FL: 5
        BR: 7
        BL: 6
         */
        private int getIndex(int[] array, int target) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == target) {
                    return i;
                }
                    
            }
            return -1; 
            }


        public Slices(int[] edge_permutations) {
            this.edge_permutations = edge_permutations;
            slice_array[0] = getIndex(edge_permutations, FR); // FR slot
            slice_array[1] = getIndex(edge_permutations, FL); // FL slot
            slice_array[2] = getIndex(edge_permutations, BL); // BL slot
            slice_array[3] = getIndex(edge_permutations, BR); // BR slot
            }


        @Override
        public int hashCode(){
            int hash = Arrays.hashCode(slice_array);
            return hash;
        }

        @Override
        public boolean equals(Object cubie){
            if (!(cubie instanceof Slices)){
                return false;
            }
            Slices new_state = (Slices) cubie;
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



    //fills eo_pruning_table
    public void EO_BFS(){

        Queue<Cubie> queue = new LinkedList<>(); 
        Cubie solvedCubeState = new Cubie(); 
        EO solved = new EO(solvedCubeState.edge_orientations); 
        queue.add(solvedCubeState);
        EO_Pruning_Table.put(solved, 0);

        while(!(queue.isEmpty())){
            Cubie current_cube = queue.poll();             
            EO current = new EO(current_cube.edge_orientations);  
            int distance = EO_Pruning_Table.get(current);

            for (String move: MOVES){
                Cubie new_state = new Cubie(current_cube);
                new_state.applyMoves(move);
                EO next = new EO(new_state.edge_orientations);
                if (!(EO_Pruning_Table.containsKey(next))){
                    EO_Pruning_Table.put(next,distance+1);
                    queue.add(new_state);
                }
                
                

            }
        }

        
        }


        //fills co_pruning_table
        public void CO_BFS(){

        Queue<Cubie> queue = new LinkedList<>();
        Cubie solvedCubeState = new Cubie();
        CO solved = new CO(solvedCubeState.corner_orientations);
        queue.add(solvedCubeState);
        CO_Pruning_Table.put(solved, 0);

        while(!(queue.isEmpty())){
            Cubie current_cube = queue.poll();             
            CO current = new CO(current_cube.corner_orientations);  
            int distance = CO_Pruning_Table.get(current);

            for (String move: MOVES){
                Cubie new_state = new Cubie(current_cube);
                new_state.applyMoves(move);
                CO next = new CO(new_state.corner_orientations);
                if (!(CO_Pruning_Table.containsKey(next))){
                    CO_Pruning_Table.put(next,distance+1);
                    queue.add(new_state);
                }
                
                

            }
        }

        
        }


        //fills slices_pruning_table
        public void Slices_BFS(){

        Queue<Cubie> queue = new LinkedList<>();
        Cubie solvedCubeState = new Cubie();
        Slices solved = new Slices(solvedCubeState.edge_permutations);
        queue.add(solvedCubeState);
        Slice_Pruning_Table.put(solved, 0);

        while(!(queue.isEmpty())){
            Cubie current_cube = queue.poll();             
            Slices current = new Slices(current_cube.edge_permutations);  
            int distance = Slice_Pruning_Table.get(current);

            for (String move: MOVES){
                Cubie new_state = new Cubie(current_cube);
                new_state.applyMoves(move);
                Slices next = new Slices(new_state.edge_permutations);
                if (!(Slice_Pruning_Table.containsKey(next))){
                    Slice_Pruning_Table.put(next,distance+1);
                    queue.add(new_state);
                }
                
                

            }
        }

        
        }


}