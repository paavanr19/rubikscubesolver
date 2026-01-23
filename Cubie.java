import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/*
CUBIE NAMES
URF - up, right, front
UFL - up, front, left
ULB - up, left, back
UBR - up, back, right

DFR - down, front, right
DLF - down, left, front
DBL - down, back, left
DRB - down, right, back

EDGE NAMES

UR - up, right
UF - up, front
UL - up, left
UB - up, back

FR - front, right
FL - front, left
BL - back, left
BR - back, right

DR - down, right
DF - down, front
DL - down, left
DB - down, back
*/


public class Cubie {

    

    char[][] cubieColors = {
        {'O','B','W'}, //urf
        {'O','W','G'}, //ufl
        {'O','G','Y'}, //ulb
        {'O','Y','B'}, //ubr
        {'R','W','B'}, //dfr
        {'R','G','W'}, //dlf
        {'R','Y','G'}, //dbl
        {'R','B','Y'} //drb
    };

    char[][] edgeColors = {
        {'O','B'}, //ur
        {'O','W'}, //uf
        {'O','G'},//ul
        {'O','Y'}, //ub
        {'W','B'}, //fr
        {'W','G'}, //fl
        {'Y','G'},//bl
        {'Y','B'}, //br
        {'R','B'}, //dr
        {'R','W'}, //df
        {'R','G'}, //dl
        {'R','Y'}, //db
    };

    public int[] corner_permutations;
    public int[] corner_orientations;
    public int[] edge_orientations;
    public int[] edge_permutations;

    public static final int URF = 0;
    public static final int UFL = 1;
    public static final int ULB = 2;
    public static final int UBR = 3;

    public static final int DFR = 4;
    public static final int DLF = 5;
    public static final int DBL = 6;
    public static final int DRB = 7;

    public static final int UR = 0;
    public static final int UF = 1;
    public static final int UL = 2;
    public static final int UB = 3;
    public static final int FR = 4;
    public static final int FL = 5;
    public static final int BL = 6;
    public static final int BR = 7;
    public static final int DR = 8;
    public static final int DF = 9;
    public static final int DL = 10;
    public static final int DB = 11;

    public Cubie(Cubie other) {
        this.edge_orientations = Arrays.copyOf(other.edge_orientations, 12);
        this.corner_orientations = Arrays.copyOf(other.corner_orientations, 8);
        this.edge_permutations = Arrays.copyOf(other.edge_permutations, 12);
        this.corner_permutations = Arrays.copyOf(other.corner_permutations, 8);
    }


    public Cubie() {
        corner_permutations = new int[] {0,1,2,3,4,5,6,7};
        corner_orientations = new int[] {0,0,0,0,0,0,0,0};
        edge_permutations = new int[] {0,1,2,3,4,5,6,7,8,9,10,11};
        edge_orientations = new int[] {0,0,0,0,0,0,0,0,0,0,0,0};
        }
    public Cubie(String filename) throws FileNotFoundException{
        File myFile = new File(filename);
        corner_permutations = new int[8];
        corner_orientations = new int[8];
        edge_permutations = new int[12];
        edge_orientations = new int[12];

        char[][] up = new char[3][3];
        char[][] down = new char[3][3];
        char[][] right = new char[3][3];
        char[][] left = new char[3][3];
        char[][] back = new char[3][3];
        char[][] front = new char[3][3];


        int lineCount = 0; //keeps track of the lines
        String[] result = new String[9]; //array of strings to hold the contents of the file

        //read file
        try (Scanner fileReader = new Scanner(myFile)){
            while (fileReader.hasNextLine() && lineCount < 9){

                String line = fileReader.nextLine(); //gets next line of text
                result[lineCount] = line; //stores line in string array, result
                lineCount++; //increment lineCount
                
            }
            }

                //fill faces of the cube 

                //up
                for (int i=0;i<3;i++){
                    for (int j =0;j<3;j++){
                        up[i][j] = result[i].charAt(j+3);
                    }
                }
 
                //down
                for (int i = 0; i<3;i++){
                    for (int j =0;j<3;j++){
                        down[i][j] = result[i+6].charAt(j+3);
                    }
                }
                
                //left
                for (int i = 0; i<3;i++){
                    for (int j =0 ;j <3; j++){
                        left[i][j] = result[i+3].charAt(j);
                    }
                }
                
                //front 
                for (int i=0;i<3;i++){
                    for (int j=0;j<3;j++){
                        front[i][j] = result[i+3].charAt(j+3);
                    }
                }
                
                //right
                for (int i=0;i<3;i++){
                    for (int j=0;j<3;j++){
                        right[i][j] = result[i+3].charAt(j+6);
                    }
                }
                
                //back
                for (int i=0;i<3;i++){
                    for (int j=0;j<3;j++){
                        back[i][j] = result[i+3].charAt(j+9);
                    }
                }



            corner_permutations = initialize_corner_permutation_array(up,down,left,right,front,back);
            edge_permutations = intialize_edge_permutation_array(up,down,left,right,front,back);
            corner_orientations = initialize_corner_orientation_array(corner_permutations,up,down,left,right,front,back);
            edge_orientations = initialize_edge_orientation_array(edge_permutations,up,down,left,right,front,back);





    }

    
     public char[] correct_edge_array(int x,char[][] up, char[][] down,
     char[][] left, char[][] right, char[][] front, char[][] back){

        if (x == UR){
            char[] ur_slot = {up[1][2],right[0][1]}; 
            return ur_slot;
        }
        else if (x == UF){
            char[] uf_slot = {up[2][1],front[0][1]};
            return uf_slot;
        }
        else if (x == UL){
             char[] ul_slot = {up[1][0],left[0][1]}; 
             return ul_slot;
        }
        else if(x == UB){
            char[] ub_slot = {up[0][1],back[0][1]};
            return ub_slot;
        }
        else if (x == FR){
            char[] fr_slot = {front[1][2],right[1][0]};
            return fr_slot;
        }
        else if(x == FL){
            char[] fl_slot = {front[1][0],left[1][2]};
            return fl_slot;
        }
        else if (x==BL){
            char[] bl_slot = {back[1][2],left[1][0]};
            return bl_slot;
        }
        else if(x==BR){
            char[] br_slot = {back[1][0],right[1][2]};
            return br_slot;
        }
        else if(x==DR){
            char[] dr_slot = {down[1][2],right[2][1]};
            return dr_slot;
        }
        else if(x==DF){
            char[] df_slot = {down[0][1],front[2][1]};
            return df_slot;
        }
        else if(x==DL){
            char[] dl_slot = {down[1][0],left[2][1]};
            return dl_slot;
        }
        else if(x==DB){
            char[] db_slot = {down[2][1],back[2][1]};
            return db_slot;
        }
        else {
            return null;
   }

     }

