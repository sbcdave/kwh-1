package org.kwh.tcp.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rpomeroy on 4/26/14.
 */
public class Record {

    // stationId_channel value timestamp
    private static String GRAPHITE_FORMAT = "%s.%s.%s %f %s\n";
    private static String localTimezone =System.getProperty("org.kwh.localTimezone","UTC");
    private static ZoneId localZoneID = ZoneId.of(localTimezone);
    private static ZoneId UTC = ZoneId.of("UTC");
    private static Map<String,String> channelCodeToName = new HashMap<>();
    private static List<String> activeChannels = new ArrayList<>();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
    private static String stationIDovrd =System.getProperty("org.kwh.stationID");
    private static String AD01channelLabel =System.getProperty("org.kwh.AD01.channel.label","Channel_AD01");
    private static String AD02channelLabel =System.getProperty("org.kwh.AD02.channel.label","Channel_AD02");
    private static String AD03channelLabel =System.getProperty("org.kwh.AD03.channel.label","Channel_AD03");
    private static String AD04channelLabel =System.getProperty("org.kwh.AD04.channel.label","Channel_AD04");
    private static String AD05channelLabel =System.getProperty("org.kwh.AD05.channel.label","Channel_AD05");
    private static String AD06channelLabel =System.getProperty("org.kwh.AD06.channel.label","Channel_AD06");
    private static String AD11channelLabel =System.getProperty("org.kwh.AD11.channel.label","Channel_AD11");
    private static String AD12channelLabel =System.getProperty("org.kwh.AD12.channel.label","Channel_AD12");
    private static String PU01channelLabel =System.getProperty("org.kwh.PU01.channel.label","Channel_PU01");
    private static String PU02channelLabel =System.getProperty("org.kwh.PU02.channel.label","Channel_PU02");
    private static String PU03channelLabel =System.getProperty("org.kwh.PU03.channel.label","Channel_PU03");
    private static String PU04channelLabel =System.getProperty("org.kwh.PU04.channel.label","Channel_PU04");
    private static String PU05channelLabel =System.getProperty("org.kwh.PU05.channel.label","Channel_PU05");
    private static String PU06channelLabel =System.getProperty("org.kwh.PU06.channel.label","Channel_PU06");
    private static Boolean AD01channelActive =Boolean.parseBoolean(System.getProperty("org.kwh.AD01.channel.active","true"));
    private static Boolean AD02channelActive =Boolean.parseBoolean(System.getProperty("org.kwh.AD02.channel.active","true"));
    private static Boolean AD03channelActive =Boolean.parseBoolean(System.getProperty("org.kwh.AD03.channel.active","true"));
    private static Boolean AD04channelActive =Boolean.parseBoolean(System.getProperty("org.kwh.AD04.channel.active","true"));
    private static Boolean AD05channelActive =Boolean.parseBoolean(System.getProperty("org.kwh.AD05.channel.active","true"));
    private static Boolean AD06channelActive =Boolean.parseBoolean(System.getProperty("org.kwh.AD06.channel.active","true"));
    private static Boolean AD11channelActive =Boolean.parseBoolean(System.getProperty("org.kwh.AD11.channel.active","true"));
    private static Boolean AD12channelActive =Boolean.parseBoolean(System.getProperty("org.kwh.AD12.channel.active","true"));
    private static Boolean PU01channelActive =Boolean.parseBoolean(System.getProperty("org.kwh.PU01.channel.active","true"));
    private static Boolean PU02channelActive =Boolean.parseBoolean(System.getProperty("org.kwh.PU02.channel.active","true"));
    private static Boolean PU03channelActive =Boolean.parseBoolean(System.getProperty("org.kwh.PU03.channel.active","true"));
    private static Boolean PU04channelActive =Boolean.parseBoolean(System.getProperty("org.kwh.PU04.channel.active","true"));
    private static Boolean PU05channelActive =Boolean.parseBoolean(System.getProperty("org.kwh.PU05.channel.active","true"));
    private static Boolean PU06channelActive =Boolean.parseBoolean(System.getProperty("org.kwh.PU06.channel.active","true"));

    
    
