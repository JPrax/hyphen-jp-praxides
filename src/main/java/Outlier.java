import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class Outlier {

    public static String[] HEADERS = { "Date", "Price"};

    public static void main(String[] args)throws IOException {
        //define the headers of the csv file;variable to be used
        Map<String, Double> standard_data = new HashMap<>();
        Map<String, Double> outlier_data = new HashMap<>();
        Double last_price = 0.000000;
        Double deviation = 0.050000;


        //get and parse file into records
        Reader in = new FileReader("input/Outliers.csv");
        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(HEADERS)
                .withFirstRecordAsHeader()
                .parse(in);

        //iterate through the record set
        for (CSVRecord record : records) {
            //get record data
            String date = record.get("Date");
            Double price = Double.valueOf(record.get("Price"));
            Integer record_count = Math.toIntExact(record.getRecordNumber());

            //Evaluate second record
            if(record_count == 1){
                standard_data.put(date,price);
                last_price = price;
            }
            else{
                //add to standard data if price is within acceptable deviation from last price
                if((Math.abs(price-last_price))/last_price<=deviation){
                    standard_data.put(date,price);
                    last_price = price;}
                //else, tag and put to outlier
                else{
                    outlier_data.put(date,price);
                }
            }

        //Write standard data
        createCSVFile("output/standard.csv",standard_data);
        //Write outlier data
        createCSVFile("output/outlier.csv",outlier_data);


        }

    }

        public static void createCSVFile(String filename, Map map ) throws IOException {
            FileWriter out = new FileWriter(filename);
            try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
                    .withHeader(HEADERS))) {
                map.forEach((author, title) -> {
                    try {
                        printer.printRecord(author, title);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

}