    public char[] correct_cubie_array(int x,char[][] up, char[][] down,
     char[][] left, char[][] right, char[][] front, char[][] back){

        if (x == URF){
            char[] urf_slot = {up[2][2],right[0][0],front[0][2]};
            return urf_slot;
        }
        else if (x == UFL){
            char[] ufl_slot = {up[2][0],front[0][0],left[0][2]};
            return ufl_slot;
        }
        else if (x == ULB){
             char[] ulb_slot = {up[0][0],left[0][0],back[0][2]};
             return ulb_slot;
        }
        else if(x == UBR){
            char[] ubr_slot = {up[0][2],back[0][0],right[0][2]};
            return ubr_slot;
        }
        else if (x == DFR){
            char[] dfr_slot = {down[0][2],front[2][2],right[2][0]};
            return dfr_slot;
        }
        else if(x == DLF){
            char[] dlf_slot = {down[0][0],left[2][2],front[2][0]};
            return dlf_slot;
        }
        else if (x==DBL){
            char[] dbl_slot = {down[2][0],back[2][2],left[2][0]};
            return dbl_slot;
        }
        else if(x==DRB){
            char[] drb_slot = {down[2][2],right[2][2],back[2][0]};
            return drb_slot;
        }
        else {
            return null;
   }
        
    }



