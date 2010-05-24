package de.saring.exerciseviewer.data;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents an recorded exercise. There is all the data of an
 * Polar S710 stored in this class right now, maybe there will be more data
 * in future to be compatible with other watches too.
 * 
 * @author  Stefan Saring
 * @version 1.0
 */
public final class EVExercise implements Serializable
{    
    /** File type of an exercise (see enums). */
    private ExerciseFileType fileType;
    /** ID of user of exercise (0 - 99). */
    private byte userID;
    /** Timestamp of exercise. */
    private Date date;
    /** Exercise type (0 to 5). */
    private byte type;
    /** Exercise type label. */
    private String typeLabel;
    /** Record mode (what was recorded in exercise). */
    private RecordingMode recordingMode;
    /** Duration of exercise in tenths of a second. */
    private int duration;
    /** Recording interval in seconds (5s, 15s, or 60s). */
    private short recordingInterval;
    /** Average heart rate of exercise. */
    private short heartRateAVG;
    /** Maximim heart rate of exercise. */
    private short  heartRateMax;
    /** The speed data of exercise (if recorded). */
    private ExerciseSpeed speed;
    /** The cadence data of exercise (if recorded). */
    private ExerciseCadence cadence;
    /** The altitude data of exercise (if recorded). */
    private ExerciseAltitude altitude;
    /** The temperature data of exercise. */
    private ExerciseTemperature temperature;
    /** Energy "wasted" for exercise (in kCal). */
    private int energy;
    /** Cumulative "wasted" energy of all exercises (in kCal). */
    private int energyTotal;
    /** Cumulative workout time (in minutes). */
    private int sumExerciseTime;
    /** Cumulative ride time (in minutes). */
    private int sumRideTime;
    /** Odometer (cumulative ride distance) in km. */
    private int odometer;
    
    /** Array of heartrate limit data (can be more then one). */
    private HeartRateLimit[] heartRateLimits;
    /** Array containing the data of all laps of exercise. */
    private Lap[] lapList;
    /** Array containing the data of all recorded samples (for each interval) of exercise. */
    private ExerciseSample[] sampleList;
    
    /** This is the list of possible file types of an exercise. */
    public enum ExerciseFileType { S710RAW, S610RAW, S510RAW, HRM, HAC4TUR, RS200SDRAW, F6RAW, SSCSV, GARMIN_TCX }


    /***** BEGIN: Generated Getters and Setters *****/
    
    public ExerciseFileType getFileType () {
        return fileType;
    }

    public void setFileType (ExerciseFileType fileType) {
        this.fileType = fileType;
    }

    public byte getUserID () {
        return userID;
    }

    public void setUserID (byte userID) {
        this.userID = userID;
    }

    public Date getDate () {
        return date;
    }

    public void setDate (Date date) {
        this.date = date;
    }

    public byte getType () {
        return type;
    }

    public void setType (byte type) {
        this.type = type;
    }

    public String getTypeLabel () {
        return typeLabel;
    }

