package com.example.user.summerproject.categoryTools;


import android.content.Context;
import android.widget.Toast;


import com.example.user.summerproject.myTools.SuggestionMessage;
import com.example.user.summerproject.word_tester.SuggestionMessageAdmin;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

public class CategoryTools {
    private Context c;
    public CategoryTools(Context c){this.c=c;}




    public void clearFile(String name){
        try {
            FileOutputStream fos;
            fos = c.openFileOutput(name, MODE_PRIVATE);
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public Animal[] readAnimals(String path){

        String FILE_NAME =path;
        int sizeAnimals =0;
        try{
            FileInputStream fin = c.openFileInput(FILE_NAME);
            int size;
            while ((size = fin.read()) != -1) {
                // add & append content

                if (Character.toString((char)size).equals("\n")) {
                    sizeAnimals++;
                }

            }


            fin.close();

        } catch (Exception error) {
            // Exception


            error.printStackTrace();
        }

        Animal[] animals = new Animal[sizeAnimals];
        for(int i=0;i<sizeAnimals; i++){
            animals[i]=new Animal();
        }

        try{
            BufferedReader reader=null;
            FileInputStream fin = c.openFileInput(FILE_NAME);
            reader=new BufferedReader(new InputStreamReader(fin,"UTF-8"));
            int size;
            int lnIndex = 0;
            String trash="";
            String character;
            int elementIndex = 0;
            // read inside if it is not null (-1 means empty)
            while ((size = reader.read()) != -1) {
                character=Character.toString((char)size);
                if( character.equals("\n") ){
                    animals[lnIndex].animal=trash;
                    lnIndex++;
                    elementIndex=0;
                    trash="";
                }else{
                    trash += character;
                }
            }
            // Set text to TextView
            reader.close();
            return animals;
        } catch (Exception error) {
            // Exception

            error.printStackTrace();
            return animals;

        }

    }

    public void addAnimal(Animal animal,String path){
        String mLine=animal.animal;
        try {
            FileOutputStream fos;
            fos = c.openFileOutput(path, MODE_APPEND);
            fos.write(mLine.getBytes());
            fos.write("\n".getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Name[] readNames(String path){

        String FILE_NAME = path;
        int sizeNames =0;

        try{
            FileInputStream fin = c.openFileInput(FILE_NAME);
            int size;
            while ((size = fin.read()) != -1) {
                // add & append content
                if (Character.toString((char)size).equals("\n")) {
                    sizeNames++;
                }

            }


            fin.close();

        } catch (Exception error) {
            // Exception
            error.printStackTrace();
        }

        Name[] names = new Name[sizeNames];
        for(int i=0;i<sizeNames; i++){
            names[i]=new Name();
        }

        try{
            BufferedReader reader=null;
            FileInputStream fin = c.openFileInput(FILE_NAME);
            reader=new BufferedReader(new InputStreamReader(fin,"UTF-8"));
            int size;
            int lnIndex = 0;
            String trash="";
            String character;
            int elementIndex = 0;
            // read inside if it is not null (-1 means empty)
            while ((size = reader.read()) != -1) {
                character=Character.toString((char)size);
                if( character.equals("\n") ){
                    names[lnIndex].name=trash;
                    lnIndex++;
                    elementIndex=0;
                    trash="";
                }else{
                    trash += character;
                }
            }
            // Set text to TextView
            reader.close();
            return names;
        } catch (Exception error) {
            // Exception

            error.printStackTrace();
            return names;

        }

    }

    public void addName(Name name,String path){
        String mLine=name.name;
        try {
            FileOutputStream fos;
            fos = c.openFileOutput(path, MODE_APPEND);
            fos.write(mLine.getBytes());
            fos.write("\n".getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Plant[] readPlants(String path){

        String FILE_NAME = path;
        int sizePlants =0;

        try{
            FileInputStream fin = c.openFileInput(FILE_NAME);
            int size;
            while ((size = fin.read()) != -1) {
                // add & append content
                if (Character.toString((char)size).equals("\n")) {
                    sizePlants++;
                }

            }


            fin.close();

        } catch (Exception error) {
            // Exception
            error.printStackTrace();
        }

        Plant[] plants = new Plant[sizePlants];
        for(int i=0;i<sizePlants; i++){
            plants[i]=new Plant();
        }

        try{
            BufferedReader reader=null;
            FileInputStream fin = c.openFileInput(FILE_NAME);
            reader=new BufferedReader(new InputStreamReader(fin,"UTF-8"));
            int size;
            int lnIndex = 0;
            String trash="";
            String character;
            int elementIndex = 0;
            // read inside if it is not null (-1 means empty)
            while ((size = reader.read()) != -1) {
                character=Character.toString((char)size);
                if( character.equals("\n") ){
                    plants[lnIndex].plant=trash;
                    lnIndex++;
                    elementIndex=0;
                    trash="";
                }else{
                    trash += character;
                }
            }
            // Set text to TextView
            reader.close();
            return plants;
        } catch (Exception error) {
            // Exception

            error.printStackTrace();
            return plants;

        }

    }

    public void addPlant(Plant plant,String path){
        String mLine=plant.plant;
        try {
            FileOutputStream fos;
            fos = c.openFileOutput(path, MODE_APPEND);
            fos.write(mLine.getBytes());
            fos.write("\n".getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Color[] readColors(String path){

        String FILE_NAME = path;
        int sizeColors =0;

        try{
            FileInputStream fin = c.openFileInput(FILE_NAME);
            int size;
            while ((size = fin.read()) != -1) {
                // add & append content
                if (Character.toString((char)size).equals("\n")) {
                    sizeColors++;
                }

            }


            fin.close();

        } catch (Exception error) {
            // Exception
            error.printStackTrace();
        }

        Color[] colors = new Color[sizeColors];
        for(int i=0;i<sizeColors; i++){
            colors[i]=new Color();
        }

        try{
            BufferedReader reader=null;
            FileInputStream fin = c.openFileInput(FILE_NAME);
            reader=new BufferedReader(new InputStreamReader(fin,"UTF-8"));
            int size;
            int lnIndex = 0;
            String trash="";
            String character;
            int elementIndex = 0;
            // read inside if it is not null (-1 means empty)
            while ((size = reader.read()) != -1) {
                character=Character.toString((char)size);
                if( character.equals("\n") ){
                    colors[lnIndex].color=trash;
                    lnIndex++;
                    elementIndex=0;
                    trash="";
                }else{
                    trash += character;
                }
            }
            // Set text to TextView
            reader.close();
            return colors;
        } catch (Exception error) {
            // Exception

            error.printStackTrace();
            return colors;

        }

    }

    public void addColor(Color color,String path){
        String mLine=color.color;
        try {
            FileOutputStream fos;
            fos = c.openFileOutput(path, MODE_APPEND);
            fos.write(mLine.getBytes());
            fos.write("\n".getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public City[] readCities(String path){

        String FILE_NAME = path;
        int sizeCities =0;

        try{
            FileInputStream fin = c.openFileInput(FILE_NAME);
            int size;
            while ((size = fin.read()) != -1) {
                // add & append content
                if (Character.toString((char)size).equals("\n")) {
                    sizeCities++;
                }

            }


            fin.close();

        } catch (Exception error) {
            // Exception
            error.printStackTrace();
        }

        City[] cities= new City[sizeCities];
        for(int i=0;i<sizeCities; i++){
            cities[i]=new City();
        }

        try{
            BufferedReader reader=null;
            FileInputStream fin = c.openFileInput(FILE_NAME);
            reader=new BufferedReader(new InputStreamReader(fin,"UTF-8"));
            int size;
            int lnIndex = 0;
            String trash="";
            String character;
            int elementIndex = 0;
            // read inside if it is not null (-1 means empty)
            while ((size = reader.read()) != -1) {
                character=Character.toString((char)size);
                if( character.equals("\n") ){
                    cities[lnIndex].city=trash;
                    lnIndex++;
                    elementIndex=0;
                    trash="";
                }else{
                    trash += character;
                }
            }
            // Set text to TextView
            reader.close();
            return cities;
        } catch (Exception error) {
            // Exception

            error.printStackTrace();
            return cities;

        }

    }

    public void addCity(City city,String path){
        String mLine=city.city;
        try {
            FileOutputStream fos;
            fos = c.openFileOutput(path, MODE_APPEND);
            fos.write(mLine.getBytes());
            fos.write("\n".getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Profession[] readProfessions(String path){

        String FILE_NAME = path;
        int sizeProfessions =0;

        try{
            FileInputStream fin = c.openFileInput(FILE_NAME);
            int size;
            while ((size = fin.read()) != -1) {
                // add & append content
                if (Character.toString((char)size).equals("\n")) {
                    sizeProfessions++;
                }

            }


            fin.close();

        } catch (Exception error) {
            // Exception
            error.printStackTrace();
        }

        Profession[] professions= new Profession[sizeProfessions];
        for(int i=0;i<sizeProfessions; i++){
            professions[i]=new Profession();
        }

        try{
            BufferedReader reader=null;
            FileInputStream fin = c.openFileInput(FILE_NAME);
            reader=new BufferedReader(new InputStreamReader(fin,"UTF-8"));
            int size;
            int lnIndex = 0;
            String trash="";
            String character;
            int elementIndex = 0;
            // read inside if it is not null (-1 means empty)
            while ((size = reader.read()) != -1) {
                character=Character.toString((char)size);
                if( character.equals("\n") ){
                    professions[lnIndex].profession=trash;
                    lnIndex++;
                    elementIndex=0;
                    trash="";
                }else{
                    trash += character;
                }
            }
            // Set text to TextView
            reader.close();
            return professions;
        } catch (Exception error) {
            // Exception

            error.printStackTrace();
            return professions;

        }

    }

    public void addProfession(Profession profession,String path){
        String mLine=profession.profession;
        try {
            FileOutputStream fos;
            fos = c.openFileOutput(path, MODE_APPEND);
            fos.write(mLine.getBytes());
            fos.write("\n".getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SuggestionMessageAdmin[] readSuggestionMessages(){

        String FILE_NAME = "suggestionmessages.txt";
        int sizeSuggestions =0;

        try{
            FileInputStream fin = c.openFileInput(FILE_NAME);
            int size;
            while ((size = fin.read()) != -1) {
                // add & append content
                if (Character.toString((char)size).equals("\n")) {
                    sizeSuggestions++;
                }

            }


            fin.close();

        } catch (Exception error) {
            // Exception
            error.printStackTrace();
        }

        SuggestionMessageAdmin[] suggestionMessages = new SuggestionMessageAdmin[sizeSuggestions];
        for(int i=0;i<sizeSuggestions; i++){
            suggestionMessages[i]= new SuggestionMessageAdmin();
        }

        try{
            BufferedReader reader=null;
            FileInputStream fin = c.openFileInput(FILE_NAME);
            reader=new BufferedReader(new InputStreamReader(fin,"UTF-8"));
            int size;
            int lnIndex = 0;
            String trash="";
            String character;
            int elementIndex = 0;
            while ((size = reader.read()) != -1) {
                character=Character.toString((char)size);
                if( character.equals("\n") ){
                    lnIndex++;
                    elementIndex=0;
                    trash="";
                }else if(character.equals("¨")){
                    switch (elementIndex){
                        case 0:
                            suggestionMessages[lnIndex].categorySuggestion=trash;
                            break;
                        case 1:
                            suggestionMessages[lnIndex].playerSuggestion=trash;
                            break;
                        case 2:
                            suggestionMessages[lnIndex].ourMessage=trash;
                            break;
                        case 3:
                            suggestionMessages[lnIndex].points=Integer.parseInt(trash);
                            break;
                        case 4:
                            suggestionMessages[lnIndex].ID=trash;
                            break;
                    }
                    elementIndex++;
                    trash="";
                }else{
                    trash += character;
                }
            }
            // Set text to TextView
            reader.close();
            return suggestionMessages;
        } catch (Exception error) {
            // Exception

            error.printStackTrace();
            return suggestionMessages;

        }
    }

    public void addSuggestionMessage(SuggestionMessageAdmin suggestionMessage){
        String mLine;

        mLine=suggestionMessage.categorySuggestion+"¨"
                +suggestionMessage.playerSuggestion+"¨"
                +suggestionMessage.ourMessage+"¨"
                +suggestionMessage.points+"¨"
                +suggestionMessage.ID+"¨";


        try {

            FileOutputStream fos;
            fos = c.openFileOutput("suggestionmessages.txt", MODE_APPEND);
            //out.write(mLine);
            fos.write(mLine.getBytes());
            fos.write("\r\n".getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