    public int[] initialize_edge_orientation_array(int[] edge_permutations,char[][] up, char[][] down,
     char[][] left, char[][] right, char[][] front, char[][] back){
        int[] return_array = new int[12];

        for(int i=0;i<edge_permutations.length;i++){
            char[] edge_slot = edgeColors[edge_permutations[i]];

            if (edge_permutations[i] == UR){
                char[] slot = correct_edge_array(i, up, down, left, right, front, back);
                if (slot[0] == edge_slot[0] && slot[1] == edge_slot[1]){
                    return_array[i] = 0;
                }
                else if (slot[0] == edge_slot[1] && slot[1] == edge_slot[0]){
                    return_array[i] = 1;
                }
                
            }
            if (edge_permutations[i] == DF){
                char[] slot = correct_edge_array(i, up, down, left, right, front, back);
                if (slot[0] == edge_slot[0] && slot[1] == edge_slot[1]){
                    return_array[i] = 0;
                }
                else if (slot[0] == edge_slot[1] && slot[1] == edge_slot[0]){
                    return_array[i] = 1;
                }
               
            }

            else if (edge_permutations[i] == UF){
                char[] slot = correct_edge_array(i, up, down, left, right, front, back);
                if (slot[0] == edge_slot[0] && slot[1] == edge_slot[1]){
                    return_array[i] = 0;
                }
                else if (slot[0] == edge_slot[1] && slot[1] == edge_slot[0]){
                    return_array[i] = 1;
                }
                
            }

            else if (edge_permutations[i] == UL){
                char[] slot = correct_edge_array(i, up, down, left, right, front, back);
                if (slot[0] == edge_slot[0] && slot[1] == edge_slot[1]){
                    return_array[i] = 0;
                }
                else if (slot[0] == edge_slot[1] && slot[1] == edge_slot[0]){
                    return_array[i] = 1;
                }
               
            }

            else if (edge_permutations[i] == UB){
                char[] slot = correct_edge_array(i, up, down, left, right, front, back);
                if (slot[0] == edge_slot[0] && slot[1] == edge_slot[1]){
                    return_array[i] = 0;
                }
                else if (slot[0] == edge_slot[1] && slot[1] == edge_slot[0]){
                    return_array[i] = 1;
                }
                
            }

            else if (edge_permutations[i] == FR){
                char[] slot = correct_edge_array(i, up, down, left, right, front, back);
                if (slot[0] == edge_slot[0] && slot[1] == edge_slot[1]){
                    return_array[i] = 0;
                }
                else if (slot[0] == edge_slot[1] && slot[1] == edge_slot[0]){
                    return_array[i] = 1;
                }
                
            }

            else if (edge_permutations[i] == FL){
                char[] slot = correct_edge_array(i, up, down, left, right, front, back);
                if (slot[0] == edge_slot[0] && slot[1] == edge_slot[1]){
                    return_array[i] = 0;
                }
                else if (slot[0] == edge_slot[1] && slot[1] == edge_slot[0]){
                    return_array[i] = 1;
                }
                
            }

            else if (edge_permutations[i] == BL){
                char[] slot = correct_edge_array(i, up, down, left, right, front, back);
                if (slot[0] == edge_slot[0] && slot[1] == edge_slot[1]){
                    return_array[i] = 0;
                }
                else if (slot[0] == edge_slot[1] && slot[1] == edge_slot[0]){
                    return_array[i] = 1;
                }
                
            }

            else if (edge_permutations[i] == BR){
                char[] slot = correct_edge_array(i, up, down, left, right, front, back);
                if (slot[0] == edge_slot[0] && slot[1] == edge_slot[1]){
                    return_array[i] = 0;
                }
                else if (slot[0] == edge_slot[1] && slot[1] == edge_slot[0]){
                    return_array[i] = 1;
                }
                
            }

            else if (edge_permutations[i] == DR){
                char[] slot = correct_edge_array(i, up, down, left, right, front, back);
                if (slot[0] == edge_slot[0] && slot[1] == edge_slot[1]){
                    return_array[i] = 0;
                }
                else if (slot[0] == edge_slot[1] && slot[1] == edge_slot[0]){
                    return_array[i] = 1;
                }
                
            }

            else if (edge_permutations[i] == DL){
                char[] slot = correct_edge_array(i, up, down, left, right, front, back);
                if (slot[0] == edge_slot[0] && slot[1] == edge_slot[1]){
                    return_array[i] = 0;
                }
                else if (slot[0] == edge_slot[1] && slot[1] == edge_slot[0]){
                    return_array[i] = 1;
                }
                
            }

            else if (edge_permutations[i] == DB){
                char[] slot = correct_edge_array(i, up, down, left, right, front, back);
                if (slot[0] == edge_slot[0] && slot[1] == edge_slot[1]){
                    return_array[i] = 0;
                }
                else if (slot[0] == edge_slot[1] && slot[1] == edge_slot[0]){
                    return_array[i] = 1;
                }
                
            }

        }
        return return_array;
     }



   
    public int[] initialize_corner_orientation_array(int[] corner_permutations,char[][] up, char[][] down,
     char[][] left, char[][] right, char[][] front, char[][] back){
        int[] return_array = new int[8];

        for (int i = 0; i < corner_permutations.length; i++){
            char[] cubie_slot = cubieColors[corner_permutations[i]];


            if (corner_permutations[i] == URF){
                char[] slot = correct_cubie_array(i,up,down,left,right,front,back);
                if (slot[0] == cubie_slot[0] && slot[1] == cubie_slot[1] && slot[2] == cubie_slot[2]){
                    return_array[i] = 0;
                }
                else if (slot[0] == cubie_slot[2] && slot[1] == cubie_slot[0] && slot[2] == cubie_slot[1]){
                    return_array[i] = 1;
                }
                else if (slot[0] == cubie_slot[1] && slot[1] == cubie_slot[2] && slot[2] == cubie_slot[0]){
                    return_array[i] = 2;
                }
                
            }

            else if (corner_permutations[i] == UFL){
                char[] slot = correct_cubie_array(i,up,down,left,right,front,back);
                if (slot[0] == cubie_slot[0] && slot[1] == cubie_slot[1] && slot[2] == cubie_slot[2]){
                    return_array[i] = 0;
                }
                else if (slot[0] == cubie_slot[2] && slot[1] == cubie_slot[0] && slot[2] == cubie_slot[1]){
                    return_array[i] = 1;
                }
                else if (slot[0] == cubie_slot[1] && slot[1] == cubie_slot[2] && slot[2] == cubie_slot[0]){
                    return_array[i] = 2;
                }
                
            }

            else if (corner_permutations[i] == ULB){
                char[] slot = correct_cubie_array(i,up,down,left,right,front,back);
                if (slot[0] == cubie_slot[0] && slot[1] == cubie_slot[1] && slot[2] == cubie_slot[2]){
                    return_array[i] = 0;
                }
                else if (slot[0] == cubie_slot[2] && slot[1] == cubie_slot[0] && slot[2] == cubie_slot[1]){
                    return_array[i] = 1;
                }
                else if (slot[0] == cubie_slot[1] && slot[1] == cubie_slot[2] && slot[2] == cubie_slot[0]){
                    return_array[i] = 2;
                }
                
            }

            else if (corner_permutations[i] == UBR){
                char[] slot = correct_cubie_array(i,up,down,left,right,front,back);
                if (slot[0] == cubie_slot[0] && slot[1] == cubie_slot[1] && slot[2] == cubie_slot[2]){
                    return_array[i] = 0;
                }
                else if (slot[0] == cubie_slot[2] && slot[1] == cubie_slot[0] && slot[2] == cubie_slot[1]){
                    return_array[i] = 1;
                }
                else if (slot[0] == cubie_slot[1] && slot[1] == cubie_slot[2] && slot[2] == cubie_slot[0]){
                    return_array[i] = 2;
                }
                
            }

            else if (corner_permutations[i] == DFR){
                char[] slot = correct_cubie_array(i,up,down,left,right,front,back);
                if (slot[0] == cubie_slot[0] && slot[1] == cubie_slot[1] && slot[2] == cubie_slot[2]){
                    return_array[i] = 0;
                }
                else if (slot[0] == cubie_slot[2] && slot[1] == cubie_slot[0] && slot[2] == cubie_slot[1]){
                    return_array[i] = 1;
                }
                else if (slot[0] == cubie_slot[1] && slot[1] == cubie_slot[2] && slot[2] == cubie_slot[0]){
                    return_array[i] = 2;
                }
                
            }

            else if (corner_permutations[i] == DLF){
                char[] slot = correct_cubie_array(i,up,down,left,right,front,back);
                if (slot[0] == cubie_slot[0] && slot[1] == cubie_slot[1] && slot[2] == cubie_slot[2]){
                    return_array[i] = 0;
                }
                else if (slot[0] == cubie_slot[2] && slot[1] == cubie_slot[0] && slot[2] == cubie_slot[1]){
                    return_array[i] = 1;
                }
                else if (slot[0] == cubie_slot[1] && slot[1] == cubie_slot[2] && slot[2] == cubie_slot[0]){
                    return_array[i] = 2;
                }
                
            }

            else if (corner_permutations[i] == DBL){
                char[] slot = correct_cubie_array(i,up,down,left,right,front,back);
                if (slot[0] == cubie_slot[0] && slot[1] == cubie_slot[1] && slot[2] == cubie_slot[2]){
                    return_array[i] = 0;
                }
                else if (slot[0] == cubie_slot[2] && slot[1] == cubie_slot[0] && slot[2] == cubie_slot[1]){
                    return_array[i] = 1;
                }
                else if (slot[0] == cubie_slot[1] && slot[1] == cubie_slot[2] && slot[2] == cubie_slot[0]){
                    return_array[i] = 2;
                }
                
            }

            else if (corner_permutations[i] == DRB){
                char[] slot = correct_cubie_array(i,up,down,left,right,front,back);
                if (slot[0] == cubie_slot[0] && slot[1] == cubie_slot[1] && slot[2] == cubie_slot[2]){
                    return_array[i] = 0;
                }
                else if (slot[0] == cubie_slot[2] && slot[1] == cubie_slot[0] && slot[2] == cubie_slot[1]){
                    return_array[i] = 1;
                }
                else if (slot[0] == cubie_slot[1] && slot[1] == cubie_slot[2] && slot[2] == cubie_slot[0]){
                    return_array[i] = 2;
                }
               
            }
            
        }
       
        return return_array;

     }

