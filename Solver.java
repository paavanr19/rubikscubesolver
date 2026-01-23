import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Solver {

    
    public static final ArrayList<String> PHASE2_MOVES = new ArrayList<>(Arrays.asList(
            "U","U'","U2","D","D'","D2","F2","B2","L2","R2"
        )); //moves that do not change orientation


    //handles updating the threshold for ida
    public class Threshold{
        public int threshold = Integer.MAX_VALUE;

        public void add_to_threshold(int new_value) {
        if (new_value < threshold) {
            threshold = new_value;
        }
    }

        public int get_new_threshold() {
            return threshold;
            }

        public void clear_threshold_array() {
        threshold = Integer.MAX_VALUE;
        }
        
        }

  
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

    
    public static final ArrayList<String> PHASE1_MOVES = new ArrayList<>(Arrays.asList(
            "R","R'","R2","L","L'","L2","B","B'","B2",
            "F","F'","F2","D","D'","D2","U","U'","U2"
        ));


    // doesn't allow same face moves
    public boolean invalidNextMove(String move, String prev) {
        if (prev == null) {
            return false;
        }
        else if (move.charAt(0) == prev.charAt(0)){
            return true;
        }
        
        return false;
}


    //computes heuristic for ida phase1
    public int computeHeuristic(Cubie cube){
        //gets distances based on cube state
        int edge_ori_distance = Phase1PruningTable.EO_Pruning_Table.get(new Phase1PruningTable.EO(cube.edge_orientations));
        int corner_ori_distance = Phase1PruningTable.CO_Pruning_Table.get(new Phase1PruningTable.CO(cube.corner_orientations));
        int slice_distance = Phase1PruningTable.Slice_Pruning_Table.get(new Phase1PruningTable.Slices(cube.edge_permutations));

        //alternative heuristic - too slow for some scrambles
        //int h = Math.max(edge_ori_distance,corner_ori_distance);
        //int h1 = Math.max(h,slice_distance);
        int m = edge_ori_distance + corner_ori_distance + slice_distance;
        return m;
    }

    public int computeHeuristic2(Cubie cube){
        //gets distances based on cube state
        int edge_perm_distance = Phase2PruningTable.EP_Pruning_Table.get(new Phase2PruningTable.EP(cube.edge_permutations));
        int corner_perm_distance = Phase2PruningTable.CP_Pruning_Table.get(new Phase2PruningTable.CP(cube.corner_permutations));
        int slice_distance = Phase2PruningTable.Slice_Permutation_Pruning_Table.get(new Phase2PruningTable.Slice_Permutations(cube.edge_permutations));

        int h = Math.max(edge_perm_distance,corner_perm_distance);
        int h1 = Math.max(h,slice_distance);
        //int m = edge_perm_distance + corner_perm_distance + slice_distance; - alternative heuristic, too slow
        return h1;
    }

  




    public ArrayList<String> ida_phase1(
        Cubie cube, int threshold, int g, ArrayList<String> tracking, String previousMove, Threshold threshold_object) {
            int h = computeHeuristic(cube);
            int f = g + h;
            if (h == 0) { // base case, solution found
            return new ArrayList<>(tracking); 
            }
            
            if (f > threshold) {//prune branch, update threshold
                threshold_object.add_to_threshold(f); 
                return null;
            }
            
            for (String new_move : PHASE1_MOVES) {
                if (invalidNextMove(new_move, previousMove)) {
                    continue; //move pruning
                }
                Cubie nextCube = new Cubie(cube); //new cube to apply moves
                nextCube.applyMoves(new_move); //apply moves to get net state
                ArrayList<String> newPath = new ArrayList<>(tracking);
                newPath.add(new_move);
                ArrayList<String> solution = ida_phase1(nextCube,threshold,g + 1,newPath,new_move,threshold_object);//recurse
                if (solution != null) {
                    return solution;
                }
            }
                return null; //no solution found
            }
            
            public ArrayList<String> ida_phase2(Cubie cube, int threshold, int g,
            ArrayList<String> tracking, String previousMove, Threshold threshold_object) {
                int h = computeHeuristic2(cube);
                int f = g + h;
                if (h == 0) { // base case, solution found
                return new ArrayList<>(tracking); 
                }
                
                if (f > threshold) { //prune branch, update threshold
                    threshold_object.add_to_threshold(f);
                    return null;
                }
                
                for (String new_move : PHASE2_MOVES) {
                    if (invalidNextMove(new_move, previousMove)) {
                        continue; //move pruning
                    }
                Cubie nextCube = new Cubie(cube); //new cube for applying moves
                nextCube.applyMoves(new_move); //get cube into next state
                ArrayList<String> newPath = new ArrayList<>(tracking);
                newPath.add(new_move);
                ArrayList<String> solution = ida_phase2(nextCube,threshold,g + 1,newPath,new_move,threshold_object);//recurse
                if (solution != null) {
                    return solution; //solution found
                }
                }
                return null;//no solution found
                }

//puts solution array in clockwise only format
public ArrayList<String> clockwise_only_string(ArrayList<String> moves){
        ArrayList<String> clockwise_only = new ArrayList<>();
        for (int i =0; i<moves.size();i++){
            if (null == moves.get(i)){
                clockwise_only.add(moves.get(i));
            }
            else switch (moves.get(i)) {
                case "R2" -> {
                    clockwise_only.add("R");
                    clockwise_only.add("R");
                }
                case "R'" -> {
                    clockwise_only.add("R");
                    clockwise_only.add("R");
                    clockwise_only.add("R");
                }
                case "L2" -> {
                    clockwise_only.add("L");
                    clockwise_only.add("L");
                }
                case "L'" -> {
                    clockwise_only.add("L");
                    clockwise_only.add("L");
                    clockwise_only.add("L");
                }
                case "B2" -> {
                    clockwise_only.add("B");
                    clockwise_only.add("B");
                }
                case "B'" -> {
                    clockwise_only.add("B");
                    clockwise_only.add("B");
                    clockwise_only.add("B");
                }
                case "F2" -> {
                    clockwise_only.add("F");
                    clockwise_only.add("F");
                }
                case "F'" -> {
                    clockwise_only.add("F");
                    clockwise_only.add("F");
                    clockwise_only.add("F");
                }
                case "U'" -> {
                    clockwise_only.add("U");
                    clockwise_only.add("U");
                    clockwise_only.add("U");
                }
                case "U2" -> {
                    clockwise_only.add("U");
                    clockwise_only.add("U");
                }
                case "D'" -> {
                    clockwise_only.add("D");
                    clockwise_only.add("D");
                    clockwise_only.add("D");
                }
                case "D2" -> {
                    clockwise_only.add("D");
                    clockwise_only.add("D");
                }
                default -> clockwise_only.add(moves.get(i));
            }
        }
        return clockwise_only;
        
    }



    //calls ida phase1 and updates threshold as needed
    ArrayList<String> phase1(Cubie cube){
        int threshold = computeHeuristic(cube);
        ArrayList<String> path = new ArrayList<>();
        Threshold track_threshold = new Threshold();
        ArrayList<String> result =  null;

        while (result == null){
            track_threshold = new Threshold();
            result = ida_phase1(cube, threshold, 0, new ArrayList<>(), null, track_threshold);
            if (result == null) {
            threshold = track_threshold.get_new_threshold();
        }
        }
        return result;
    }

    //calls ida phase2 and updates threshhold as needed
     ArrayList<String> phase2(Cubie cube){
        int threshold = computeHeuristic2(cube);
        ArrayList<String> path = new ArrayList<>();
        Threshold track_threshold = new Threshold();
        ArrayList<String> result =  null;

        while (result == null){
            track_threshold = new Threshold();
            result = ida_phase2(cube, threshold, 0, new ArrayList<>(), null, track_threshold);
            if (result == null) {
            threshold = track_threshold.get_new_threshold();
        }
        }
        return result;
    }

    public ArrayList<String> Solve(Cubie cube){
        Phase1PruningTable phase1_pruning_tables = new Phase1PruningTable();
        phase1_pruning_tables.EO_BFS();
        phase1_pruning_tables.CO_BFS();
        phase1_pruning_tables.Slices_BFS();
        //System.out.println("Built phase 1 pruning tables");


        Solver solve_cube = new Solver();
        Cubie cube_for_solving = new Cubie(cube);
        ArrayList<String> phase1_moves = solve_cube.phase1(cube_for_solving);
        
        
        
        Cubie post_phase1 = new Cubie(cube_for_solving);
        for (String move : phase1_moves) {
            post_phase1.applyMoves(move);
        }
        
        if (post_phase1.isSolved()) {
            //System.out.println("The cube was solved after phase1");
            ArrayList<String> final_moves = clockwise_only_string(phase1_moves);
            return final_moves;
        } else { //run phase2
            Phase2PruningTable phase2_pruning_tables = new Phase2PruningTable();
            phase2_pruning_tables.CP_BFS();
            phase2_pruning_tables.EP_BFS();
            phase2_pruning_tables.Slices_BFS();
            //System.out.println("Built phase 2 pruning tables");

        ArrayList<String> phase2_moves = solve_cube.phase2(new Cubie(post_phase1));
        

        Cubie post_phase2 = new Cubie(post_phase1);
        for (String move : phase2_moves) {
            post_phase2.applyMoves(move);
        }

        ArrayList<String> final_result = new ArrayList<>(phase1_moves);
        final_result.addAll(phase2_moves);
        ArrayList<String> final_moves = clockwise_only_string(final_result);
            return final_moves;
    }
        
        
    }


	public static void main(String[] args) {
		System.out.println("number of arguments: " + args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}
        

		if (args.length < 2) {
			System.out.println("File names are not specified");
			System.out.println("usage: java " + MethodHandles.lookup().lookupClass().getName() + " input_file output_file");
			return;
		}

		
		
		// TODO
		File input = new File(args[0]);
        Cubie cube = null;
        
        try (Scanner myReader = new Scanner(input)) {
            cube = new Cubie(args[0]);
        }
         catch (FileNotFoundException e) {
            System.out.println("Issue with reading from file");
        }
        
    
            
        // solve...

        Solver solve_cube = new Solver();
        ArrayList<String> solution = solve_cube.Solve(cube);
        //System.out.println("Solution: " + String.join(" ", solution));
       
		

        try {
            File output = new File(args[1]);
            FileWriter myWriter = new FileWriter(output);
            for (String move : solution){
                myWriter.write(move);
            }
            myWriter.close();
            } catch (IOException e) {
                System.out.println("There was an issue printing the solution to the file");
                e.printStackTrace();
            }
            
            

	}


}