    public void setTypeLabel (String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public RecordingMode getRecordingMode () {
        return recordingMode;
    }

    public void setRecordingMode (RecordingMode recordingMode) {
        this.recordingMode = recordingMode;
    }

    public int getDuration () {
        return duration;
    }

    public void setDuration (int duration) {
        this.duration = duration;
    }

    public short getRecordingInterval () {
        return recordingInterval;
    }

    public void setRecordingInterval (short recordingInterval) {
        this.recordingInterval = recordingInterval;
    }

    public short getHeartRateAVG () {
        return heartRateAVG;
    }

    public void setHeartRateAVG (short heartRateAVG) {
        this.heartRateAVG = heartRateAVG;
    }

    public short getHeartRateMax () {
        return heartRateMax;
    }

    public void setHeartRateMax (short heartRateMax) {
        this.heartRateMax = heartRateMax;
    }

    public ExerciseSpeed getSpeed () {
        return speed;
    }

    public void setSpeed (ExerciseSpeed speed) {
        this.speed = speed;
    }

    public ExerciseCadence getCadence () {
        return cadence;
    }

    public void setCadence (ExerciseCadence cadence) {
        this.cadence = cadence;
    }

    public ExerciseAltitude getAltitude () {
        return altitude;
    }

    public void setAltitude (ExerciseAltitude altitude) {
        this.altitude = altitude;
    }

    public ExerciseTemperature getTemperature () {
        return temperature;
    }

    public void setTemperature (ExerciseTemperature temperature) {
        this.temperature = temperature;
    }

    public int getEnergy () {
        return energy;
    }

    public void setEnergy (int energy) {
        this.energy = energy;
    }

    public int getEnergyTotal () {
        return energyTotal;
    }

    public void setEnergyTotal (int energyTotal) {
        this.energyTotal = energyTotal;
    }

    public int getSumExerciseTime () {
        return sumExerciseTime;
    }

    public void setSumExerciseTime (int sumExerciseTime) {
        this.sumExerciseTime = sumExerciseTime;
    }

    public int getSumRideTime () {
        return sumRideTime;
    }

    public void setSumRideTime (int sumRideTime) {
        this.sumRideTime = sumRideTime;
    }

    public int getOdometer () {
        return odometer;
    }

    public void setOdometer (int odometer) {
        this.odometer = odometer;
    }

    public HeartRateLimit[] getHeartRateLimits () {
        return heartRateLimits;
    }

    public void setHeartRateLimits (HeartRateLimit[] heartRateLimits) {
        this.heartRateLimits = heartRateLimits;
    }

    public Lap[] getLapList () {
        return lapList;
    }

    public void setLapList (Lap[] lapList) {
        this.lapList = lapList;
    }

    public ExerciseSample[] getSampleList () {
        return sampleList;
    }

    public void setSampleList (ExerciseSample[] sampleList) {
        this.sampleList = sampleList;
    }
    
    /***** END: Generated Getters and Setters *****/
        
    /** 
     * In most file formats (e.g. S710Raw, HRM) there are no distance values for each
     * recorded sample. So they need to be calculated from the sample time and speed.
     * This calculation is sometimes not total precise, the distance of last sample is
     * smaller/larger then the exercise distance. So all the sample distances needs to
     * get recalculated in relation to the exercise distance.
     */
    public void repairSamples () 
    {
        // is all the required speed data available ?
        if ((this.speed == null) || (this.speed.getDistance () == 0) || 
            (this.sampleList == null) || (this.sampleList.length == 0)) {
            return;
        }

        // it's possible that there are not recorded samples for the whole exercise time
        // (e.g. connection problems) => in this case we can't repair the sample distances
        if (this.sampleList.length < (duration / 10 / recordingInterval)) {
            return;
        }  

        // calculate relation of exercise distance to last sample distance
        ExerciseSample lastSample = this.sampleList[this.sampleList.length - 1];
        double fRelation = lastSample.getDistance () / (double) this.speed.getDistance ();

        // process all samples and recalculate the sample distance in relation to exercise distance 
        for (ExerciseSample sample : this.sampleList) {
            sample.setDistance ((int) Math.round (sample.getDistance () / fRelation));
        }
    }

    /** 
     * Returns a string representation of this object. 
     * @return string with object content
     */
    @Override
    public String toString ()
    {
        StringBuilder sBuilder = new StringBuilder ();

        sBuilder.append (EVExercise.class.getName () + ":\n");
        sBuilder.append (" [fileType=" + this.fileType + "\n");
        sBuilder.append ("  userID=" + this.userID + "\n");
        sBuilder.append ("  date=" + this.date + "\n");
        sBuilder.append ("  type=" + this.type + "\n");
        sBuilder.append ("  typeLabel=" + this.typeLabel + "\n");
        sBuilder.append ("  duration=" + this.duration + "\n");
        sBuilder.append ("  recordingInterval=" + this.recordingInterval + "\n");
        sBuilder.append ("  heartRateAVG=" + this.heartRateAVG + "\n");
        sBuilder.append ("  heartRateMax=" + this.heartRateMax + "\n");
        sBuilder.append ("  energy=" + this.energy + "\n");
        sBuilder.append ("  energyTotal=" + this.energyTotal + "\n");
        sBuilder.append ("  sumExerciseTime=" + this.sumExerciseTime + "\n");
        sBuilder.append ("  sumRideTime=" + this.sumRideTime + "\n");
        sBuilder.append ("  odometer=" + this.odometer + "]\n");

        if (this.recordingMode != null) sBuilder.append (this.recordingMode);
        if (this.speed != null) sBuilder.append (this.speed);
        if (this.cadence != null) sBuilder.append (this.cadence);
        if (this.altitude != null) sBuilder.append (this.altitude);
        if (this.temperature != null) sBuilder.append (this.temperature);

        if (this.heartRateLimits != null) {
            for (int i = 0; i < this.heartRateLimits.length; i++) {
                sBuilder.append ("arrayIndex " + i + ": ");
                sBuilder.append (this.heartRateLimits[i]);
            }
        }

        if (this.lapList != null) {
            for (int i = 0; i < this.lapList.length; i++) {
                sBuilder.append ("arrayIndex " + i + ": ");
                sBuilder.append (this.lapList[i]);
            }
        }

        if (this.sampleList != null) {
            for (int i = 0; i < this.sampleList.length; i++) {
                sBuilder.append ("arrayIndex " + i + ": ");
                sBuilder.append (this.sampleList[i]);
            }
        }

        return sBuilder.toString ();
    }
}