    public int getCornerCubie(char[] colors){
        char[] colors1 = {colors[0],colors[1],colors[2]};
        char[] colors2 = {colors[2],colors[0],colors[1]};
        char[] colors3 = {colors[1],colors[2],colors[0]};

        for (int i=0;i<cubieColors.length;i++){
            if (Arrays.equals(colors1,cubieColors[i])){
                return i;
            }
            else if (Arrays.equals(colors2,cubieColors[i])){
                return i;
            }
            else if (Arrays.equals(colors3,cubieColors[i])){
                return i;
            }
        }
    return -1; //error
    }

    public int getEdgeCubie(char[] colors){
        char[] colors1 = {colors[0],colors[1]};
        char[] colors2 = {colors[1],colors[0]};

        for (int i=0;i<edgeColors.length;i++){
            if (Arrays.equals(colors1,edgeColors[i])){
                return i;
            }
            else if (Arrays.equals(colors2,edgeColors[i])){
                return i;
            }
        }
        return -1; //error
    }

    public int[] intialize_edge_permutation_array(char[][] up, char[][] down,
     char[][] left, char[][] right, char[][] front, char[][] back){

        char[] ur_slot = {up[1][2],right[0][1]}; 
        char[] uf_slot = {up[2][1],front[0][1]};
        char[] ul_slot = {up[1][0],left[0][1]}; //
        char[] ub_slot = {up[0][1],back[0][1]};
        char[] fr_slot = {front[1][2],right[1][0]};
        char[] fl_slot = {front[1][0],left[1][2]};
        char[] bl_slot = {back[1][2],left[1][0]};
        char[] br_slot = {back[1][0],right[1][2]};
        char[] dr_slot = {down[1][2],right[2][1]};
        char[] df_slot = {down[0][1],front[2][1]};
        char[] dl_slot = {down[1][0],left[2][1]};
        char[] db_slot = {down[2][1],back[2][1]};

        int ur = getEdgeCubie(ur_slot);
        int uf = getEdgeCubie(uf_slot);
        int ul = getEdgeCubie(ul_slot);
        int ub = getEdgeCubie(ub_slot);

        int fr = getEdgeCubie(fr_slot);
        int fl = getEdgeCubie(fl_slot);
        int bl = getEdgeCubie(bl_slot);
        int br = getEdgeCubie(br_slot);

        int dr = getEdgeCubie(dr_slot);
        int df = getEdgeCubie(df_slot);
        int dl = getEdgeCubie(dl_slot);
        int db = getEdgeCubie(db_slot);

        int[] permutation_array = new int[] {ur,uf,ul,ub,fr,fl,bl,br,dr,df,dl,db};
        

        return permutation_array;

     }

    public int[] initialize_corner_permutation_array(char[][] up, char[][] down,
     char[][] left, char[][] right, char[][] front, char[][] back){

        char[] urf_slot = {up[2][2],right[0][0],front[0][2]};
        char[] ufl_slot = {up[2][0],front[0][0],left[0][2]};
        char[] ulb_slot = {up[0][0],left[0][0],back[0][2]};
        char[] ubr_slot = {up[0][2],back[0][0],right[0][2]};
        char[] dfr_slot = {down[0][2],front[2][2],right[2][0]};
        char[] dlf_slot = {down[0][0],left[2][2],front[2][0]};
        char[] dbl_slot = {down[2][0],back[2][2],left[2][0]};
        char[] drb_slot = {down[2][2],right[2][2],back[2][0]};

        int urf = getCornerCubie(urf_slot);
        int ufl = getCornerCubie(ufl_slot);
        int ulb = getCornerCubie(ulb_slot);
        int ubr = getCornerCubie(ubr_slot);

        int dfr = getCornerCubie(dfr_slot);
        int dlf = getCornerCubie(dlf_slot);
        int dbl = getCornerCubie(dbl_slot);
        int drb = getCornerCubie(drb_slot);

        int[] permutation_array = new int[] {urf,ufl,ulb,ubr,dfr,dlf,dbl,drb};
        return permutation_array;

     }

