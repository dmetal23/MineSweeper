/*
Programmer: Daniel Rojas
Class: Comp 182, Summer 2013
*/

   import java.util.*;

   public class MineGrid { 
      private class Cell { 
         private int row, col; 
         private boolean marked, covered, mined; 
         private int adjcount; 
      	
      	
         public Cell(int r, int c){
            row = r;
            col = c;
            mined = false;
            adjcount = 0;
            covered = true;
            marked = false; 
         }
      
         public boolean getMarked(){
            return marked;
         }
      	
         public boolean getCovered(){
            return covered;
         }
      	
         public boolean getMined(){
            return mined;
         }
      	
         public int getAdjcount(){
            return adjcount;
         }
      	
         public int getRow(){
            return row; 
         }
      	
         public int getCol(){
            return col;
         }
      	
         public void setCovered(boolean value) {
            if(value == true)
               covered = true;
            else
               covered = false;
         }
      	
         public void setMined(boolean value) {
            if(value == true)
               mined = true;
            else
               mined = false; 
         }
      	
         public void setAdjcount(int c) {
            adjcount = c;
         }
      
         public void setMarked(boolean value){
            if(value == true)
               marked = true;
            else
               marked = false;
         }
      
         private String toStringUncovered(){
            String s = "";
         	if(marked == true)
               return "#";
            if(mined == true)
               return "X";
            else {
               if(adjcount == 0)
                  return "_";
               else {
                  return s + adjcount; 
               }
            }
         }
      	
         public String toString() {
            if(covered == true && marked == false) 
               return "?";
				else if(covered == true && marked == true)
					return "#";
            else 
               return toStringUncovered();
         }
      }
       
      private Cell[][] cells; //reference to class
   	
      private int rows, cols, mines; 
   	
      public MineGrid(int r, int c, int m) {
         rows = r;
         cols = c;
         mines = m;
         cells = new Cell[rows][cols];
      	
         for(int i = 0; i< rows; i++) {
            for(int j = 0; j < cols; j++) {
               cells[i][j] = new Cell(i,j);
            }
         }
         positionMines();
         calculateAdjacencyCounts();
      }
   	
      public void positionMines(){
         for(int i = 0; i < mines; i++){
            int r = (int)(Math.random() * rows);
            int c = (int)(Math.random() * cols);
            if(cells[r][c].mined == true)
               continue; 
            else
               cells[r][c].setMined(true);
         }
      }
   	
      private boolean isValidPosition(int r, int c){
      	
         if(r >= rows || r < 0 || c >= cols || c < 0)
            return false;
         else
            return true; 
      }
   	
      enum Dir{
         U,
         UR,
         R , 
         DR, 
         D ,
         DL ,
         L ,
         UL;
      }
   	
      private Cell getNeighbor(Cell cell, Dir direction){ 
      
         int r = cell.getRow();
         int c = cell.getCol();
      
         switch(direction) {
         
            case U: 
               if(isValidPosition(r-1, c) == true)
                  return cells[r-1][c];
               break;
         
         
            case UR:
               if(isValidPosition(r-1, c+1) == true)
                  return cells[r-1][c+1];
               break;
         	
            case R:
               if(isValidPosition(r, c+1) == true)
                  return cells[r][c+1];
               break;	
         
            case DR:
               if(isValidPosition(r+1, c+1) == true)
                  return cells[r+1][c+1];
               break;
         	
            case D:
               if(isValidPosition(r+1, c) == true)
                  return cells[r+1][c];
               break;
         	
            case DL:
               if(isValidPosition(r+1, c-1) == true)
                  return cells[r+1][c-1];
               break;
         
            case L:
               if(isValidPosition(r, c-1) == true)
                  return cells[r][c-1];
               break;
         	
            case UL:
               if(isValidPosition(r-1, c-1) == true)
                  return cells[r-1][c-1];
               break;
         
         }
         return null;
      		
      }
   	
      private void calculateAdjacencyCounts(){
      	
         for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++) {
               if(cells[i][j].mined == false){
                  int x = 0;
                  cells[i][j].setAdjcount(x);
               
                  for(Dir d : Dir.values())
                  {
                     if(getNeighbor(cells[i][j], d) != null && getNeighbor(cells[i][j], d).mined == true)
                     {
                        x++;
                     }
                  }
                  cells[i][j].setAdjcount(x);
               }
            }
         }
      }
   	
      public String toStringUncovered(boolean uncovered){
         String s = "";
         String t = "";
         for(int i = 0; i< rows; i++) {
            for(int j = 0; j < cols; j++) {
               if(uncovered == true) {
                  t = cells[i][j].toStringUncovered();
               }
               else {
                  t = cells[i][j].toString();
               }
               s = s + " ";
               s = s + t;
            }
            s = s + "\n";
         }
         return s;
      }
		
      public String toString() { 
         return toStringUncovered(false);
      }
						
      boolean minesMarked(){
         for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
               if(cells[i][j].mined == true && cells[i][j].marked == true)
                  return true; 
            }
         }
			return false; 
      }
   		
      boolean nonMinesUncovered(){
         for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
               if(cells[i][j].mined == false && cells[i][j].covered == false)
                  return true; 
            }
         }
         return false;
      }
   	
      public void commandLine() {
      	
         Scanner input = new Scanner(System.in);
      
			System.out.println("Commands: >show, >u r c, >m r c, >quit"); 
			
         while(true) {
            System.out.print(">");
            String line = input.nextLine();
            String[] tokens = line.split(" ");
            String command = tokens[0];
            line = line.toLowerCase();
         	
            if(command.equals("show")) {
               System.out.print(toString()); 
            }
				
            else if(command.equals("u")) {
               int r = Integer.parseInt(tokens[1]);
               int c = Integer.parseInt(tokens[2]);
               boolean valid = isValidPosition(r,c);
               if(tokens.length != 3){
                  System.out.println("Error! This command requires 2 arguments!");
						continue;
					}
					if(valid && cells[r][c].mined == true && cells[r][c].marked == false) {
							cells[r][c].setCovered(false); 
							System.out.print(toString());
                     System.out.println("Game over, you lose!"); 
                     System.exit(0);
                  }
               if(valid && cells[r][c].covered == true) {
                  if(cells[r][c].marked == true) {
                     System.out.println("Please unmark the cell before uncovering!"); 
                     continue;
                  }
                  cells[r][c].setCovered(false);
                  System.out.print(toString());			
               }
               else if(valid && cells[r][c].covered == false) {
                  System.out.println("That cell is already uncovered!");
                  continue;
               }
               else if(valid && cells[r][c].covered == true){
                  cells[r][c].setCovered(false); 
               }
					else {
						System.out.println("Please enter valid coordinates!");
						continue;
					}
					
					if(minesMarked() == true && nonMinesUncovered() == true){ //checking for win
						System.out.println("You have won the game!");
						System.exit(1);
					}
            }
				
            else if(command.equals("m")) {
               int r = Integer.parseInt(tokens[1]);
               int c = Integer.parseInt(tokens[2]);
               boolean valid = isValidPosition(r,c);
               if(tokens.length != 3) {
                  System.out.println("Error! This command requires 2 arguments!");
						continue;
					}
               if(valid && cells[r][c].marked == false && cells[r][c].covered == false) {
                  System.out.println("That cell is already uncovered.");
                  continue;
               }
               else if(valid && cells[r][c].marked == false) {
                  cells[r][c].setMarked(true);
                  System.out.print(toString());
               }
               else if(valid && cells[r][c].marked == true) {
                  cells[r][c].setMarked(false);
                  System.out.print(toString());
               }
					else {
						System.out.println("Please enter valid coordinates.");
						continue;
					}
					
					if(minesMarked() == true && nonMinesUncovered() == true){ //checking for win
						System.out.println("You have won the game!");
						System.exit(2);
					}
            }
				
            else if(command.equals("quit")) {
               System.out.println("You have closed the current game.");
               System.exit(3);
            }
				
				else {
					System.out.println("Command not recognized. Please enter a valid command.");
					continue;
				}
         }
      }
   }
	
		
