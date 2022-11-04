package com.ikem.hng_backend_bidding_task;


import com.google.common.hash.Hashing;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.json.CDL;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Collectors;

public class HngBackendBiddingTaskApplication {

    public static void main(String[] args) {
        //csvToJson();
        readCSV();
    }
    static void readCSV() {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter file path:");
        String filePath = input.nextLine();

        new File("./json").mkdirs();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] lineInArray;
            String columns = String.join(",", reader.peek());
            System.out.println(columns);
            int counter = 0;
            while ((lineInArray = reader.readNext()) != null) {


                if (counter <= 0) {
                    counter++;
                    continue;
                }

                String filename = "./json/" + lineInArray[1].trim() + ".json";



                System.out.println(filename);
                String content = String.join(",", lineInArray);

                String csvString = "format,"+ columns + "\n" + "CHIP-0007," + content;
                System.out.println(csvString);
                String json = CDL.toJSONArray(csvString).toString().
                        replace("[", "")
                        .replace("]", "");
                System.out.println(json);
                Files.write(Path.of(filename), json.getBytes(StandardCharsets.UTF_8));

            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static String jsonFileHash(String jsonFilename) {
        return Hashing.sha256()
                .hashString(jsonFilename, StandardCharsets.UTF_8)
                .toString();
    }
}