    public boolean isSolved(){
        for (int i=0;i<corner_permutations.length;i++){
            if (corner_permutations[i]!=i){
                return false;
            }
        }

        for (int i=0;i<corner_orientations.length;i++){
            if (corner_orientations[i]!=0){
                return false;
            }
        }

        for (int i=0;i<edge_orientations.length;i++){
            if (edge_orientations[i]!=0){
                return false;
            }
        }
        return true;
    }

public void rotateR_Clockwise() {

    // Corners
    int temp1 = corner_permutations[URF];
    corner_permutations[URF] = corner_permutations[DFR]; 
    corner_permutations[DFR] = corner_permutations[DRB]; 
    corner_permutations[DRB] = corner_permutations[UBR]; 
    corner_permutations[UBR] = temp1;                     

    int temp2 = corner_orientations[URF];
    corner_orientations[URF] = (corner_orientations[DFR] + 2) % 3;
    corner_orientations[DFR] = (corner_orientations[DRB] + 1) % 3;
    corner_orientations[DRB] = (corner_orientations[UBR] + 2) % 3;
    corner_orientations[UBR] = (temp2 + 1) % 3;

    int temp3 = edge_permutations[FR];
    edge_permutations[FR] = edge_permutations[DR];
    edge_permutations[DR] = edge_permutations[BR];
    edge_permutations[BR] = edge_permutations[UR];
    edge_permutations[UR] = temp3;

    int temp4 = edge_orientations[FR];
    edge_orientations[FR] = (edge_orientations[DR]+0)%2;
    edge_orientations[DR] = (edge_orientations[BR]+0)%2;
    edge_orientations[BR] = (edge_orientations[UR]+0)%2;
    edge_orientations[UR] = (temp4+0)%2;

    
}

public void rotateR_CounterClockwise(){
    rotateR_Clockwise();
    rotateR_Clockwise();
    rotateR_Clockwise();
}

public void rotateR2(){
    rotateR_Clockwise();
    rotateR_Clockwise();
}

public void rotateL_Clockwise() {

    int temp1 = corner_permutations[UFL];
    corner_permutations[UFL] = corner_permutations[ULB];
    corner_permutations[ULB] = corner_permutations[DBL];
    corner_permutations[DBL] = corner_permutations[DLF];
    corner_permutations[DLF] = temp1;


    int temp2 = corner_orientations[UFL];
    corner_orientations[UFL] = (corner_orientations[ULB] + 1) % 3;
    corner_orientations[ULB] = (corner_orientations[DBL] + 2) % 3;
    corner_orientations[DBL] = (corner_orientations[DLF] + 1) % 3;
    corner_orientations[DLF] = (temp2 + 2) % 3;


    int temp3 = edge_permutations[BL];
    edge_permutations[BL] = edge_permutations[DL];
    edge_permutations[DL] = edge_permutations[FL];
    edge_permutations[FL] = edge_permutations[UL];
    edge_permutations[UL] = temp3;

    int temp4 = edge_orientations[BL];
    edge_orientations[BL] = (edge_orientations[DL]+0)%2;
    edge_orientations[DL] = (edge_orientations[FL]+0)%2;
    edge_orientations[FL] = (edge_orientations[UL]+0)%2;
    edge_orientations[UL] = (temp4+0)%2;
}

public void rotateL_CounterClockwise(){
    rotateL_Clockwise();
    rotateL_Clockwise();
    rotateL_Clockwise();
}

public void rotateL2(){
    rotateL_Clockwise();
    rotateL_Clockwise();
}

public void rotateF_Clockwise(){
    int temp1 = corner_permutations[URF]; 
    corner_permutations[URF] = corner_permutations[UFL];
    corner_permutations[UFL] = corner_permutations[DLF]; 
    corner_permutations[DLF] = corner_permutations[DFR]; 
    corner_permutations[DFR] = temp1; 

    int temp2 = corner_orientations[URF];
    corner_orientations[URF] = (corner_orientations[UFL] + 1) % 3;
    corner_orientations[UFL] = (corner_orientations[DLF] + 2) % 3;
    corner_orientations[DLF] = (corner_orientations[DFR] + 1) % 3;
    corner_orientations[DFR] = (temp2 + 2) % 3;

    int temp3 = edge_permutations[UF];
    edge_permutations[UF] = edge_permutations[FL];
    edge_permutations[FL] = edge_permutations[DF];
    edge_permutations[DF] = edge_permutations[FR];
    edge_permutations[FR] = temp3;

    int temp4 = edge_orientations[UF];
    edge_orientations[UF] = (edge_orientations[FL]+1)%2;
    edge_orientations[FL] = (edge_orientations[DF]+1)%2;
    edge_orientations[DF] = (edge_orientations[FR]+1)%2;
    edge_orientations[FR] = (temp4+1)%2;

}

public void rotateF2(){
    rotateF_Clockwise();
    rotateF_Clockwise();
}

public void rotateF_CounterClockwise(){
    rotateF_Clockwise();
    rotateF_Clockwise();
    rotateF_Clockwise();
}

public void rotateU_Clockwise() {
    int temp1 = corner_permutations[URF];
    corner_permutations[URF] = corner_permutations[UBR];
    corner_permutations[UBR] = corner_permutations[ULB];
    corner_permutations[ULB] = corner_permutations[UFL];
    corner_permutations[UFL] = temp1;

    int temp2 = corner_orientations[URF];
    corner_orientations[URF] = (corner_orientations[UBR] + 0) % 3;
    corner_orientations[UBR] = (corner_orientations[ULB] + 0) % 3;
    corner_orientations[ULB] = (corner_orientations[UFL] + 0) % 3;
    corner_orientations[UFL] = (temp2 + 0) % 3;

    int temp3 = edge_permutations[UF];
    edge_permutations[UF] = edge_permutations[UR];
    edge_permutations[UR] = edge_permutations[UB];
    edge_permutations[UB] = edge_permutations[UL];
    edge_permutations[UL] = temp3;

    int temp4 = edge_orientations[UF];
    edge_orientations[UF] = (edge_orientations[UR]+0)%2;
    edge_orientations[UR] = (edge_orientations[UB]+0)%2;
    edge_orientations[UB] = (edge_orientations[UL]+0)%2;
    edge_orientations[UL] = (temp4+0)%2;
}

public void rotateU_CounterClockwise(){
    rotateU_Clockwise();
    rotateU_Clockwise();
    rotateU_Clockwise();
}

public void rotateU2(){
    rotateU_Clockwise();
    rotateU_Clockwise();
}


public void rotateB_Clockwise() {
    int temp1 = corner_permutations[ULB];
    corner_permutations[ULB] = corner_permutations[UBR];
    corner_permutations[UBR] = corner_permutations[DRB];
    corner_permutations[DRB] = corner_permutations[DBL];
    corner_permutations[DBL] = temp1;

    int temp2 = corner_orientations[ULB];
    corner_orientations[ULB] = (corner_orientations[UBR] + 1) % 3;
    corner_orientations[UBR] = (corner_orientations[DRB] + 2) % 3;
    corner_orientations[DRB] = (corner_orientations[DBL] + 1) % 3;
    corner_orientations[DBL] = (temp2 + 2) % 3;

    int temp3 = edge_permutations[UB];
    edge_permutations[UB] = edge_permutations[BR];
    edge_permutations[BR] = edge_permutations[DB];
    edge_permutations[DB] = edge_permutations[BL];
    edge_permutations[BL] = temp3;

    int temp4 = edge_orientations[UB];
    edge_orientations[UB] = (edge_orientations[BR]+1)%2;
    edge_orientations[BR] = (edge_orientations[DB]+1)%2;
    edge_orientations[DB] = (edge_orientations[BL]+1)%2;
    edge_orientations[BL] = (temp4+1)%2;

}

public void rotateB_CounterClockwise(){
    rotateB_Clockwise();
    rotateB_Clockwise();
    rotateB_Clockwise();
}

public void rotateB2(){
    rotateB_Clockwise();
    rotateB_Clockwise();
}
public void rotateD_Clockwise() {
    int temp1 = corner_permutations[DFR];
    corner_permutations[DFR] = corner_permutations[DLF];
    corner_permutations[DLF] = corner_permutations[DBL];
    corner_permutations[DBL] = corner_permutations[DRB];
    corner_permutations[DRB] = temp1;

    int temp3 = corner_orientations[DFR];
    corner_orientations[DFR] = (corner_orientations[DLF] + 0) % 3;
    corner_orientations[DLF] = (corner_orientations[DBL] + 0) % 3;
    corner_orientations[DBL] = (corner_orientations[DRB] + 0) % 3;
    corner_orientations[DRB] = (temp3 + 0) % 3;

    int temp2 = edge_permutations[DF];
    edge_permutations[DF] = edge_permutations[DL];
    edge_permutations[DL] = edge_permutations[DB];
    edge_permutations[DB] = edge_permutations[DR];
    edge_permutations[DR] = temp2;

    int temp4 = edge_orientations[DF];
    edge_orientations[DF] = (edge_orientations[DL]+0)%2;
    edge_orientations[DL] = (edge_orientations[DB]+0)%2;
    edge_orientations[DB] = (edge_orientations[DR]+0)%2;
    edge_orientations[DR] = (temp4+0)%2;
}
public void rotateD2(){
    rotateD_Clockwise();
    rotateD_Clockwise();
}

public void rotateD_CounterClockwise(){
    rotateD_Clockwise();
    rotateD_Clockwise();
    rotateD_Clockwise();
}





public void applyMoves(String moves){
    String[] movesList = moves.split("\\s+");
    int len = movesList.length;
    
    for (int i=0;i<len;i++){
        String current = movesList[i];
        switch (current) {
            case "R" -> rotateR_Clockwise();
            case "F" -> rotateF_Clockwise();
            case "B" -> rotateB_Clockwise();
            case "U" -> rotateU_Clockwise();
            case "D" -> rotateD_Clockwise();
            case "L" -> rotateL_Clockwise();
            case "R2" -> rotateR2(); 
            case "L2" -> rotateL2(); 
            case "B2" -> rotateB2(); 
            case "F2" ->  rotateF2();
            case "U2" -> rotateU2(); 
            case "D2" ->rotateD2(); 
            case "L'"-> rotateL_CounterClockwise(); 
            case "R'"->rotateR_CounterClockwise(); 
            case "F'"-> rotateF_CounterClockwise(); 
            case "U'"->rotateU_CounterClockwise();
            case "B'"->rotateB_CounterClockwise();
            case "D'"->rotateD_CounterClockwise();
            default -> System.out.println("Unknown move: " + current);
        }
    }
}