    static {
        channelCodeToName.put("AD01", AD01channelLabel);
        channelCodeToName.put("AD02", AD02channelLabel);
        channelCodeToName.put("AD03", AD03channelLabel);
        channelCodeToName.put("AD04", AD04channelLabel);
        channelCodeToName.put("AD05", AD05channelLabel);
        channelCodeToName.put("AD06", AD06channelLabel);
        channelCodeToName.put("AD11", AD11channelLabel);
        channelCodeToName.put("AD12", AD12channelLabel);
        channelCodeToName.put("PU01", PU01channelLabel);
        channelCodeToName.put("PU02", PU02channelLabel);
        channelCodeToName.put("PU03", PU03channelLabel);
        channelCodeToName.put("PU04", PU04channelLabel);
        channelCodeToName.put("PU05", PU05channelLabel);
        channelCodeToName.put("PU06", PU06channelLabel);
        channelCodeToName.put("TM", "TimeStamp");
        
    }

    static {
    	if (AD01channelActive) activeChannels.add("AD01");
    	if (AD02channelActive) activeChannels.add("AD02");
    	if (AD03channelActive) activeChannels.add("AD03");
    	if (AD04channelActive) activeChannels.add("AD04");
    	if (AD05channelActive) activeChannels.add("AD05");
    	if (AD06channelActive) activeChannels.add("AD06");
    	if (AD11channelActive) activeChannels.add("AD11");
    	if (AD12channelActive) activeChannels.add("AD12");
    	if (PU01channelActive) activeChannels.add("PU01");
    	if (PU02channelActive) activeChannels.add("PU02");
    	if (PU03channelActive) activeChannels.add("PU03");
    	if (PU04channelActive) activeChannels.add("PU04");
    	if (PU05channelActive) activeChannels.add("PU05");
    	if (PU06channelActive) activeChannels.add("PU06");
    }
    
    static Logger Logger = LoggerFactory.getLogger("MyRecord");
    
    private String stationID;
    private LocalDateTime timestamp;
    private Map<String, Double> channelData;

    public Record(String stationID, LocalDateTime timestamp, Map<String,
            Double> channelData) {
        this.stationID = stationID;
        this.timestamp = timestamp;
        this.channelData = channelData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        if (!channelData.equals(record.channelData)) return false;
        if (!stationID.equals(record.stationID)) return false;
        if (!timestamp.equals(record.timestamp)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stationID.hashCode();
        result = 31 * result + timestamp.hashCode();
        result = 31 * result + channelData.hashCode();
        return result;
    }

    public String getStationID() {
    	// possibility to override the station ID if necessary
    	if (stationIDovrd != null)
    	{
    		return stationIDovrd;
    	}
    	else
    	{
    		return stationID;
    	}
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String formatTimestamp() {
        return this.getTimestamp().format(formatter);
    }

    public Map<String, Double> getChannelData() {
        return channelData;
    }

    public List<String> toGraphite() {
        // path value timestamp \n
        long epochTime = getEpochTime();
  
        List<String> data = new ArrayList();
        
        // second loop to create the list
        for (Map.Entry<String,Double> entry : this.channelData.entrySet()) {
        	if (activeChannels.contains(entry.getKey())) {
                data.add(String.format(GRAPHITE_FORMAT,
                        getStationID(),
                        entry.getKey(),
                        channelCodeToName.getOrDefault(entry.getKey(), entry.getKey()),
                        entry.getValue(),
                        epochTime));
                Logger.info(","+getStationID()+","+entry.getKey()+","+channelCodeToName.getOrDefault(entry.getKey(), entry.getKey())+","+entry.getValue()+","+getTimestamp());
            }
        }
        System.out.println(data);
        return data;
    }

    private long getEpochTime() {
    	System.out.println("Local EpochTime (" + localZoneID + "): " + this.getTimestamp().atZone(localZoneID).toEpochSecond());
    	System.out.println("UTC EpochTime: " + this.getTimestamp().atZone(UTC).toEpochSecond());
        return this.getTimestamp().atZone(localZoneID).toEpochSecond();
    }
}
