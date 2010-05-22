package de.saring.exerciseviewer.parser.impl

import de.saring.exerciseviewer.core.PVException
import de.saring.exerciseviewer.data.PVExercise
import de.saring.exerciseviewer.parser.ExerciseParser

/**
 * This class contains all unit tests for the GarminTcxParser class.
 *
 * @author Stefan Saring
 */
class GarminTcxParserTest extends GroovyTestCase {
    
    /** Instance to be tested. */
    private ExerciseParser parser
    
    /**
     * This method initializes the environment for testing.
     */
    void setUp () {
        parser = new GarminTcxParser ()
    }
    
    /**
     * This method must fail on parsing an exerise file which doesn't exists.
     */
    void testParseExerciseMissingFile () {
        shouldFail (PVException) {
            parser.parseExercise ('misc/testdata/garmin-tcx/unknown-file.tcx')
        }
    }
    
    /**
     * This test parses a TCX file from a Garmin Forerunner 305 (Running, no heartrate data, 1 lap).
     */
    void testForerunner305_Running_NoHeartrate_1Lap () {        
        
        def exercise = parser.parseExercise ('misc/testdata/garmin-tcx/Forerunner305-Running-NoHeartrate-1Lap.tcx')
        assertEquals (PVExercise.ExerciseFileType.GARMIN_TCX, exercise.fileType)
        
        def calDate = Calendar.getInstance ()        
        calDate.set (2007, 8-1, 7, 2, 42, 41)
        assertEquals ((int) (calDate.time.time / 1000), (int) (exercise.date.time / 1000))
        assertEquals (2325 * 10, exercise.duration)            
        
        // heart rates
        assertEquals (0, exercise.heartRateAVG)
        assertEquals (0, exercise.heartRateMax)
        assertEquals (285, exercise.energy)

        // heartrate limits not available in TCX files
        assertNull (exercise.heartRateLimits)

        // distance & speed & odometer
        assertEquals (8349, exercise.speed.distance)
	    assertEquals (72.284d, exercise.speed.speedMax, 0.001d)
	    assertEquals (12.927d, exercise.speed.speedAVG, 0.001d)

	    // altitude
        assertEquals (-4, exercise.altitude.altitudeMin)
        assertEquals (8, exercise.altitude.altitudeAVG)
        assertEquals (21, exercise.altitude.altitudeMax)
        assertEquals (149, exercise.altitude.ascent)
        
        // lap data
        assertEquals  (1, exercise.lapList.size())

	    assertEquals (2325 * 10, exercise.lapList[0].timeSplit)        
        assertEquals (0, exercise.lapList[0].heartRateSplit)        
        assertEquals (0, exercise.lapList[0].heartRateAVG)
        assertEquals (0, exercise.lapList[0].heartRateMax)
        assertEquals (8349, exercise.lapList[0].speed.distance)
	    assertEquals (12.926d, exercise.lapList[0].speed.speedAVG, 0.001d)
		assertEquals (0, exercise.lapList[0].speed.speedEnd)        
        assertEquals (10, exercise.lapList[0].altitude.altitude)
        assertEquals (149, exercise.lapList[0].altitude.ascent)
    }
    
    /**
     * This test parses a TCX file from a Garmin Edge 705 (Running, heartrate data, 2 laps).
     */
    void testEdge705_Running_Heartrate_2Laps () {
        
        def exercise = parser.parseExercise ('misc/testdata/garmin-tcx/Edge705-Running-Heartrate-2Laps.tcx')        
        assertEquals (PVExercise.ExerciseFileType.GARMIN_TCX, exercise.fileType)
        
        def calDate = Calendar.getInstance ()        
        calDate.set (2009, 12-1, 9, 6, 54, 25)
        assertEquals ((int) (calDate.time.time / 1000), (int) (exercise.date.time / 1000))
        assertEquals (5811,9 * 10, exercise.duration)            
        
        // heart rates
        assertEquals (157, exercise.heartRateAVG) 
        assertEquals (173, exercise.heartRateMax)
        assertEquals (2251, exercise.energy)
        
        // heartrate limits not available in TCX files
        assertNull (exercise.heartRateLimits)
        
        // distance & speed & odometer
        assertEquals (18990, exercise.speed.distance)
        assertEquals (164.425d, exercise.speed.speedMax, 0.001d)
        assertEquals (11.762d, exercise.speed.speedAVG, 0.001d)
        
        // altitude
        assertEquals (94, exercise.altitude.altitudeMin)
        assertEquals (115, exercise.altitude.altitudeAVG)
        assertEquals (153, exercise.altitude.altitudeMax)
        assertEquals (388, exercise.altitude.ascent)
        
        // lap data
        assertEquals  (2, exercise.lapList.size())
        
        assertEquals (3166.3 * 10, exercise.lapList[0].timeSplit)        
        assertEquals (168, exercise.lapList[0].heartRateSplit)        
        assertEquals (158, exercise.lapList[0].heartRateAVG)
        assertEquals (173, exercise.lapList[0].heartRateMax)
        assertEquals (10618, exercise.lapList[0].speed.distance)
        assertEquals (12.074d, exercise.lapList[0].speed.speedAVG, 0.001d)
        assertEquals (135, exercise.lapList[0].altitude.altitude)
        assertEquals (213, exercise.lapList[0].altitude.ascent)
        assertEquals (9.009, exercise.lapList[0].speed.speedEnd, 0.001d)  
        
        assertEquals ((3166.3 + 2645.6) * 10, exercise.lapList[1].timeSplit)        
        assertEquals (160, exercise.lapList[1].heartRateSplit)        
        assertEquals (155, exercise.lapList[1].heartRateAVG)
        assertEquals (166, exercise.lapList[1].heartRateMax)
        assertEquals (10618 + 8372, exercise.lapList[1].speed.distance)
        assertEquals (11.390d, exercise.lapList[1].speed.speedAVG, 0.001d)
        assertEquals (12.568, exercise.lapList[1].speed.speedEnd, 0.01)  
        assertEquals (112, exercise.lapList[1].altitude.altitude)
        assertEquals (175, exercise.lapList[1].altitude.ascent)
    }
}