    @Override
    public String toString(){
        char[][] up = new char[3][3];
        char[][] down = new char[3][3];
        char[][] left = new char[3][3];
        char[][] right = new char[3][3];
        char[][] front = new char[3][3];
        char[][] back = new char[3][3];

        up[1][1] = 'O';
        down[1][1] = 'R';
        right[1][1] = 'B';
        left[1][1] = 'G';
        front[1][1] = 'W';
        back[1][1] = 'Y';



        //URF SLOT

        int corner_id_0 = corner_permutations[URF];
        int corner_orientation_0 = corner_orientations[URF];
        char[] cubie_colors_0 = cubieColors[corner_id_0];

        switch (corner_orientation_0) {
            case 0 -> {
                up[2][2] = cubie_colors_0[0];
                right[0][0] = cubie_colors_0[1];
                front[0][2] = cubie_colors_0[2];
            }
            case 1 -> {
                up[2][2] = cubie_colors_0[2];
                right[0][0] = cubie_colors_0[0];
                front[0][2] = cubie_colors_0[1];
            }
            case 2 -> {
                up[2][2] = cubie_colors_0[1];
                right[0][0] = cubie_colors_0[2];
                front[0][2] = cubie_colors_0[0];
            }
            default -> {
                System.out.println("Error with cubie orientation in slot URF");
            }
        }

        //UFL SLOT
        int corner_id_1 = corner_permutations[UFL];
        int corner_orientation_1 = corner_orientations[UFL];
        char[] cubie_colors_1 = cubieColors[corner_id_1];

        switch (corner_orientation_1) {
            case 0 -> {
                up[2][0] = cubie_colors_1[0];
                front[0][0] = cubie_colors_1[1];
                left[0][2] = cubie_colors_1[2];
            }
            case 1 -> {
                up[2][0] = cubie_colors_1[2];
                front[0][0] = cubie_colors_1[0];
                left[0][2] = cubie_colors_1[1];
            }
            case 2 -> {
                up[2][0] = cubie_colors_1[1];
                front[0][0] = cubie_colors_1[2];
                left[0][2] = cubie_colors_1[0];
            }
            default -> {
                System.out.println("Error with cubie orientation in slot UFL");
            }
        }

        //ULB SLOT
        int corner_id_2 = corner_permutations[ULB];
        int corner_orientation_2 = corner_orientations[ULB];
        char[] cubie_colors_2 = cubieColors[corner_id_2];

        switch (corner_orientation_2) {
            case 0 -> {
                up[0][0] = cubie_colors_2[0];
                left[0][0] = cubie_colors_2[1];
                back[0][2] = cubie_colors_2[2];
            }
            case 1 -> {
                up[0][0] = cubie_colors_2[2];
                left[0][0] = cubie_colors_2[0];
                back[0][2] = cubie_colors_2[1];
            }
            case 2 -> {
                up[0][0] = cubie_colors_2[1];
                left[0][0] = cubie_colors_2[2];
                back[0][2] = cubie_colors_2[0];
            }
            default -> {
                System.out.println("Error with cubie orientation in slot ULB");
            }
        }

        //UBR SLOT
        int corner_id_3 = corner_permutations[UBR];
        int corner_orientation_3 = corner_orientations[UBR];
        char[] cubie_colors_3 = cubieColors[corner_id_3];

        switch (corner_orientation_3) {
            case 0 -> {
                up[0][2] = cubie_colors_3[0];
                back[0][0] = cubie_colors_3[1];
                right[0][2] = cubie_colors_3[2];
            }
            case 1 -> {
                up[0][2] = cubie_colors_3[2];
                back[0][0] = cubie_colors_3[0];
                right[0][2] = cubie_colors_3[1];
            }
            case 2 -> {
                up[0][2] = cubie_colors_3[1];
                back[0][0] = cubie_colors_3[2];
                right[0][2] = cubie_colors_3[0];
            }
            default -> {
                System.out.println("Error with cubie orientation in slot UBR");
            }
        }

        //DFR SLOT
        int corner_id_4 = corner_permutations[DFR];
        int corner_orientation_4 = corner_orientations[DFR];
        char[] cubie_colors_4 = cubieColors[corner_id_4];

        switch (corner_orientation_4) {
            case 0 -> {
                down[0][2] = cubie_colors_4[0];
                front[2][2] = cubie_colors_4[1];
                right[2][0] = cubie_colors_4[2];
            }
            case 1 -> {
                down[0][2] = cubie_colors_4[2];
                front[2][2] = cubie_colors_4[0];
                right[2][0] = cubie_colors_4[1];
            }
            case 2 -> {
                down[0][2] = cubie_colors_4[1];
                front[2][2] = cubie_colors_4[2];
                right[2][0] = cubie_colors_4[0];
            }
            default -> {
                System.out.println("Error with cubie orientation in slot DFR");
            }
        }

        //DLF SLOT
        int corner_id_5 = corner_permutations[DLF];
        int corner_orientation_5 = corner_orientations[DLF];
        char[] cubie_colors_5 = cubieColors[corner_id_5];

        switch (corner_orientation_5) {
            case 0 -> {
                down[0][0] = cubie_colors_5[0];
                left[2][2] = cubie_colors_5[1];
                front[2][0] = cubie_colors_5[2];
            }
            case 1 -> {
                down[0][0] = cubie_colors_5[2];
                left[2][2] = cubie_colors_5[0];
                front[2][0] = cubie_colors_5[1];
            }
            case 2 -> {
                down[0][0] = cubie_colors_5[1];
                left[2][2] = cubie_colors_5[2];
                front[2][0] = cubie_colors_5[0];
            }
            default -> {
                System.out.println("Error with cubie orientation in slot DLF");
            }
        }

        //DBL SLOT
        int corner_id_6 = corner_permutations[DBL];
        int corner_orientation_6 = corner_orientations[DBL];
        char[] cubie_colors_6 = cubieColors[corner_id_6];

        switch (corner_orientation_6) {
            case 0 -> {
                down[2][0] = cubie_colors_6[0];
                back[2][2] = cubie_colors_6[1];
                left[2][0] = cubie_colors_6[2];
            }
            case 1 -> {
                down[2][0] = cubie_colors_6[2];
                back[2][2] = cubie_colors_6[0];
                left[2][0] = cubie_colors_6[1];
            }
            case 2 -> {
                down[2][0] = cubie_colors_6[1];
                back[2][2] = cubie_colors_6[2];
                left[2][0] = cubie_colors_6[0];
            }
            default -> {
                System.out.println("Error with cubie orientation in slot DBL");
            }
        }

        //DRB SLOT
        int corner_id_7 = corner_permutations[DRB];
        int corner_orientation_7 = corner_orientations[DRB];
        char[] cubie_colors_7 = cubieColors[corner_id_7];

        switch (corner_orientation_7) {
            case 0 -> {
                down[2][2] = cubie_colors_7[0];
                right[2][2] = cubie_colors_7[1];
                back[2][0] = cubie_colors_7[2];
            }
            case 1 -> {
                down[2][2] = cubie_colors_7[2];
                right[2][2] = cubie_colors_7[0];
                back[2][0] = cubie_colors_7[1];
            }
            case 2 -> {
                down[2][2] = cubie_colors_7[1];
                right[2][2] = cubie_colors_7[2];
                back[2][0] = cubie_colors_7[0];
            }
            default -> {
                System.out.println("Error with cubie orientation in slot DRB");
            }
        }

        //UR SLOT
        int edge_id_0 = edge_permutations[UR];
        int edge_orientation_0 = edge_orientations[UR];
        char[] edge_colors_0 = edgeColors[edge_id_0];

        switch(edge_orientation_0){
            case 0-> {
                up[1][2] = edge_colors_0[0];
                right[0][1] = edge_colors_0[1];
            }
            case 1->{
                up[1][2] = edge_colors_0[1];
                right[0][1] = edge_colors_0[0];
            }
            default -> {
                System.out.println("Error with edge orientation in slot UR");
            }
        }

        //UF SLOT
        int edge_id_1 = edge_permutations[UF];
        int edge_orientation_1 = edge_orientations[UF];
        char[] edge_colors_1 = edgeColors[edge_id_1];

        switch(edge_orientation_1){
            case 0-> {
                up[2][1] = edge_colors_1[0];
                front[0][1] = edge_colors_1[1];
            }
            case 1->{
                up[2][1] = edge_colors_1[1];
                front[0][1] = edge_colors_1[0];
            }
            default -> {
                System.out.println("Error with edge orientation in slot UF");
            }
        }

        //UL SLOT
        int edge_id_2 = edge_permutations[UL];
        int edge_orientation_2 = edge_orientations[UL];
        char[] edge_colors_2 = edgeColors[edge_id_2];

        switch(edge_orientation_2){
            case 0-> {
                up[1][0] = edge_colors_2[0];
                left[0][1] = edge_colors_2[1];
            }
            case 1->{
                up[1][0] = edge_colors_2[1];
                left[0][1] = edge_colors_2[0];
            }
            default -> {
                System.out.println("Error with edge orientation in slot UL");
            }
        }

        //UB SLOT
        int edge_id_3 = edge_permutations[UB];
        int edge_orientation_3 = edge_orientations[UB];
        char[] edge_colors_3 = edgeColors[edge_id_3];

        switch(edge_orientation_3){
            case 0-> {
                up[0][1] = edge_colors_3[0];
                back[0][1] = edge_colors_3[1];
            }
            case 1->{
                up[0][1] = edge_colors_3[1];
                back[0][1] = edge_colors_3[0];
            }
            default -> {
                System.out.println("Error with edge orientation in slot UB");
            }
        }

        //FR SLOT
        int edge_id_4 = edge_permutations[FR];
        int edge_orientation_4 = edge_orientations[FR];
        char[] edge_colors_4 = edgeColors[edge_id_4];

        switch(edge_orientation_4){
            case 0-> {
                front[1][2] = edge_colors_4[0];
                right[1][0] = edge_colors_4[1];
            }
            case 1->{
                front[1][2] = edge_colors_4[1];
                right[1][0] = edge_colors_4[0];
            }
            default -> {
                System.out.println("Error with edge orientation in slot FR");
            }
        }

        //FL SLOT
        int edge_id_5 = edge_permutations[FL];
        int edge_orientation_5 = edge_orientations[FL];
        char[] edge_colors_5 = edgeColors[edge_id_5];

        switch(edge_orientation_5){
            case 0-> {
                front[1][0] = edge_colors_5[0];
                left[1][2] = edge_colors_5[1];
            }
            case 1->{
                front[1][0] = edge_colors_5[1];
                left[1][2] = edge_colors_5[0];
            }
            default -> {
                System.out.println("Error with edge orientation in slot FL");
            }
        }

        //BL SLOT
        int edge_id_6 = edge_permutations[BL];
        int edge_orientation_6 = edge_orientations[BL];
        char[] edge_colors_6 = edgeColors[edge_id_6];

        switch(edge_orientation_6){
            case 0-> {
                back[1][2] = edge_colors_6[0];
                left[1][0] = edge_colors_6[1];
            }
            case 1->{
                back[1][2] = edge_colors_6[1];
                left[1][0] = edge_colors_6[0];
            }
            default -> {
                System.out.println("Error with edge orientation in slot BL");
            }
        }

        //BR SLOT
        int edge_id_7 = edge_permutations[BR];
        int edge_orientation_7 = edge_orientations[BR];
        char[] edge_colors_7 = edgeColors[edge_id_7];

        switch(edge_orientation_7){
            case 0-> {
                back[1][0] = edge_colors_7[0];
                right[1][2] = edge_colors_7[1];
            }
            case 1->{
                back[1][0] = edge_colors_7[1];
                right[1][2] = edge_colors_7[0];
            }
            default -> {
                System.out.println("Error with edge orientation in slot BR");
            }
        }

        //DR SLOT
        int edge_id_8 = edge_permutations[DR];
        int edge_orientation_8 = edge_orientations[DR];
        char[] edge_colors_8 = edgeColors[edge_id_8];

        switch(edge_orientation_8){
            case 0-> {
                down[1][2] = edge_colors_8[0];
                right[2][1] = edge_colors_8[1];
            }
            case 1->{
                down[1][2] = edge_colors_8[1];
                right[2][1] = edge_colors_8[0];
            }
            default -> {
                System.out.println("Error with edge orientation in slot DR");
            }
        }

        //DF SLOT
        int edge_id_9 = edge_permutations[DF];
        int edge_orientation_9 = edge_orientations[DF];
        char[] edge_colors_9 = edgeColors[edge_id_9];

        switch(edge_orientation_9){
            case 0-> {
                down[0][1] = edge_colors_9[0];
                front[2][1] = edge_colors_9[1];
            }
            case 1->{
                down[0][1] = edge_colors_9[1];
                front[2][1] = edge_colors_9[0];
            }
            default -> {
                System.out.println("Error with edge orientation in slot DF");
            }
        }

        //DL SLOT
        int edge_id_10 = edge_permutations[DL];
        int edge_orientation_10 = edge_orientations[DL];
        char[] edge_colors_10 = edgeColors[edge_id_10];

        switch(edge_orientation_10){
            case 0-> {
                down[1][0] = edge_colors_10[0];
                left[2][1] = edge_colors_10[1];
            }
            case 1->{
                down[1][0] = edge_colors_10[1];
                left[2][1] = edge_colors_10[0];
            }
            default -> {
                System.out.println("Error with edge orientation in slot DL");
            }
        }

        //DB SLOT
        int edge_id_11 = edge_permutations[DB];
        int edge_orientation_11 = edge_orientations[DB];
        char[] edge_colors_11 = edgeColors[edge_id_11];

        switch(edge_orientation_11){
            case 0-> {
                down[2][1] = edge_colors_11[0];
                back[2][1] = edge_colors_11[1];
            }
            case 1->{
                down[2][1] = edge_colors_11[1];
                back[2][1] = edge_colors_11[0];
            }
            default -> {
                System.out.println("Error with edge orientation in slot DB");
            }
        }
        StringBuilder sb = new StringBuilder();

    // Print UP face
    for (int i = 0; i < 3; i++) {
        sb.append("   "); 
        for (int j = 0; j < 3; j++) {
            sb.append(up[i][j]);
        }
        sb.append("\n");
    }

    // Print LEFT, FRONT, RIGHT, BACK faces side by side
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) sb.append(left[i][j]);
        for (int j = 0; j < 3; j++) sb.append(front[i][j]);
        for (int j = 0; j < 3; j++) sb.append(right[i][j]);
        for (int j = 0; j < 3; j++) sb.append(back[i][j]);
        sb.append("\n");
    }

    // Print DOWN face
    for (int i = 0; i < 3; i++) {
        sb.append("   "); // indent for bottom face
        for (int j = 0; j < 3; j++) {
            sb.append(down[i][j]);
        }
        sb.append("\n");
    }

    return sb.toString();
    }



}
