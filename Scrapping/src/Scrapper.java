//By Ian

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import opennlp.tools.doccat.*;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class Scrapper {

    public static DoccatModel model = null;
	final static String url = "https://www.worldometers.info/coronavirus/country/";
	final static String vaccinationUrl = "https://static01.nyt.com/newsgraphics/2021/01/19/world-vaccinations-tracker/6ff2598878b72ffa9e9bd6a14f25d1e554e9ebb3/all_with_rate.json";
	final static String[] keywords = {
			"pfizer vaccine",
			"covid singapore",
			"mask singapore",
			"safe distancing",
			"tracetogether",
			"rediscover voucher",
			"moderna vaccine",
			"coronavirus policy",
			"covid19 policy"
	};
	final static String[] countries = {
			"Singapore", 
			"Malaysia",
			"Indonesia",
			"Brazil",
			"France",
			"Russia",
			"India",
			"China",
			"UK",
			"Italy",
			"Turkey",
			"Spain",
			"Germany",
			"Meixco",
			"Colombia",
			"Poland",
			"US"
			};
	
	public static void main(String[] args) {
		
		getVaccinationData(vaccinationUrl);
		
		//writeCasesToCSV(countries);
		
		//trainModel(); 
		
		//gwriteTweetsToCSV(keywords);
		
	}
	
	
	public static void getVaccinationData(String url) {
		
		try {

			FileWriter csvWriter = new FileWriter("vaccinationdata.csv");
			/**
			csvWriter.append("Geo ID");
			csvWriter.append(",");
			csvWriter.append("Location");
			csvWriter.append(",");
			csvWriter.append("Total Vaccinations");
			csvWriter.append(",");
			csvWriter.append("Population");
			csvWriter.append(",");
			csvWriter.append("Continent");
			csvWriter.append(",");
			csvWriter.append("Vaccination Rate");
			csvWriter.append(",");
			csvWriter.append("GDP Per Capita");
			csvWriter.append(",");
			csvWriter.append("Region");
			csvWriter.append(",");
			csvWriter.append("Last Updated");
			csvWriter.append("\n");**/
			String json = readUrl(url);
			System.out.println("Done scrapping vaccination data");
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
			    JSONObject object = jsonArray.getJSONObject(i);
			    System.out.println(object.getString("display_name"));
			    if (!object.has("IncomeGroup") || !object.has("total_vaccinations") || !object.has("gdp_per_cap"))
					continue;
				csvWriter.append(object.getString("geoid"));
				csvWriter.append(",");
				csvWriter.append(object.getString("display_name"));
				csvWriter.append(",");
				csvWriter.append(object.getString("total_vaccinations"));
				csvWriter.append(",");
				csvWriter.append(object.getString("population"));
				csvWriter.append(",");
				csvWriter.append(object.getString("CONTINENT"));
				csvWriter.append(",");
				csvWriter.append(object.getString("vaccinations_rate"));
				csvWriter.append(",");
				csvWriter.append(object.getString("IncomeGroup"));
				csvWriter.append(",");
				csvWriter.append(object.getString("gdp_per_cap"));
				csvWriter.append(",");
				csvWriter.append(object.getString("Region"));
				csvWriter.append(",");
				csvWriter.append(object.getString("last_updated"));
				csvWriter.append("\n");
			}

			System.out.println("Done exporting vaccination data");
			csvWriter.flush();
			csvWriter.close();
			
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public static void writeCasesToCSV(String[] countries) {
		try {
			

			FileWriter csvWriter = new FileWriter("dailycoronadata.csv");
			csvWriter.append("Country");
			csvWriter.append(",");
			csvWriter.append("Date");
			csvWriter.append(",");
			csvWriter.append("Number of Cases");
			csvWriter.append("\n");
			
			for (String i : countries) {
				final String tempUrl = url + i;
				String[] dailyLabels = getDailyCasesLabel(tempUrl);
				String[] dailyData = getDailyCasesData(tempUrl);

				System.out.println("Done scrapping daily cases for "+i);
				int j;
				for (j = 0; j<dailyLabels.length;j++) {

					csvWriter.append(i);
					csvWriter.append(",");
					csvWriter.append(dailyLabels[j].replace(",", ""));
					csvWriter.append(",");
					csvWriter.append(dailyData[j]);

					csvWriter.append("\n");
				}

				System.out.println("Done exporting daily cases for "+i);
			}


			csvWriter.flush();
			csvWriter.close();
	
			

			
		}
		catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeTweetsToCSV(String[] keywords) {

		try {

			FileWriter csvWriter = new FileWriter("tweets.csv");

			csvWriter.append("Keyword");
			csvWriter.append(",");
			csvWriter.append("Tweet");
			csvWriter.append(",");
			csvWriter.append("Sentiment");
			csvWriter.append("\n");
			
			for (String j : keywords) {
				System.out.println(j);
				for (String i : getTweets(j,false)) {
					if (i==null)
						continue;
					csvWriter.append(j);
					csvWriter.append(",");
					csvWriter.append(i.replace(",", "").replace("\n", "").replace("\r", ""));
					csvWriter.append(",");
					//0 = negative 1 = positive
					if (classifyNewText(i)==0) {
						csvWriter.append("negative");
						csvWriter.append(",");
						
					}else {

						csvWriter.append("positive");
						csvWriter.append(",");
					}

					csvWriter.append("\n");
				}
			}
			

			System.out.println("Done exporting tweets");
			csvWriter.flush();
			csvWriter.close();
			
		}
		catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String[] getDailyCasesLabel(String url) {
		String[] emptyArray = new String[0];
		try {
			/**
			 * Get the labels (dates)
			 * **/
			final Document document = Jsoup.connect(url).get();
			Elements scriptElements = document.getElementsByTag("script");
			for (Element element : scriptElements) {
				if (element.data().contains("Daily New Cases")) {

			        Pattern patternLabel = Pattern.compile(".*categories: ([^}]*)}");
			        Matcher matcherLabel = patternLabel.matcher(element.data());
			        if (matcherLabel.find()) {
			        	String labels = matcherLabel.group(1).replaceAll("\\[|\\]", "");
			            String[] temparray = labels.split(",");
			            String[] labelarray = new String[temparray.length/2];
			            int i;
			            int j=0;
			            for (i = 0; i < labelarray.length; i++) {	            	  
			            	labelarray[i] =temparray[j] + temparray[j+1];
			            	j+=2;
			            }		
			            return labelarray;
			            
			        } else {
			            System.err.println("No match found!");
			        }
			        break;
				}
				
			}
			
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
	
		}

		return emptyArray;
		
	}
	
	public static String[] getDailyCasesData(String url) {
		String[] emptyArray = new String[0];
		try {
			final Document document = Jsoup.connect(url).get();
			Elements scriptElements = document.getElementsByTag("script");
			
			for (Element element : scriptElements) {
				if (element.data().contains("Daily New Cases")) {

			        
			        Pattern patternData = Pattern.compile(".*?data: ([^}]*)}");
			        Matcher matcherData = patternData.matcher(element.data());
			        if (matcherData.find()) {
			            String[] dataarray = matcherData.group(1).replaceAll("\\[|\\]", "").split((","));
			            return dataarray;
			        } else {
			            System.err.println("No match found!");
			        }
			        break;
				}
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
	
		}

		return emptyArray;
	}

    public static String[] getTweets(String name,boolean isPopular){
        Twitter twitter = TwitterFactory.getSingleton();
        Query query = new Query(name+"+exclude:retweets");
        if (isPopular)
            query.resultType(Query.POPULAR);
        else
            query.resultType(Query.RECENT);

        query.setCount(100);
        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        String[] tweets = new String[result.getCount()];
        int i = 0;
        for (Status status : result.getTweets()) {
            tweets[i] = status.getText();
            i++;
        }

		System.out.println("Done scrapping tweets");
        return tweets;

    }

    public static void trainModel() {
        MarkableFileInputStreamFactory dataIn = null;
        try {
            dataIn = new MarkableFileInputStreamFactory(
                    new File("training.txt"));

            ObjectStream<String> lineStream = null;
            lineStream = new PlainTextByLineStream(dataIn, StandardCharsets.UTF_8);
            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

            TrainingParameters tp = new TrainingParameters();
            tp.put(TrainingParameters.CUTOFF_PARAM, "2");
            tp.put(TrainingParameters.ITERATIONS_PARAM, "30");

            DoccatFactory df = new DoccatFactory();
            model = DocumentCategorizerME.train("en", sampleStream, tp, df);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dataIn != null) {
                try {
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    public static int classifyNewText(String text){
        DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);

        double[] outcomes = myCategorizer.categorize(text.split(" "));
        String category = myCategorizer.getBestCategory(outcomes);

        if (category.equalsIgnoreCase("1")){
            return 1;
        } else {
            return 0;
        }

    }
    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read); 

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }

    }
	
}
