package jurassic_emporium_buying_tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Formatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Jurassic_Emporium_Buying_Tool
{

    public class Menu
    {
        //Global Variables
        Scanner keyboard;
        Formatter screen;
        List<Dinosaur_Bone> Bone_List;
        List<Grid_Point> Map_Data_List;
        String Shop_Name;

        public Menu()
        {
            //Initializes the Scanner and Formatter
            keyboard = new Scanner(System.in);
            screen = new Formatter(System.out);
            Map_Data_List = new ArrayList<Grid_Point>();
            Bone_List = new ArrayList<>();
        }

        private void Start_Menu()
        {
            //Displays the Menu for the User to choose from
            screen.format("- - - - - MAIN MENU - - - - - \n");
            screen.format("1) Load World Map\n");
            screen.format("2) View World Map\n");
            screen.format("3) Buy a dinousaur bone\n");
            screen.format("4) Save list\n");
            screen.format("5) Load list\n");
            screen.format("6) Change Shop Name\n");
            screen.format("7) Exit\n");
            screen.format(">");

            //Uses a switch statement to use the correct function
            int choice = keyboard.nextInt();
            switch (choice)
            {
                case 1:
                    Load_World_Map();
                    break;
                case 2:
                    Show_World_Map();
                    break;
                case 3:
                    Buy_Bones();
                    break;
                case 4:
                    Save_Files();
                    break;
                case 5:
                    Load_Files();
                    break;
                case 6:
                    Change_Shop_Name();
                    break;
                case 7:
                    return;
            }
            Start_Menu();

        }

        private List<Grid_Point> Load_World_Map()
        {
            //Initializes a StreamReader object that reads in the map from Map.txt
            File temp = new File("Map.txt");
            Scanner read;
            try
            {
                read = new Scanner(temp);
                Grid_Point grid_temp;
                String current_line;
                while (read.hasNextLine())
                {
                    //Creates a new Grid Point object to store the incoming data from the Map
                    grid_temp = new Grid_Point();
                    current_line = read.nextLine();
                    if (current_line != "" && current_line != null)
                    {
                        grid_temp.column = Integer.parseInt(current_line.split(",")[0]);
                        grid_temp.row = Integer.parseInt(current_line.split(",")[1]);
                        grid_temp.type = current_line.split(",")[2];
                        //Adds the Grid Point object to a List<Grid_Point>
                        Map_Data_List.add(grid_temp);
                    }
                }
                read.close();
                screen.format("The Map has Successfully been Loaded!\n");

            } catch (Exception e)
            {
                screen.format("Map.txt Failed to Open!\n");
            }

            return Map_Data_List;
        }

        private void Buy_Bones()
        {
            //If the Bone List is Empty, then return
            if (Bone_List.size() < 1)
            {
                screen.format("Sorry, there are no Dinosaur Bones Available\n");
                return;
            }
            
            //Lists the Dinosaur Bones for the user to see
            screen.format("\n- - - - - DINOSAUR BONE STORE - - - - -\n");
            for (int i = 0; i < Bone_List.size(); i++)
            {
                if (!Bone_List.get(i).is_sold)
                    screen.format("%s | ID# %d\n", Bone_List.get(i).Name, Bone_List.get(i).ID_Num);
            }

            //Asks the user for the number they want by ID Number
            screen.format("Please enter the ID# >");
            int selection = keyboard.nextInt();
            for (int i = 0; i < Bone_List.size(); i++)
            {
                //Error checks the Bone List input number
                if (selection == Bone_List.get(i).ID_Num)
                {
                    screen.format("Are you sure you sure you want to buy %s? [Y / N]\n>", Bone_List.get(i).Name);
                    String confirmation = keyboard.next();
                    if (confirmation.equals("Y") || confirmation.equals("y"))
                    {
                        Bone_List.get(i).is_sold = true;
                        //Gets the personal information from the user after they agree to buy dinosaur bone
                        Buyer_Information buyer = new Buyer_Information(keyboard, screen);
                        buyer.Get_Buyer_Info();
                        //Monograms the sold dinosaur bone with the buyer's name
                        MonoGram mono = new MonoGram(buyer.Name, screen);
                        return;
                    } 
                    else
                    {
                        screen.format("Purchase Cancelled!\n");
                        Buy_Bones();
                    }
                }
            }
            screen.format("Invalid ID Number!\n");
            Buy_Bones();

        }

        private void Show_World_Map()
        {
            //First has to find the dimensions of the map
            int max_col = 0, max_row = 0;
            for (int i = 0; i < Map_Data_List.size(); i++)
            {
                if (Map_Data_List.get(i).column >= max_col)
                {
                    max_col = Map_Data_List.get(i).column;
                }
                if (Map_Data_List.get(i).row >= max_row)
                {
                    max_row = Map_Data_List.get(i).row;
                }
            }
            
            //Creates a char 2d array to store the map data
            Grid_Point tempNode;
            char[][] displayArray = new char[20][60];

            //Translate Array List to 2D Array
            for (int i = 0; i < Map_Data_List.size(); i++)
            {
                tempNode = Map_Data_List.get(i);
                switch (tempNode.type)
                {
                    case "1":
                        displayArray[tempNode.row][tempNode.column] = '*'; break;
                    case "X":
                        displayArray[tempNode.row][tempNode.column] = 'X'; break;
                    case "$":
                        displayArray[tempNode.row][tempNode.column] = '$'; break;
                    default:
                        displayArray[tempNode.row][tempNode.column] = '.';
                }
            }
            
            //Updates the World Map List to Include Dinosaur Bones
            for (int i = 0; i < Bone_List.size(); i++)
            {
                if (Bone_List.get(i).is_sold)
                {
                    displayArray[Bone_List.get(i).Y_Coordinate][Bone_List.get(i).X_Coordinate] = '$';
                } 
                else
                {
                    displayArray[Bone_List.get(i).Y_Coordinate][Bone_List.get(i).X_Coordinate] = 'X';
                }
            }
            
            File temp = new File("World_Map.txt");
            Formatter write;
            try
            {
                write = new Formatter(temp);
                
                write.format("- - - - - - - - " + Shop_Name + " - - - - - - - -\r\n");
                for (int i = 0; i < 20; i++)
                {
                    for (int colLoop = 0; colLoop < 60; colLoop++)
                    {
                        //Writes the Prime Meridian to the Map
                        if (colLoop == 30)
                            write.format("|");
                        write.format("%c", displayArray[i][colLoop]);
                    }
                    write.format("\r\n");
                    if (i == 9)
                    {
                        for (int x = 0; x <= max_col; x++)
                        {
                            //Writes the equator to the map
                            if (x == 30)
                            {
                                write.format("|");
                            }
                            write.format("-");
                        }
                        write.format(" Equator");
                        write.format("\r\n");
                    }
                }
                write.format("                         Prime Meridian");
                write.close();
            } 
            catch (Exception e)
            {
                screen.format("Failed to open");
            }
          
            //Opens the NotePad Program to view the Map File
            ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "World_Map.txt");
            try
            {
                pb.start();
            } catch (IOException ex)
            {
                Logger.getLogger(Jurassic_Emporium_Buying_Tool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void Save_Files()
        {
            //Saves the Bone List to Saved_Bones.txt
            File temp = new File("Saved_Bones.txt");
            Formatter write;
            try
            {
                write = new Formatter(temp);
                write.format(Shop_Name + "\r\n");
                for (int i = 0; i < Bone_List.size(); i++)
                {
                    write.format(Bone_List.get(i).Name + "," + Bone_List.get(i).Longitude + "," + Bone_List.get(i).Latitude + "," + Bone_List.get(i).Price + ","
                            + Bone_List.get(i).ID_Num + "," + Bone_List.get(i).is_sold + "\r\n");
                }
                write.close();
                screen.format("Dinosaur Bones were Successfully Saved!\n");
            } catch (Exception e)
            {
                screen.format("Failed to Write File");
            }
        }

        private void Load_Files()
        {
            //Reads the Bone List from Saved_Bones.txt
            File temp = new File("Saved_Bones.txt");
            Scanner read;
            try
            {
                read = new Scanner(temp);
                Dinosaur_Bone bone_temp;
                Bone_List.clear();
                String current_line;
                Shop_Name = read.nextLine();
                while (read.hasNextLine())
                {
                    //Creates a new Dinosaur Bone to store the incoming data
                    bone_temp = new Dinosaur_Bone();
                    current_line = read.nextLine();
                    if (!current_line.equals(""))
                    {
                        bone_temp.Name = current_line.split(",")[0];
                        bone_temp.Longitude = Integer.parseInt(current_line.split(",")[1]);
                        bone_temp.Latitude = Integer.parseInt(current_line.split(",")[2]);
                        bone_temp.Price = Double.parseDouble(current_line.split(",")[3]);
                        bone_temp.ID_Num = Integer.parseInt(current_line.split(",")[4]);
                        bone_temp.is_sold = Boolean.parseBoolean(current_line.split(",")[5]);
                        bone_temp.Find_X_Cord();
                        bone_temp.Find_Y_Cord();
                        //Adds the new dino bone to the List<Dinosaur_Bone>
                        Bone_List.add(bone_temp);
                    }
                }
                read.close();
                screen.format("Successfully Loaded Save_Bones.txt\n");
            } catch (Exception e)
            {
                screen.format("Failed to open Saved_Bones.txt\n");
            }
        }
        
        private void Change_Shop_Name()
        {
            //Changes the Shop Name 
            screen.format("The Current Shop Name is %s\n", Shop_Name);
            screen.format("New Shop Name: ");
            keyboard.nextLine();
            Shop_Name = keyboard.nextLine();
        }

    }
    
    public class Dinosaur_Bone
    {
    //Class Attributes
        public double Age, Price, Weight;
        public String Condition, Origin_Country, Prospector, Name;
        public int X_Coordinate, Y_Coordinate, ID_Num, Latitude, Longitude;
        public boolean is_sold;

        private void Find_X_Cord()
        {
            //Finds the X_Coordinate from the Longitude
            if (Longitude <= 0)
            {
                for (int i = 0; i < 18; i++)
                {
                    if (Longitude * -1 < ((6 * i) - 3))
                    {
                        X_Coordinate = 31 - i;
                        break;
                    }
                }
            } else
            {
                for (int i = 0; i < 18; i++)
                {
                    if (Longitude < (((180 / 29.0) * i) - (180 / 29 / 2.0)))
                    {
                        X_Coordinate = 29 + i;
                        break;
                    } else if (i == 17)
                    {
                        X_Coordinate = 59;
                    }
                }
            }
        }

        private void Find_Y_Cord()
        {
            //Finds the Y_Coordinate from the Latitude
            if (Latitude <= 0)
            {
                for (int i = 0; i < 10; i++)
                {
                    if (Latitude * -1 < ((9 * i) - (9 / 2.0)))
                    {
                        Y_Coordinate = 8 + i;
                        break;
                    } else if (i == 9)
                    {
                        Y_Coordinate = 19;
                    }
                }
            } else
            {
                for (int i = 0; i < 10; i++)
                {
                    if (Latitude < ((10 * i) - 5))
                    {
                        Y_Coordinate = 10 - i;
                        break;
                    }
                }
            }
        }
}
    
    public class Grid_Point
    {
    //Public Variables
    public int row, column;
    public String type;
}


    public class MonoGram
    {
        Formatter outfile;

        public MonoGram(String Name, Formatter outfile_temp)
        {
            outfile = outfile_temp;
            //Outfiles the Top of the Bone
            outfile.format("  ___   ___\n"
                    + " /   \\_/   \\\n"
                    + "|          |\n");

            for (int i = 0; i < Name.length(); i++)
            {
                switch (Name.charAt(i))
                {
                    case 'a':
                    case 'A':
                        A();
                        break;
                    case 'b':
                    case 'B':
                        B();
                        break;
                    case 'c':
                    case 'C':
                        C();
                        break;
                    case 'd':
                    case 'D':
                        D();
                        break;
                    case 'e':
                    case 'E':
                        E();
                        break;
                    case 'f':
                    case 'F':
                        F();
                        break;
                    case 'g':
                    case 'G':
                        G();
                        break;
                    case 'h':
                    case 'H':
                        H();
                        break;
                    case 'i':
                    case 'I':
                        I();
                        break;
                    case 'j':
                    case 'J':
                        J();
                        break;
                    case 'k':
                    case 'K':
                        K();
                        break;
                    case 'l':
                    case 'L':
                        L();
                        break;
                    case 'm':
                    case 'M':
                        M();
                        break;
                    case 'n':
                    case 'N':
                        N();
                        break;
                    case 'o':
                    case 'O':
                        O();
                        break;
                    case 'p':
                    case 'P':
                        P();
                        break;
                    case 'q':
                    case 'Q':
                        Q();
                        break;
                    case 'r':
                    case 'R':
                        R();
                        break;
                    case 's':
                    case 'S':
                        S();
                        break;
                    case 't':
                    case 'T':
                        T();
                        break;
                    case 'u':
                    case 'U':
                        U();
                        break;
                    case 'v':
                    case 'V':
                        V();
                        break;
                    case 'w':
                    case 'W':
                        W();
                        break;
                    case 'x':
                    case 'X':
                        X();
                        break;
                    case 'y':
                    case 'Y':
                        Y();
                        break;
                    case 'z':
                    case 'Z':
                        Z();
                        break;
                    case ' ':
                        Space();
                        break;
                }
                outfile.format("\n|          |\n");
            }
            
            //Outfiles the Bottom of the Bone
            outfile.format("|     _    |\n" + " \\___/ \\___/\n");
        }

        private void A()
        {
            outfile.format("|          |\n" +
"|    /\\    |\n" +
"|   /  \\   |\n" +
"|  /----\\  |\n" +
"| /      \\ |");
        }

        private void B()
        {
            outfile.format("|   ____   |\n" +
"|  |    |  |\n" +
"|  |----   |\n" +
"|  |    |  |\n" +
"|   ----   |");
        }

        private void C()
        {
            outfile.format("|   -----  |\n" +
"|  |       |\n" +
"|  |       |\n" +
"|  |       |\n" +
"|   -----  |");
        }

        private void D()
        {
            outfile.format("|   ----   |\n" +
"|  |    |  |\n" +
"|  |    |  |\n" +
"|  |    /  |\n" +
"|  -----   |");
        }

        private void E()
        {
            outfile.format("|   -----  |\n" +
"|  |       |\n" +
"|  |----   |\n" +
"|  |       |\n" +
"|   -----  |");
        }

        private void F()
        {
            outfile.format("|   -----  |\n" +
"|  |       |\n" +
"|  |----   |\n" +
"|  |       |\n" +
"|  |       |");
        }

        private void G()
        {
            outfile.format("|   -----  |\n" +
"|  |       |\n" +
"|  |  --   |\n" +
"|  |    |  |\n" +
"|   ----   |");
        }

        private void H()
        {
            outfile.format("|  |    |  |\n" +
"|  |    |  |\n" +
"|  |----|  |\n" +
"|  |    |  |\n" +
"|  |    |  |");
        }

        private void I()
        {
            outfile.format("|  ------  |\n" +
"|     |    |\n" +
"|     |    |\n" +
"|     |    |\n" +
"|  ------  |");
        }

        private void J()
        {
            outfile.format("|  ------  |\n" +
"|     |    |\n" +
"|     |    |\n" +
"|     |    |\n" +
"|  ---     |");
        }

        private void K()
        {
            outfile.format("|  | /     |\n" +
"|  |/      |\n" +
"|  |       |\n" +
"|  |\\      |\n" +
"|  | \\     |");
        }

        private void L()
        {
            outfile.format("|  |       |\n" +
"|  |       |\n" +
"|  |       |\n" +
"|  |       |\n" +
"|  |_____  |");
        }

        private void M()
        {
            outfile.format("|  |\\  /|  |\n" +
"|  | \\/ |  |\n" +
"|  |    |  |\n" +
"|  |    |  |\n" +
"|  |    |  |");
        }

        private void N()
        {
            outfile.format("|  |\\   |  |\n" +
"|  | \\  |  |\n" +
"|  |  \\ |  |\n" +
"|  |   \\|  |\n" +
"|  |    |  |");
        }

        private void O()
        {
            outfile.format("|   /--\\   |\n" +
"|  |    |  |\n" +
"|  |    |  |\n" +
"|  |    |  |\n" +
"|   \\--/   |");
        }

        private void P()
        {
            outfile.format("|  |---\\   |\n" +
"|  |    |  |\n" +
"|  |---/   |\n" +
"|  |       |\n" +
"|  |       |");
        }

        private void Q()
        {
            outfile.format("|   /--\\   |\n" +
"|  |    |  |\n" +
"|  |    |  |\n" +
"|  |  \\ |  |\n" +
"|   \\--/\\  |");
        }

        private void R()
        {
            outfile.format("|  |---\\   |\n" +
"|  |    |  |\n" +
"|  |---/   |\n" +
"|  | \\     |\n" +
"|  |  \\    |");
        }

        private void S()
        {
            outfile.format("|   /--\\   |\n" +
"|  |       |\n" +
"|  |---\\   |\n" +
"|       |  |\n" +
"|  ____/   |");
        }

        private void T()
        {
            outfile.format("|  ------  |\n" +
"|     |    |\n" +
"|     |    |\n" +
"|     |    |\n" +
"|     |    |");
        }

        private void U()
        {
            outfile.format("|  |     | |\n" +
"|  |     | |\n" +
"|  |     | |\n" +
"|  |     | |\n" +
"|   \\---/  |");
        }

        private void V()
        {
            outfile.format("|  \\    /  |\n" +
"|   \\  /   |\n" +
"|    \\/    |\n" +
"|          |\n" +
"|          |");
        }

        private void W()
        {
            outfile.format("|  |    |  |\n" +
"|  |    |  |\n" +
"|  | |  |  |\n" +
"|  | |  |  |\n" +
"|  |----|  |");
        }

        private void X()
        {
            outfile.format("|   \\  /  |\n" +
"|    \\/   |\n" +
"|    /\\   |\n" +
"|   /  \\  |\n" +
"|  /    \\ |");
        }

        private void Y()
        {
            outfile.format("|  \\    /  |\n" +
"|   \\  /   |\n" +
"|    \\/    |\n" +
"|    |     |\n" +
"|    |     |");
        }

        private void Z()
        {
            outfile.format("|  ------  |\n" +
"|      /   |\n" +
"|     /    |\n" +
"|    /     |\n" +
"|   /____  |");
        }

        private void Space()
        {
            outfile.format("|          |\n" +
"|          |");
        }
    }
    
    public class Buyer_Information
    {
        //Class Attributes
        String Name, Email, Street_Name, City, State, Country;
        int Street_Number, Postal_Code;
        Scanner infile; Formatter outfile;
        public Buyer_Information(Scanner in_temp, Formatter out_temp)
        {
            infile = in_temp;
            outfile = out_temp;
        }
        
        public void Get_Buyer_Info()
        {
            //Gets the personal information from the user after agreeing to buy
            infile.nextLine();
            outfile.format("Please enter your name: ");
            this.Name = infile.nextLine();
            outfile.format("Please enter your Email Address: ");
            this.Email = infile.nextLine();
            outfile.format("- - - - - ADDRESS - - - - -\n");
            outfile.format("Please enter your Street Number: ");
            this.Street_Number = infile.nextInt();
            infile.nextLine();
            outfile.format("Please enter your Street Name: ");
            this.Street_Name = infile.nextLine();
            outfile.format("Please enter your City: ");
            this.City = infile.nextLine();
            outfile.format("Please enter your State: ");
            this.State = infile.nextLine();
            outfile.format("Please enter your Country: ");
            this.Country = infile.nextLine();
            outfile.format("Please enter your Postal Code: ");
            this.Postal_Code = infile.nextInt();
        }
    }

    public static void main(String[] args)
    {
        Jurassic_Emporium_Buying_Tool Begin = new Jurassic_Emporium_Buying_Tool();
        Begin.Start();
    }

    public void Start()
    {
        //Starts the Program
        Menu StartProg = new Menu();
        StartProg.Start_Menu();
    }
}
