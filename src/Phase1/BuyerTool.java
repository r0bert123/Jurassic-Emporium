/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Phase1;

import java.util.*;
import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Gabriel I.F. Whites
 */
public class BuyerTool {
    
    String ShopName;

    public class Bone
    {
        String name;
        int id, sold;
        int x,y; /**coordinates*/
    }
    ArrayList<Bone> BoneArray = new ArrayList<>();
    ArrayList<Grid_Point> List = new ArrayList<>();
    
    public class Grid_Point {

        public int row, column;
        public String type;
    }
    
    Scanner keyboard;
    Formatter screen;

    public BuyerTool()
    {
        keyboard=new Scanner(System.in);
        screen=new Formatter(System.out);
    }
            
    public void run()
    {
        Menu();
    }
    
    public void Menu()
    {
        int choice;
            
        screen.format("1) What would you like to do?\n");
        screen.format("2) Load the map\n");
        screen.format("3) View the map\n");
        screen.format("4) Buy a dinousaur bone\n");
        screen.format("5) Save list");
        screen.format("6) Load list");
        screen.format(">");
        
        choice = keyboard.nextInt();
            
        switch(choice)
        {
            case 1:
            {
                LoadMap();
                break;
            }
                
            case 2:
            {
                ViewMap();
                break;
            }
                
            case 3:
            {
                Buy();
                break;
            }
                
            case 4:
            {
                SaveList();
                break;
            }
                
            case 5:
            {
                LoadList();
                break;
            } 
        }
        screen.format("\u000C");
        Menu();
    }
        //My attempt at using your function  
    private List<Grid_Point> LoadMap() {

        //Initializes a StreamReader object that reads in the map
        File temp = new File("MapMarket.txt");
        Scanner read;
        try {
            read = new Scanner(temp);
            Grid_Point grid_temp;
            String current_line;
            while (read.hasNextLine()) 
            {
                grid_temp = new Grid_Point();
                current_line = read.nextLine();
                if (current_line != "" && current_line != null)
                {
                    grid_temp.column = Integer.parseInt(current_line.split(",")[0]);
                    grid_temp.row = Integer.parseInt(current_line.split(",")[1]);
                    grid_temp.type = current_line.split(",")[2];
                    Map_Data_List.add(grid_temp);
                }
            }
                
                //Checks the Shop Name
                if (ShopName.equals(null))
                    ShopName = "Jurassic Emporium";
                read.close();
                screen.format("The Map has Successfully been Loaded!\n");

            } catch (Exception e) {
                screen.format("MapMarket.txt Failed to Open!\n");
            }

            return Map_Data_List;
        }
        
        
        /*public void LoadMap()
        {
               filename= "MMarket.txt"
                        1200 lines
                        load into ArrayList 60,20
                        land or ocean (0/1)
                        map grid points
                                class that contains
                                        coordinates
                                        map typ
        
        }*/
        
        public void ViewMap()
        {
            create map showing original location
                    x to show location of bone
                    $ to sold bone
        }
        
        public void Buy()
        {
            int i;
            int purchase;
            
            for(i=0;i<=length of list;i++)
                if(notsold)
                    screen.format("%s:%d\n"name,id);
            screen.format("Input ID to purchase\n");
            screen.format(">")
            purchase=keyboard.nextInt();
            screen.format("Are you sure?\n");
            screen.format("Y : Yes\n");
            screen.format("N : No\n");
                if((keyboard.next().equals("Y")||(keyboard.next().equals("y"))))
                    for(i=0;i<length.list;i++)
                        if(id==purchase)
                        {
                            sold = 1;
                            break;
                        }
        }
                    
        public void SaveList()
        {
            save list of dinosaur bones to file
                    list one bone per line
                        location, value, sold/or not
        }    
            
        public void LoadList()
        {
            load list of dinosaur bones from file
                list one bone per line
                    location, value, sold/or not
        }
   
       
        
 public static void main(String[] args) {
        // TODO code application logic here
    
         BuyerTool myMain=new BuyerTool();
         myMain.run();
         
    }
}
